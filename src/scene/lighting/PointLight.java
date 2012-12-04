package scene.lighting;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import scene.Scene;
import scene.data.Vector3f;
import scene.material.Color3f;

public class PointLight implements Light {
	
	private Vector3f position;
	private Color3f color;		// Eventueel voor later
	private float intensity;
	
	public PointLight(Vector3f position) {
		this(position, 0.5f, new Color3f(1, 1, 1));
	}

	public PointLight(Vector3f position, float intensity) {
		this(position, intensity, new Color3f(1, 1, 1));
	}

	public PointLight(Vector3f position, float intensity, Color3f color) {
		this.position = position;
		this.intensity = intensity;
		this.color = color;
	}
	
	/* (non-Javadoc)
	 * @see scene.lighting.Light#rayTo(scene.data.Vector3f)
	 */
	@Override
	public Vector3f rayTo(Vector3f point) {
		return position.minus(point).normalize();
	}

	@Override
	public float getShadowPercentage(Scene scene, Vector3f point) {
		Hit lightHit = scene.trace(new Ray(point, rayTo(point)), Settings.EPS);

		// FIXED: if the ray hits something beyond the light (t > 1) -> no shadow!
		if (lightHit != null && lightHit.getT() > 1)
			return 0;

		return (lightHit == null) ? 0 : lightHit.getSurface().getMaterial().getShadowPercentage();
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
