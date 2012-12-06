package scene.geometry;

import raytracer.*;
import scene.data.Matrix4f;
import scene.data.Point2f;
import scene.data.Vector3f;
import scene.material.Color3f;
import scene.material.DiffuseMaterial;
import scene.material.Material;

import java.util.Random;

public class Triangle extends Surface {
	
	// Vertices
	private Vertex v1;
	private Vertex v2;
	private Vertex v3;
	private Material material;
	
	// If single triangle, not in collection
	public Triangle(Vector3f v1, Vector3f v2, Vector3f v3, Material mat) {
		// Calculate normal vector for triangle and store in vertices
		Vector3f N = generateNormal(v1, v2, v3);
		
		Vertex vert1 = new Vertex(v1, N);
		Vertex vert2 = new Vertex(v2, N);
		Vertex vert3 = new Vertex(v3, N);
		
		this.v1 = vert1;
		this.v2 = vert2;
		this.v3 = vert3;
		this.material = mat;
	}

	public Triangle(Triangle triag) {
		this.v1 = new Vertex(triag.v1);
		this.v2 = new Vertex(triag.v2);
		this.v3 = new Vertex(triag.v3);

		// TODO clone??!
		this.material = triag.material;
	}

	private Vector3f generateNormal(Vector3f v1, Vector3f v2, Vector3f v3) {
		Vector3f U = v2.minus(v1);
		Vector3f V = v3.minus(v1);

		return U.crossProduct(V);
	}

	public Triangle(Vector3f v1, Vector3f v2, Vector3f v3) {
		this(v1, v2, v3, null);
	}
	
	// Triangle made out of vertices that have normal vectors 
	public Triangle(Vertex v1, Vertex v2, Vertex v3, Material mat) {
		if (v1.getNormal() == null) {
			Vector3f N = generateNormal(v1.getPoint(), v2.getPoint(), v3.getPoint());

			v1 = new Vertex(v1.getPoint(), N, v1.getTexture());
			v2 = new Vertex(v2.getPoint(), N, v2.getTexture());
			v3 = new Vertex(v3.getPoint(), N, v3.getTexture());
		}

		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.material = mat;
	}
	
	public Triangle(Vertex v1, Vertex v2, Vertex v3) {
		this(v1, v2, v3, null);
	}
	
	public Triangle(float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, Material mat) {
		this(new Vector3f(x1, y1, z1), new Vector3f(x2, y2, z2), new Vector3f(x3, y3, z3), mat);
	}
	
	
	@Override
	public Hit hit(Ray ray, float t0, float t1) {
		if (Settings.COLLECT_STATS)
			Stats.incIntersections();

		float a = v1.getPoint().x - v2.getPoint().x;
		float b = v1.getPoint().y - v2.getPoint().y;
		float c = v1.getPoint().z - v2.getPoint().z;
		float d = v1.getPoint().x - v3.getPoint().x;
		float e = v1.getPoint().y - v3.getPoint().y;
		float f = v1.getPoint().z - v3.getPoint().z;
		float g = ray.getDirection().x;
		float h = ray.getDirection().y;
		float i = ray.getDirection().z;
		float j = v1.getPoint().x - ray.getOrigin().x;
		float k = v1.getPoint().y - ray.getOrigin().y;
		float l = v1.getPoint().z - ray.getOrigin().z;
		
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
		
		if (t < t0 || t > t1) return null;
		if (gamma < 0 || gamma > 1) return null;
		if (beta < 0 || beta > 1 - gamma) return null;
		
		// Calculate normal vector on point of hit via barycentric coordinates, calculated  above (v1 is a)
		float alpha = 1 - beta - gamma;
		
		Vector3f interpolatedNormal = v1.getNormal().multiply(alpha).sum(v2.getNormal().multiply(beta).sum(v3.getNormal().multiply(gamma)));

		Point2f interpolatedTexture = null;
		if (v1.hasTextureCoordinate()) {
			interpolatedTexture = v1.getTexture().multiply(alpha).sum(v2.getTexture().multiply(beta).sum(v3.getTexture().multiply(gamma)));
		}
		
		// Calculate in what point the hit occurred
		Vector3f where = ray.getOrigin().sum(ray.getDirection().multiply(t));
		
		return new Hit(ray, this, where, interpolatedNormal.normalize(), interpolatedTexture, t);
	}

	@Override
	public BoundingBox boundingBox() {
		// TODO this method might be useful globally
		// For make benefit storing of vertices in array TODO maybe move this to class level
		Vector3f[] vertices = new Vector3f[3];
		vertices[0] = v1.getPoint();
		vertices[1] = v2.getPoint();
		vertices[2] = v3.getPoint();

		return new BoundingBox(vertices);
	}

	@Override
	public Material getMaterial() {
		return this.material;
	}

	@Override
	public void setMaterial(Material mat) {
		if (Settings.RANDOM_COLOR_TRIANGLES) {
			Random rand = new Random();
			this.material = new DiffuseMaterial(new Color3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
		} else {
			this.material = mat;
		}
	}

	@Override
	public void applyTransformation(Matrix4f transformation) {
		v1 = v1.applyTransformation(transformation);
		v2 = v2.applyTransformation(transformation);
		v3 = v3.applyTransformation(transformation);
	}

	@Override
	public float getProjectedSurfaceArea() {
		// Use v1 as origin for local axis system, calculate area of of square defined by transformed v2 and v3
		Vector3f l2 = v2.getPoint().minus(v1.getPoint());
		Vector3f l3 = v3.getPoint().minus(v1.getPoint());

		return (l2.norm() * l3.norm()) / 4;
	}

}
