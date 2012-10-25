package scene.geometry;

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
}
