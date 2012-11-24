package scene;

import raytracer.Hit;
import raytracer.Ray;
import scene.data.Vector3f;
import scene.geometry.Surface;
import scene.lighting.Light;
import scene.material.Color3f;

import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 24/11/12
 * Time: 00:36
 * To change this template use File | Settings | File Templates.
 */
public interface Scene {
	void setBackground(Color3f background);

	Color3f getBackground();

	void setCamera(Camera cam);

	Camera getCamera();

	void addLightSource(Light light);

	HashSet<Light> getLightSources();

	void addSurface(Surface surface);

	HashSet<Surface> getSurfaces();

	boolean isInShade(Vector3f point, Light light);

	Hit trace(Ray ray);

	Hit trace(Ray ray, float eps);

	Hit traceAny(Ray ray, float eps);
}
