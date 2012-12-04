package gui;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import raytracer.Stats;
import scene.BasicScene;
import scene.GridAcceleratedScene;
import scene.Scene;
import scene.material.Color3f;
import scene.parser.SceneBuilder;

import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class Renderer implements MainWindowListener {
	ExecutorService threadPool;
	
	private Scene scene;
	private final CgPanel panel;
	private int passes;
	private int cores = Runtime.getRuntime().availableProcessors();
	private long startTime;

	public Renderer(CgPanel panel, int passes) {
		this(panel, null, passes);
	}

	public Renderer(CgPanel panel, Scene scene, int passes) {
		// Display some warnings about debug settings
		if (Settings.COLLECT_STATS) {
			System.err.println("WARNING: collecting stats, running single threaded!");
			cores = 1;
		}

		if (Settings.FIX_SINGLE_THREAD) {
			cores = 1;
			System.err.println("WARNING: hardcoded to run on one core");
		}

		if (Settings.SHOULD_REPAINT_AFTER_EVERY_PIXEL)
			System.err.println("WARNING: flushing after every pixel draw is not good for performance!");

		if (Settings.AA != 1)
			System.err.println("WARNING: AA enabled");

		this.panel = panel;
		this.passes = passes;
		this.scene = scene;
	}

	// TODO implement this
	public void reloadFile() {
		System.err.println("Reloadfile not yet implemented");
	}

	public void loadSDL(String file) {
		SceneBuilder sceneBuilder;

		if (scene == null) {
			sceneBuilder = new SceneBuilder(new GridAcceleratedScene(new BasicScene()));
		} else {
			sceneBuilder = new SceneBuilder(scene);
		}

		try {
			scene = sceneBuilder.loadScene(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void loadScene(Scene scene) {
		this.scene = scene;
	}

	@Override
	public Color3f renderPixel(int x, int y) {
		if (Settings.COLLECT_STATS)
			Stats.resetIntersections();

		Ray ray = scene.getCamera().rayToPixel(x, y);

		Hit hit = scene.trace(ray);

		// Do shading and color pixel
		Color3f pixelColor;
		if (hit == null) {
			pixelColor = scene.getBackground();
		} else {
			pixelColor = hit.getSurface().getMaterial().getColor(scene, hit);
		}

		if (Settings.COLLECT_STATS) {
			float color = (float)Math.log((Stats.getNumIntersections()) + 1) / 10;

			return new Color3f(color, color, color);
		} else {
			return pixelColor;
		}
	}
	
	public void render() {
		scene.preProcess();

		Runtime rt = Runtime.getRuntime();

		System.out.println("Memory usage before render start: " + (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024) + "mb");

		if (threadPool != null) {
			abortRender(false);
		} else {
			this.threadPool = Executors.newFixedThreadPool(cores);
		}

		startTime = System.currentTimeMillis();
		// Split up the canvas in blocks to dispatch to the threadpool
		for (int i = 0; i < cores; i++) {
			threadPool.execute(new renderJob(i, passes));
		}
	}

	@Override
	public void abortRender(boolean shouldFlush) {
		System.out.println("Shutting down threadpool");

		try {
			threadPool.shutdownNow();
			threadPool.awaitTermination(100, TimeUnit.MILLISECONDS);

			threadPool = Executors.newFixedThreadPool(cores);
		} catch (RejectedExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException f) {
			f.printStackTrace();
		} finally {
			if (shouldFlush) {
				panel.repaint();
			} else {
				panel.clear();
			}
		}
	}

	private class renderJob implements Runnable {
		private int sliceNo;
		private int currentDepth;
		
		public renderJob(int sliceNo, int currentDepth) {
			this.sliceNo = sliceNo;
			this.currentDepth = currentDepth;
		}

		@Override
		public void run() {
			Color3f pixelColor;
			
			for (int x = sliceNo * (panel.getWidth() / cores) + 1; x <= (sliceNo + 1) * (panel.getWidth() / cores); x += currentDepth) {
				// FIXED: shutdownNow on threadpool will call interrupt() on all threads, we should be so kind to do something with it
				if (Thread.currentThread().isInterrupted()) return;

				for (int y = 1; y < panel.getHeight(); y += currentDepth) {

					if (currentDepth != passes) {
						//if (x % (currentDepth * 2) == 0 && y % (currentDepth * 2) == 0) break;
					}

					pixelColor = renderPixel(x, y);

					// Paint pixel

					for (int i = x; i < x + currentDepth; i++) {
						for (int j = y; j < y + currentDepth; j++) {
							panel.drawPixel(i, j, pixelColor);
							if (Settings.SHOULD_REPAINT_AFTER_EVERY_PIXEL) panel.repaint();
						}
					}
				}
			}

			panel.repaint();

			if (currentDepth == 1) {
				long endTime = System.currentTimeMillis();
				System.out.println("Slice " + sliceNo + " took " + (endTime - startTime) + "ms");
				return;
			}

			try {
				threadPool.execute(new renderJob(sliceNo, currentDepth / 2));
			} catch (RejectedExecutionException e) {
				// Do nothing here, pool is shutting down
			}
		}
	}
}
