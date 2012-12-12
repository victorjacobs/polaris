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
import scene.material.*;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 02/12/12
 * Time: 14:31
 */
public class AllEffects extends Demo {
	public static void main(String[] args) {
		(new AllEffects()).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
		scene.setCamera(new Camera(new Point3f(7, 5, 7), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 60));
		scene.setBackground(new Color3f(0.2f, 0.2f, 0.2f));

		scene.addLightSource(new AreaLight(new Point3f(10, 10, 0)));

		Surface plane = new Model("data/objects/plane.obj");
		Material planeMaterial = new DiffuseMaterial(new Texture("data/textures/essos.jpg"));
		plane.applyTransformation(AffineTransformation.scale(5));
		plane.setMaterial(planeMaterial);
		scene.addSurface(plane);

		Material veryReflective = new ReflectiveMaterial(0.9f);
		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 1.33f);
		Material green = new DiffuseMaterial(new Color3f(0, 0.7f, 0));

		Surface sphere1 = new Sphere(new Vector3f(2, 1, 1), 1, green);
		scene.addSurface(sphere1);

		Surface sphere2 = new Sphere(new Vector3f(-1, 1, -1), 1, veryReflective);
		scene.addSurface(sphere2);

		Surface bunny = new Model("data/objects/bunny.obj");
		bunny.setMaterial(glass);
		bunny.applyTransformation(AffineTransformation.translate(new Vector3f(0, 0, 5)));
		bunny.applyTransformation(AffineTransformation.scale(0.5f));
		scene.addSurface(bunny);
	}
}