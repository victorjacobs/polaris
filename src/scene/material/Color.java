package scene.material;

/**
 * Differs from java.awt.Color in that all values are between 0 and 1 float.
 * 
 * @author Victor
 *
 */
public class Color {
	
	private float r;
	private float g;
	private float b;
	
	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public float getRed() {
		return r;
	}

	public float getGreen() {
		return g;
	}

	public float getBlue() {
		return b;
	}
	
}
