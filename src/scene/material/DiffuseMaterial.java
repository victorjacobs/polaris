package scene.material;

import raytracer.Hit;
import scene.Scene;
import scene.lighting.AmbientLight;
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
		
		float sumR = 0, sumG = 0, sumB = 0, dotProduct;

		for (Light light : scene.getLightSources()) {
			if (!(light instanceof AmbientLight) && !scene.isInShade(hit.getPoint(), light)) {
				dotProduct = Math.max(0, hit.getNormal().dotProduct(light.rayTo(hit.getPoint()).normalize()));
				sumR += (light.intensity() * light.color().getRed()) * dotProduct;
				sumG += (light.intensity() * light.color().getGreen()) * dotProduct;
				sumB += (light.intensity() * light.color().getBlue()) * dotProduct;
			}
		}

		sumR *= getUnshadedColorAt(hit.getTextureCoordinates()).getRed();
		sumG *= getUnshadedColorAt(hit.getTextureCoordinates()).getGreen();
		sumB *= getUnshadedColorAt(hit.getTextureCoordinates()).getBlue();
		
		return new Color3f(ambientLight.getRed() + sumR, ambientLight.getGreen() + sumG, ambientLight.getRed() + sumB);
	}
	
}
