package raytracer;

import scene.TraversalStrategy;
import scene.geometry.Surface;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 25/01/13
 * Time: 15:03
 */
public class BoundingVolumeHierarchy implements TraversalStrategy {

	private KDTree tree;

	@Override
	public Hit trace(Ray ray, float eps) {
		return tree.trace(ray, eps);
	}

	@Override
	public void prepare(List<Surface> primitiveBag) {
		tree = new KDTree(primitiveBag);
	}

	@Override
	public TraversalStrategy clean() {
		return new BoundingVolumeHierarchy();
	}
}
