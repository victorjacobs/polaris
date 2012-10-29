package scene.lighting;

import scene.geometry.Vector3f;
import scene.material.Color3f;

public class AmbientLight implements Light {

	private float intensity;
	private Color3f color;
	
	public AmbientLight(Color3f color, float intensity) {
		this.intensity = intensity;
		this.color = color;
	}
	
	@Override
	public Vector3f rayTo(Vector3f point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float intensity() {
		return this.intensity;
	}

	@Override
	public Color3f color() {
		return this.color;
	}

}
