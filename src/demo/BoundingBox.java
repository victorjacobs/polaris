package demo;

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

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 24/11/12
 * Time: 02:06
 */
public class BoundingBox implements SceneGenerator {
	public static void main(String[] args) {
		(new Demo(new BoundingBox())).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
		Camera camera = new Camera(new Vector3f(8, 8, 8), new Vector3f(-5, -4, -5), new Vector3f(0, 1, 0), 5, 60);
		scene.setCamera(camera);
		scene.setBackground(new Color3f(0.2f, 0.2f, 0.2f));

		Material mat = new DiffuseMaterial(new Color3f(0, 1, 0));
		Surface elf = new Model("data/objects/nightfury.obj", mat);
		elf.applyTransformation(AffineTransformation.rotation(new Vector3f(0, 1, 0), 180));

		// Move elf to origin
		raytracer.BoundingBox bb = elf.boundingBox();
		Vector3f translate = bb.getMin().sum(bb.getMax()).divideBy(2).negate();

		Matrix4f trans = AffineTransformation.translate(translate);

		elf.applyTransformation(trans);

		trans = AffineTransformation.scale(new Vector3f(0.5f, 0.5f, 0.5f));

		elf.applyTransformation(trans);



		Light light = new PointLight(new Vector3f(20, 20, 20));

		scene.addLightSource(light);
		scene.setBackground(new Color3f(0.3f, 0.3f, 0.3f));
		scene.addSurface(elf);
	}

	@Override
	public void generateScene(Scene scene, int size) {
		generateScene(scene);
	}

	@Override
	public String getName() {
		return "Boundingbox Text";
	}
}
