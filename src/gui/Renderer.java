package gui;

import raytracer.Hit;
import raytracer.Ray;
import scene.Scene;
import scene.material.Color3f;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Renderer {
	ExecutorService threadPool;
	
	private Scene scene;
	private final CgPanel panel;
	private final int cores = Runtime.getRuntime().availableProcessors();
	private long startTime;

	public Renderer(Scene scene, CgPanel panel) {
		threadPool = Executors.newFixedThreadPool(cores);
		this.scene = scene;
		this.panel = panel;

		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				int x = mouseEvent.getX();
				int y = mouseEvent.getY();

				if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
					// Just render one pixel
					System.out.println("Rendering pixel (" + x + ", " + y + ")");

					renderPixel(x, y);
				} else {
					// Show context menu
					JPopupMenu context = new JPopupMenu();
					JMenuItem saveToFile = new JMenuItem("Save");

					saveToFile.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent arg0) {
							JFileChooser filePicker = new JFileChooser();
							FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
							filePicker.addChoosableFileFilter(filter);
							filePicker.showSaveDialog(Renderer.this.panel);

							Renderer.this.panel.saveImage(filePicker.getSelectedFile().getPath() + ".png");
						}
					});

					context.add(saveToFile);
					context.show(Renderer.this.panel, x, y);
				}
			}
		});

	}

	private Color3f renderPixel(int x, int y) {
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
