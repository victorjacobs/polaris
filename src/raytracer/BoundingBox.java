package raytracer;

import com.sun.org.apache.xerces.internal.impl.dv.xs.YearDV;
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
		this.min = new Vector3f(0, 0, 0);
		this.max = new Vector3f(0, 0, 0);
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
}
