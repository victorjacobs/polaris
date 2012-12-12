package scene.material;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import scene.Scene;
import scene.data.Vector3f;

// TODO reflectie etc
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
		if (recursionDepth > Settings.MAX_RECURSION_DEPTH) return new Color3f(0, 0, 0); // TODO solve
		Color3f phong = super.phongHighlight(scene, hit, recursionDepth);
		Ray reflectedRay = new Ray(hit.getPoint(), hit.getRay().getDirection().reflectOver(hit.getNormal().negate()));

		Ray nextRay;
		Hit nextHit;
		float kr, kg, kb;
		float c;

		if (hit.getRay().getDirection().dotProduct(hit.getNormal()) <= 0) {
			// Ray entering from the outside
			nextRay = refractedRay(hit, hit.getNormal(), n, refractionCoefficient);
			kr = kg = kb = 1;

			c = -hit.getRay().getDirection().dotProduct(hit.getNormal());
		} else {
			// Normal oriented in same direction as ray, this is an inner ray
			nextRay = refractedRay(hit, hit.getNormal().negate(), refractionCoefficient, n);
			// Set k to something
			kr = (float)Math.exp(- ar * hit.getT());
			kg = (float)Math.exp(- ag * hit.getT());
			kb = (float)Math.exp(- ab * hit.getT());

			if (nextRay != null) {
				c = nextRay.getDirection().dotProduct(hit.getNormal());
			} else {
				// Internal reflection
				nextHit = scene.trace(reflectedRay, Settings.EPS);
				Color3f colorUnattenuated = nextHit.getSurface().getMaterial().getColor(scene, nextHit, recursionDepth + 1);

				return new Color3f(kr * colorUnattenuated.getRed(), kg * colorUnattenuated.getGreen(), kb * colorUnattenuated.getBlue());
			}
		}

		float r0 = ((refractionCoefficient - 1) * (refractionCoefficient - 1)) / ((refractionCoefficient + 1) * (refractionCoefficient + 1));
		float r = r0 + (1 - r0) * (float)Math.pow((1 - c), 5);

		Hit reflectHit = scene.trace(reflectedRay, Settings.EPS);
		Color3f reflectColor;
		if (reflectHit == null) {
			reflectColor = scene.getBackground();
		} else {
			reflectColor = reflectHit.getSurface().getMaterial().getColor(scene, reflectHit, recursionDepth + 1);
		}

		Hit transmittedHit = scene.trace(nextRay, Settings.EPS);
		Color3f transmittedColor;
		if (transmittedHit == null) {
			transmittedColor = scene.getBackground();
		} else {
			transmittedColor = transmittedHit.getSurface().getMaterial().getColor(scene, transmittedHit, recursionDepth + 1);
		}

		Color3f nextColor;

		nextColor = reflectColor.multiply(r).sum(transmittedColor.multiply(1 - r));
		nextColor = new Color3f(kr * nextColor.getRed(), kg * nextColor.getGreen(), kb * nextColor.getRed());

		// Add phong
		if (recursionDepth == 1) {
			// Only add phong on the end of the recursion chain
			nextColor = nextColor.sum(phong.multiply(r));
		}

		return nextColor;

	}
	
	private Ray refractedRay(Hit hit, Vector3f n, float n1, float n2) {
		Vector3f d = hit.getRay().getDirection();

		Vector3f t1 = d.minus(n.multiply(d.dotProduct(n))).multiply(n1 / n2);
		float sqrt = 1 - (n1 * n1 * (1 - (d.dotProduct(n) * d.dotProduct(n))) / (n2 * n2));

		if (sqrt < 0) return null;

		Vector3f t2 = n.multiply((float) Math.sqrt(sqrt));

		return new Ray(hit.getPoint(), t1.minus(t2).normalize());
	}

	@Override
	// TODO make this dependant of actual light loss in material!
	public float getShadowPercentage() {
		return 0.5f;
	}
}
