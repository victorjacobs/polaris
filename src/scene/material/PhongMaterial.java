package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.RayTracer;
import scene.geometry.Vector3f;
import scene.lighting.AmbientLight;
import scene.lighting.Light;

public class PhongMaterial extends Material {
	// TODO add intensity
	private Color3f phongColor = new Color3f(1, 1, 1);
	private float phongExponent;
	
	public PhongMaterial(Color3f baseColor, float phongExponent) {
		super(baseColor);
		this.phongExponent = phongExponent;
	}
	
	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer, int recursionDepth) {
		Color3f ambientLight = super.getColor(lights, hit, tracer, recursionDepth);
		
		if (isInShade(lights, hit, tracer)) {
			return ambientLight;
		}
		
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
				
				sumR += phongColor.getRed() * light.color().getRed() * dotProduct;
				sumG += phongColor.getGreen() * light.color().getGreen() * dotProduct;
				sumB += phongColor.getBlue() * light.color().getBlue() * dotProduct;
			}
		}
		
		return new Color3f(ambientLight.getRed() + sumR, ambientLight.getGreen() + sumG, ambientLight.getBlue() + sumB);
	}

}
