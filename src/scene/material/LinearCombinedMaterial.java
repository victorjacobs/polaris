package scene.material;

import raytracer.Hit;
import raytracer.RayTracer;
import scene.lighting.Light;

import java.util.HashSet;

public class LinearCombinedMaterial extends Material {

	Material mat1;
	Material mat2;
	
	public LinearCombinedMaterial(Color3f baseColor, Material mat1, Material mat2) {
		super(baseColor);
		
		this.mat1 = mat1;
		this.mat2 = mat2;
	}

	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer, int recursionDepth) {
		// TODO Auto-generated method stub
		return null;
	}

}
