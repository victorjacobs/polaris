package demo;

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
public class RunSoftShadows extends Demo {
	public static void main(String[] args) {
		(new RunSoftShadows()).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
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
}
