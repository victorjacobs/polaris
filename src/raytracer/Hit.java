package raytracer;

import scene.geometry.Surface;
import scene.data.Vector3f;

// TODO getting too convoluted?
public class Hit {
	private Vector3f point;
	private Vector3f normal;
	private float t;
	private Surface surface;
	private Ray ray;
	
	public Hit(Ray ray, Surface surface, Vector3f point, Vector3f normal, float t) {
		this.point = point;
		this.normal = normal;
		this.t = t;
		this.surface = surface;
		this.ray = ray;
	}
	
	public Ray getRay() {
		return this.ray;
	}
	
	public Surface getSurface() {
		return surface;
	}
	
	public Vector3f getPoint() {
		return point;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public float getT() {
		return t;
	}
}
