package scene;

import raytracer.Hit;
import raytracer.Ray;
import scene.geometry.Surface;
import scene.lighting.Light;

import java.util.HashSet;

// TODO matrix stack voor transformaties van surfaces
// TODO multiple cameras
public class Scene {
	private HashSet<Surface> surfaces;
	private HashSet<Light> lights;
	private Camera camera;
	
	public Scene(String file) {
		// TODO Parse from file xml
		this();
	}

	public Hit trace(Ray ray) {
		return trace(ray, 0);
	}

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

	public Hit traceAny(Ray ray, float eps) {
		Hit hit;

		for (Surface surf : getSurfaces()) {
			hit = surf.hit(ray, eps, Float.POSITIVE_INFINITY);

			if (hit != null) return hit;
		}

		return null;
	}
	
	public Scene() {
		surfaces = new HashSet<Surface>();
		lights = new HashSet<Light>();
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
		surfaces.add(surface);
	}
	
	public HashSet<Surface> getSurfaces() {
		return new HashSet<Surface>(surfaces);
	}
}
