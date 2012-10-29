package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.lighting.AmbientLight;
import scene.lighting.Light;

public class DiffuseMaterial implements Material {

	private Color3f baseColor;
	private boolean isInShade = false;
	
	public DiffuseMaterial(Color3f baseColor) {
		this.baseColor = baseColor;
	}
	
	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer) {
		float sumR, sumG, sumB, dotProduct;
		
		sumR = 0;
		sumG = 0;
		sumB = 0;
		
		// TODO: p84 paragraph 4.5.4
		for (Light light : lights) {
			if (light instanceof AmbientLight) {
				// Always add ambient lighting
				sumR += light.intensity() * light.color().getRed();
				sumG += light.intensity() * light.color().getGreen();
				sumB += light.intensity() * light.color().getBlue();
			} else {
				if (tracer.traceAny(new Ray(hit.getPoint(), light.rayTo(hit.getPoint())), 0.01f) == null) {
					// doesn't hit something
					dotProduct = Math.max(0, hit.getNormal().dotProduct(light.rayTo(hit.getPoint()).normalize()));
					sumR += (light.intensity() * light.color().getRed()) * dotProduct;
					sumG += (light.intensity() * light.color().getGreen()) * dotProduct;
					sumB += (light.intensity() * light.color().getBlue()) * dotProduct;
				} else {
					isInShade = true;
				}
			}
			
		}
		
		sumR *= baseColor.getRed();
		sumG *= baseColor.getGreen();
		sumB *= baseColor.getBlue();
		
		return new Color3f(Math.min(1, sumR), Math.min(1, sumG), Math.min(1, sumB));
	}
	
	public boolean isInShade() {
		return this.isInShade;
	}
	
}
