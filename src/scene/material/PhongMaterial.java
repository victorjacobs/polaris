package scene.material;

import raytracer.Hit;
import scene.Scene;
import scene.data.Vector3f;
import scene.lighting.Light;

public class PhongMaterial extends DiffuseMaterial {
	// TODO add intensity
	// TODO klopt niet helemaal voor extended lights
	private float phongExponent;
	
	public PhongMaterial(Color3f baseColor, float phongExponent) {
		super(baseColor);
		this.phongExponent = phongExponent;
	}

	public Color3f phongHighlight(Scene scene, Hit hit, int recursionDepth) {
		return getColor(scene, hit, recursionDepth, true);
	}

	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		return getColor(scene, hit, recursionDepth, false);
	}

	private Color3f getColor(Scene scene, Hit hit, int recursionDepth, boolean onlyPhongHighlight) {
		Color3f diffuseColor = super.getColor(scene, hit, recursionDepth);
		
		Vector3f halfVector;
		float dotProduct;

		float sumR = 0;
		float sumG = 0;
		float sumB = 0;

		float curR, curG, curB;
		float shadowPercentage;

		for (Light light : scene.getLightSources()) {
			shadowPercentage = light.getShadowPercentage(scene, hit.getPoint());

			halfVector = hit.getRay().getDirection().negate().sum(light.rayTo(hit.getPoint())).normalize();
			dotProduct = (float) Math.pow(Math.max(0, hit.getNormal().dotProduct(halfVector)), phongExponent);

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

		Color3f phongHighlight = new Color3f(sumR, sumG, sumB);

		if (onlyPhongHighlight) {
			return phongHighlight;
		} else {
			return phongHighlight.sum(diffuseColor);
		}
	}

	@Override
	public float getShadowPercentage() {
		return 1;
	}

}
