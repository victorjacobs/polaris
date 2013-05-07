package raytracer;

import scene.TraversalStrategy;
import scene.data.Vector3f;
import scene.geometry.Surface;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 19/11/12
 * Time: 12:06
 * To change this template use File | Settings | File Templates.
 */
public class Grid implements TraversalStrategy {
	// Bounding box of the entire scene
	private BoundingBox bb;

	// Bag filled with all primitives that are included in the grid
	private List<Surface> primitiveBag;

	// Number of cells in grid
	private int[] M;
	private int totalNumberOfCells;

	// Size of the cells
	private float[] cellSize;

	// Elements
	private Surface[] L;
	// Offsets
	private int[] C;

	public Grid() {
		this.M = new int[3];
	}

	private void buildGrid() {
		long startTime = System.currentTimeMillis();

		// Allocate memory to C
		// Note: +1 for last element C[N] that contains total length
		C = new int[totalNumberOfCells + 1];

		// Iteration variables
		Vector3f max;
		Vector3f min;

		int xMin, xMax, yMin, yMax, zMin, zMax;

		// Build grid
		// First loop: build C
		for (Surface surf : primitiveBag) {
			// Find out in which cell this primitive is
			// Transform to grid coordinates
			min = surf.boundingBox().getMin().minus(bb.getMin()).divideBy(cellSize);
			max = surf.boundingBox().getMax().minus(bb.getMin()).divideBy(cellSize);

			// FIXED: loop broken: should clamp min and max in loop, not only in linearizeCellCoords(),
			// this made that elements were added twice

			xMin = clamp((int)Math.floor(min.x), 0, M[0] - 1);
			xMax = clamp((int)Math.floor(max.x), 0, M[0] - 1);
			yMin = clamp((int)Math.floor(min.y), 0, M[1] - 1);
			yMax = clamp((int)Math.floor(max.y), 0, M[1] - 1);
			zMin = clamp((int)Math.floor(min.z), 0, M[2] - 1);
			zMax = clamp((int)Math.floor(max.z), 0, M[2] - 1);

			// Add this surface to all cells that overlap
			for (int x = xMin; x <= xMax; x++) {
				for (int y = yMin; y <= yMax; y++) {
					for (int z = zMin; z <= zMax; z++) {
						C[linearizeCellCoords(x, y, z)] += 1;
					}
				}
			}
		}

		// Modify C so that every element points to absolute index, and not just to number of elements in the cell
		// Note: this makes the structure so that C[i] actually points to next cell!
		for (int i = 1; i <= totalNumberOfCells; i++) {
			C[i] += C[i - 1];
		}

		// C[M - 1] now contains total length of list, allocate L
		L = new Surface[C[totalNumberOfCells - 1]];

		// Start filling up L
		for (Surface surf : primitiveBag) {
			min = surf.boundingBox().getMin().minus(bb.getMin()).divideBy(cellSize);
			max = surf.boundingBox().getMax().minus(bb.getMin()).divideBy(cellSize);

			xMin = clamp((int)Math.floor(min.x), 0, M[0] - 1);
			xMax = clamp((int)Math.floor(max.x), 0, M[0] - 1);
			yMin = clamp((int)Math.floor(min.y), 0, M[1] - 1);
			yMax = clamp((int)Math.floor(max.y), 0, M[1] - 1);
			zMin = clamp((int)Math.floor(min.z), 0, M[2] - 1);
			zMax = clamp((int)Math.floor(max.z), 0, M[2] - 1);

			// Add this surface to all cells that overlap
			for (int x = xMin; x <= xMax; x++) {
				for (int y = yMin; y <= yMax; y++) {
					for (int z = zMin; z <= zMax; z++) {
						L[--C[linearizeCellCoords(x, y, z)]] = surf;
					}
				}
			}
		}

		System.out.println("Grid built in " + (System.currentTimeMillis() - startTime) + "ms");
		System.out.print("  Number of cells: { ");
		for (int m : M) System.out.print(m + " ");
		System.out.println("}");
		System.out.print("  Cell size: { ");
		for (float size : cellSize) System.out.print(size + " ");
		System.out.println("}");
		System.out.println("  C has size: " + C.length);
		System.out.println("  L has size: " + L.length);
		System.out.println("  Min: " + bb.getMin() + " max: " + bb.getMax());

		// After this operation, C is properly built, this means that C[O] actually points to the start index of cell 0
	}

