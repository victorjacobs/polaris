import gui.CgPanel;
import gui.RendererParallel;

import javax.swing.JFrame;

import scene.Camera;
import scene.Scene;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.geometry.Vector3f;
import scene.lighting.AmbientLight;
import scene.lighting.PointLight;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;
import scene.material.Material;
import scene.material.ReflectiveMaterial;
import scene.material.RefractiveMaterial;

/*
 * TODO parse squares
 * TODO refactor material hierarchy
 * TODO Fix reflections
 * 		+ recursive with reflection coefficient
 * TODO refractive surfaces
 * TODO texture mapping
 * TODO XML
 * TODO Soft shadows
 * 		+ Area lights
 * TODO Anti aliasing
 * TODO DOF
 * TODO color changing in reflections
 */

public class Run {
	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		Camera camera = new Camera(new Vector3f(2, 2, 2), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 5, 45);
		//PointLight light1 = new PointLight(new Vector3f(-20, 10, 0));
		PointLight light1 = new PointLight(new Vector3f(10, 10, 0));
		AmbientLight aLight = new AmbientLight(new Color3f(1, 1, 1), 0.1f);
		
		Scene scene = new Scene();
		scene.setCamera(camera);
		scene.addLightSource(light1);
		scene.addLightSource(aLight);
		
		RendererParallel renderer = new RendererParallel(scene, panel);
		
		// Load object from file
		Material mat = new DiffuseMaterial(new Color3f(1, 1, 1));
		Surface foo = new Model("data/objects/banana.obj", mat);
		//Material redMat = new DiffuseMaterial(new Color3f(1, 0, 0));
		Material redMat = new ReflectiveMaterial(0.5f);
		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 2f);
		Material mat2 = new DiffuseMaterial(new Color3f(0, 1, 0));
		Surface plane = new Model("data/objects/plane.obj", mat);
		Surface sphere = new Sphere(new Vector3f(0, 0.5f, 0), 0.5f, mat2);
		Surface sphere3 = new Sphere(new Vector3f(0.5f, 0.5f, -1.5f), 0.5f, mat2);
		Surface sphere2 = new Sphere(new Vector3f(1f, 0.5f, -0.5f), 0.5f, glass);
		scene.addSurface(sphere);
		scene.addSurface(sphere2);
		scene.addSurface(plane);
		scene.addSurface(sphere3);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
		renderer.render(16);

	}
}
