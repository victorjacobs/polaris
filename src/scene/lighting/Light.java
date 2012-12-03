package scene.lighting;

import scene.Scene;
import scene.data.Vector3f;
import scene.material.Color3f;

public interface Light {

	@Deprecated		// Eigenlijk niet deprecated!!
	public abstract Vector3f rayTo(Vector3f point);

	public float getShadowPercentage(Scene scene, Vector3f point);

	public abstract float intensity();

	public abstract Color3f color();

}