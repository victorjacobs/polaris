package gui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.Scene;
import scene.material.Color3f;

public class RendererParallel {
	ExecutorService threadPool;
	
	private Scene scene;
	private CgPanel panel;
	private RayTracer rayTracer;
	private final int cores = Runtime.getRuntime().availableProcessors();
	
	public RendererParallel(Scene scene, CgPanel panel) {
		threadPool = Executors.newFixedThreadPool(cores);
		this.scene = scene;
		this.panel = panel;
		this.rayTracer = new RayTracer(scene);
	}
	
	public void render(int startDepth) {
		// Split up the canvas in blocks to dispatch to the threadpool
		for (int i = 0; i < cores; i++) {
			threadPool.execute(new renderJob(threadPool, scene, panel, rayTracer, i, startDepth));
		}
	}
	
	private class renderJob implements Runnable {
		private Scene scene;
		private CgPanel panel;
		private RayTracer rayTracer;
		private int sliceNo;
		private ExecutorService threadPool;
		private int depth;
		
		public renderJob(ExecutorService pool, Scene scene, CgPanel panel, RayTracer tracer, int sliceNo, int depth) {
			System.out.println("Job for slice " + sliceNo + " depth " + depth + " queued for execution");
			
			this.scene = scene;
			this.panel = panel;
			this.rayTracer = tracer;
			this.sliceNo = sliceNo;
			this.threadPool = pool;
			this.depth = depth;
		}

		@Override
		public void run() {
			System.out.println("Job for slice " + sliceNo + " depth " + depth + " started execution");
			
			Hit hit;
			Ray ray;
			
			long start = System.currentTimeMillis();
			
			for (int x = sliceNo * (panel.getWidth() / cores) + 1; x <= (sliceNo + 1) * (panel.getWidth() / cores); x += depth) {
				for (int y = 1; y < panel.getHeight(); y += depth) {
					
					ray = scene.getCamera().rayToPixel(x, y);
					
					hit = rayTracer.trace(ray);
					
					// Do shading and color pixel
					Color3f pixelColor;
					if (hit == null) {
						pixelColor = new Color3f(0, 0, 0);
					} else {
						pixelColor = hit.getSurface().getMaterial().getColor(scene.getLightSources(), hit, rayTracer);
					}
					
					// Fill cache
					
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
			
			threadPool.execute(new renderJob(threadPool, scene, panel, rayTracer, sliceNo, depth / 2));
			
			System.out.println("Render completed in " + Math.round((end - start) / 1000) + "s");
		}
	}
}
