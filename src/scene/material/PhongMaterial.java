package scene.material;

import raytracer.Hit;
import scene.Scene;
import scene.data.Vector3f;
import scene.lighting.AmbientLight;
import scene.lighting.Light;

public class PhongMaterial extends Material {
	// TODO add intensity
	private Color3f phongColor = new Color3f(1, 1, 1);
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
		
		float sumR = ambientLight.getRed();
		float sumG = ambientLight.getGreen();
		float sumB = ambientLight.getBlue();
		
		for (Light light : scene.getLightSources()) {
			if (!(light instanceof AmbientLight) && !scene.isInShade(hit.getPoint(), light)) {
				// Note: negate ray direction because we need viewing vector
				halfVector = hit.getRay().getDirection().negate().sum(light.rayTo(hit.getPoint())).normalize();
				
				dotProduct = (float) Math.pow(Math.max(0, hit.getNormal().dotProduct(halfVector)), phongExponent);
				
				sumR += phongColor.getRed() * light.color().getRed() * light.intensity() * dotProduct;
				sumG += phongColor.getGreen() * light.color().getGreen() * light.intensity() * dotProduct;
				sumB += phongColor.getBlue() * light.color().getBlue() * light.intensity() * dotProduct;
			}
		}
		
		return new Color3f(ambientLight.getRed() + sumR, ambientLight.getGreen() + sumG, ambientLight.getBlue() + sumB);
	}

}
