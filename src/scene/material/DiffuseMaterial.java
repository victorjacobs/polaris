package scene.material;

import raytracer.Hit;
import raytracer.RayTracer;
import scene.Scene;
import scene.lighting.AmbientLight;
import scene.lighting.Light;

import java.util.HashSet;

public class DiffuseMaterial extends Material {

	public DiffuseMaterial(Color3f baseColor) {
		super(baseColor);
	}
	
	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		Color3f ambientLight = super.getColor(scene, hit, recursionDepth);
		
		if (isInShade(scene, hit)) {
			return ambientLight;
		}
		
		float sumR, sumG, sumB, dotProduct;
		
		sumR = 0;
		sumG = 0;
		sumB = 0;
		
		for (Light light : scene.getLightSources()) {
			if (!(light instanceof AmbientLight)) {
				dotProduct = Math.max(0, hit.getNormal().dotProduct(light.rayTo(hit.getPoint()).normalize()));
				sumR += (light.intensity() * light.color().getRed()) * dotProduct;
				sumG += (light.intensity() * light.color().getGreen()) * dotProduct;
				sumB += (light.intensity() * light.color().getBlue()) * dotProduct;
			}
		}
		
		sumR *= baseColor.getRed();
		sumG *= baseColor.getGreen();
		sumB *= baseColor.getBlue();
		
		return new Color3f(ambientLight.getRed() + Math.min(1, sumR), ambientLight.getGreen() + Math.min(1, sumG), ambientLight.getRed() + Math.min(1, sumB));
	}
	
}
