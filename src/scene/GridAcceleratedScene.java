package scene;

import raytracer.*;
import scene.data.Vector3f;
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
		Vector3f d = ray.getDirection();
		Vector3f e = ray.getOrigin();

		// Ray misses grid
		Vector3f gridEntryPoint = (grid.hit(ray) == null) ? null : grid.hit(ray).getPoint();
		if (gridEntryPoint == null) return null;

		// Number of cells
		int[] numOfCells = grid.getNumberOfCells();

		// Cell size
		float[] cellSize = grid.getCellSize();

		// Eye point ray expressed in coordinate system of the grid
		Vector3f oGrid = gridEntryPoint.minus(grid.getOrigin());
		// Cell coordinate where eye point is found
		float[] oCell = {grid.clamp(oGrid.x / cellSize[0], 0, numOfCells[0] - 1),
				grid.clamp(oGrid.y / cellSize[1], 0, numOfCells[1] - 1),
				grid.clamp(oGrid.z / cellSize[2], 0, numOfCells[2] - 1)};

		// Calculate deltas
		// Absolute value is needed because if R is negative, the delta still needs to be positive (since t should
		// always be positive, it's direction along ray)
		float[] delta = new float[3];
		delta[0] = Math.abs(cellSize[0] / (d.x));
		delta[1] = Math.abs(cellSize[1] / (d.y));
		delta[2] = Math.abs(cellSize[2] / (d.z));

		// Iteration variables, start values
		// This takes into account that the ray doesn't have to start in (0, 0, 0)
		float tX, tY, tZ;

		if (d.x < 0) {
			tX = ((float)Math.floor(oCell[0]) * cellSize[0] - oGrid.x) / (d.x);
		} else {
			tX = (((float)Math.floor(oCell[0]) + 1) * cellSize[0] - oGrid.x) / (d.x);
		}

		if (d.y < 0) {
			tY = ((float)Math.floor(oCell[1]) * cellSize[1] - oGrid.y) / (d.y);
		} else {
			tY = (((float)Math.floor(oCell[1]) + 1) * cellSize[1] - oGrid.y) / (d.y);
		}

		if (d.z < 0) {
			tZ = ((float)Math.floor(oCell[2]) * cellSize[2] - oGrid.z) / (d.z);
		} else {
			tZ = (((float)Math.floor(oCell[2]) + 1) * cellSize[2] - oGrid.z) / (d.z);
		}

		// Optimise loops by splitting declaration from actual use
		List<Surface> surfaces = null;
		float lowestT = 0;
		Hit hit, closestHit;
		float nextTX = tX, nextTY = tY, nextTZ = tZ;

		float hitX, hitY, hitZ;

		// Result variable
		// Also take into account that ray doesn't start at origin
		int[] cell = {(int)Math.floor(oCell[0]), (int)Math.floor(oCell[1]), (int)Math.floor(oCell[2])};

		// Traverse cells
		// TODO change the condition here to make sure it ends
		int cellsTraversed = 0;
		while (true) {
			// FIXED should check cell where ray starts before traversing!!
			// Check for intersection
			surfaces = grid.getSurfacesForCell(cell);

			if (!surfaces.isEmpty()) {
				lowestT = Float.POSITIVE_INFINITY;
				closestHit = null;

				for (Surface surface : surfaces) {
					hit = surface.hit(ray, eps, lowestT);

					if (Settings.COLLECT_STATS)
						Stats.incIntersections();

					if (hit != null) {
						// FIXED: check whether hit point is actually in cell
						// Check whether hit was actually in cell and not outside of cell
						if (hit.getPoint().x >= cell[0] * cellSize[0] + grid.getOrigin().x
								&& hit.getPoint().x <= (cell[0] + 1) * cellSize[0] + grid.getOrigin().x
								&& hit.getPoint().y >= cell[1] * cellSize[1] + grid.getOrigin().y
								&& hit.getPoint().y <= (cell[1] + 1) * cellSize[1] + grid.getOrigin().y
								&& hit.getPoint().z >= cell[2] * cellSize[2] + grid.getOrigin().z
								&& hit.getPoint().z <= (cell[2] + 1) * cellSize[2] + grid.getOrigin().z) {
							lowestT = hit.getT();
							closestHit = hit;
						}
					}
				}

				// FIXED: only return when a closest hit was found
				if (closestHit != null) return closestHit;
			}

			// FIXED: always using "<" could cause deadlocks
			if (tX <= tY && tX <= tZ) {
				// Next intersection
				nextTX += delta[0];
				// Increment/decrement next cell coordinate
				cell[0] += Math.signum(d.x);
			}

			if (tY <= tX && tY <= tZ) {
				nextTY += delta[1];
				cell[1] += Math.signum(d.y);
			}

			if (tZ <= tX && tZ <= tY) {
				nextTZ += delta[2];
				cell[2] += Math.signum(d.z);
			}

			tX = nextTX;
			tY = nextTY;
			tZ = nextTZ;

			// Check if outside bounding box
			for (int i = 0; i < 3; i++) {
				if (cell[i] >= numOfCells[i] || cell[i] < 0) return null;
			}

			cellsTraversed++;

			if (cellsTraversed > 100000) throw new RuntimeException("Traversing stalled, current cell: " + cell[0] + " " + cell[1] + " " + cell[2]);
		}
	}

	@Override
	public void preProcess() {
		if (grid == null) {
			System.out.println("Preprocessing scene");
			grid = new Grid(primitiveBag);
		}
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
