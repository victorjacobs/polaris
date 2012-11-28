package scene;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import scene.geometry.Surface;
import scene.data.Vector3f;
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

	public BasicScene() {
		surfaces = new HashSet<Surface>();
		lights = new HashSet<Light>();
		background = new Color3f(0, 0, 0);
	}

	@Override
	public void setBackground(Color3f background) {
		this.background = background;
	}

	@Override
	public Color3f getBackground() {
		return background;
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
		return new HashSet<Surface>(surfaces);
	}

	@Override
	public boolean isInShade(Vector3f point, Light light) {
		return traceAny(new Ray(point, light.rayTo(point)), Settings.EPS) != null;
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

			if (hit != null) {
				lowestT = hit.getT();
				closestHit = hit;
			}
		}

		return closestHit;
	}

	@Override
	public Hit traceAny(Ray ray, float eps) {
		Hit hit;

		for (Surface surf : getSurfaces()) {
			hit = surf.hit(ray, eps, Float.POSITIVE_INFINITY);

			if (hit != null) return hit;
		}

		return null;
	}

	@Override
	public void preProcess() {
		// Basic scene doesn't need any preprocessing
	}
}
