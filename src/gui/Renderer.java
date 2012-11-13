package gui;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.Scene;
import scene.material.Color3f;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Renderer {
	ExecutorService threadPool;
	
	private Scene scene;
	private CgPanel panel;
	private final int cores = Runtime.getRuntime().availableProcessors();
	
	public Renderer(Scene scene, CgPanel panel) {
		threadPool = Executors.newFixedThreadPool(cores);
		this.scene = scene;
		this.panel = panel;

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				System.out.println(mouseEvent.getXOnScreen());
			}
		});
	}

	private Color3f renderPixel(int x, int y) {
		Ray ray = scene.getCamera().rayToPixel(x, y);

		Hit hit = scene.trace(ray);

		// Do shading and color pixel
		Color3f pixelColor;
		if (hit == null) {
			pixelColor = new Color3f(0, 0, 0);
		} else {
			pixelColor = hit.getSurface().getMaterial().getColor(scene, hit);
		}

		return pixelColor;
	}
	
	public void render(int startDepth) {
		// Split up the canvas in blocks to dispatch to the threadpool
		for (int i = 0; i < cores; i++) {
			threadPool.execute(new renderJob(i, startDepth));
		}
	}
	
	private class renderJob implements Runnable {
		private int sliceNo;
		private int depth;
		
		public renderJob(int sliceNo, int depth) {
			System.out.println("Job for slice " + sliceNo + " depth " + depth + " queued for execution");

			this.sliceNo = sliceNo;
			this.depth = depth;
		}

		@Override
		public void run() {
			System.out.println("Job for slice " + sliceNo + " depth " + depth + " started execution");
			
			long start = System.currentTimeMillis();
			Color3f pixelColor;
			
			for (int x = sliceNo * (panel.getWidth() / cores) + 1; x <= (sliceNo + 1) * (panel.getWidth() / cores); x += depth) {
				for (int y = 1; y < panel.getHeight(); y += depth) {

					//if (x % (depth * 2) == 0 && y % (depth * 2) == 0) break;

					pixelColor = renderPixel(x, y);

					// Paint pixel

					for (int i = x; i < x + depth; i++) {
						for (int j = y; j < y + depth; j++) {
							panel.drawPixel(i, j, pixelColor);
						}
					}

					panel.repaint();
				}
			}
			

			long end = System.currentTimeMillis();
			
			System.out.println("Job for slice " + sliceNo + " depth " + depth + " finished execution");
			
			if (depth == 1) return;
			
			threadPool.execute(new renderJob(sliceNo, depth / 2));
			
			System.out.println("Render completed in " + Math.round((end - start) / 1000) + "s");
		}
	}
}
