package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.RayTracer;
import scene.geometry.Vector3f;
import scene.lighting.AmbientLight;
import scene.lighting.Light;

public class PhongMaterial extends DiffuseMaterial {
	// TODO add intensity
	private Color3f phongColor = new Color3f(1, 1, 1);
	private float phongExponent;
	
	public PhongMaterial(Color3f baseColor, float phongExponent) {
		super(baseColor);
		this.phongExponent = phongExponent;
	}
	
	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer) {
		Color3f diffuseShading = super.getColor(lights, hit, tracer);
		
		if (isInShade()) return diffuseShading;		// In this case parent calculated that said pixel is in the shade of something else, don't do anything then
		
		Vector3f halfVector;
		float dotProduct;
		
		float sumR = 0;
		float sumG = 0;
		float sumB = 0;
		
		for (Light light : lights) {
			if (!(light instanceof AmbientLight)) {
				// Note: negate ray direction because we need viewing vector
				halfVector = hit.getRay().getDirection().negate().sum(light.rayTo(hit.getPoint())).normalize();
				
				dotProduct = (float) Math.pow(Math.max(0, hit.getNormal().dotProduct(halfVector)), phongExponent);
				
				sumR += phongColor.getRed() * light.intensity() * light.color().getRed() * dotProduct;
				sumG += phongColor.getGreen() * light.intensity() * light.color().getGreen() * dotProduct;
				sumB += phongColor.getBlue() * light.intensity() * light.color().getBlue() * dotProduct;
			}
		}
		
		return new Color3f(diffuseShading.getRed() + sumR, diffuseShading.getGreen() + sumG, diffuseShading.getBlue() + sumB);
	}

}
