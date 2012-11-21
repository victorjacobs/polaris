package raytracer;

import scene.data.Vector3f;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 19/11/12
 * Time: 12:05
 * To change this template use File | Settings | File Templates.
 */
public class BoundingBox {

	private Vector3f min;
	private Vector3f max;

	public BoundingBox(Vector3f min, Vector3f max) {
		this.min = min;
		this.max = max;
	}

	public Vector3f getMin() {
		return min;
	}

	public Vector3f getMax() {
		return max;
	}
}
