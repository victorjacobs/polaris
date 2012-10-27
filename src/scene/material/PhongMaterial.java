package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import scene.geometry.Vector3f;
import scene.lighting.Light;

public class PhongMaterial extends DiffuseMaterial {

	private Color phongColor = new Color(1, 1, 1);
	private float phongExponent = 1000f;
	
	public PhongMaterial(Color baseColor) {
		super(baseColor);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Color getColor(HashSet<Light> lights, Hit hit) {
		Color diffuseShading = super.getColor(lights, hit);
		Vector3f halfVector;
		float dotProduct;
		
		float sumR = 0;
		float sumG = 0;
		float sumB = 0;
		
		for (Light light : lights) {
			halfVector = hit.getRay().getDirection().sum(light.rayTo(hit.getPoint())).normalize();
			
			dotProduct = (float) Math.pow(Math.max(0, hit.getNormal().dotProduct(halfVector)), phongExponent);
			
			sumR += phongColor.getRed() * light.intensity() * light.color().getRed() * dotProduct;
			sumG += phongColor.getGreen() * light.intensity() * light.color().getGreen() * dotProduct;
			sumB += phongColor.getBlue() * light.intensity() * light.color().getBlue() * dotProduct;
		}
		
		//return new Color(diffuseShading.getRed() + sumR, diffuseShading.getGreen() + sumG, diffuseShading.getBlue() + sumB);
		return new Color(sumR, sumG, sumB);
	}

}
