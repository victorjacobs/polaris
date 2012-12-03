package scene.lighting;

import scene.Scene;
import scene.data.Vector3f;
import scene.material.Color3f;

public class DirectionalLight implements Light {

	@Override
	public Vector3f rayTo(Vector3f point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getShadowPercentage(Scene scene, Vector3f point) {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public float intensity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Color3f color() {
		// TODO Auto-generated method stub
		return null;
	}

}
