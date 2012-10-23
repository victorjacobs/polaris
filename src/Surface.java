import java.awt.Color;

public interface Surface {
	
	public boolean hit(Ray ray, float t0, float t1);
	public void boundingBox();
	public Color getColor();
	
}
