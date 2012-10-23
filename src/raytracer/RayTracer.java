package raytracer;
import geometry.Surface;
import gui.CgPanel;

import java.awt.Color;
import java.util.HashSet;


public class RayTracer {
	public static final int SCREEN_X = 640;
	public static final int SCREEN_Y = 480;
	
	private CgPanel panel;
	private HashSet<Surface> surfaces;
	private Camera camera;
	
	public RayTracer(CgPanel panel, Camera cam) {
		this.panel = panel;
		this.surfaces = new HashSet<Surface>();
		this.camera = cam;
	}
	
	public void addSurface(Surface surf) {
		surfaces.add(surf);
	}
	
	public void trace() {
		// Go over all Surfaces and paint them
		Ray ray;
		Color surfaceColor;
		
		for (int x = 1; x < panel.getHeight(); x++) {
			for (int y = 1; y < panel.getWidth(); y++) {
				ray = new Ray(camera, x, y);
				
				for (Surface surf : surfaces) {
					surfaceColor = surf.getColor();
					if (surf.hit(ray, 0, Float.POSITIVE_INFINITY)) {
						panel.drawPixel(x, y, surfaceColor.getRed() / 255, surfaceColor.getGreen() / 255, surfaceColor.getBlue() / 255);
					}
				}
			}
		}
	}
}
