package raytracer;

import scene.geometry.Surface;
import scene.geometry.Vector3f;

public class Hit {
	private Vector3f point;
	private Vector3f normal;
	private float t;
	private Surface surface;
	
	public Hit(Surface surface, Vector3f point, Vector3f normal, float t) {
		this.point = point;
		this.normal = normal;
		this.t = t;
		this.surface = surface;
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
