package scene.geometry;

import raytracer.BoundingBox;
import raytracer.Hit;
import raytracer.Ray;
import scene.data.*;
import scene.material.Material;

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
		
		return new Hit(ray, this, where, normal, getLocalCoordinateFor(new Point3f(where)), t);
	}

	@Override
	public BoundingBox boundingBox() {
		Vector3f min = new Vector3f(center.x - radius, center.y - radius, center.z - radius);
		Vector3f max = new Vector3f(center.x + radius, center.y + radius, center.z + radius);

		return new BoundingBox(min, max);
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

	private Point2f getLocalCoordinateFor(Point3f point) {
		float theta = (float)Math.acos((point.z - center.z) / radius);
		float phi = (float)Math.atan2((point.y - center.y), (point.x - center.x));

		phi = (phi < 0) ? (float)(phi + 2 * Math.PI) : phi;

		float u = (float)(phi / (2 * Math.PI));
		float v = (float)((Math.PI - theta) / Math.PI);

		return new Point2f(u, v);
	}

}
