package scene.geometry;

import raytracer.Hit;
import raytracer.Ray;
import scene.material.Material;

public class Sphere implements Surface {
	
	private Vector3f center;
	private float radius;
	private Material material;
	
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
		
		if (disc <= 0) return null;
		
		// t berekenen voor z buffering
		float tPlus = (-B + (float)Math.sqrt(disc)) / (2 * A);
		float tMin = (-B - (float)Math.sqrt(disc)) / (2 * A);
		
		
		float t = Math.min(tPlus, tMin);
		
		if (t < t0 || t > t1) return null;
		
		// Calculate hit point
		Vector3f where = ray.getOrigin().sum(ray.getDirection().multiply(t));
		
		// Normal vector
		Vector3f normal = where.minus(center).normalize();
		
		return new Hit(ray, this, where, normal, t);
	}

	@Override
	public void boundingBox() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

}
