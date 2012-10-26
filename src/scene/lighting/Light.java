package scene.lighting;

import java.awt.Color;

import scene.geometry.Vector3f;

public interface Light {

	public abstract Vector3f rayTo(Vector3f point);

	public abstract float intensity();

	public abstract Color color();

}