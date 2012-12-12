package scene;

import scene.data.Point3f;
import scene.data.Vector3f;
import scene.geometry.Sphere;
import scene.material.Color3f;
import scene.material.Texture;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 12/12/12
 * Time: 12:17
 */
public class EnvironmentMap {

	private Texture text;

	public EnvironmentMap(String path) {
		text = new Texture(path);
	}

	public Color3f getColorInDirection(Vector3f direction) {
		Sphere sphere = new Sphere(1f);

		return text.getColor(sphere.getLocalCoordinateFor(new Point3f(direction.normalize())));
	}
}
