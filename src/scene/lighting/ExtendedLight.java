package scene.lighting;

import scene.data.Vector3f;
import scene.material.Color3f;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 19/11/12
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedLight implements Light {

	private Vector3f position;
	private float intensity;
	private Dimension size;

	public ExtendedLight() {

	}

	@Override
	public Vector3f rayTo(Vector3f point) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public float intensity() {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public Color3f color() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
