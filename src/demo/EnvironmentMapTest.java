package demo;

import scene.Camera;
import scene.EnvironmentMap;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.material.ReflectiveMaterial;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 12/12/12
 * Time: 12:35
 */
public class EnvironmentMapTest implements SceneGenerator {

	public static void main(String[] args) {
		(new Demo(new EnvironmentMapTest())).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
		scene.setEnvironmentMap(new EnvironmentMap("data/em/lores.jpg"));

		scene.setCamera(new Camera(new Point3f(10f, 3, 0), new Vector3f(-10, -3, 0), new Vector3f(0, 0, -1), 70));

		Model teapot = new Model("data/objects/teapot.obj");
		teapot.applyTransformation(AffineTransformation.rotation(new Vector3f(1, 0, 0), -90));
		teapot.applyTransformation(AffineTransformation.rotation(new Vector3f(0, 0, 1), 90));
		teapot.applyTransformation(AffineTransformation.translate(new Vector3f(-1, -1, 2)));
		teapot.setMaterial(new ReflectiveMaterial(0.9f));
		//teapot.setMaterial(new RefractiveMaterial(new Color3f(1, 1, 1), 2f));

		Surface sphere = new Sphere(new Vector3f(3, 4, 0), 1, new ReflectiveMaterial(0.9f));
		scene.addSurface(sphere);

		scene.addSurface(teapot);
	}

	@Override
	public void generateScene(Scene scene, int size) {
		generateScene(scene);
	}
}
