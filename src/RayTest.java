import static org.junit.Assert.*;

import org.junit.Test;


public class RayTest {

	@Test
	public void testOneRay() {
		// Fix configuration
		Vector3f origin = new Vector3f(0, 0, 0);
		Ray ray = new Ray(origin, 0, 0);
		
		Vector3f direction = ray.getDirection();
		
		assertEquals(-319.5f, direction.x, 0.01);
		assertEquals(-239.5f, direction.y, 0.01);
		assertEquals(-10f, direction.z, 0.01);
	}

}
