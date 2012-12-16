package scene;

import raytracer.Ray;
import raytracer.Settings;
import scene.data.Matrix4f;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.data.Vector4f;
import scene.geometry.AffineTransformation;


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

	public void rotate(int direction) {
		Matrix4f trans = null;
		Vector4f affineVector = null;

		// Ugly
		switch (direction) {
			case 0:
				// Left
				trans = AffineTransformation.rotation(up, 10);
				affineVector = new Vector4f(gaze);
				affineVector = trans.multiply(affineVector);
				gaze = new Vector3f(affineVector.x, affineVector.y, affineVector.z);
				break;
			case 1:
				// Right
				trans = AffineTransformation.rotation(up, -10);
				affineVector = new Vector4f(gaze);
				affineVector = trans.multiply(affineVector);
				gaze = new Vector3f(affineVector.x, affineVector.y, affineVector.z);
				break;
			case 2:
				// Up
				trans = AffineTransformation.rotation(up.crossProduct(gaze), -10);
				affineVector = new Vector4f(up);
				affineVector = trans.multiply(affineVector);
				up = new Vector3f(affineVector.x, affineVector.y, affineVector.z);

				affineVector = new Vector4f(gaze);
				affineVector = trans.multiply(affineVector);
				gaze = new Vector3f(affineVector.x, affineVector.y, affineVector.z);
				break;
			case 3:
				// Down
				trans = AffineTransformation.rotation(up.crossProduct(gaze), 10);
				affineVector = new Vector4f(up);
				affineVector = trans.multiply(affineVector);
				up = new Vector3f(affineVector.x, affineVector.y, affineVector.z);

				affineVector = new Vector4f(gaze);
				affineVector = trans.multiply(affineVector);
				gaze = new Vector3f(affineVector.x, affineVector.y, affineVector.z);
				break;
		}
	}

	public void move(int direction) {
		// Idee: position verschuiven 1 eenheid in de gaze richting

		// TODO: ugly
		switch (direction) {
			case 0:
				// Left
				position = position.sum(up.crossProduct(gaze).normalize());
				break;
			case 1:
				// Right
				position = position.sum(up.crossProduct(gaze).normalize().negate());
				break;
			case 2:
				// Forward
				position = position.sum(gaze.normalize());
				break;
			case 3:
				// Backward
				position = position.sum(gaze.normalize().negate());
				break;
		}
	}

	public Ray rayToPixel(int x, int y) {
		return rayToPixel(x, y, 0.5f, 0.5f);
	}

	public Ray rayToPixel(int x, int y, float p, float q) {
		// Generate direction
		// NOTE: l, r, t, b hebben niets te maken met SCREEN!
		float aspectRatio = (float) Settings.SCREEN_Y / Settings.SCREEN_X;
		
		float r = getDistanceToScreen() * (float) Math.tan(getFOV() / 2);
		float l = -r;
		float t = aspectRatio * l;
		float b = -t;

		float u = l + ((r - l) * (x + p)) / Settings.SCREEN_X;
		float v = b + ((t - b) * (y + q)) / Settings.SCREEN_Y;
		
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
