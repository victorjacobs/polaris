import gui.CgPanel;

import scene.material.Color;

import javax.swing.JFrame;

import raytracer.RayTracer;
import scene.Camera;
import scene.Scene;
import scene.geometry.Model;
import scene.geometry.Vector3f;
import scene.lighting.PointLight;
import scene.material.DiffuseMaterial;
import scene.material.Material;


public class Run {
	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		// TODO: entire axis system is in reverse!
		Camera camera = new Camera(new Vector3f(5, 5, 5), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 5);
		PointLight light1 = new PointLight(new Vector3f(10, 0, 5));
		
		Scene scene = new Scene();
		scene.setCamera(camera);
		scene.addLightSource(light1);
		
		RayTracer rayTracer = new RayTracer(panel, scene);
		
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
//		Surface sphere1 = new Sphere(new Vector3f(1, 5, 10), 1, Color.GREEN);
//		Surface sphere2 = new Sphere(new Vector3f(2, 0, 100), 3, Color.YELLOW);
//		
//		rayTracer.addSurface(sphere1);
//		rayTracer.addSurface(sphere2);
		
		// Enkele sphere
//		Surface sphere = new Sphere(new Vector3f(0, 0, 10), 1, Color.GREEN);
//		
//		rayTracer.addSurface(sphere);
		
		// Load object from file
		Material mat = new DiffuseMaterial(new Color(1, 1, 1));
		Model cylinder = new Model("data/sphere.obj", mat);
		scene.addSurface(cylinder);
		
		rayTracer.trace();
		
		panel.repaint();
	}
}
