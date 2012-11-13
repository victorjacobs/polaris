import raytracer.Ray;
import raytracer.RayTracer;
import scene.Scene;
import scene.geometry.Sphere;
import scene.geometry.Surface;
import scene.geometry.Vector3f;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;
import scene.material.Material;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 11/11/12
 * Time: 03:00
 * To change this template use File | Settings | File Templates.
 */
public class TestSphere {

	public static void main(String[] args) {
		Scene scene = new Scene();
		RayTracer tracer = new RayTracer(scene);
		Material mat = new DiffuseMaterial(new Color3f(1, 0, 0));

		Surface sphere = new Sphere(new Vector3f(0, 0, 0), 2, mat);
		scene.addSurface(sphere);

		Ray ray1 = new Ray(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0));

		System.out.println(tracer.trace(ray1).getT());
	}

}
