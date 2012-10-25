package raytracer;
import gui.CgPanel;

import java.awt.Color;
import java.util.HashSet;

import scene.Camera;
import scene.geometry.Surface;
import scene.lighting.PointLight;



// TODO werken met een Scene object ipv alles hier apart toe te voegen
public class RayTracer {
	public static final int SCREEN_X = 640;
	public static final int SCREEN_Y = 480;
	
	private CgPanel panel;
	private HashSet<Surface> surfaces;
	private HashSet<PointLight> lightSources;
	private Camera camera;
	
	public RayTracer(CgPanel panel, Camera cam) {
		this.panel = panel;
		this.surfaces = new HashSet<Surface>();
		this.lightSources = new HashSet<PointLight>();
		this.camera = cam;
	}
	
	public void addSurface(Surface surf) {
		surfaces.add(surf);
	}
	
	public void addLightSource(PointLight light) {
		lightSources.add(light);
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
				ray = new Ray(camera, x, y);
				
				for (Surface surf : surfaces) {
					hit = surf.hit(ray, 0, lowestT);
					
					if (hit != null) {
						lowestT = hit.getT();
						closestHit = hit;
					}
				}
				
				// Do shading and color pixel
				
				if (closestHit != null) {
					surfaceColor = closestHit.getSurface().getColor();
					
					// Shading, TODO refactor to own method
					sumR = 0;
					sumG = 0;
					sumB = 0;
					
					// TODO: p84 paragraph 4.5.4
					for (PointLight light : lightSources) {
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
