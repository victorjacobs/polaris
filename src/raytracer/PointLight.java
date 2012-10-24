package raytracer;

import geometry.Vector3f;

import java.awt.Color;

public class PointLight {
	
	private Vector3f position;
	private Color color;		// Eventueel voor later
	
	public PointLight(Vector3f position) {
		this.position = position;
		this.color = Color.WHITE;
	}
	
	public Vector3f rayTo(Vector3f point) {
		return position.minus(point);
	}
}
