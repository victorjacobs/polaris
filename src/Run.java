import java.awt.Color;

import javax.swing.JFrame;


public class Run {
	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		Camera camera = new Camera(new Vector3f(-10, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1), 10);
		RayTracer rayTracer = new RayTracer(panel, camera);
		rayTracer.addSurface(new Triangle(10, 5, 5, 15, 2, 3, 10, 7, 8, Color.GREEN));
		rayTracer.trace();
	}
}
