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
		// TODO this is a bad thing for multithreading
		if (grid == null)
			grid = new Grid(primitiveBag);

		// Ray misses grid
		Vector3f gridEntryPoint = grid.hit(ray);
		if (gridEntryPoint == null) return null;

		//System.out.println(gridEntryPoint);

		// Number of cells
		int[] numOfCells = grid.getNumberOfCells();

		// Cell size
		float[] cellSize = grid.getCellSize();

		// Eye point ray expressed in coordinate system of the grid
		Vector3f oGrid = e.minus(grid.getOrigin());
		// Cell coordinate where eye point is found
		float[] oCell = {oGrid.x / cellSize[0], oGrid.y/ cellSize[1], oGrid.z / cellSize[2]};

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
		float t = 0;
		List<Surface> surfaces = null;
		float lowestT = 0;
		Hit hit, closestHit;

		// Result variable
		// Also take into account that ray doesn't start at origin
		int[] cell = {(int)Math.floor(oCell[0]), (int)Math.floor(oCell[1]), (int)Math.floor(oCell[2])};

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
			} else {
				t = tZ;
				tZ += delta[2];
				cell[2] += Math.signum(d.z);
			}

			// Check if outside bounding box
			for (int i = 0; i < 3; i++) {
				if (cell[i] >= numOfCells[i] || cell[i] < 0) return null;
			}

			// Check for intersection
			surfaces = grid.getSurfacesForCell(cell);

			if (surfaces.isEmpty()) continue;

			lowestT = Float.POSITIVE_INFINITY;
			closestHit = null;

			for (Surface surface : surfaces) {
				hit = surface.hit(ray, eps, lowestT);

				if (hit != null) {
					lowestT = hit.getT();
					closestHit = hit;
				}
			}

			return closestHit;
		}

		//return scene.trace(ray, eps);
	}

	@Override
	public Hit traceAny(Ray ray, float eps) {
		return scene.traceAny(ray, eps);
	}
}
