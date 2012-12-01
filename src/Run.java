import gui.PolarisMainWindow;
import gui.Renderer;
import scene.BasicScene;
import scene.Camera;
import scene.GridAcceleratedScene;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.lighting.AmbientLight;
import scene.lighting.PointLight;
import scene.material.*;

/*
 * FIXME shadows for textures broken
 * TODO parse squares !! (make general)
 * TODO texture mapping
 * TODO Soft shadows
 * 		+ Area lights
 * TODO Anti aliasing
 * TODO color changing in reflections
 * TODO count intersection tests
 */

public class Run {
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();

		Scene scene = new GridAcceleratedScene(new BasicScene());
		Renderer renderer = new Renderer(mainWindow.getRenderPanel(), 1);
		mainWindow.setListener(renderer);

		renderer.loadScene(scene);

		scene.setCamera(new Camera(new Point3f(0, 1, 0), new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), 60));
		scene.setBackground(new Color3f(0.1f, 0.1f, 0.1f));
		scene.addLightSource(new PointLight(new Vector3f(-3, 3, 3), 0.9f));
		scene.addLightSource(new AmbientLight(new Color3f(0.1f, 0.1f, 0.1f), 0.1f));

		Texture texture = new Texture("data/textures/world.jpg");
		Material diffuseMaterial = new DiffuseMaterial(texture);

		Surface plane = new Model("data/objects/plane.obj");
		plane.setMaterial(new DiffuseMaterial(new Color3f(1, 1, 1)));
		scene.addSurface(plane);

		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 1.33f);

		Surface sphere = new Sphere(new Vector3f(3, 1, 0), 0.6f, diffuseMaterial);
		scene.addSurface(sphere);

		mainWindow.display();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}

		renderer.render();
	}
}
