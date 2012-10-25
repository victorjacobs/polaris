package raytracer;
import geometry.Surface;
import gui.CgPanel;

import java.awt.Color;
import java.util.HashSet;

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
		float lowestT = Float.POSITIVE_INFINITY;
		
		for (int x = 1; x < panel.getWidth(); x++) {
			for (int y = 1; y < panel.getHeight(); y++) {
				ray = new Ray(camera, x, y);
				
				for (Surface surf : surfaces) {
					surfaceColor = surf.getColor();
					hit = surf.hit(ray, 0, lowestT);
					
					if (hit != null) {
						// Shading, maybe refactor to own method
						float dotProduct;
						float sumR;
						
						
						for (PointLight light : lightSources) {
							//dotProduct = Math.max(0, surf.normalInHitPoint().dotProduct(light.));
						}
						
						panel.drawPixel(x, y, surfaceColor.getRed() / 255, surfaceColor.getGreen() / 255, surfaceColor.getBlue() / 255);
						lowestT = hit.getT();
					}
				}
				// Reset t for next pixel
				lowestT = Float.POSITIVE_INFINITY;
			}
		}
	}
}
