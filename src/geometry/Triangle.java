package geometry;

import java.awt.Color;

import raytracer.Ray;

public class Triangle implements Surface {
	
	// Vertices
	private Vertex v1;
	private Vertex v2;
	private Vertex v3;
	private Color fillColor;
	private float currentT;
	private Vector3f normalInHitPoint;
	
	// If single triangle, not in collection
	public Triangle(Vector3f v1, Vector3f v2, Vector3f v3, Color fillColor) {
		// Calculate normal vector for triangle and store in vertices
		Vector3f U = v2.minus(v1);
		Vector3f V = v3.minus(v1);
		
		Vector3f N = U.crossProduct(V);
		
		Vertex vert1 = new Vertex(v1, N);
		Vertex vert2 = new Vertex(v2, N);
		Vertex vert3 = new Vertex(v3, N);
		
		this.v1 = vert1;
		this.v2 = vert2;
		this.v3 = vert3;
		this.fillColor = fillColor;
	}
	
	// Triangle made out of vertices that have normal vectors 
	public Triangle(Vertex v1, Vertex v2, Vertex v3, Color fillColor) {
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
		
		float a = v1.getPoint().x - v2.getPoint().x;
		float b = v1.getPoint().y - v2.getPoint().y;
		float c = v1.getPoint().z - v2.getPoint().z;
		float d = v1.getPoint().x - v3.getPoint().x;
		float e = v1.getPoint().y - v3.getPoint().y;
		float f = v1.getPoint().z - v3.getPoint().z;
		float g = ray.getDirection().x;
		float h = ray.getDirection().y;
		float i = ray.getDirection().z;
		float j = v1.getPoint().x - ray.getCamera().getPosition().x;
		float k = v1.getPoint().y - ray.getCamera().getPosition().y;
		float l = v1.getPoint().z - ray.getCamera().getPosition().z;
		
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
		
		this.currentT = t;
		
		// Calculate normal vector on point of hit via barycentric coordinates, calculated  above (v1 is a)
		float alpha = 1 - beta - gamma;
		
		Vector3f interpolatedNormal = v1.getNormal().multiply(alpha).sum(v2.getNormal().multiply(beta).sum(v3.getNormal().multiply(gamma)));
		
		this.normalInHitPoint = interpolatedNormal;
		
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

	@Override
	public float getCurrentT() {
		return currentT;
	}

	@Override
	public Vector3f normalInHitPoint() {
		return this.normalInHitPoint;
	}

}
