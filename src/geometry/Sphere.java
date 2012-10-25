package geometry;

import java.awt.Color;

import raytracer.Hit;
import raytracer.Ray;

public class Sphere implements Surface {
	
	private Vector3f center;
	private float radius;
	private Color fillColor;
	
	public Sphere(Vector3f center, float radius, Color fillColor) {
		this.center = center;
		this.radius = radius;
		this.fillColor = fillColor;
	}

	@Override
	public Hit hit(Ray ray, float t0, float t1) {
		// Compute discriminant (see page 77)
		float A = ray.getDirection().dotProduct(ray.getDirection());
		float B = ray.getDirection().multiply(2).dotProduct(ray.getCamera().getPosition().minus(center));
		Vector3f C1 = ray.getCamera().getPosition().minus(center);
		float C = C1.dotProduct(C1) - radius * radius;
		
		float disc = B * B - 4 * A * C;
		
		if (disc < 0) return null;
		
		// t berekenen voor z buffering
		float tPlus = (ray.getDirection().negate().dotProduct(C1) + disc) / ray.getDirection().dotProduct(ray.getDirection());
		float tMin = (ray.getDirection().negate().dotProduct(C1) - disc) / ray.getDirection().dotProduct(ray.getDirection());
		
		System.out.println("tPlus: " + tPlus + " tMin: " + tMin);
		
		//float t = Math.min(tPlus, tMin);
		float t = Math.abs(Math.min(tPlus, tMin));
		
		if (t < t0 || t > t1) return null;
		
		// Calculate hit point
		Vector3f where = ray.getCamera().getPosition().sum(ray.getDirection().multiply(t));
		
		// Normal vector TODO ofwel omgekeerd
		Vector3f normal = center.minus(where);
		
		return new Hit(this, where, normal, tPlus);
	}

	@Override
	public void boundingBox() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getColor() {
		return fillColor;
	}

}
