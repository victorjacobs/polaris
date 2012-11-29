package gui;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import scene.BasicScene;
import scene.GridAcceleratedScene;
import scene.Scene;
import scene.lighting.AmbientLight;
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
		// Display some warnings about debug settings
		if (Settings.FIX_SINGLE_THREAD) {
			cores = 1;
			System.err.println("WARNING: hardcoded to run on one core");
		}

		if (Settings.SHOULD_REPAINT_AFTER_EVERY_PIXEL)
			System.err.println("WARNING: flushing after every pixel draw is not good for performance!");

		if (Settings.COLLECT_STATS)
			System.err.println("WARNING: collecting stats, performance might be affected");

		this.panel = panel;
		this.passes = passes;
	}

	@Deprecated
	public Renderer(Scene scene, CgPanel panel, int passes) {
		this.scene = scene;
		this.panel = panel;
		this.passes = passes;
	}

	// TODO implement this
	public void reloadFile() {
		System.err.println("Reloadfile not yet implemented");
	}

	// TODO add ambient light to SDL
	public void loadSDL(String file) {
		SceneBuilder sceneBuilder = new SceneBuilder(new GridAcceleratedScene(new BasicScene()));

		try {
			scene = sceneBuilder.loadScene(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		AmbientLight aLight = new AmbientLight(new Color3f(1, 1, 1), 0.1f);
		scene.addLightSource(aLight);
	}

	public void loadScene(Scene scene) {
		this.scene = scene;
	}

	@Override
	public Color3f renderPixel(int x, int y) {
		Ray ray = scene.getCamera().rayToPixel(x, y);

		Hit hit = scene.trace(ray);

		// Do shading and color pixel
		Color3f pixelColor;
		if (hit == null) {
			pixelColor = scene.getBackground();
		} else {
			pixelColor = hit.getSurface().getMaterial().getColor(scene, hit);
		}

		return pixelColor;
	}
	
	public void render() {
		scene.preProcess();

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
