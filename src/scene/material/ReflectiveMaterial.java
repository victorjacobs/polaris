package scene.material;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.Scene;
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
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		if (recursionDepth > RayTracer.MAX_RECURSION_DEPTH) return new Color3f(0, 0, 0);

		Color3f phongShading = super.getColor(scene, hit, recursionDepth);
		
		// Construct ray from hit (angle incoming ray and normal same for outgoing and normal)
		Vector3f rayDirection = hit.getRay().getDirection().reflectOver(hit.getNormal());
		Ray outgoingRay = new Ray(hit.getPoint(), rayDirection);
		
		Hit nextSurfaceHit = scene.trace(outgoingRay, RayTracer.EPS);
		
		Color3f other;
		
		if (nextSurfaceHit != null) {
			other = nextSurfaceHit.getSurface().getMaterial().getColor(scene, nextSurfaceHit, recursionDepth + 1);
		} else {
			//other = new Color3f(0.1f, 0.1f, 0.1f);	// TODO
			other = new Color3f(0, 0, 0);
		}
		
		return new Color3f(reflectionCoefficient * other.getRed() + phongShading.getRed(),
				reflectionCoefficient * other.getGreen() + phongShading.getGreen(),
				reflectionCoefficient * other.getBlue() + phongShading.getBlue());
	}

}
