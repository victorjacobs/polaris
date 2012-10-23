package geometry;

import java.awt.Color;

import raytracer.Ray;

public interface Surface {
	
	public boolean hit(Ray ray, float t0, float t1);
	public void boundingBox();
	public Color getColor();
	
}
