package scene.material;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.geometry.Vector3f;
import scene.lighting.Light;

import java.util.HashSet;

public class ReflectiveMaterial extends PhongMaterial {

	private float reflectionCoefficient;
	
	public ReflectiveMaterial(float reflectionCoefficient) {
		super(new Color3f(1, 1, 1), 10);		// TODO this value should be higher, but for pointlight this isn't very pretty
		this.reflectionCoefficient = reflectionCoefficient;
	}

	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer, int recursionDepth) {
		if (recursionDepth > RayTracer.MAX_RECURSION_DEPTH) return new Color3f(0, 0, 0);

		Color3f phongShading = super.getColor(lights, hit, tracer, recursionDepth);
		
		// Construct ray from hit (angle incoming ray and normal same for outgoing and normal)
		Vector3f rayDirection = hit.getRay().getDirection().reflectOver(hit.getNormal());
		Ray outgoingRay = new Ray(hit.getPoint(), rayDirection);
		
		Hit nextSurfaceHit = tracer.trace(outgoingRay, RayTracer.EPS);
		
		Color3f other;
		
		if (nextSurfaceHit != null) {
			other = nextSurfaceHit.getSurface().getMaterial().getColor(lights, nextSurfaceHit, tracer, recursionDepth + 1);
		} else {
			other = new Color3f(0.1f, 0.1f, 0.1f);	// TODO
		}
		
		return new Color3f(reflectionCoefficient * other.getRed() + phongShading.getRed(),
				reflectionCoefficient * other.getGreen() + phongShading.getGreen(),
				reflectionCoefficient * other.getBlue() + phongShading.getBlue());
	}

}
