package gui;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.Scene;
import scene.material.Color;

public class Renderer {
	
	private Scene scene;
	private CgPanel panel;
	private RayTracer rayTracer;
	
	public Renderer(Scene scene, CgPanel panel) {
		this.scene = scene;
		this.panel = panel;
		this.rayTracer = new RayTracer(scene);
	}
	
	public void render(int depth) {
		Ray ray;
		Hit hit;
		
		for (int x = 1; x < panel.getWidth(); x += depth) {
			for (int y = 1; y < panel.getHeight(); y += depth) {
				ray = scene.getCamera().rayToPixel(x, y);
				
				hit = rayTracer.trace(ray);
				
				// Do shading and color pixel
				Color pixelColor;
				if (hit == null) {
					pixelColor = new Color(0, 0, 0);
				} else {
					pixelColor = hit.getSurface().getMaterial().getColor(scene.getLightSources(), hit, rayTracer);
				}
				
				
				// Paint pixel
				// TODO cache pixels
				
				for (int i = x; i < x + depth; i++) {
					for (int j = y; j < y + depth; j++) {
						panel.drawPixel(i, j, pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue());
						panel.repaint();
					}
				}
			}
		}
		
		if (depth == 1) return;
		
		render(depth / 2);
	}
	
}
