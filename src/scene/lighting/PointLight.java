package scene.lighting;




import java.awt.Color;

import scene.geometry.Vector3f;



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
