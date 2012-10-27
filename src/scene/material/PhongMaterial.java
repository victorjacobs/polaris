package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import scene.geometry.Vector3f;
import scene.lighting.Light;

public class PhongMaterial extends DiffuseMaterial {

	private Color phongColor = new Color(1, 1, 1);
	private float phongExponent;
	
	public PhongMaterial(Color baseColor, float phongExponent) {
		super(baseColor);
		this.phongExponent = phongExponent;
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
			// Note: negate ray direction because we need viewing vector
			halfVector = hit.getRay().getDirection().negate().sum(light.rayTo(hit.getPoint())).normalize();
			
			dotProduct = (float) Math.pow(Math.max(0, hit.getNormal().dotProduct(halfVector)), phongExponent);
			
			sumR += phongColor.getRed() * light.intensity() * light.color().getRed() * dotProduct;
			sumG += phongColor.getGreen() * light.intensity() * light.color().getGreen() * dotProduct;
			sumB += phongColor.getBlue() * light.intensity() * light.color().getBlue() * dotProduct;
		}
		
		return new Color(diffuseShading.getRed() + sumR, diffuseShading.getGreen() + sumG, diffuseShading.getBlue() + sumB);
	}

}
