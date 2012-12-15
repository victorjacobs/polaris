package demo;

import raytracer.Settings;
import scene.Camera;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.lighting.AreaLight;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 02/12/12
 * Time: 00:49
 */
public class SoftShadows implements SceneGenerator {
	public static void main(String[] args) {
		(new Demo(new SoftShadows())).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
		if (Settings.SOFT_SHADOW_SAMPLES == 1)
			throw new RuntimeException("ERROR: running soft shadows demo is ridiculous without enabling soft shadows");

		scene.setBackground(new Color3f(0.3f, 0.3f, 0.3f));
		scene.setCamera(new Camera(new Point3f(0, 1, 0), new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), 60));
		//scene.addLightSource(new PointLight(new Vector3f(6, 4, -3)));
		scene.addLightSource(new AreaLight(new Point3f(0, 5, 0)));

		Surface sphere = new Sphere(new Vector3f(4, 1, -1), 0.5f, new DiffuseMaterial(new Color3f(0, 1, 0)));
		scene.addSurface(sphere);

		Surface plane = new Model("data/objects/plane.obj");
		plane.applyTransformation(AffineTransformation.scale(20));
		plane.setMaterial(new DiffuseMaterial(new Color3f(1, 1, 1)));
		scene.addSurface(plane);
	}

	@Override
	public void generateScene(Scene scene, int size) {
		generateScene(scene);
	}
}
