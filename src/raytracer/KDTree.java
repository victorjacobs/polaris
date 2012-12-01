package raytracer;

import scene.data.Vector3f;
import scene.geometry.Surface;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 01/12/12
 * Time: 20:23
 */
public class KDTree {

	private KDTree left, right;
	// leafData is null when this isn't a leaf
	private List<Surface> leafData = null;
	// Cache bounding box around this tree (or leaf)
	private BoundingBox boundingBox;

	public KDTree(List<Surface> data) {
		this(data, 0);
	}

	/**
	 *
	 * @param data
	 * @param nextAxis	Next axis over which to split the tree (0 == x, 1 == y, 2 == z)
	 */
	private KDTree(List<Surface> data, int nextAxis) {
		// Always generate a bounding box around the data
		boundingBox = generateBoundingBox(data);

		if (data.size() < Settings.KDTREE_ELEMENTS_IN_LEAF) {
			left = null;
			right = null;

			leafData = data;
		} else {
			// Split up data set and build sub trees
			List<Surface> leftData = new LinkedList<Surface>();
			List<Surface> rightData = new LinkedList<Surface>();

			// Calculate split
			Vector3f split = boundingBox.getMax().minus(boundingBox.getMin()).divideBy(2).sum(boundingBox.getMin());

			for (Surface surf : data) {
				if (surf.boundingBox().getMin().get(nextAxis) < split.get(nextAxis)) {
					leftData.add(surf);
				}

				if (surf.boundingBox().getMax().get(nextAxis) > split.get(nextAxis)) {
					rightData.add(surf);
				}
			}

			left = new KDTree(leftData, (nextAxis + 1) % 3);
			right = new KDTree(rightData, (nextAxis + 1) % 3);
		}
	}

	private BoundingBox generateBoundingBox(List<Surface> surfaces) {
		BoundingBox bb = new BoundingBox();

		for (Surface surf : surfaces) {
			bb = bb.add(surf.boundingBox());
		}

		return bb;
	}

	public Hit hit(Ray ray, float eps) {
		Hit actualHit = null;

		if (isLeaf()) {
			Hit hit;
			float lowestT = Float.MAX_VALUE;

			for (Surface surf : leafData) {
				hit = surf.hit(ray, eps, lowestT);

				if (hit != null) {
					lowestT = hit.getT();
					actualHit = hit;
				}
			}
		} else {
			// Hit left and right child
			Hit leftHit = left.hit(ray, eps);
			Hit rightHit = right.hit(ray, eps);

			if (leftHit == null && rightHit == null) {
				return null;
			} else if (leftHit == null) {
				actualHit = rightHit;
			} else if (rightHit == null) {
				actualHit = leftHit;
			} else {
				actualHit = (leftHit.getT() < rightHit.getT()) ? leftHit : rightHit;
			}
		}

		return actualHit;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public boolean isLeaf() {
		return leafData != null;
	}
}
