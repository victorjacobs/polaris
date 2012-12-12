package demo;

import scene.Camera;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.geometry.Surface;
import scene.lighting.AreaLight;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;
import scene.material.Material;
import scene.material.ReflectiveMaterial;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 06/12/12
 * Time: 18:26
 */
public class Teapots implements SceneGenerator {

	public static void main(String[] args) {
		(new Demo(new Teapots())).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
		generateScene(scene, 1000);
	}

	@Override
	public void generateScene(Scene scene, int size) {
		scene.setCamera(new Camera(new Point3f(-5, 10f, 10f), new Vector3f(10, -10f, -10f), new Vector3f(0, 1, 0), 60));
		scene.setBackground(new Color3f(0.2f, 0.2f, 0.2f));

		scene.addLightSource(new AreaLight(new Point3f(-10, 3, 3)));
		scene.addLightSource(new AreaLight(new Point3f(2.5f, 10, 3f)));
//		scene.addLightSource(new PointLight(new Vector3f(3, 1, -10), 0.3f));
//		scene.addLightSource(new PointLight(new Vector3f(3, 1, 10), 0.3f));

		Model motherTeapot = new Model("data/objects/teapot.obj");
		Model teapot;

		Surface plane = new Model("data/objects/plane.obj");
		plane.applyTransformation(AffineTransformation.scale(20));
		plane.setMaterial(new ReflectiveMaterial(0.3f));
		scene.addSurface(plane);

		Random rand = new Random();
		float x, y, z;
		Material randomColor;


		for (int i = 0; i < size; i++) {
			randomColor = new DiffuseMaterial(new Color3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));

			x = 5 * rand.nextFloat();
			y = 5 * rand.nextFloat();
			z = 5 * rand.nextFloat();

			teapot = motherTeapot.duplicate();
			teapot.applyTransformation(AffineTransformation.scale(0.1f));
			teapot.applyTransformation(AffineTransformation.rotation(new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()), 180 * rand.nextFloat()));
			teapot.applyTransformation(AffineTransformation.translate(new Vector3f(x, y, z)));
			teapot.setMaterial(randomColor);
			scene.addSurface(teapot);
		}


	}

}
