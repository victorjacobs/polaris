package geometry;

import java.awt.Color;

import raytracer.Hit;
import raytracer.Ray;

public interface Surface {
	
	public Hit hit(Ray ray, float t0, float t1);
	public void boundingBox();
	public Color getColor();
	
	// TODO translate, rotate etc
	
}
