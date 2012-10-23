
public class Camera {
	private Vector3f gaze;
	private Vector3f up;
	private Vector3f position;
	private float distanceToScreen;

	public Camera(Vector3f position, Vector3f gaze, Vector3f up, float distanceToScreen) {
		setPosition(position);
		setGaze(gaze);
		setUp(up);
		setDistanceToScreen(distanceToScreen);
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
}
