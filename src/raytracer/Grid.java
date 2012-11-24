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

	private List<Surface> surfaceBag;

	// Number of cells in grid
	private int[] M;

	// Elements
	private Surface[] L;
	// Offsets
	private int[] C;

	public Grid(List<Surface> surfaceBag) {
		this.surfaceBag = surfaceBag;
		this.M = new int[3];

		calculateGridDimensions();

		// Build grid
	}

	private void calculateGridDimensions() {
		for (Surface surf : surfaceBag) {
			if (bb == null)
				bb = new BoundingBox(surf.boundingBox());

			bb = bb.add(surf.boundingBox());
		}

		float[] dimensions = bb.size();
		float volume = bb.getVolume();

		for (int i = 0; i < 3; i++) {
			M[i] = Math.round(dimensions[i] * (float)Math.sqrt((gridDensity * surfaceBag.size()) / volume));
		}


	}
}
