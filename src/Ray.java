/**
 * Class representing a ray
 * 
 * @author Victor
 */
public class Ray {
	
	private Vector3f origin;
	
	public Vector3f getOrigin() {
		return origin;
	}

	public void setOrigin(Vector3f origin) {
		this.origin = origin;
	}

	private Vector3f direction;
	
	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}

	// x and y have to be converted to u and v!
	// TODO Vectors u and v dependent of camera placement, for now fix them
	public Ray(Vector3f origin, float x, float y) {
		setOrigin(origin);
		
		// Generate direction
		// NOTE: l, r, t, b hebben niets te maken met SCREEN!
		float l = -RayTracer.SCREEN_X / 2;
		float r = RayTracer.SCREEN_X / 2;
		float t = RayTracer.SCREEN_Y / 2;
		float b = -RayTracer.SCREEN_Y / 2;
		
		float u = l + ((r - l) * (x + 0.5f)) / RayTracer.SCREEN_X;
		float v = b + ((t - b) * (y + 0.5f)) / RayTracer.SCREEN_Y;
		
		Vector3f direction = new Vector3f(u, v, -RayTracer.VIEWING_DISTANCE);
		setDirection(direction);
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("Origin: " + getOrigin().toString() + " ");
		str.append("Direction: " + getDirection().toString());
		
		return str.toString();
	}
	
}
