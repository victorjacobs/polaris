package demo;

import scene.Camera;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.lighting.PointLight;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 17/12/12
 * Time: 01:00
 */
public class NightFury implements SceneGenerator {

	public static void main(String[] args) {
		(new Demo(new NightFury())).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
		scene.setCamera(new Camera(new Point3f(10, 10, 10), new Vector3f(-10, -10, -10), new Vector3f(0, 1, 0), 50));
		scene.setBackground(new Color3f(0.3f, 0.3f, 0.3f));
		scene.addLightSource(new PointLight(new Vector3f(10, 20, 10), 0.2f));
		scene.addLightSource(new PointLight(new Vector3f(-5, 10, 0)));

		Model nightFury = new Model("data/objects/nightfury.obj");
		nightFury.moveToOrigin();
		nightFury.applyTransformation(AffineTransformation.rotation(new Vector3f(0, 1, 0), 180));
		nightFury.setMaterial(new DiffuseMaterial(new Color3f(0, 0.7f, 0)));

		scene.addSurface(nightFury);
	}

	@Override
	public void generateScene(Scene scene, int size) {
		generateScene(scene);
	}

	@Override
	public String getName() {
		return "Nightfury";
	}
}
