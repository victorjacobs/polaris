package raytracer;
import scene.geometry.Vector3f;

/**
 * Class representing a ray
 * 
 * @author Victor
 */
public class Ray {
	
	private Vector3f origin;
	private Vector3f direction;
	
	public Ray(Vector3f origin, Vector3f direction) {
		this.origin = origin;
		this.direction = direction;
	}
	
	public Vector3f getOrigin() {
		return origin;
	}
	
	public Vector3f getDirection() {
		return direction;
	}
	
	@Override
	public String toString() {
		return "Direction: " + getDirection().toString();
	}
	
}
