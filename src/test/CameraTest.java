package test;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import scene.Camera;
import scene.geometry.Vector3f;


public class CameraTest {

	Camera cam;
	
	@Before
	public void setup() {
		cam = new Camera(new Vector3f(-10, 0, 0), new Vector3f(1, 0, 0), new Vector3f(0, 0, 1), 10);
	}
	
	@Test
	public void testW() {
		assertEquals(new Vector3f(-1, 0, 0), cam.getW());
	}
	
	@Test
	public void testU() {
		assertEquals(new Vector3f(0, -1, 0), cam.getU());
	}
	
	@Test
	public void testV() {
		assertEquals(new Vector3f(0, 0, 1), cam.getV());
	}

}
