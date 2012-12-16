package scene;

import raytracer.Grid;
import raytracer.Hit;
import raytracer.Ray;
import scene.geometry.Surface;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 24/11/12
 * Time: 00:39
 * To change this template use File | Settings | File Templates.
 */
public class GridAcceleratedScene extends SceneDecorator {
	private List<Surface> primitiveBag;
	private Grid grid;

	public GridAcceleratedScene(BasicScene scene) {
		super(scene);
		primitiveBag = new LinkedList<Surface>();
		System.out.println("Loaded grid accelerated renderer");
	}

	@Override
	public void addSurface(Surface surf) {
		scene.addSurface(surf);

		// Dump primitives in surf in the bag
		primitiveBag.addAll(surf.getPrimitiveSurfaces());
	}

	@Override
	// TODO: maybe refactor to Grid
	public Hit trace(Ray ray, float eps) {
		return grid.trace(ray, eps);
	}

	@Override
	public boolean preProcess() {
		if (grid == null) {
			System.out.println("Preprocessing scene");
			grid = new Grid(primitiveBag);
			return true;
		}

		return false;
	}

	@Override
	public void clear() {
		super.clear();

		primitiveBag.clear();
		grid = null;

		// JVM should come pick up garbage
		System.gc();
	}
}
