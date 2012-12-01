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

	public BoundingBox() {
		// FIXED: shouldn't be set to 000! Add() will do weird stuff!
		this.min = null;
		this.max = null;
	}

	public BoundingBox(Vector3f[] points) {
		// Clone vectors into new variables, otherwise the for loop will change the original vertex
		Vector3f min = (Vector3f)points[0].clone();
		Vector3f max = (Vector3f)points[0].clone();

		for (int i = 1; i < points.length; i++) {
			if (points[i].x < min.x) min.x = points[i].x;
			if (points[i].y < min.y) min.y = points[i].y;
			if (points[i].z < min.z) min.z = points[i].z;
			if (points[i].x > max.x) max.x = points[i].x;
			if (points[i].y > max.y) max.y = points[i].y;
			if (points[i].z > max.z) max.z = points[i].z;
		}

		this.min = min;
		this.max = max;
	}

	public BoundingBox(Vector3f min, Vector3f max) {
		this.min = min;
		this.max = max;
	}

	public BoundingBox(BoundingBox boundingBox) {
		this.min = boundingBox.getMin();
		this.max = boundingBox.getMax();
	}

	public Vector3f getMin() {
		return min;
	}

	public Vector3f getMax() {
		return max;
	}

	public BoundingBox add(BoundingBox otherBB) {
		Vector3f oMin = otherBB.getMin();
		Vector3f oMax = otherBB.getMax();

		// Don't be too strict in this check for great benefit
		if (this.min == null || this.max == null) {
			return new BoundingBox(oMin, oMax);
		}

		Vector3f nMin = min, nMax = max;

		if (oMin.x < nMin.x) nMin.x = oMin.x;
		if (oMin.y < nMin.y) nMin.y = oMin.y;
		if (oMin.z < nMin.z) nMin.z = oMin.z;
		if (oMax.x > nMax.x) nMax.x = oMax.x;
		if (oMax.y > nMax.y) nMax.y = oMax.y;
		if (oMax.z > nMax.z) nMax.z = oMax.z;

		return new BoundingBox(nMin, nMax);
	}

	public float[] size() {
		float x = Math.abs(getMax().x - getMin().x);
		float y = Math.abs(getMax().y - getMin().y);
		float z = Math.abs(getMax().z - getMin().z);

		float[] out = {x, y, z};

		return out;
	}

	public float getVolume() {
		float[] dimensions = size();

		return dimensions[0] * dimensions[1] * dimensions[2];
	}

	public boolean contains(Vector3f point) {
		if (point.compareTo(getMin()) >= 0 && point.compareTo(getMax()) <= 0) return true;

		return false;
	}

	// TODO evt ook t0 en t1?
	public Hit hit(Ray ray) {
		Vector3f d = ray.getDirection();
		Vector3f e = ray.getOrigin();

		float[] tMin = new float[3];
		float[] tMax = new float[3];
		float a = 0;
		// Store min of max and max of min
		float min = Float.MIN_VALUE;
		float max = Float.MAX_VALUE;

		Vector3f hitPoint = null;

		// Calculate t values for hit in every dimension
		for (int i = 0; i < 3; i++) {
			a = 1 / d.get(i);

			if (a >= 0) {
				tMin[i] = a * (getMin().get(i) - e.get(i));
				tMax[i] = a * (getMax().get(i) - e.get(i));
			} else {
				tMin[i] = a * (getMax().get(i) - e.get(i));
				tMax[i] = a * (getMin().get(i) - e.get(i));
			}

			if (tMin[i] >= 0 && tMin[i] > min) min = tMin[i];
			if (tMax[i] >= 0 && tMax[i] < max) max = tMax[i];
		}

		// looks suspicious, but if the max of the mins is larger than the min of the maxes, no hit
		if (max < min || max < 0) {
			return null;
		} else {
			hitPoint = ray.getOrigin().sum(ray.getDirection().multiply(min));
			return new Hit(ray, null, hitPoint, null, null, min);
		}

//		if (tMin[0] > tMax[1] || tMin[1] > tMax[0]) {
//			return false;
//		} else if (tMin[1] > tMax[2] || tMin[2] > tMax[1]) {
//			return false;
//		} else if (tMin[2] > tMax[0] || tMin[0] > tMax[2]) {
//			return false;
//		} else {
//			return true;
//		}
	}

	public BoundingBox increaseBy(float inc) {
		BoundingBox newBox = new BoundingBox(getMin().minus(new Vector3f(inc, inc, inc)), getMax().sum(new Vector3f(inc, inc, inc)));

		return newBox;
	}
}
