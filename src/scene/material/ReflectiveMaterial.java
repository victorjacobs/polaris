package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.geometry.Vector3f;
import scene.lighting.Light;

public class ReflectiveMaterial extends PhongMaterial {

	private float reflectionCoefficient;
	
	public ReflectiveMaterial(float reflectionCoefficient) {
		super(new Color3f(1, 1, 1), 10);		// TODO this value should be higher, but for pointlight this isn't very pretty
		this.reflectionCoefficient = reflectionCoefficient;
	}

	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer) {
		Color3f phongShading = super.getColor(lights, hit, tracer);
		
		// Construct ray from hit (angle incoming ray and normal same for outgoing and normal)
		Vector3f rayDirection = hit.getRay().getDirection().reflectOver(hit.getNormal());
		Ray outgoingRay = new Ray(hit.getPoint(), rayDirection);
		
		Hit nextSurfaceHit = tracer.trace(outgoingRay, 0.000001f);
		
		Color3f other;
		
		if (nextSurfaceHit != null) {
			other = nextSurfaceHit.getSurface().getMaterial().getColor(lights, nextSurfaceHit, tracer);
		} else {
			other = new Color3f(0.1f, 0.1f, 0.1f);
		}
		
		return new Color3f(reflectionCoefficient * other.getRed() + phongShading.getRed(), reflectionCoefficient * other.getGreen() + phongShading.getGreen(), reflectionCoefficient * other.getBlue() + phongShading.getBlue());
	}

}
