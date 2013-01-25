package scene;

import raytracer.BoundingVolumeHierarchy;
import raytracer.Grid;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 25/01/13
 * Time: 14:41
 */
public class SceneConstructor {

	public static Scene getGridScene() {
		Scene scene = new Scene();
		scene.setTraversalStrategy(new Grid());

		return scene;
	}

	public static Scene getBVHScene() {
		Scene scene = new Scene();
		scene.setTraversalStrategy(new BoundingVolumeHierarchy());

		return scene;
	}

}
