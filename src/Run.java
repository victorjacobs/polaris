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
import scene.material.PhongMaterial;
import scene.material.ReflectiveMaterial;


public class Run {
	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		Camera camera = new Camera(new Vector3f(2, 2, 2), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 5, 45);
		PointLight light1 = new PointLight(new Vector3f(-20, 10, 0));
		AmbientLight aLight = new AmbientLight(new Color3f(1, 1, 1), 0.1f);
		
		Scene scene = new Scene();
		scene.setCamera(camera);
		scene.addLightSource(light1);
		scene.addLightSource(aLight);
		
		RendererParallel renderer = new RendererParallel(scene, panel);
		
		// Load object from file
		Material mat = new DiffuseMaterial(new Color3f(1, 1, 1));
		//Material redMat = new DiffuseMaterial(new Color3f(1, 0, 0));
		Material redMat = new ReflectiveMaterial();
		Material mat2 = new DiffuseMaterial(new Color3f(0, 1, 0));
		Surface plane = new Model("data/objects/plane.obj", mat);
		Surface sphere = new Sphere(new Vector3f(0, 0.5f, 0), 0.5f, mat2);
		Surface sphere2 = new Sphere(new Vector3f(1f, 0.5f, -0.5f), 0.5f, redMat);
		scene.addSurface(sphere);
		scene.addSurface(sphere2);
		scene.addSurface(plane);
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
		renderer.render(16);
		
	}
}
