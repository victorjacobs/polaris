package scene;

import raytracer.Grid;
import raytracer.Hit;
import raytracer.Ray;
import scene.data.Vector3f;
import scene.geometry.Surface;
import scene.lighting.Light;
import scene.material.Color3f;

import java.util.HashSet;
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
		System.out.println("Loaded grid accelerated renderereererer");
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
		Vector3f d = ray.getDirection();
		Vector3f e = ray.getOrigin();

		// If this is first ray that's traced, build grid
		if (grid == null)
			grid = new Grid(primitiveBag);

		// Number of cells
		int[] numOfCells = grid.getNumberOfCells();

		// Cell size
		float[] cellSize = grid.getCellSize();

		// Calculate deltas
		// Absolute value is needed because if R is negative, the delta still needs to be positive (since t should
		// always be positive, it's direction along ray)
		float[] delta = new float[3];
		delta[0] = Math.abs(cellSize[0] / (d.x));
		delta[1] = Math.abs(cellSize[1] / (d.y));
		delta[2] = Math.abs(cellSize[2] / (d.z));

		// Iteration variables, start values
		float tX = (cellSize[0] - e.x) / (d.x);
		float tY = (cellSize[1] - e.y) / (d.y);
		float tZ = (cellSize[2] - e.z) / (d.z);
		float t = 0;
		List<Surface> surfaces = null;

		// Result variable (starts at origin)
		int[] cell = {0, 0, 0};

		// Traverse cells
		// TODO change the condition here to make sure it ends
		while (true) {
			if (tX < tY && tX < tZ) {
				t = tX;
				// Next intersection
				tX += delta[0];
				// Increment/decrement next cell coordinate
				cell[0] += Math.signum(d.x);
			} else if (tY < tX && tY < tZ) {
				t = tY;
				tY += delta[1];
				cell[1] += Math.signum(d.y);
			} else if (tZ < tX && tZ < tY) {
				t = tZ;
				tZ += delta[2];
				cell[2] += Math.signum(d.z);
			} else {
				throw new IllegalStateException("wut");
			}

			// Check if outside bounding box
			for (int i = 0; i < 3; i++) {
				if (cell[i] > numOfCells[i] || cell[i] < 0) return null;
			}

			// Check for intersection
			surfaces = grid.getSurfacesForCell(cell);

			for (Surface surface : surfaces) {
				// Find closest hit here
			}
		}

		//return scene.trace(ray, eps);
	}

	@Override
	public Hit traceAny(Ray ray, float eps) {
		return scene.traceAny(ray, eps);
	}
}
