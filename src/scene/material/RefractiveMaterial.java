package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.geometry.Vector3f;
import scene.lighting.Light;

public class RefractiveMaterial extends PhongMaterial {
	private float refractionCoefficient;
	private float n = 1;		// n where ray is travelling in
	
	public RefractiveMaterial(Color3f baseColor, float refractionCoefficient) {
		super(baseColor, 100);
		this.refractionCoefficient = refractionCoefficient;
	}
	
	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer, int recursionDepth) {
		if (recursionDepth > RayTracer.MAX_RECURSION_DEPTH) return new Color3f(0, 0, 0);
		//Color3f phong = super.getColor(lights, hit, tracer);

		Ray internalRay = refractedRay(hit, hit.getNormal(), n, refractionCoefficient);

		//System.out.println("Incoming: " + hit.getRay().getDirection() + " internal: " + internalRay.getDirection());

		Hit otherSideHit = tracer.trace(internalRay, 0.001f);

		if (otherSideHit == null) return new Color3f(0, 0, 0);

		Ray exitingRay = refractedRay(otherSideHit, otherSideHit.getNormal().negate() , refractionCoefficient, n);

		return new Color3f(0, Math.abs(exitingRay.getDirection().y * 3), Math.abs(exitingRay.getDirection().z * 3));

		// Find color other side
//		Hit finalHit = tracer.trace(exitingRay, 0.01f);
//
//		if (finalHit != null) {
//			return finalHit.getSurface().getMaterial().getColor(lights, hit, tracer, recursionDepth + 1);
//		} else {
//			return new Color3f(0, 0, 0);
//		}
	}
	
	private Ray refractedRay(Hit hit, Vector3f n, float n1, float n2) {
		Vector3f d = hit.getRay().getDirection();

		Vector3f t1 = d.minus(n.multiply(d.dotProduct(n))).multiply(n1 / n2);
		float sqrt = (float) Math.sqrt(1 - (n1 * n1 * (1 - (d.dotProduct(n) * d.dotProduct(n))) / (n2 * n2)));
		Vector3f t2 = n.multiply(sqrt);

		return new Ray(hit.getPoint(), t1.minus(t2));
	}
}
