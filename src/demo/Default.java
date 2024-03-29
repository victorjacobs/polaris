package demo;

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
 * TODO UI: be able to load .obj files, use BoundingBox as template
 * TODO color changing in reflections
 */

public class Default implements SceneGenerator {
	public static void main(String[] args) {
		(new Demo(new Default())).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
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
	}

	@Override
	public void generateScene(Scene scene, int size) {
		generateScene(scene);
	}

	@Override
	public String getName() {
		return "Default";
	}
}
