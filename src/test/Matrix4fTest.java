package test;

import org.junit.Test;
import scene.data.Matrix4f;
import scene.data.Vector4f;

import static org.junit.Assert.assertEquals;

public class Matrix4fTest {
	
	Matrix4f m1 = new Matrix4f(4, 0, 4, 2, 7, 0, 1, 5, 9, 4, 9, 2, 4, 8, 0, 8);
	Matrix4f m2 = new Matrix4f(4, 6, 5, 3, 5, 3, 6, 2, 2, 4, 6, 9, 0, 6, 0, 0);
	
	@Test
	public void testMatrixMult() {
		Matrix4f m3 = new Matrix4f(24, 52, 44, 48, 30, 76, 41, 30, 74, 114, 123, 116, 56, 96, 68, 28);
		assertEquals(m3, m1.multiply(m2));
	}
	
	@Test
	public void testVectorMult() {
		Vector4f v = new Vector4f(2, 1, 5, 0);
		Vector4f res = new Vector4f(28, 19, 67, 16);
		
		assertEquals(res, m1.multiply(v));
	}

}
