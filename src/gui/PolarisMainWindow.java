package gui;

import raytracer.Settings;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor Date: 19/11/12
 * Time: 10:48
 */
// TODO macify it https://github.com/nebulorum/macify
public class PolarisMainWindow extends JFrame {

	private CgPanel renderPanel;
	private JMenuBar menuBar;
	private MainWindowListener listener;

	public PolarisMainWindow() {
		setSize(Settings.SCREEN_X, Settings.SCREEN_Y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Polaris dev");

		this.renderPanel = new CgPanel();
		this.menuBar = new JMenuBar();

		layoutMenuBar();
		layoutWindow();
	}

	private void layoutMenuBar() {
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				listener.loadSDL(openDialog("sdl").getPath());
				listener.render(16);
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
				listener.render(16);
			}
		});
		render.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(render);

		menuBar.add(fileMenu);
	}

	private File openDialog(String extension) {
		JFileChooser filePicker = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(extension.toUpperCase(), extension);
		filePicker.addChoosableFileFilter(filter);
		//filePicker.setCurrentDirectory(this.class.);
		filePicker.showOpenDialog(renderPanel);

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

	public CgPanel getRenderPanel() {
		return renderPanel;
	}

	public void display() {
		setVisible(true);
	}

	public void setListener(MainWindowListener listener) {
		this.listener = listener;
	}
}
