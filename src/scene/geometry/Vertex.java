package scene.geometry;

import scene.data.Matrix4f;
import scene.data.Vector3f;
import scene.data.Vector4f;

/**
 * Class representing a vertex, this means: a point + normal vector in point + texture vector in point
 * @author victor
 *
 */
public class Vertex {

	private Vector3f point;
	private Vector3f normal;
	private Vector3f texture;
	
	public Vertex() {
		
	}
	
	public Vertex(Vector3f point, Vector3f normal) {
		this.point = point;
		this.normal = normal;
	}
	
	public Vertex(Vector3f point) {
		this.point = point;
	}
	
	public Vector3f getPoint() {
		return point;
	}

	public void setPoint(Vector3f point) {
		this.point = point;
	}

	public Vector3f getNormal() {
		return normal;
	}

	public void setNormal(Vector3f normal) {
		this.normal = normal;
	}

	public Vector3f getTexture() {
		return texture;
	}

	public void setTexture(Vector3f texture) {
		this.texture = texture;
	}

	public Vertex applyTransformation(Matrix4f transformation) {
		// Note: normal vectors shouldn't be translated!! Therefore reset the last row and column of the transformation matrix
		Vector4f pointHomogeneous = new Vector4f(point.x, point.y, point.z, 1);
		Vector4f normalHomogeneous = new Vector4f(normal.x, normal.y, normal.z, 1);

		pointHomogeneous = transformation.multiply(pointHomogeneous);
		transformation.setColumn(3, 0, 0, 0, 1);
		transformation.setRow(3, 0, 0, 0, 1);
		normalHomogeneous = transformation.multiply(normalHomogeneous);

		Vector3f newPoint = new Vector3f(pointHomogeneous.x, pointHomogeneous.y, pointHomogeneous.z);
		Vector3f newNormal = new Vector3f(normalHomogeneous.x, normalHomogeneous.y, normalHomogeneous.z);

		return new Vertex(newPoint, newNormal.normalize());
	}
}
