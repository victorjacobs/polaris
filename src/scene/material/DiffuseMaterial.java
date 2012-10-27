package scene.material;

import scene.material.Color;
import java.util.HashSet;

import raytracer.Hit;
import scene.lighting.Light;

public class DiffuseMaterial implements Material {

	private Color baseColor;
	
	public DiffuseMaterial(Color baseColor) {
		this.baseColor = baseColor;
	}
	
	@Override
	public Color getColor(HashSet<Light> lights, Hit hit) {
		float sumR, sumG, sumB, dotProduct;
		
		// Shading, TODO refactor to own method
		sumR = 0;
		sumG = 0;
		sumB = 0;
		
		// TODO: p84 paragraph 4.5.4
		for (Light light : lights) {
			// TODO Shadows: ray from hit point to light source
			// TODO move ambient lighting somewhere else
			
			dotProduct = Math.max(0, hit.getNormal().dotProduct(light.rayTo(hit.getPoint()).normalize()));
			sumR += baseColor.getRed() * (0.1f + (light.intensity() * light.color().getRed()) * dotProduct);
			sumG += baseColor.getGreen() * (0.1f + (light.intensity() * light.color().getGreen()) * dotProduct);
			sumB += baseColor.getBlue() * (0.1f + (light.intensity() * light.color().getBlue()) * dotProduct);
		}
		
		return new Color(Math.min(1, sumR), Math.min(1, sumG), Math.min(1, sumB));
	}
	
}
