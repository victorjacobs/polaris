package scene.lighting;

import java.awt.Color;
import scene.geometry.Vector3f;

public class PointLight implements Light {
	
	private Vector3f position;
	private Color color;		// Eventueel voor later
	private float intensity;
	
	public PointLight(Vector3f position) {
		this.position = position;
		this.color = Color.WHITE;
		this.intensity = 0.5f;
	}
	
	/* (non-Javadoc)
	 * @see scene.lighting.Light#rayTo(scene.geometry.Vector3f)
	 */
	@Override
	public Vector3f rayTo(Vector3f point) {
		return position.minus(point).normalize();
	}
	
	/* (non-Javadoc)
	 * @see scene.lighting.Light#intensity()
	 */
	@Override
	public float intensity() {
		return this.intensity;
	}
	
	/* (non-Javadoc)
	 * @see scene.lighting.Light#color()
	 */
	@Override
	public Color color() {
		return this.color;
	}
}
