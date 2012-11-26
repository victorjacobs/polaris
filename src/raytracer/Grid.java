package raytracer;

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
// TODO complixity of addressing something in array <> linked list?
public class Grid {
	// Grid density
	private static final int gridDensity = 7;

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

	public Grid(List<Surface> primitiveBag) {
		this.primitiveBag = primitiveBag;
		this.M = new int[3];

		calculateGridDimensions();

		// Allocate memory to C
		// Note: +1 for last element C[N] that contains total length
		C = new int[totalNumberOfCells + 1];

		// Iteration variables
		Vector3f max;
		Vector3f min;

		// Build grid
		// First loop: build C
		for (Surface surf : primitiveBag) {
			// Find out in which cell this primitive is
			// Transform to grid coordinates
			min = surf.boundingBox().getMin().minus(bb.getMin()).divideBy(cellSize);
			max = surf.boundingBox().getMax().minus(bb.getMin()).divideBy(cellSize);

			// Add this surface to all cells that overlap
			for (int x = (int)Math.floor(min.x); x < (int)Math.floor(max.x); x++) {
				for (int y = (int)Math.floor(min.y); y < (int)Math.floor(max.y); y++) {
					for (int z = (int)Math.floor(min.z); z < (int)Math.floor(max.z); z++) {
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

			// Add this surface to all cells that overlap
			for (int x = (int)Math.floor(min.x); x < (int)Math.floor(max.x); x++) {
				for (int y = (int)Math.floor(min.y); y < (int)Math.floor(max.y); y++) {
					for (int z = (int)Math.floor(min.z); z < (int)Math.floor(max.z); z++) {
						L[--C[linearizeCellCoords(x, y, z)]] = surf;
					}
				}
			}
		}

		System.out.println("Grid built");
		System.out.print("  Number of cells: { ");
		for (int m : M) System.out.print(m + " ");
		System.out.println("}");
		System.out.print("  Cell size: { ");
		for (float size : cellSize) System.out.print(size + " ");
		System.out.println("}");
		System.out.println("  C has size: " + C.length);
		System.out.println("  L has size: " + L.length);

		// After this operation, C is properly built, this means that C[O] actually points to the start index of cell 0
	}

	private int linearizeCellCoords(int x, int y, int z) {
		return ((M[1] * z) + y) * M[0] + x;
	}

	private void calculateGridDimensions() {
		// Number of cells
		for (Surface surf : primitiveBag) {
			if (bb == null)
				bb = new BoundingBox(surf.boundingBox());

			bb = bb.add(surf.boundingBox());
		}

		float[] dimensions = bb.size();
		float volume = bb.getVolume();

		for (int i = 0; i < 3; i++) {
			M[i] = Math.round(dimensions[i] * (float)Math.cbrt((gridDensity * primitiveBag.size()) / volume));
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

	public Vector3f hit(Ray ray) {
		return bb.hit(ray);
	}

	public List<Surface> getSurfacesForCell(int[] cell) {
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
}
