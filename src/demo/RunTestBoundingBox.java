package demo;

import gui.PolarisMainWindow;
import gui.Renderer;
import raytracer.BoundingBox;
import scene.BasicScene;
import scene.Camera;
import scene.Scene;
import scene.data.Matrix4f;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.geometry.Surface;
import scene.lighting.Light;
import scene.lighting.PointLight;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;
import scene.material.Material;
import scene.material.RefractiveMaterial;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 24/11/12
 * Time: 02:06
 */
public class RunTestBoundingBox {
	public static void main(String[] args) {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Polaris");

		PolarisMainWindow window = new PolarisMainWindow();
		Renderer renderer = new Renderer(window.getRenderPanel(), 32);

		Scene scene = new BasicScene();
		renderer.loadScene(scene);

		Camera camera = new Camera(new Vector3f(10, 10, 10), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 5, 60);
		scene.setCamera(camera);

		Material mat = new DiffuseMaterial(new Color3f(0, 1, 0));
		Surface elf = new Model("data/objects/elephav.obj", mat);

		// Move elf to origin
		BoundingBox bb = elf.boundingBox();
		Vector3f translate = bb.getMin().sum(bb.getMax()).divideBy(2).negate();

		Matrix4f trans = AffineTransformation.translate(translate);

		elf.applyTransformation(trans);

		trans = AffineTransformation.scale(new Vector3f(0.5f, 0.5f, 0.5f));

		elf.applyTransformation(trans);



		Light light = new PointLight(new Vector3f(20, 20, 20));

		scene.addLightSource(light);
		scene.setBackground(new Color3f(0.3f, 0.3f, 0.3f));
		scene.addSurface(elf);

		window.display();

		try {
			Thread.sleep(100);
		} catch (Throwable e) {

		}

		// Disable actual rendering for make greater power saving
		renderer.render();
	}
}