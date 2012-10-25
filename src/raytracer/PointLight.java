package raytracer;

import geometry.Vector3f;

import java.awt.Color;

public class PointLight {
	
	private Vector3f position;
	private Color color;		// Eventueel voor later
	private float intensity;
	
	public PointLight(Vector3f position) {
		this.position = position;
		this.color = Color.WHITE;
		this.intensity = 0.5f;
	}
	
	public Vector3f rayTo(Vector3f point) {
		return position.minus(point).normalize();
	}
	
	public float intensity() {
		return this.intensity;
	}
	
	public Color color() {
		return this.color;
	}
}
