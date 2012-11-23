package gui;

import raytracer.Hit;
import raytracer.Ray;
import scene.Scene;
import scene.lighting.AmbientLight;
import scene.material.Color3f;
import scene.parser.SceneBuilder;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class Renderer implements MainWindowListener {
	ExecutorService threadPool;
	
	private Scene scene;
	private final CgPanel panel;
	private int passes;
	private final int cores = Runtime.getRuntime().availableProcessors();
	private long startTime;

	public Renderer(CgPanel panel, int passes) {
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

	}

	// TODO add ambient light to SDL
	public void loadSDL(String file) {
		SceneBuilder sceneBuilder = new SceneBuilder();

		try {
			scene = sceneBuilder.loadScene(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		AmbientLight aLight = new AmbientLight(new Color3f(1, 1, 1), 0.1f);
		scene.addLightSource(aLight);
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
		if (threadPool != null) {
			System.out.println("Shutting down threadpool for new render");
			List<Runnable> threads = threadPool.shutdownNow();
			try {
				threadPool.awaitTermination(500, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		threadPool = Executors.newFixedThreadPool(cores);

		panel.clear();

		startTime = System.currentTimeMillis();
		// Split up the canvas in blocks to dispatch to the threadpool
		for (int i = 0; i < cores; i++) {
			threadPool.execute(new renderJob(i, passes));
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
				for (int y = 1; y < panel.getHeight(); y += currentDepth) {

					if (currentDepth != passes) {
						//if (x % (currentDepth * 2) == 0 && y % (currentDepth * 2) == 0) break;
					}

					pixelColor = renderPixel(x, y);

					// Paint pixel

					for (int i = x; i < x + currentDepth; i++) {
						for (int j = y; j < y + currentDepth; j++) {
							panel.drawPixel(i, j, pixelColor);
						}
					}
				}
			}

			panel.repaint();

			if (currentDepth == 1) {
				long endTime = System.currentTimeMillis();
				System.out.println("Slice " + sliceNo + " took " + Math.round((endTime - startTime) / 1000) + "s");
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
