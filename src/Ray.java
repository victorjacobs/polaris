/**
 * Class representing a ray
 * 
 * @author Victor
 */
public class Ray {
	
	private Camera camera;

	private Vector3f direction;

	// x and y have to be converted to u and v!
	// TODO Vectors u and v dependent of camera placement, for now fix them
	public Ray(Camera cam, float x, float y) {
		setCamera(cam);
		
		// Generate direction
		// NOTE: l, r, t, b hebben niets te maken met SCREEN!
		float l = -20;
		float r = 20;
		float t = 15;
		float b = -15;
		
		float u = l + ((r - l) * (x + 0.5f)) / RayTracer.SCREEN_X;
		float v = b + ((t - b) * (y + 0.5f)) / RayTracer.SCREEN_Y;
		
		Vector3f direction1 = getCamera().getW().multiply(getCamera().getDistanceToScreen()).negate();
		Vector3f direction2 = getCamera().getU().multiply(u);
		Vector3f direction3 = getCamera().getV().multiply(v);
		
		Vector3f direction = direction1.sum(direction2.sum(direction3));
		
		setDirection(direction);
	}
	
	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public Vector3f getDirection() {
		return direction;
	}

	public void setDirection(Vector3f direction) {
		this.direction = direction;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		
		str.append("Direction: " + getDirection().toString());
		
		return str.toString();
	}
	
}
