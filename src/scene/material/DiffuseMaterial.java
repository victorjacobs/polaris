package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.lighting.Light;

public class DiffuseMaterial implements Material {

	private Color3f baseColor3f;
	
	public DiffuseMaterial(Color3f baseColor3f) {
		this.baseColor3f = baseColor3f;
	}
	
	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer) {
		float sumR, sumG, sumB, dotProduct;
		
		sumR = 0;
		sumG = 0;
		sumB = 0;
		
		// TODO: p84 paragraph 4.5.4
		for (Light light : lights) {
			// TODO Shadows: ray from hit point to light source
			// TODO move ambient lighting somewhere else
			
			if (tracer.trace(new Ray(hit.getPoint(), light.rayTo(hit.getPoint())), 0.01f) == null) {
				// doesn't hit something
				dotProduct = Math.max(0, hit.getNormal().dotProduct(light.rayTo(hit.getPoint()).normalize()));
				sumR += baseColor3f.getRed() * (0.1f + (light.intensity() * light.color().getRed()) * dotProduct);
				sumG += baseColor3f.getGreen() * (0.1f + (light.intensity() * light.color().getGreen()) * dotProduct);
				sumB += baseColor3f.getBlue() * (0.1f + (light.intensity() * light.color().getBlue()) * dotProduct);
			} else {
				sumR = 0.1f;
				sumG = 0.1f;
				sumB = 0.1f;
			}
			
		}
		
		return new Color3f(Math.min(1, sumR), Math.min(1, sumG), Math.min(1, sumB));
	}
	
}
