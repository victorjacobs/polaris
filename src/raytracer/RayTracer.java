package raytracer;
import gui.CgPanel;

import scene.material.Color;

import scene.Camera;
import scene.Scene;
import scene.geometry.Surface;

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
		Ray ray;
		Hit hit, closestHit = null;
		Camera cam = scene.getCamera();
		
		float lowestT;
		
		long start = System.currentTimeMillis();
		
		for (int x = 1; x < panel.getWidth(); x++) {
			for (int y = 1; y < panel.getHeight(); y++) {
				// Reset t for next pixel
				lowestT = Float.POSITIVE_INFINITY;
				
				ray = cam.rayToPixel(x, y);
				
				for (Surface surf : scene.getSurfaces()) {
					hit = surf.hit(ray, 0, lowestT);
					
					if (hit != null) {
						lowestT = hit.getT();
						closestHit = hit;
					}
				}
				
				// Do shading and color pixel
				
				if (closestHit != null) {
					Color pixelColor = closestHit.getSurface().getMaterial().getColor(scene.getLightSources(), closestHit);

					// Paint pixel
					panel.drawPixel(x, y, pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue());
					//System.out.println("Drawing on (" + x + ", " + y + "), color " + pixelColor);
					
					closestHit = null;
				}
			}
		}
		
		long stop = System.currentTimeMillis();
		
		System.out.println("Render completed in " + Math.round((stop - start) / 1000) + "s");
	}
	
	
}
