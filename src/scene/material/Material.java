package scene.material;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.lighting.AmbientLight;
import scene.lighting.Light;

import java.util.HashSet;

/**
 * Class representing a material, the color that is contained herein is the so called "ambient color".
 * Normally the colors of all the effects are the same as this one.
 */
public abstract class Material {
	
	protected Color3f baseColor;
	
	public Material(Color3f baseColor) {
		this.baseColor = baseColor;
	}

	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer) {
		return getColor(lights, hit, tracer, 1);
	}

	// Implements ambient light
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer, int recursionDepth) {
			for (Light light : lights) {
			if (light instanceof AmbientLight) {
				return new Color3f(baseColor.getRed() * light.intensity() * light.color().getRed(),
									baseColor.getGreen() * light.intensity() * light.color().getGreen(),
									baseColor.getBlue() * light.intensity() * light.color().getBlue());
			}
		}
		
		return new Color3f(0, 0, 0);
	}
	
	// NOTE: raakvlakken zijn *normaal* geen probleem aangezien deze bedekt zijn door 1 vd vlakken
	public boolean isInShade(HashSet<Light> lights, Hit hit, RayTracer tracer) {
		for (Light light : lights) {
			if (!(light instanceof AmbientLight)) {
				if (tracer.traceAny(new Ray(hit.getPoint(), light.rayTo(hit.getPoint())), RayTracer.EPS) != null) {
					return true;
				}
			}
		}
		
		return false;
	}
}
