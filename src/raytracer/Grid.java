package raytracer;

import scene.geometry.Surface;

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

	// Elements
	private Surface[] L;
	// Offsets
	private int[] C;

	public Grid(List<Surface> primitiveBag) {
		this.primitiveBag = primitiveBag;
		this.M = new int[3];

		calculateGridDimensions();

		// Build grid
	}

	private void calculateGridDimensions() {
		for (Surface surf : primitiveBag) {
			if (bb == null)
				bb = new BoundingBox(surf.boundingBox());

			bb = bb.add(surf.boundingBox());
		}

		float[] dimensions = bb.size();
		float volume = bb.getVolume();

		for (int i = 0; i < 3; i++) {
			M[i] = Math.round(dimensions[i] * (float)Math.sqrt((gridDensity * primitiveBag.size()) / volume));
		}
	}

	public float[] getCellSize() {
		float[] out = new float[3];

		for (int i = 0; i < 3; i++) {
			out[i] = (bb.getMax().minus(bb.getMin())).get(i) / M[i];
		}

		return out;
	}

	public List<Surface> getSurfacesForCell(int[] cell) {
		return null;
	}

	public int[] getNumberOfCells() {
		return M.clone();
	}
}
