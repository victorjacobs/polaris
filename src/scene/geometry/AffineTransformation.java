package scene.geometry;

import scene.geometry.Matrix4f;
import scene.geometry.Vector3f;

/**
 * Class that constructs 4x4 matrices representing certain affine transformations in homogenous coordinates
 */
public class AffineTransformation {
	public static Matrix4f rotation(Vector3f axis, float angle) {
		// Convert angle to radians
		angle = (float)Math.toRadians(angle);

		// Produce orthogonal basis from axis
		Vector3f w = axis.normalize();
		Vector3f t = w.replaceSmallestComponentWith(1).normalize();
		Vector3f u = t.crossProduct(w).normalize();
		Vector3f v = w.crossProduct(u);

		Matrix4f basis = new Matrix4f();
		basis.setRow(0, u.x, u.y, u.z, 0);
		basis.setRow(1, v.x, v.y, v.z, 0);
		basis.setRow(2, w.x, w.y, w.z, 0);
		basis.setRow(3, 0, 0, 0, 1);

		Matrix4f rotate = new Matrix4f((float)Math.cos(angle), -(float)Math.sin(angle), 0, 0,
				(float)Math.sin(angle), (float)Math.cos(angle), 0, 0,
				0, 0, 1, 0,
				0, 0, 0, 1);

		return basis.transpose().multiply(rotate).multiply(basis);
	}

	public static Matrix4f translate(Vector3f direction) {
		return new Matrix4f(1, 0, 0, direction.x,
							0, 1, 0, direction.y,
							0, 0, 1, direction.z,
							0, 0, 0, 1);
	}

	public static Matrix4f scale(Vector3f scale) {
		return new Matrix4f(scale.x, 0, 0, 0,
							0, scale.y, 0, 0,
							0, 0, scale.z, 0,
							0, 0, 0, 1);
	}
}
