package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.RayTracer;
import scene.lighting.AmbientLight;
import scene.lighting.Light;

public class DiffuseMaterial extends Material {

	public DiffuseMaterial(Color3f baseColor) {
		super(baseColor);
	}
	
	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer) {
		Color3f ambientLight = super.getColor(lights, hit, tracer);
		
		if (isInShade(lights, hit, tracer)) {
			return ambientLight;
		}
		
		float sumR, sumG, sumB, dotProduct;
		
		sumR = 0;
		sumG = 0;
		sumB = 0;
		
		// TODO: p84 paragraph 4.5.4
		for (Light light : lights) {
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
