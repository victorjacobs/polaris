package demo;

import scene.Camera;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.lighting.PointLight;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;
import scene.material.Material;
import scene.material.Texture;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 26/02/13
 * Time: 11:19
 */
public class TextureTest implements SceneGenerator {

	public static void main(String[] args) {
		(new Demo(new TextureTest())).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
		scene.setCamera(new Camera(new Point3f(0, 1, 0), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0), 60));
		scene.setBackground(new Color3f(0.2f, 0.2f, 0.2f));

		scene.addLightSource(new PointLight(new Vector3f(0, 3, 0), 1));

//		scene.addLightSource(new AreaLight(new Point3f(0, 3, 0)));


		Surface plane = new Model("data/objects/plane.obj");
		plane.setMaterial(new DiffuseMaterial(new Color3f(1, 1, 1)));
		plane.applyTransformation(AffineTransformation.scale(10));

		scene.addSurface(plane);

		Texture text = new Texture("data/textures/worldhd.jpg");
		Material mat = new DiffuseMaterial(text);
		Surface sphere = new Sphere(new Vector3f(0, 1, 3), 1, mat);

		scene.addSurface(sphere);
	}

	@Override
	public void generateScene(Scene scene, int size) {
		generateScene(scene);
	}
}
