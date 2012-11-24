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
public class Grid {
	private List<Surface> surfaceBag;

	private Surface[] grid;

	public Grid(List<Surface> surfaceBag) {
		this.surfaceBag = surfaceBag;

		// Build grid
	}
}
