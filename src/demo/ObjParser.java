package demo;

import scene.Camera;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.lighting.AreaLight;
import scene.material.*;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 16/12/12
 * Time: 17:23
 */
public class ObjParser implements SceneGenerator {

	public static void main(String[] args) {
		(new Demo(new ObjParser())).runHeadless();
	}

	@Override
	public void generateScene(Scene scene) {
		scene.setCamera(new Camera(new Point3f(5, 5, 5), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 60));
		scene.setBackground(new Color3f(0.2f, 0.2f, 0.2f));
		scene.addLightSource(new AreaLight(new Point3f(-8, 20, -10), 0.5f));
		scene.addLightSource(new AreaLight(new Point3f(-10, 5, -5)));

		// Generate some materials
		Material green = new DiffuseMaterial(new Color3f(0, 0.7f, 0));
		Material white = new DiffuseMaterial(new Color3f(1, 1, 1));
		Material smurfBlue = new DiffuseMaterial(new Color3f(0.1875f, 0.5898f, 0.86f));
		Material wood = new DiffuseMaterial(new Texture("data/textures/wood2.jpg"));

		Model plane = new Model("data/objects/plane.obj");
		plane.applyTransformation(AffineTransformation.scale(10));
		plane.applyTransformation(AffineTransformation.translate(new Vector3f(-1, 0, -1)));
		plane.setMaterial(new ReflectiveMaterial(0.4f));
		scene.addSurface(plane);

		// Load some models
		Model b787 = new Model("data/objects/787.obj");
		b787.moveToOrigin();
		b787.setMaterial(white);
		b787.applyTransformation(AffineTransformation.scale(0.7f));
		b787.applyTransformation(AffineTransformation.rotation(new Vector3f(0, 1, 0), 180));
		b787.applyTransformation(AffineTransformation.translate(new Vector3f(-3, 0.95f, -4)));

		scene.addSurface(b787);


		Model gh = new Model("data/objects/globalhawk.obj");
		gh.setMaterial(wood);
		gh.applyTransformation(AffineTransformation.scale(0.01f));
		gh.moveToOrigin();
		gh.applyTransformation(AffineTransformation.translate(new Vector3f(2, 0.5f, -1)));

		scene.addSurface(gh);


		Model crawler = new Model("data/objects/crawler.obj");
		crawler.setMaterial(green);
		crawler.applyTransformation(AffineTransformation.scale(0.3f));
		crawler.moveToOrigin();
		crawler.applyTransformation(AffineTransformation.translate(new Vector3f(-1, 0.3f, 2)));

		scene.addSurface(crawler);
	}

	@Override
	public void generateScene(Scene scene, int size) {
		generateScene(scene);
	}
}
