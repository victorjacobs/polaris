package scene;

import raytracer.Grid;
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
 * Time: 00:48
 * To change this template use File | Settings | File Templates.
 */
public abstract class SceneDecorator implements Scene {
	private Scene scene;

	public SceneDecorator(Scene scene) {
		this.scene = scene;
	}

	@Override
	public Hit trace(Ray ray) {
		return trace(ray, 0);
	}

	@Override
	public Hit trace(Ray ray, float eps) {
		return scene.trace(ray, eps);
	}

	@Override
	public Hit traceAny(Ray ray, float eps) {
		return scene.traceAny(ray, eps);
	}

	@Override
	public void setBackground(Color3f background) {
		scene.setBackground(background);
	}

	@Override
	public Color3f getBackground() {
		return scene.getBackground();
	}

	@Override
	public void setCamera(Camera cam) {
		scene.setCamera(cam);
	}

	@Override
	public Camera getCamera() {
		return scene.getCamera();
	}

	@Override
	public void addLightSource(Light light) {
		scene.addLightSource(light);
	}

	@Override
	public HashSet<Light> getLightSources() {
		return scene.getLightSources();
	}

	@Override
	public void addSurface(Surface surface) {
		scene.addSurface(surface);
	}

	@Override
	public HashSet<Surface> getSurfaces() {
		return scene.getSurfaces();
	}

	@Override
	public boolean isInShade(Vector3f point, Light light) {
		return scene.isInShade(point, light);
	}
}
