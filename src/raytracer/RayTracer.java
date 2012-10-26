package raytracer;
import gui.CgPanel;

import java.awt.Color;

import scene.Scene;
import scene.geometry.Surface;
import scene.lighting.Light;

public class RayTracer {
	public static final int SCREEN_X = 640;
	public static final int SCREEN_Y = 480;
	
	private CgPanel panel;
	private Scene scene;
	
	public RayTracer(CgPanel panel, Scene scene) {
		this.panel = panel;
		this.scene = scene;
	}
	
	public void trace() {
		// Go over all Surfaces and paint them
		Ray ray;
		Color surfaceColor;
		Hit hit;
		float dotProduct;
		float sumR = 0;
		float sumG = 0;
		float sumB = 0;
		
		float lowestT = Float.POSITIVE_INFINITY;
		Hit closestHit = null;
		
		long start = System.currentTimeMillis();
		
		for (int x = 1; x < panel.getWidth(); x++) {
			for (int y = 1; y < panel.getHeight(); y++) {
				ray = new Ray(scene.getCamera(), x, y);
				
				for (Surface surf : scene.getSurfaces()) {
					hit = surf.hit(ray, 0, lowestT);
					
					if (hit != null) {
						lowestT = hit.getT();
						closestHit = hit;
					}
				}
				
				// Do shading and color pixel
				
				if (closestHit != null) {
					surfaceColor = closestHit.getSurface().getMaterial().getColor();
					
					// Shading, TODO refactor to own method
					sumR = 0;
					sumG = 0;
					sumB = 0;
					
					// TODO: p84 paragraph 4.5.4
					for (Light light : scene.getLightSources()) {
						// TODO Shadows: ray from hit point to light source
						
						dotProduct = Math.max(0, closestHit.getNormal().dotProduct(light.rayTo(closestHit.getPoint()).normalize()));
						sumR += surfaceColor.getRed() / 255 * (0.1f + (light.intensity() * light.color().getRed()) / 255 * dotProduct);
						sumG += surfaceColor.getGreen() / 255 * (0.1f + (light.intensity() * light.color().getGreen()) / 255 * dotProduct);
						sumB += surfaceColor.getBlue() / 255 * (0.1f + (light.intensity() * light.color().getBlue()) / 255 * dotProduct);
					}
					// Paint pixel
					panel.drawPixel(x, y, Math.min(1, sumR), Math.min(1, sumG), Math.min(1, sumB));
					closestHit = null;
				}
				
				// Reset t for next pixel
				lowestT = Float.POSITIVE_INFINITY;
			}
		}
		
		long stop = System.currentTimeMillis();
		
		System.out.println("Render completed in " + Math.round((stop - start) / 1000) + "s");
	}
	
	
}
