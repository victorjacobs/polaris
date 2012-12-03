package test;

import org.junit.Test;
import scene.data.Vector3f;
import scene.geometry.Triangle;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 03/12/12
 * Time: 14:52
 */
public class SurfaceAreaTest {

	@Test
	public void testTriangleSurface() {
		Vector3f v1 = new Vector3f(0, 0, 0);
		Vector3f v2 = new Vector3f(1, 0, 0);
		Vector3f v3 = new Vector3f(0, 0, 2);

		Triangle triag = new Triangle(v1, v2, v3);

		assertEquals(0.5f, triag.getProjectedSurfaceArea());
	}

}
