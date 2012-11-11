package scene;

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
