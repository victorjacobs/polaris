package gui;

import raytracer.Hit;
import raytracer.Ray;
import scene.Scene;
import scene.lighting.AmbientLight;
import scene.material.Color3f;
import scene.parser.SceneBuilder;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Renderer implements MainWindowListener{
	ExecutorService threadPool;
	
	private Scene scene;
	private final CgPanel panel;
	private final int cores = Runtime.getRuntime().availableProcessors();
	private long startTime;

	public Renderer(CgPanel panel) {
		this(null, panel);
	}

	public Renderer(Scene scene, CgPanel panel) {
		threadPool = Executors.newFixedThreadPool(cores);
		this.scene = scene;
		this.panel = panel;

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
	
	public void render(int startDepth) {
		panel.clear();

		startTime = System.currentTimeMillis();
		// Split up the canvas in blocks to dispatch to the threadpool
		for (int i = 0; i < cores; i++) {
			threadPool.execute(new renderJob(i, startDepth));
		}
	}
	
	private class renderJob implements Runnable {
		private int sliceNo;
		private int depth;
		
		public renderJob(int sliceNo, int depth) {
			this.sliceNo = sliceNo;
			this.depth = depth;
		}

		@Override
		public void run() {
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
				}
			}

			panel.repaint();

			if (depth == 1) {
				long endTime = System.currentTimeMillis();
				System.out.println("Slice " + sliceNo + " took " + Math.round((endTime - startTime) / 1000) + "s");
				return;
			}
			
			threadPool.execute(new renderJob(sliceNo, depth / 2));
		}
	}
}
