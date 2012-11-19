package scene.lighting;

import scene.data.Vector3f;
import scene.material.Color3f;

public interface Light {

	public abstract Vector3f rayTo(Vector3f point);

	public abstract float intensity();

	public abstract Color3f color();

}