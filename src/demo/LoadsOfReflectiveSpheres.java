package demo;

import scene.Camera;
import scene.Scene;
import scene.data.Vector3f;
import scene.geometry.AffineTransformation;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.lighting.PointLight;
import scene.material.*;

/**
 * User: victor
 * Date: 08/11/12
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public class LoadsOfReflectiveSpheres implements SceneGenerator {

	public static void main(String[] args) {
		(new Demo(new LoadsOfReflectiveSpheres())).runStandalone();
	}

	@Override
	public void generateScene(Scene scene) {
		PointLight light1 = new PointLight(new Vector3f(-20, 10, 0));
		scene.addLightSource(light1);
		scene.setBackground(new Color3f(0.1f, 0.1f, 0.1f));

		Camera camera = new Camera(new Vector3f(4, 4, 4), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 5, 45);
		scene.setCamera(camera);


		// Add some stuff to the scene
		Material notSoReflectiveMaterial = new ReflectiveMaterial(0.2f);
		Material veryReflectiveMaterial = new ReflectiveMaterial(0.9f);
		Material greenDiffuseMaterial = new DiffuseMaterial(new Color3f(0, 1, 0));
		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 1.33f);

		Surface plane = new Model("data/objects/plane.obj", notSoReflectiveMaterial);
		plane.applyTransformation(AffineTransformation.scale(new Vector3f(20, 20, 20)));

		scene.addSurface(plane);

		Surface surf;

		for (float z = 0.2f; z <= 2; z += 0.5f) {
			for (float x = -1f; x <= 1f; x += 0.5f) {
				for (float y = -1f; y <= 1f; y += 0.5f) {

					if ((x) % 2 == 0 || y % 2 == 0) {
						surf = new Sphere(new Vector3f(x, z, y), 0.2f, greenDiffuseMaterial);
					} else {
						surf = new Sphere(new Vector3f(x, z, y), 0.2f, veryReflectiveMaterial);
					}

					scene.addSurface(surf);
				}
			}
		}
	}

	@Override
	public void generateScene(Scene scene, int size) {
		generateScene(scene);
	}
}
