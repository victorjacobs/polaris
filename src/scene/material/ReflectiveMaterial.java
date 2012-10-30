package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.geometry.Vector3f;
import scene.lighting.Light;

public class ReflectiveMaterial extends PhongMaterial {

	public ReflectiveMaterial() {
		super(new Color3f(1, 1, 1), 100);
	}

	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer) {
		Color3f aa = super.getColor(lights, hit, tracer);
		
		// Construct ray from hit (angle incoming ray and normal same for outgoing and normal)
		float projectionOnNormal = hit.getRay().getDirection().dotProduct(hit.getNormal());
		Vector3f rayDirection = hit.getNormal().multiply(2 * projectionOnNormal).minus(hit.getRay().getDirection());
		
		Ray outgoingRay = new Ray(hit.getPoint(), rayDirection);
		
		Hit nextSurfaceHit = tracer.trace(outgoingRay, 0.1f);
		
		Color3f other;
		
		if (nextSurfaceHit != null) {
			other = nextSurfaceHit.getSurface().getMaterial().getColor(lights, nextSurfaceHit, tracer);
		} else {
			other = new Color3f(0.1f, 0.1f, 0.1f);
		}
		
		return new Color3f(other.getRed() + aa.getRed(), other.getGreen() + aa.getGreen(), other.getBlue() + aa.getBlue());
	}

}
