package raytracer;

import scene.geometry.Surface;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 01/12/12
 * Time: 20:23
 */
public class KDTree {

	private KDTree left, right;
	private KDTreeNode node;

	public KDTree(List<Surface> data) {
		//if (data.length() < Settings.)
	}

	private class KDTreeNode<E> {

		private E data;

		private KDTreeNode() {

		}
	}
}
