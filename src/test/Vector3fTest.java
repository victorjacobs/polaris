package test;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import scene.geometry.Vector3f;




public class Vector3fTest {
	
	Vector3f one;
	Vector3f two;
	
	@Before
	public void setup() {
		one = new Vector3f(2, 3, 1);
		two = new Vector3f(5, 1, 2);
	}

	@Test
	public void testCrossProduct() {
		assertEquals(new Vector3f(5, 1, -13), one.crossProduct(two));
	}
	
	@Test
	public void testTrivialOperations() {
		assertEquals(new Vector3f(-3, 2, -1), one.minus(two));
		assertEquals(new Vector3f(-2, -3, -1), one.negate());
	}

}
