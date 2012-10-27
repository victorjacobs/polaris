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
	
	public void trace(int depth) {
		long start = System.currentTimeMillis();
		
		traceRecursiveStep(depth);
		
		long stop = System.currentTimeMillis();
		
		System.out.println("Render completed in " + Math.round((stop - start) / 1000) + "s");
	}
	
	public void traceRecursiveStep(int depth) {
		Ray ray;
		Hit hit, closestHit = null;
		Camera cam = scene.getCamera();
		
		float lowestT;
		
		for (int x = 1; x < panel.getWidth(); x += depth) {
			for (int y = 1; y < panel.getHeight(); y += depth) {
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
				Color pixelColor;
				if (closestHit == null) {
					pixelColor = new Color(0, 0, 0);
				} else {
					pixelColor = closestHit.getSurface().getMaterial().getColor(scene.getLightSources(), closestHit);
				}
				
				
				// Paint pixel
				// TODO cache pixels
				
				for (int i = x; i < x + depth; i++) {
					for (int j = y; j < y + depth; j++) {
						panel.drawPixel(i, j, pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue());
						panel.repaint();
					}
				}
				
				closestHit = null;
			}
		}
		
		if (depth == 1) return;
		
		trace(depth / 2);
	}
	
	
}