	@Override
	public Hit trace(Ray ray, float eps) {
		Vector3f d = ray.getDirection();

		// Ray misses grid
		Vector3f gridEntryPoint = (hit(ray) == null) ? null : hit(ray).getPoint();
		if (gridEntryPoint == null) return null;

		// Number of cells
		int[] numOfCells = getNumberOfCells();

		// Cell size
		float[] cellSize = getCellSize();

		// Eye point ray expressed in coordinate system of the grid
		Vector3f oGrid = gridEntryPoint.minus(getOrigin());
		// Cell coordinate where eye point is found
		float[] oCell = {clamp(oGrid.x / cellSize[0], 0, numOfCells[0] - 1),
				clamp(oGrid.y / cellSize[1], 0, numOfCells[1] - 1),
				clamp(oGrid.z / cellSize[2], 0, numOfCells[2] - 1)};

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

		// Optimize loops by splitting declaration from actual use
		List<Surface> surfaces = null;
		float lowestT = 0;
		Hit hit, closestHit;
		float nextTX = tX, nextTY = tY, nextTZ = tZ;

		// Result variable
		// Also take into account that ray doesn't start at origin
		int[] cell = {(int)Math.floor(oCell[0]), (int)Math.floor(oCell[1]), (int)Math.floor(oCell[2])};

		// Traverse cells
		// TODO change the condition here to make sure it ends
		int cellsTraversed = 0;
		while (true) {
			// FIXED should check cell where ray starts before traversing!!
			// Check for intersection
			surfaces = getSurfacesForCell(cell);

			if (!surfaces.isEmpty()) {
				lowestT = Float.POSITIVE_INFINITY;
				closestHit = null;

				for (Surface surface : surfaces) {
					hit = surface.hit(ray, eps, lowestT);

					if (hit != null) {
						// FIXED: check whether hit point is actually in cell
						// Check whether hit was actually in cell and not outside of cell
						if (hit.getPoint().x >= cell[0] * cellSize[0] + getOrigin().x
								&& hit.getPoint().x <= (cell[0] + 1) * cellSize[0] + getOrigin().x
								&& hit.getPoint().y >= cell[1] * cellSize[1] + getOrigin().y
								&& hit.getPoint().y <= (cell[1] + 1) * cellSize[1] + getOrigin().y
								&& hit.getPoint().z >= cell[2] * cellSize[2] + getOrigin().z
								&& hit.getPoint().z <= (cell[2] + 1) * cellSize[2] + getOrigin().z) {
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

			// TODO herp derp
			if (cellsTraversed > 100000) throw new RuntimeException("Traversing stalled, current cell: " + cell[0] + " " + cell[1] + " " + cell[2]);
		}
	}

	@Override
	public void prepare(List<Surface> primitiveBag) {
		this.primitiveBag = primitiveBag;

		calculateGridDimensions();
		buildGrid();
	}

	@Override
	public TraversalStrategy clean() {
		return new Grid();
	}

	private int linearizeCellCoords(int x, int y, int z) {
		// This construction is needed because if an object is part of the overall bounding box, the calculation for
		// cell coordinates breaks down.
		// Clamp variables
		x = (x == M[0]) ? M[0] - 1 : x;
		y = (y == M[1]) ? M[1] - 1 : y;
		z = (z == M[2]) ? M[2] - 1 : z;
		return (((M[1] * z) + y) * M[0]) + x;
	}

	private void calculateGridDimensions() {
		// Number of cells
		for (Surface surf : primitiveBag) {
			if (bb == null)
				bb = new BoundingBox(surf.boundingBox());

			bb = bb.add(surf.boundingBox());
		}

		// Empty scene
		if (bb == null)
			bb = new BoundingBox(new Vector3f(0, 0, 0), new Vector3f(0, 0, 0));

		// Add some eps to the boundingbox
		// TODO is this okay value? NOPE
		bb = bb.increaseBy(0.000001f * bb.getVolume());

		float[] dimensions = bb.size();
		float volume = bb.getVolume();

		for (int i = 0; i < 3; i++) {
			// Enforce that at there is at least one cell in either dimension
			M[i] = Math.max(1, Math.round(dimensions[i] * (float)Math.cbrt((Settings.GRID_DENSITY * primitiveBag.size()) / volume)));
		}

		totalNumberOfCells = M[0] * M[1] * M[2];

		// Size of cells
		cellSize = new float[3];

		for (int i = 0; i < 3; i++) {
			cellSize[i] = (bb.getMax().minus(bb.getMin())).get(i) / M[i];
		}
	}

	public float[] getCellSize() {
		return cellSize.clone();
	}

	private Hit hit(Ray ray) {
		return bb.hit(ray);
	}

	private List<Surface> getSurfacesForCell(int[] cell) {
		List<Surface> out = new LinkedList<Surface>();
		int linearizedCoords = linearizeCellCoords(cell[0], cell[1], cell[2]);

		for (int i = C[linearizedCoords]; i < C[linearizedCoords + 1]; i++) {
			out.add(L[i]);
		}

		return out;
	}

	public int[] getNumberOfCells() {
		return M.clone();
	}

	public Vector3f getOrigin() {
		return bb.getMin();
	}


	private int clamp(int val, int min, int max) {
		return (int) clamp((float) val, (float) min, (float) max);
	}

	private float clamp(float val, float min, float max) {
		if (val < min) return min;
		if (val > max) return max;
		return val;
	}
}
