package scene.material;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import scene.Scene;
import scene.lighting.Light;

public class DiffuseMaterial extends Material {

	public DiffuseMaterial(Texture texture) {
		super(texture);
	}

	public DiffuseMaterial(Color3f baseColor) {
		super(baseColor);
	}
	
	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		Color3f ambientLight = super.getColor(scene, hit, recursionDepth);

		// TODO this isn't right: when no lights, 2 * ambientLight will be returned!
		// TODO refactor this to higher up?
		float sumR = ambientLight.getRed();
		float sumG = ambientLight.getGreen();
		float sumB = ambientLight.getBlue();
		float dotProduct;

		float curR, curG, curB;
		float shadowPercentage;

		Hit lightHit;

		for (Light light : scene.getLightSources()) {
			lightHit = scene.trace(new Ray(hit.getPoint(), light.rayTo(hit.getPoint())), Settings.EPS);

			shadowPercentage = (lightHit == null) ? 0 : lightHit.getSurface().getMaterial().getShadowPercentage();

			dotProduct = Math.max(0, hit.getNormal().dotProduct(light.rayTo(hit.getPoint()).normalize()));
			curR = (light.intensity() * light.color().getRed()) * dotProduct;
			curG = (light.intensity() * light.color().getGreen()) * dotProduct;
			curB = (light.intensity() * light.color().getBlue()) * dotProduct;

			sumR += (1 - shadowPercentage) * curR;
			sumG += (1 - shadowPercentage) * curG;
			sumB += (1 - shadowPercentage) * curB;
		}

		sumR *= getUnshadedColorAt(hit.getTextureCoordinates()).getRed();
		sumG *= getUnshadedColorAt(hit.getTextureCoordinates()).getGreen();
		sumB *= getUnshadedColorAt(hit.getTextureCoordinates()).getBlue();
		
		return new Color3f(ambientLight.getRed() + sumR, ambientLight.getGreen() + sumG, ambientLight.getRed() + sumB);
	}

	@Override
	public float getShadowPercentage() {
		return 1;
	}

}
