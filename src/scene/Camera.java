package scene;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.geometry.Vector3f;


public class Camera {
	private Vector3f gaze;
	private Vector3f up;
	private Vector3f position;
	private float distanceToScreen;
	private float FOV;

	public Camera(Vector3f position, Vector3f gaze, Vector3f up, float distanceToScreen, float FOV) {
		setPosition(position);
		setGaze(gaze);
		setUp(up);
		setDistanceToScreen(distanceToScreen);
		setFOV(FOV);
	}
	
	public Ray rayToPixel(int x, int y) {
		// Generate direction
		// NOTE: l, r, t, b hebben niets te maken met SCREEN!
		float aspectRatio = (float)RayTracer.SCREEN_Y / RayTracer.SCREEN_X;
		
		float r = getDistanceToScreen() * (float) Math.tan(getFOV() / 2);
		float l = -r;
		float b = aspectRatio * r;	// TODO this isn't a real fix... ach wel.
		float t = aspectRatio * l;
		
		float u = l + ((r - l) * (x + 0.5f)) / RayTracer.SCREEN_X;
		float v = b + ((t - b) * (y + 0.5f)) / RayTracer.SCREEN_Y;
		
		Vector3f direction1 = getW().multiply(getDistanceToScreen()).negate();
		Vector3f direction2 = getU().multiply(u);
		Vector3f direction3 = getV().multiply(v);
		
		Vector3f direction = direction1.sum(direction2.sum(direction3));
		
		return new Ray(position, direction.normalize());
	}
	
	public Vector3f getW() {
		return getGaze().normalize().negate();
	}
	
	public Vector3f getU() {
		return getUp().crossProduct(getW()).normalize();
	}
	
	public Vector3f getV() {
		return getW().crossProduct(getU());
	}
	
	
	public Vector3f getGaze() {
		return gaze;
	}

	public void setGaze(Vector3f gaze) {
		this.gaze = gaze;
	}

	public Vector3f getUp() {
		return up;
	}

	public void setUp(Vector3f up) {
		this.up = up;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public float getDistanceToScreen() {
		return distanceToScreen;
	}

	public void setDistanceToScreen(float distanceToScreen) {
		this.distanceToScreen = distanceToScreen;
	}
	
	public float getFOV() {
		return FOV;
	}

	public void setFOV(float FOV) {
		this.FOV = FOV;
	}
}
