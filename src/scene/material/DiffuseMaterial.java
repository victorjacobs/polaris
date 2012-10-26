package scene.material;

import java.awt.Color;

public class DiffuseMaterial implements Material {

	private Color baseColor;
	
	public DiffuseMaterial(Color baseColor) {
		this.baseColor = baseColor;
	}
	
	@Override
	public Color getColor() {
		return this.baseColor;
	}
	
}
