package scene.geometry;

import raytracer.BoundingBox;
import raytracer.Hit;
import raytracer.Ray;
import scene.material.Material;
import scene.data.Matrix4f;
import scene.data.Vector3f;
import scene.data.Vector4f;

public class Sphere extends Surface {
	
	private Vector3f center;
	private float radius;
	private Material material;

	public Sphere(float radius) {
		this.radius = radius;
		this.center = new Vector3f(0, 0, 0);
	}

	public Sphere(Vector3f center, float radius, Material mat) {
		this.center = center;
		this.radius = radius;
		this.material = mat;
	}

	@Override
	public Hit hit(Ray ray, float t0, float t1) {
		// Compute discriminant (see page 77)
		float A = ray.getDirection().dotProduct(ray.getDirection());
		float B = ray.getOrigin().minus(center).multiply(2).dotProduct(ray.getDirection());
		Vector3f C1 = ray.getOrigin().minus(center);
		float C = C1.dotProduct(C1) - radius * radius;
		
		float disc = B * B - 4 * A * C;
		
		if (disc < 0) return null;

		// t berekenen voor z buffering
		float tPlus = (-B + (float)Math.sqrt(disc)) / (2 * A);
		float tMin = (-B - (float)Math.sqrt(disc)) / (2 * A);

		float t;

		if (tMin > t0 && tMin < t1) {
			t = tMin;
		} else if (tPlus > t0 && tPlus < t1) {
			t = tPlus;
		} else {
			return null;
		}

		// Calculate hit point
		Vector3f where = ray.getOrigin().sum(ray.getDirection().multiply(t));
		
		// Normal vector
		Vector3f normal = where.minus(center).normalize();
		
		return new Hit(ray, this, where, normal, t);
	}

	@Override
	public BoundingBox boundingBox() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Material getMaterial() {
		return this.material;
	}

	@Override
	public void setMaterial(Material mat) {
		this.material = mat;
	}

	@Override
	public void applyTransformation(Matrix4f transformation) {
		Vector4f homogenous = new Vector4f(center.x, center.y, center.z, 1);
		Vector4f transformedCenter = transformation.multiply(homogenous);

		center = new Vector3f(transformedCenter.x, transformedCenter.y, transformedCenter.z);
	}

}
