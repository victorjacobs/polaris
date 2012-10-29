package scene.lighting;

import scene.geometry.Vector3f;
import scene.material.Color3f;

public class PointLight implements Light {
	
	private Vector3f position;
	private Color3f color;		// Eventueel voor later
	private float intensity;
	
	public PointLight(Vector3f position) {
		this.position = position;
		this.color = new Color3f(1, 1, 1);
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
	public Color3f color() {
		return this.color;
	}
}
