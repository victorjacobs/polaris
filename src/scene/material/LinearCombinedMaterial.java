package scene.material;

import raytracer.Hit;
import scene.Scene;

public class LinearCombinedMaterial extends Material {

	Material mat1;
	Material mat2;
	
	public LinearCombinedMaterial(Color3f baseColor, Material mat1, Material mat2) {
		super(baseColor);
		
		this.mat1 = mat1;
		this.mat2 = mat2;
	}

	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
