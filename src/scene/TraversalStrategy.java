package scene;

import raytracer.Hit;
import raytracer.Ray;
import scene.geometry.Surface;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 25/01/13
 * Time: 14:27
 */
public interface TraversalStrategy {

	Hit trace(Ray ray, float eps);
	void prepare(List<Surface> primitiveBag);
	TraversalStrategy clean();

}
