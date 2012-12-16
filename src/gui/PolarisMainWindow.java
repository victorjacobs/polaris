package gui;


import demo.*;
import gui.Panel.ScreenPanel;
import raytracer.Settings;
import scene.material.Color3f;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: victor Date: 19/11/12
 * Time: 10:48
 */
// TODO macify it https://github.com/nebulorum/macify
public class PolarisMainWindow extends JFrame implements KeyListener {

	private ScreenPanel renderPanel;
	private JMenuBar menuBar;
	private MainWindowListener listener;
	private File filePickerCurrentDir;
	private Map<String, SceneGenerator> sceneGenerators;

	public PolarisMainWindow() {
		setSize(Settings.SCREEN_X, Settings.SCREEN_Y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Polaris dev");

		this.renderPanel = new ScreenPanel();
		this.menuBar = new JMenuBar();
		this.filePickerCurrentDir = new File(".");
		this.sceneGenerators = new HashMap<String, SceneGenerator>();

		sceneGenerators.put("All effects", new AllEffects());
		sceneGenerators.put("A lot of spheres", new ALotOfSpheres());
		sceneGenerators.put("Teapots", new Teapots());
		sceneGenerators.put("Soft shadows", new SoftShadows());
		sceneGenerators.put("Loads of reflective spheres", new LoadsOfReflectiveSpheres());
		sceneGenerators.put("Environment map", new EnvironmentMapTest());

		sceneGenerators = new TreeMap<String, SceneGenerator>(sceneGenerators);

		layoutMenuBar();
		layoutWindow();

		if (Settings.ENABLE_CAMERA_MOVE_IN_UI)
			addKeyListener(this);
	}

	private void layoutMenuBar() {
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				listener.abortRender(true);

				File file = openDialog("sdl");

				if (file == null) return;

				listener.loadSDL(file.getPath());
				listener.render();
			}
		});
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(open);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {	// TODO move to method
				JFileChooser filePicker = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG", "png");
				filePicker.addChoosableFileFilter(filter);
				filePicker.showSaveDialog(renderPanel);

				renderPanel.saveImage(filePicker.getSelectedFile().getPath() + ".png");
			}
		});
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(save);

		JMenuItem render = new JMenuItem("Render");
		render.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				listener.reload();
				listener.render();
			}
		});
		render.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(render);

		JMenuItem abort = new JMenuItem("Abort");
		abort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				listener.abortRender(true);
			}
		});
		abort.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(abort);

		JMenuItem forceRepaint = new JMenuItem("Force repaint");
		forceRepaint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				getRenderPanel().repaint();
			}
		});
		//abort.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(forceRepaint);

		menuBar.add(fileMenu);

		JMenu demoMenu = new JMenu("Demo");
		JMenuItem item;

		for (final String demoName : sceneGenerators.keySet()) {
			item = new JMenuItem(demoName);

			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent actionEvent) {
					listener.abortRender(true);

					SceneGenerator sg = sceneGenerators.get(demoName);

					listener.applySceneGenerator(sg);

					listener.render();
				}
			});

			demoMenu.add(item);
		}

		menuBar.add(demoMenu);
	}

	private File openDialog(String extension) {
		JFileChooser filePicker = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(extension.toUpperCase(), extension);
		filePicker.addChoosableFileFilter(filter);
		filePicker.setCurrentDirectory(filePickerCurrentDir);
		filePicker.showOpenDialog(renderPanel);

		filePickerCurrentDir = filePicker.getSelectedFile();

		return filePicker.getSelectedFile();
	}

	private void layoutWindow() {
		getContentPane().add(renderPanel);

		renderPanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				int x = mouseEvent.getX();
				int y = mouseEvent.getY();

				if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
					// Just render one pixel
					System.out.println("Rendering pixel (" + x + ", " + y + ")");

					renderPanel.drawPixel(x, y, new Color3f(1, 0, 0));
					renderPanel.flush();

					listener.renderPixel(x, y);
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
							filePicker.showSaveDialog(renderPanel);

							renderPanel.saveImage(filePicker.getSelectedFile().getPath() + ".png");
						}
					});

					context.add(saveToFile);
					context.show(renderPanel, x, y);
				}
			}
		});
	}

	public ScreenPanel getRenderPanel() {
		return renderPanel;
	}

	public void display() {
		setVisible(true);
	}

	public void setListener(MainWindowListener listener) {
		this.listener = listener;
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {

	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		// This is ugly
		switch (keyEvent.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				listener.rotateCamera(0);
				break;
			case KeyEvent.VK_RIGHT:
				listener.rotateCamera(1);
				break;
			case KeyEvent.VK_UP:
				listener.rotateCamera(2);
				break;
			case KeyEvent.VK_DOWN:
				listener.rotateCamera(3);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
