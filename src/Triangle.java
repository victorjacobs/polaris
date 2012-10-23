
public class Triangle implements Surface {
	
	// Vertices
	private Vector3f v1;
	private Vector3f v2;
	private Vector3f v3;
	
	public Triangle(Vector3f v1, Vector3f v2, Vector3f v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}
	
	public Triangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3) {
		this(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2), new Vector3f(x3, y3, z3));
	}

	@Override
	public boolean hit(Ray ray, float t0, float t1) {
		// Compute M
		float M1 = (v1.y - v3.y) * ray.getDirection().z - ray.getDirection().y * (v1.z - v3.y);
		float M2 = ray.getDirection().x * (v1.z - v3.z) - (v1.x - v3.x) * ray.getDirection().z;
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void boundingBox() {
		// TODO Auto-generated method stub
		
	}

}
