import gui.CgPanel;
import gui.RendererParallel;
import scene.Camera;
import scene.Scene;
import scene.geometry.*;
import scene.lighting.AmbientLight;
import scene.lighting.PointLight;
import scene.material.*;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 08/11/12
 * Time: 20:06
 * To change this template use File | Settings | File Templates.
 */
public class RunTestRefraction {

	public static void main(String[] args) {
		CgPanel panel = new CgPanel();
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.getContentPane().add(panel);
		frame.setVisible(true);

		Camera camera = new Camera(new Vector3f(-1, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 1, 0), 1, 45);
		//PointLight light1 = new PointLight(new Vector3f(-20, 10, 0));
		PointLight light1 = new PointLight(new Vector3f(-1, 1, 0));
		AmbientLight aLight = new AmbientLight(new Color3f(1, 1, 1), 0.1f);

		Scene scene = new Scene();
		scene.setCamera(camera);
		scene.addLightSource(light1);
		scene.addLightSource(aLight);

		RendererParallel renderer = new RendererParallel(scene, panel);

		Material glass = new RefractiveMaterial(new Color3f(1, 1, 1), 1.33f);
		Material green = new DiffuseMaterial(new Color3f(0, 1, 0));

		Surface sphere = new Sphere(new Vector3f(0, 0, 0), 0.2f, glass);
		Surface triag = new Triangle(2, 0, -1, 2, 0, 1, 2, 1, 0, green);

		scene.addSurface(sphere);
		//scene.addSurface(triag);



		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}

		renderer.render(16);
	}

}
