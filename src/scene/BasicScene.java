package scene;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import raytracer.Stats;
import scene.data.Vector3f;
import scene.geometry.Surface;
import scene.lighting.Light;
import scene.material.Color3f;

import java.util.HashSet;

/**
 * Represents an actual scene, this means: everything that's rendered onscreen is in here.
 */
public class BasicScene implements Scene {
	private HashSet<Surface> surfaces;
	private HashSet<Light> lights;
	private Camera camera;
	private Color3f background;
	private EnvironmentMap environmentMap;

	public BasicScene() {
		this(null);
	}

	public BasicScene(EnvironmentMap environmentMap) {
		this.environmentMap = environmentMap;
		surfaces = new HashSet<Surface>();
		lights = new HashSet<Light>();
		background = new Color3f(0, 0, 0);
	}

	@Override
	public void setBackground(Color3f background) {
		this.background = background;
	}

	@Override
	public void setEnvironmentMap(EnvironmentMap environmentMap) {
		this.environmentMap = environmentMap;
	}

	@Override
	public Color3f getBackground(Vector3f direction) {
		if (environmentMap != null) {
			return environmentMap.getColorInDirection(direction);
		} else if (background == null) {
			return new Color3f(0, 0, 0);
		} else {
			return background;
		}
	}

	@Override
	public void setCamera(Camera cam) {
		this.camera = cam;
	}
	
	@Override
	public Camera getCamera() {
		return this.camera;
	}
	
	@Override
	public void addLightSource(Light light) {
		lights.add(light);
	}
	
	@Override
	public HashSet<Light> getLightSources() {
		return new HashSet<Light>(lights);
	}
	
	@Override
	public void addSurface(Surface surface) {
		surfaces.add(surface);
	}
	
	@Override
	public HashSet<Surface> getSurfaces() {
		// TODO nadenken of hier een kopie genomen moet worden
		return surfaces;
	}

	@Override
	public Hit trace(Ray ray) {
		return trace(ray, 0);
	}

	@Override
	public Hit trace(Ray ray, float eps) {
		float lowestT = Float.POSITIVE_INFINITY;
		Hit hit, closestHit = null;

		for (Surface surf : getSurfaces()) {
			hit = surf.hit(ray, eps, lowestT);

			if (Settings.COLLECT_STATS)
				Stats.incIntersections();

			if (hit != null) {
				lowestT = hit.getT();
				closestHit = hit;
			}
		}

		return closestHit;
	}

	@Override
	public boolean preProcess() {
		// Basic scene doesn't need any preprocessing
		return false;
	}

	@Override
	public void clear() {
		surfaces.clear();
		lights.clear();
		camera = null;
		background = null;
		environmentMap = null;

		// JVM should come pick up garbage
		System.gc();
	}
}
