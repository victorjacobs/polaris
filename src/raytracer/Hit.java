package raytracer;

import scene.data.Point2f;
import scene.geometry.Surface;
import scene.data.Vector3f;

public class Hit {
	private Vector3f point;
	private Vector3f normal;
	private Point2f textureCoordinates;
	private float t;
	private Surface surface;
	private Ray ray;
	
	public Hit(Ray ray, Surface surface, Vector3f point, Vector3f normal, Point2f textureCoordinates, float t) {
		this.point = point;
		this.normal = normal;
		this.textureCoordinates = textureCoordinates;
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

	public Point2f getTextureCoordinates() {
		return textureCoordinates;
	}
}
