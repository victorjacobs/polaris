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
	private BasicScene scene;
	private List<Surface> primitiveBag;
	private Grid grid;

	public GridAcceleratedScene(BasicScene scene) {
		super(scene);
		this.scene = scene;
		primitiveBag = new LinkedList<Surface>();
		System.out.println("Loaded grid accelerated renderereererer");
	}

	@Override
	public Hit trace(Ray ray, float eps) {
		// TODO grid acceleration magic here
		return scene.trace(ray, eps);
	}

	@Override
	public Hit traceAny(Ray ray, float eps) {
		return scene.traceAny(ray, eps);
	}

	private void dumpPrimitivesInBag() {
		for (Surface surf : scene.getSurfaces()) {
			primitiveBag.addAll(surf.getPrimitiveSurfaces());
		}
	}

	private void setUpGrid() {
		grid = new Grid(primitiveBag);
	}
}
