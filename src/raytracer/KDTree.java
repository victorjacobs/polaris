package raytracer;

import scene.geometry.Surface;

import java.util.Collections;
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
	 * @param currentAxis	Next axis over which to split the tree (0 == x, 1 == y, 2 == z)
	 */
	private KDTree(List<Surface> data, int currentAxis) {
		// Always generate a bounding box around the data
		boundingBox = generateBoundingBox(data);

		if (data.size() <= Settings.KDTREE_ELEMENTS_IN_LEAF) {
			left = null;
			right = null;

			leafData = data;
		} else {
			// Split up data set and build sub trees
			List<Surface> leftData = null;
			List<Surface> rightData = null;
			List<Surface> nextLeftData = new LinkedList<Surface>();
			List<Surface> nextRightData = new LinkedList<Surface>();

			// Calculate split (SAH)
			// Find possible split planes
			List<Float> planes = new LinkedList<Float>();
			BoundingBox curBoundingBox;
			for (Surface surf : data) {
				curBoundingBox = surf.boundingBox();
				planes.add(curBoundingBox.getMin().get(currentAxis));
				planes.add(curBoundingBox.getMax().get(currentAxis));
			}

			Collections.sort(planes);


			// Iterate over the current axis, calculate heuristic (this should decrease monotonically)
			//  ==> If starts to increase, stop iteration and use this as split

			float leftArea, rightArea;
			float nextSA;
			float prevPlane = Float.MAX_VALUE;		// To prevent doubles
			float minSA = Float.MAX_VALUE;

			for (float split : planes) {
				if (prevPlane == split) continue;

				// Reset for next step
				nextLeftData.clear();
				nextRightData.clear();
				leftArea = 0;
				rightArea = 0;

				// Calculate heuristic (store in nextSA)
				for (Surface surf : data) {
					if (surf.boundingBox().getMin().get(currentAxis) <= split) {
						nextLeftData.add(surf);
						leftArea += surf.getProjectedSurfaceArea();
					}

					if (surf.boundingBox().getMax().get(currentAxis) >= split) {
						nextRightData.add(surf);
						rightArea += surf.getProjectedSurfaceArea();
					}
				}

				nextSA = leftArea * (nextLeftData.size() - nextRightData.size()) + (leftArea + rightArea) * nextRightData.size();

				if (nextSA < minSA) {
					minSA = nextSA;
					leftData = new LinkedList<Surface>(nextLeftData);
					rightData = new LinkedList<Surface>(nextRightData);
				}

				prevPlane = split;
			}

			System.out.println(minSA);

			System.out.println("Left num nodes: " + leftData.size() + " right: " + rightData.size());

			left = new KDTree(leftData, (currentAxis + 1) % 3);
			right = new KDTree(rightData, (currentAxis + 1) % 3);
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

		// Early return if the ray doesn't hit with this bounding box
		if (boundingBox.hit(ray) == null) return null;

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
