package scene.material;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.Scene;
import scene.geometry.Sphere;
import scene.geometry.Vector3f;
import scene.lighting.Light;

import java.util.HashSet;

public class RefractiveMaterial extends PhongMaterial {
	private float refractionCoefficient;
	private float n = 1;		// n where ray is travelling in
	private float ar, ag, ab;
	
	public RefractiveMaterial(Color3f baseColor, float refractionCoefficient) {
		super(baseColor, 100);
		this.refractionCoefficient = refractionCoefficient;

		ar = ag = ab = 0.1f;
	}
	
	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		if (recursionDepth > RayTracer.MAX_RECURSION_DEPTH) return new Color3f(0, 0, 0);
		Color3f phong = super.getColor(scene, hit, recursionDepth);

		Ray nextRay;
		Hit nextHit;
		float kr, kg, kb;

		if (hit.getRay().getDirection().dotProduct(hit.getNormal()) > 0) {
			// Normal oriented in same direction as ray, this is an inner ray
			nextRay = refractedRay(hit, hit.getNormal().negate(), refractionCoefficient, n);
			// Set k to something
			kr = (float)Math.exp(- ar * hit.getT());
			kg = (float)Math.exp(- ag * hit.getT());
			kb = (float)Math.exp(- ab * hit.getT());
		} else {
			// Ray entering from the outside
			nextRay = refractedRay(hit, hit.getNormal(), n, refractionCoefficient);
			kr = kg = kb = 1;
		}

		if (nextRay == null) {
			// Complete internal reflection
			Vector3f reflectedDirection = hit.getRay().getDirection().reflectOver(hit.getNormal().negate());

			nextHit = scene.trace(new Ray(hit.getPoint(), reflectedDirection), RayTracer.EPS);
		} else {
			nextHit = scene.trace(nextRay, RayTracer.EPS);
		}

		if (nextHit != null) {
			Color3f colorUnattenuated = nextHit.getSurface().getMaterial().getColor(scene, nextHit, recursionDepth + 1).sum(phong);

			return new Color3f(kr * colorUnattenuated.getRed(), kg * colorUnattenuated.getGreen(), kb * colorUnattenuated.getBlue());
		} else {
			return phong;
		}

	}
	
	private Ray refractedRay(Hit hit, Vector3f n, float n1, float n2) {
		Vector3f d = hit.getRay().getDirection();

		Vector3f t1 = d.minus(n.multiply(d.dotProduct(n))).multiply(n1 / n2);
		float sqrt = 1 - (n1 * n1 * (1 - (d.dotProduct(n) * d.dotProduct(n))) / (n2 * n2));

		if (sqrt < 0) return null;

		Vector3f t2 = n.multiply((float) Math.sqrt(sqrt));

		return new Ray(hit.getPoint(), t1.minus(t2).normalize());
	}
}
