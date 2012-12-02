package demo;

import gui.PolarisMainWindow;
import gui.Renderer;
import scene.BasicScene;
import scene.BoundingBoxAcceleratedScene;
import scene.Camera;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.material.*;

/*
 * FIXME shadows for textures broken
 * TODO UI: be able to load .obj files, use RunTestBoundingBox as template
 * TODO Soft shadows
 * 		+ Area lights
 * TODO Anti aliasing
 * TODO color changing in reflections
 * TODO count intersection tests
 * TODO scene background ===== ambient light
 */

public class RunDefault {
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow mainWindow = new PolarisMainWindow();

		Scene scene = new BoundingBoxAcceleratedScene(new BasicScene());
		Renderer renderer = new Renderer(mainWindow.getRenderPanel(), 1);
		mainWindow.setListener(renderer);

		renderer.loadScene(scene);

		scene.setCamera(new Camera(new Point3f(0, 7, 0), new Vector3f(0, -1, 0), new Vector3f(1, 0, 0), 60));
		scene.setBackground(new Color3f(0.3f, 0.3f, 0.3f));
		//scene.addLightSource(new PointLight(new Vector3f(-3, 3, 3), 0.9f));

		Texture worldTexture = new Texture("data/textures/world.jpg");
		Material world = new DiffuseMaterial(worldTexture);

		Texture checkerboardTexture = new Texture("data/textures/checkerboard.png");
		Material checkerboard = new DiffuseMaterial(checkerboardTexture);

		Surface plane = new Model("data/objects/plane.obj");
		plane.setMaterial(checkerboard);
		plane.applyTransformation(AffineTransformation.scale(new Vector3f(3, 3, 3)));
		scene.addSurface(plane);

		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 1.33f);

		Surface sphere = new Sphere(new Vector3f(0.3f, 3, 0), 1f, glass);
		scene.addSurface(sphere);

		mainWindow.display();

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {

		}

		renderer.render();
	}
}
