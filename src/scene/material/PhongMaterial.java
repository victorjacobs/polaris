package scene.material;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import scene.Scene;
import scene.data.Vector3f;
import scene.lighting.Light;

public class PhongMaterial extends Material {
	// TODO add intensity
	// TODO klopt niet helemaal voor extended lights
	private float phongExponent;
	
	public PhongMaterial(Color3f baseColor, float phongExponent) {
		super(baseColor);
		this.phongExponent = phongExponent;
	}
	
	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		Color3f ambientLight = super.getColor(scene, hit, recursionDepth);
		
		Vector3f halfVector;
		float dotProduct;
		
		float sumR = 0, sumG = 0, sumB = 0;
		Hit lightHit;

		for (Light light : scene.getLightSources()) {
			lightHit = scene.trace(new Ray(hit.getPoint(), light.rayTo(hit.getPoint())), Settings.EPS);

			if (lightHit == null) {
				// Note: negate ray direction because we need viewing vector
				halfVector = hit.getRay().getDirection().negate().sum(light.rayTo(hit.getPoint())).normalize();
				
				dotProduct = (float) Math.pow(Math.max(0, hit.getNormal().dotProduct(halfVector)), phongExponent);
				
				sumR += light.color().getRed() * light.intensity() * dotProduct;
				sumG += light.color().getGreen() * light.intensity() * dotProduct;
				sumB += light.color().getBlue() * light.intensity() * dotProduct;
			}
		}
		
		return new Color3f(ambientLight.getRed() + sumR, ambientLight.getGreen() + sumG, ambientLight.getBlue() + sumB);
	}

	@Override
	public float getShadowPercentage() {
		return 1;
	}

}
