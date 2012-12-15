package demo;

import scene.Camera;
import scene.EnvironmentMap;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.Model;
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
		scene.setEnvironmentMap(new EnvironmentMap("data/em/sea.png"));

		scene.setCamera(new Camera(new Point3f(10f, 3, -10f), new Vector3f(-10, -3, 10f), new Vector3f(0, 1, 0), 60));

		Model teapot = new Model("data/objects/teapot.obj");
		teapot.setMaterial(new ReflectiveMaterial(0.9f));
		//teapot.setMaterial(new RefractiveMaterial(new Color3f(1, 1, 1), 2f));

		scene.addSurface(teapot);
	}

	@Override
	public void generateScene(Scene scene, int size) {
		generateScene(scene);
	}
}
