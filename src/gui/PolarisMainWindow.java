package gui;

import com.apple.eawt.Application;
import raytracer.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: victor Date: 19/11/12
 * Time: 10:48
 */
public class PolarisMainWindow extends JFrame {

	private CgPanel renderPanel;
	private JMenuBar menuBar;

	public PolarisMainWindow() {
		Application.getApplication();

		setSize(Settings.SCREEN_X, Settings.SCREEN_Y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Polaris dev");

		this.renderPanel = new CgPanel();
		this.menuBar = new JMenuBar();

		layoutWindow();
	}

	private void layoutWindow() {
		getContentPane().add(renderPanel);
		setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("File");
		JMenuItem open = new JMenuItem("Open");
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				System.out.println("Should implement this");
			}
		});
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(open);

		JMenuItem save = new JMenuItem("Save");
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				System.out.println("Should implement this");
			}
		});
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,  Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
		fileMenu.add(save);

		menuBar.add(fileMenu);
	}

	public CgPanel getRenderPanel() {
		return renderPanel;
	}

	public void display() {
		setVisible(true);
	}
}
