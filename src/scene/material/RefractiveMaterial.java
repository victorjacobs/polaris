package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.geometry.Vector3f;
import scene.lighting.Light;

public class RefractiveMaterial extends Material {
	private float refractionCoefficient;
	private float n = 1;		// n where ray is travelling in
	
	public RefractiveMaterial(Color3f baseColor, float refractionCoefficient) {
		super(baseColor);
		this.refractionCoefficient = refractionCoefficient;
	}
	
	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer, int recursionDepth) {
		if (recursionDepth > RayTracer.MAX_RECURSION_DEPTH) return new Color3f(0, 0, 0);
		//Color3f phong = super.getColor(lights, hit, tracer);

		Ray nextRay;
		Hit nextHit;

		if (hit.getRay().getDirection().dotProduct(hit.getNormal()) < 0) {
			// Normal oriented different direction than normal, this is an inside ray
			nextRay = refractedRay(hit, hit.getNormal().negate(), refractionCoefficient, n);
		} else {
			nextRay = refractedRay(hit, hit.getNormal(), n, refractionCoefficient);
		}

		if (nextRay == null) {
			// Internal reflection
			Vector3f reflectedDirection = hit.getRay().getDirection().reflectOver(hit.getNormal().negate());

			nextHit = tracer.trace(new Ray(hit.getPoint(), reflectedDirection), recursionDepth + 1);
		} else {
			nextHit = tracer.trace(nextRay, recursionDepth + 1);
		}

		if (nextHit != null) {
//			return new Color3f(nextHit.getRay().getDirection().x, nextHit.getRay().getDirection().y, nextHit.getRay().getDirection().z);
//			return new Color3f(nextRay.getDirection().x, nextRay.getDirection().y, nextRay.getDirection().z);
			return nextHit.getSurface().getMaterial().getColor(lights, hit, tracer);
		} else {
			return new Color3f(0, 0, 1);
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
