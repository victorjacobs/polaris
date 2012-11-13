import gui.*;
import gui.Renderer;
import scene.Camera;
import scene.Scene;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.geometry.Vector3f;
import scene.lighting.AmbientLight;
import scene.lighting.PointLight;
import scene.material.*;

import javax.swing.*;

/*
 * TODO optimalisatie schaduwstralen: check inwendig product normaal ding en straal
 * TODO: verschil tussen shading normaal en echte normaal op een vlak!!!
 * TODO parse squares
 * TODO refactor material hierarchy
 * TODO Fix reflections
 * 		+ recursive with reflection coefficient
 * TODO refractive surfaces
 * TODO texture mapping
 * TODO XML
 * TODO Soft shadows
 * 		+ Area lights
 * TODO Anti aliasing
 * TODO DOF
 * TODO color changing in reflections
 */

public class Run {
	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.getContentPane().add(panel);
		frame.setVisible(true);
		
		Camera camera = new Camera(new Vector3f(-2, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), 2, 45);
		//PointLight light1 = new PointLight(new Vector3f(-20, 10, 0));
		PointLight light1 = new PointLight(new Vector3f(1, 1, 1));
		AmbientLight aLight = new AmbientLight(new Color3f(1, 1, 1), 0.1f);
		
		Scene scene = new Scene();
		scene.setCamera(camera);
		scene.addLightSource(light1);
		scene.addLightSource(aLight);
		
		Renderer renderer = new gui.Renderer(scene, panel);
		
		// Load object from file
		Material mat = new DiffuseMaterial(new Color3f(0, 1, 0));
		Material redMat = new DiffuseMaterial(new Color3f(1, 0, 0));
		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 1.33f);

		Surface sphereGlass = new Sphere(new Vector3f(20, 0, 0), 4, glass);
		Surface sphereGreen3 = new Sphere(new Vector3f(40, 7, 0), 2, mat);
		Surface sphereGreen = new Sphere(new Vector3f(40, -10, 0), 4, mat);
		Surface sphereGreen2 = new Sphere(new Vector3f(40, 10, 5), 4, redMat);

		scene.addSurface(sphereGlass);
		scene.addSurface(sphereGreen);
		scene.addSurface(sphereGreen2);
		scene.addSurface(sphereGreen3);

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
		renderer.render(16);

	}
}
