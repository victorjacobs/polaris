package scene;
import raytracer.Ray;
import raytracer.Settings;
import scene.data.Point3f;
import scene.data.Vector3f;


public class Camera {
	private Vector3f gaze;
	private Vector3f up;
	private Vector3f position;
	private float distanceToScreen;
	private float FOV;

	// TODO toegelaten distance to screen te hardcoden voor algemeen geval?
	public Camera(Point3f position, Vector3f gaze, Vector3f up, float FOV) {
		this(new Vector3f(position), gaze, up, 5, FOV);
	}

	public Camera(Vector3f position, Vector3f gaze, Vector3f up, float distanceToScreen, float FOV) {
		this.position = position;
		this.gaze = gaze;
		this.up = up;
		this.distanceToScreen = distanceToScreen;
		this.FOV = (float)Math.toRadians(FOV);
	}
	
	public Ray rayToPixel(int x, int y) {
		// Generate direction
		// NOTE: l, r, t, b hebben niets te maken met SCREEN!
		float aspectRatio = (float) Settings.SCREEN_Y / Settings.SCREEN_X;
		
		float r = getDistanceToScreen() * (float) Math.tan(getFOV() / 2);
		float l = -r;
		float t = aspectRatio * l;
		float b = -t;

		float u = l + ((r - l) * (x + 0.5f)) / Settings.SCREEN_X;
		float v = b + ((t - b) * (y + 0.5f)) / Settings.SCREEN_Y;
		
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

	public Vector3f getUp() {
		return up;
	}

	public Vector3f getPosition() {
		return position;
	}
	
	public float getDistanceToScreen() {
		return distanceToScreen;
	}
	
	public float getFOV() {
		return FOV;
	}
}
