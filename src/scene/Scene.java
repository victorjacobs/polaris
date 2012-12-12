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

	Color3f getBackground(Vector3f direction);

	void setCamera(Camera cam);

	Camera getCamera();

	void addLightSource(Light light);

	HashSet<Light> getLightSources();

	void addSurface(Surface surface);

	HashSet<Surface> getSurfaces();

	Hit trace(Ray ray);

	Hit trace(Ray ray, float eps);

	/**
	 * This is called right before the renderer starts dispatching renderWorkers. Usually used for preprocessing the
	 * scene into an acceleration structure. More general: it contains the last piece of code in scene that'll be
	 * executed on a single thread.
	 */
	void preProcess();

	void clear();

	void setEnvironmentMap(EnvironmentMap environmentMap);
}
