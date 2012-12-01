package scene;

import raytracer.Hit;
import raytracer.Ray;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 01/12/12
 * Time: 17:51
 */
public class BoundingBoxAcceleratedScene extends SceneDecorator {

	public BoundingBoxAcceleratedScene(Scene scene) {
		super(scene);
	}

	@Override
	public Hit trace(Ray ray, float eps) {
		return super.trace(ray, eps);    //To change body of overridden methods use File | Settings | File Templates.
	}

	@Override
	public void preProcess() {
		super.preProcess();    //To change body of overridden methods use File | Settings | File Templates.
	}
}
