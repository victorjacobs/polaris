import gui.*;
import gui.Renderer;
import scene.Camera;
import scene.Scene;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.data.Vector3f;
import scene.lighting.AmbientLight;
import scene.lighting.PointLight;
import scene.material.*;

/*
 * TODO optimalisatie schaduwstralen: check inwendig product normaal ding en straal
 * TODO parse squares
 * TODO Fix reflections
 * 		+ recursive with reflection coefficient
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
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();
		mainWindow.display();

		Camera camera = new Camera(new Vector3f(2, 2, 2), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 5, 60);
		PointLight light2 = new PointLight(new Vector3f(-20, 10, 0), 0.2f);
		PointLight light1 = new PointLight(new Vector3f(10, 10, 0));
		AmbientLight aLight = new AmbientLight(new Color3f(1, 1, 1), 0.1f);

		Scene scene = new Scene();
		scene.setCamera(camera);
		scene.addLightSource(light1);
		scene.addLightSource(aLight);
		scene.addLightSource(light2);

		Renderer renderer = new Renderer(scene, mainWindow.getRenderPanel());

		// Load object from file
		Material mat = new DiffuseMaterial(new Color3f(1, 1, 1));
		Surface foo = new Model("data/objects/banana.obj", mat);
		//Material redMat = new DiffuseMaterial(new Color3f(1, 0, 0));
		Material redMat = new ReflectiveMaterial(0.5f);
		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 1.33f);
		Material mat2 = new DiffuseMaterial(new Color3f(0, 1, 0));
		Surface plane = new Model("data/objects/plane.obj", mat);
		Surface sphere = new Sphere(new Vector3f(0, 0.5f, 0), 0.5f, redMat);
		Surface sphere3 = new Sphere(new Vector3f(0.5f, 0.5f, -2f), 0.5f, mat2);
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
