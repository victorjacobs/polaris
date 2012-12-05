package scene;

import raytracer.Hit;
import raytracer.KDTree;
import raytracer.Ray;
import scene.geometry.Surface;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 01/12/12
 * Time: 17:51
 */
public class BoundingBoxAcceleratedScene extends SceneDecorator {
	private KDTree tree;
	private List<Surface> primitiveBag;

	public BoundingBoxAcceleratedScene(Scene scene) {
		super(scene);

		System.out.println("BVH KDTree acceleration structure loaded");

		primitiveBag = new LinkedList<Surface>();
	}

	@Override
	public void addSurface(Surface surface) {
		super.addSurface(surface);

		primitiveBag.addAll(surface.getPrimitiveSurfaces());
	}

	@Override
	public Hit trace(Ray ray, float eps) {
		return tree.hit(ray, eps);
	}

	@Override
	public void preProcess() {
		if (tree == null) {
			System.out.println("Preprocessing scene");

			long startTime = System.currentTimeMillis();

			tree = new KDTree(primitiveBag);

			System.out.println("Building KDTree took " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}

	@Override
	public void clear() {
		super.clear();

		tree = null;
		primitiveBag.clear();

		// JVM should come pick up garbage
		System.gc();
	}
}
