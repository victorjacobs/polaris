package scene;

import raytracer.Hit;
import raytracer.KDTree;
import raytracer.Ray;
import scene.data.Vector3f;
import scene.geometry.Surface;
import scene.lighting.Light;

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
	private List<Surface> primitiveList;

	public BoundingBoxAcceleratedScene(Scene scene) {
		super(scene);

		System.out.println("BVH KDTree acceleration structure loaded");

		primitiveList = new LinkedList<Surface>();
	}

	@Override
	public void addSurface(Surface surface) {
		super.addSurface(surface);

		primitiveList.addAll(surface.getPrimitiveSurfaces());
	}

	@Override
	public boolean isInShade(Vector3f point, Light light) {
		// TODO OVERRIDE
		return scene.isInShade(point, light);
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

			tree = new KDTree(primitiveList);

			System.out.println("Building KDTree took " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}
}
