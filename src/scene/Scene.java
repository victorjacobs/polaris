package scene;

import raytracer.Hit;
import raytracer.Ray;
import scene.data.Vector3f;
import scene.geometry.Surface;
import scene.lighting.Light;
import scene.material.Color3f;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an actual scene, this means: everything that's rendered onscreen is in here.
 */
public class Scene {
	private HashSet<Surface> surfaces;
	private HashSet<Light> lights;
	private Camera camera;
	private Color3f background;
	private EnvironmentMap environmentMap;
	private List<Surface> primitivesBag;

	private TraversalStrategy strategy;

	private boolean shouldRebuildStructure = true;

	public Scene() {
		this(null);
	}

	public Scene(EnvironmentMap environmentMap) {
		this.environmentMap = environmentMap;
		surfaces = new HashSet<Surface>();
		lights = new HashSet<Light>();
		primitivesBag = new LinkedList<Surface>();
		background = new Color3f(0, 0, 0);
	}

	public void setTraversalStrategy(TraversalStrategy strategy) {
		this.strategy = strategy;
	}

	public void setBackground(Color3f background) {
		this.background = background;
	}

	public void setEnvironmentMap(EnvironmentMap environmentMap) {
		this.environmentMap = environmentMap;
	}

	public Color3f getBackground(Vector3f direction) {
		if (environmentMap != null) {
			return environmentMap.getColorInDirection(direction);
		} else if (background == null) {
			return new Color3f(0, 0, 0);
		} else {
			return background;
		}
	}

	public void setCamera(Camera cam) {
		this.camera = cam;
	}
	
	public Camera getCamera() {
		return this.camera;
	}
	
	public void addLightSource(Light light) {
		lights.add(light);
	}
	
	public HashSet<Light> getLightSources() {
		return new HashSet<Light>(lights);
	}
	
	public void addSurface(Surface surface) {
		shouldRebuildStructure = true;
		surfaces.add(surface);
		primitivesBag.addAll(surface.getPrimitiveSurfaces());
	}

	public Hit trace(Ray ray) {
		return trace(ray, 0);
	}

	public Hit trace(Ray ray, float eps) {
		// If a better strategy is available then brute force, use it
		if (strategy != null) {
			return strategy.trace(ray, eps);
		}

		// Otherwise just bruteforce
		float lowestT = Float.POSITIVE_INFINITY;
		Hit hit, closestHit = null;

		for (Surface surf : primitivesBag) {
			hit = surf.hit(ray, eps, lowestT);

			if (hit != null) {
				lowestT = hit.getT();
				closestHit = hit;
			}
		}

		return closestHit;
	}

	public boolean prepare() {
		if (strategy != null && shouldRebuildStructure) {
			strategy = strategy.clean();
			strategy.prepare(primitivesBag);
			shouldRebuildStructure = false;
			return true;
		}

		return false;
	}

	public void clear() {
		surfaces.clear();
		lights.clear();
		camera = null;
		background = null;
		environmentMap = null;
		primitivesBag.clear();
		shouldRebuildStructure = true;

		// JVM should come pick up garbage
		System.gc();
	}
}
