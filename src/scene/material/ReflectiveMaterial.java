package scene.material;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import scene.Scene;
import scene.data.Vector3f;

import java.util.Random;

public class ReflectiveMaterial extends PhongMaterial {

	private float reflectionCoefficient;
	private float n = 0.2f;
	private final Random rand;

	public ReflectiveMaterial(float reflectionCoefficient) {
		this(new Color3f(1, 1, 1), reflectionCoefficient);
	}

	public ReflectiveMaterial(Color3f color, float reflectionCoefficient) {
		super(color, 10);		// TODO this value should be higher, but for pointlight this isn't very pretty
		this.reflectionCoefficient = reflectionCoefficient;
		rand = new Random();
	}

	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		if (recursionDepth > Settings.MAX_RECURSION_DEPTH) return new Color3f(0, 0, 0);

		Color3f phongShading = super.phongHighlight(scene, hit, recursionDepth);

		// Construct ray from hit (angle incoming ray and normal same for outgoing and normal)
		Vector3f rayDirection = hit.getRay().getDirection().reflectOver(hit.getNormal()).sum(new Vector3f(rand.nextFloat() / 10, rand.nextFloat() / 10, rand.nextFloat() / 10));
		Ray outgoingRay = new Ray(hit.getPoint(), rayDirection);

		// TODO Fresnel Reflectance
//		float r0 = ((n - 1) * (n - 1)) / ((n + 1) * (n + 1));
//		float c = outgoingRay.getDirection().normalize().dotProduct(hit.getNormal().normalize());
//		float r = r0 + (1 - r0) * (float)Math.pow((1 - c), 5);
		
		Hit nextSurfaceHit = scene.trace(outgoingRay, Settings.EPS);
		
		Color3f other;
		
		if (nextSurfaceHit != null) {
			other = nextSurfaceHit.getSurface().getMaterial().getColor(scene, nextSurfaceHit, recursionDepth + 1);
		} else {
			other = scene.getBackground(outgoingRay.getDirection());
		}

		// TODO moet ambient lighting ook geschaald worden met reflectiecoeff?
		return new Color3f(reflectionCoefficient * other.getRed() + phongShading.getRed(),
				reflectionCoefficient * other.getGreen() + phongShading.getGreen(),
				reflectionCoefficient * other.getBlue() + phongShading.getBlue());
	}

}
