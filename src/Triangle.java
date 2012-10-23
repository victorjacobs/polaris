import java.awt.Color;


public class Triangle implements Surface {
	
	// Vertices
	private Vector3f v1;
	private Vector3f v2;
	private Vector3f v3;
	private Color fillColor;
	
	public Triangle(Vector3f v1, Vector3f v2, Vector3f v3, Color fillColor) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.fillColor = fillColor;
	}
	
	public Triangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, Color fillColor) {
		this(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2), new Vector3f(x3, y3, z3), fillColor);
	}
	
	// TODO: eventueel volgorde veranderen voor optimalere berekening, zie p79
	@Override
	public boolean hit(Ray ray, float t0, float t1) {
		
		float a = v1.x - v2.x;
		float b = v1.y - v2.y;
		float c = v1.z - v2.z;
		float d = v1.x - v3.x;
		float e = v1.y - v3.y;
		float f = v1.z - v3.z;
		float g = ray.getDirection().x;
		float h = ray.getDirection().y;
		float i = ray.getDirection().z;
		float j = v1.x - ray.getCamera().getPosition().x;
		float k = v1.y - ray.getCamera().getPosition().y;
		float l = v1.z - ray.getCamera().getPosition().z;
		
		// Compute M
		float M1 = e * i - h * f;
		float M2 = g * f - d * i;
		float M3 = d * h - e * g;
		float M = a * M1 + b * M2 + c * M3;
		
		// Beta
		float beta = (j * M1 + k * M2 + l * M3) / M;
		
		// Gamma
		float G1 = a * k - j * b;
		float G2 = j * c - a * l;
		float G3 = b * l - k * c;
		float gamma = (i * G1 + h * G2 + g * G3) / M;
		
		// t
		float t = - (f * G1 + e * G2 + d * G3) / M;
		
		if (t < t0 || t > t1) return false;
		if (gamma < 0 || gamma > 1) return false;
		if (beta < 0 || beta > 1 - gamma) return false;
				
		return true;
	}

	@Override
	public void boundingBox() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getColor() {
		return this.fillColor;
	}

}
