package raytracer;

import geometry.Vector3f;

public class Hit {
	private Vector3f point;
	private Vector3f normal;
	private float t;
	
	public Hit(Vector3f point, Vector3f normal, float t) {
		this.point = point;
		this.normal = normal;
		this.t = t;
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
