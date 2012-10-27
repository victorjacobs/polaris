import gui.CgPanel;
import gui.Renderer;

import javax.swing.JFrame;

import scene.Camera;
import scene.Scene;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.geometry.Vector3f;
import scene.lighting.PointLight;
import scene.material.Color;
import scene.material.DiffuseMaterial;
import scene.material.Material;


public class Run {
	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		Camera camera = new Camera(new Vector3f(5, 5, 5), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 5, 45);
		PointLight light1 = new PointLight(new Vector3f(10, -10, 15));
		
		Scene scene = new Scene();
		scene.setCamera(camera);
		scene.addLightSource(light1);
		
		Renderer renderer = new Renderer(scene, panel);
		
		// Load object from file
		Material mat = new DiffuseMaterial(new Color(1, 1, 1));
		Surface sphere = new Sphere(new Vector3f(0, 0, 0), 1, mat);
		Surface sphere2 = new Sphere(new Vector3f(2, 0, 0), 1, mat);
		scene.addSurface(sphere);
		scene.addSurface(sphere2);
		
		renderer.render(16);
		
	}
}
