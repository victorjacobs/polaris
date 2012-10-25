package geometry;

import java.awt.Color;

import raytracer.Ray;

public interface Surface {
	
	// TODO refactor to returning an object that contains hit information instead of storing in object
	public boolean hit(Ray ray, float t0, float t1);
	public void boundingBox();
	public Color getColor();
	
	public float getCurrentT();
	public Vector3f normalInHitPoint();
	
}
