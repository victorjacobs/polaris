package gui;

import gui.Panel.ScreenPanel;
import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import raytracer.Stats;
import scene.BasicScene;
import scene.GridAcceleratedScene;
import scene.Scene;
import scene.data.Vector3f;
import scene.material.Color3f;
import scene.parser.SceneBuilder;

import java.io.FileNotFoundException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class Renderer implements MainWindowListener {
	private ExecutorService threadPool;
	
	private Scene scene;
	private final ScreenPanel panel;
	private int passes;
	private int cores;
	private long startTime;
	private String loadedSDL = null;

	public Renderer(ScreenPanel panel, int passes) {
		this(panel, null, passes);
	}

	public Renderer(ScreenPanel panel, Scene scene, int passes) {
		this.panel = panel;
		this.passes = passes;
		this.scene = scene;
		this.cores = Runtime.getRuntime().availableProcessors();

		// Display some warnings about debug settings
		if (Settings.SOFT_SHADOW_SAMPLES == 1)
			System.err.println("WARNING: soft shadows disabled");

		if (Settings.COLLECT_STATS) {
			cores = 1;
			System.err.println("WARNING: collecting stats, running single threaded!");
		}

		if (Settings.FIX_SINGLE_THREAD) {
			cores = 1;
			System.err.println("WARNING: hardcoded to run on one core");
		}

		if (Settings.SHOULD_REPAINT_AFTER_EVERY_PIXEL)
			System.err.println("WARNING: flushing after every pixel draw is not good for performance!");

		if (Settings.AA != 1)
			System.err.println("WARNING: AA enabled");
	}

	public void reloadFile() {
		if (loadedSDL == null)
			return;

		loadSDL(loadedSDL);
	}

	// TODO fails for opening new files
	public void loadSDL(String file) {
		loadedSDL = file;

		SceneBuilder sceneBuilder;

		if (scene == null) {
			sceneBuilder = new SceneBuilder(new GridAcceleratedScene(new BasicScene()));
		} else {
			scene.clear();
			sceneBuilder = new SceneBuilder(scene);
		}

		try {
			scene = sceneBuilder.loadScene(file);
		} catch (FileNotFoundException e) {
			System.err.println("SDL file " + file + " not found.");
		}

	}

	public void loadScene(Scene scene) {
		this.scene = scene;
	}

	@Override
	public Color3f renderPixel(int x, int y) {
		if (Settings.COLLECT_STATS)
			Stats.resetIntersections();

		Ray ray;
		Hit hit;
		Color3f pixelColor;
		Vector3f unclampedColor = new Vector3f();

		if (Settings.AA == 1) {
			ray = scene.getCamera().rayToPixel(x, y);

			hit = scene.trace(ray);

			// Do shading and color pixel
			if (hit == null) {
				pixelColor = scene.getBackground();
			} else {
				pixelColor = hit.getSurface().getMaterial().getColor(scene, hit);
			}
		} else {
			Random rand = new Random();

			for (int i = 0; i < Settings.AA; i++) {
				for (int j = 0; j < Settings.AA; j++) {
					float p = (i + rand.nextFloat()) / Settings.AA;
					float q = (j + rand.nextFloat()) / Settings.AA;

					ray = scene.getCamera().rayToPixel(x, y, p, q);
					hit = scene.trace(ray, Settings.EPS);

					if (hit == null) {
						unclampedColor = unclampedColor.sum(new Vector3f(scene.getBackground()));
					} else {
						unclampedColor = unclampedColor.sum(new Vector3f(hit.getSurface().getMaterial().getColor(scene, hit)));
					}
				}
			}

			pixelColor = new Color3f(unclampedColor.divideBy(Settings.AA * Settings.AA));
		}

		if (Settings.INTERSECTION_TESTS_FALSE_COLOR) {
			float color = (float)Math.log((Stats.getNumIntersections()) + 1) / 10;

			return new Color3f(color, color, color);
		} else {
			return pixelColor;
		}
	}
	
	public void render() {
		long startTime = System.currentTimeMillis();

		scene.preProcess();

		long duration = System.currentTimeMillis() - startTime;

		if (Settings.COLLECT_STATS)
			Stats.setStructureBuildTime(duration);

		System.out.println("Building acceleration structure took " + duration + "ms");

		Runtime rt = Runtime.getRuntime();

		int memUsage = (int)(rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);

		if (Settings.COLLECT_STATS)
			Stats.setMemoryUsage(memUsage);

		System.out.println("Memory usage before render start: " + memUsage + "mb");

		if (threadPool != null) {
			abortRender(false);
		} else {
			this.threadPool = Executors.newFixedThreadPool(cores);
		}

		this.startTime = System.currentTimeMillis();
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

	// NOTE: this only works when rendering in one pass
	public void join() {
		threadPool.shutdown();

		try {
			threadPool.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
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
			panel.flush();

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
