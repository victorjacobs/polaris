import gui.*;
import gui.Renderer;
import scene.Camera;
import scene.Scene;
import scene.geometry.Model;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.data.Vector3f;
import scene.lighting.AmbientLight;
import scene.lighting.PointLight;
import scene.material.*;

import javax.swing.*;

/**
 * User: victor
 * Date: 08/11/12
 * Time: 16:40
 * To change this template use File | Settings | File Templates.
 */
public class RunLoadsOfReflectiveSpheres {

	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.getContentPane().add(panel);
		frame.setVisible(true);

		Camera camera = new Camera(new Vector3f(2, 2, 2), new Vector3f(-5, -5, -5), new Vector3f(0, 1, 0), 5, 45);
		//PointLight light1 = new PointLight(new Vector3f(-20, 10, 0));
		PointLight light1 = new PointLight(new Vector3f(-10, 10, -10), 0.9f);
		PointLight light2 = new PointLight(new Vector3f(-10, 10, -10), 0.7f);
		AmbientLight aLight = new AmbientLight(new Color3f(1, 1, 1), 0.1f);

		Scene scene = new Scene();
		scene.setCamera(camera);
		scene.addLightSource(light1);
		//scene.addLightSource(light2);
		scene.addLightSource(aLight);

		Renderer renderer = new gui.Renderer(scene, panel, 16);

		// Add some stuff to the scene
		Material notSoReflectiveMaterial = new ReflectiveMaterial(0.2f);
		Material veryReflectiveMaterial = new ReflectiveMaterial(0.9f);
		Material greenDiffuseMaterial = new DiffuseMaterial(new Color3f(0, 1, 0));
		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 1.33f);

		Surface plane = new Model("data/objects/plane.obj", notSoReflectiveMaterial);

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

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		renderer.render();
	}

}
