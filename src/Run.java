import geometry.Model;
import geometry.Vector3f;
import gui.CgPanel;

import javax.swing.JFrame;

import raytracer.Camera;
import raytracer.RayTracer;


public class Run {
	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		// TODO: why the hell moet up vector naar beneden gericht zijn?
		Camera camera = new Camera(new Vector3f(0, 0, -10), new Vector3f(0, 0, 1), new Vector3f(0, -1, 0), 5);
		RayTracer rayTracer = new RayTracer(panel, camera);
		
		// Fancy driehoek, non overlapping
//		Surface triag1 = new Triangle(5, 0, 10, 5, -7, -3, 5, 0, 0, Color.CYAN);
//		Surface triag2 = new Triangle(5, 0, 10, 5, 7, -3, 5, 0, 0, Color.RED);
//		Surface triag3 = new Triangle(5, 0, 0, 5, -7, -3, 5, 7, -3, Color.BLUE);
//		
//		rayTracer.addSurface(triag1);
//		rayTracer.addSurface(triag2);
//		rayTracer.addSurface(triag3);
		
		// Driehoeken, overlapping
//		Surface triag1 = new Triangle(5, 0, 10, 5, 5, 0, 5, -5, 0, Color.BLUE);
//		Surface triag2 = new Triangle(3, 0, 2, 3, 3, 0, 3, -3, 0, Color.GREEN);
//		
//		rayTracer.addSurface(triag1);
//		rayTracer.addSurface(triag2);
		
		// Sphere, overlapping
		// TODO als rand overlapt met andere bol
//		Surface sphere1 = new Sphere(new Vector3f(100, 5, 0), 20, Color.GREEN);
//		Surface sphere2 = new Sphere(new Vector3f(20, 0, 0), 10, Color.YELLOW);
//		
//		rayTracer.addSurface(sphere1);
//		rayTracer.addSurface(sphere2);
		
		// Enkele sphere
//		Surface sphere = new Sphere(new Vector3f(100, 0, 0), 20, Color.GREEN);
//		
//		rayTracer.addSurface(sphere);
		
		// Load object from file
		Model cylinder = new Model("data/teapot.obj");
		rayTracer.addSurface(cylinder);
		
		rayTracer.trace();
		
		panel.repaint();
	}
}
