package scene.material;

import raytracer.Hit;
import scene.Scene;
import scene.lighting.AmbientLight;
import scene.lighting.Light;

/**
 * Class representing a material, the color that is contained herein is the so called "ambient color".
 * Normally the colors of all the effects are the same as this one.
 */
public abstract class Material {
	
	protected Color3f baseColor;
	
	public Material(Color3f baseColor) {
		this.baseColor = baseColor;
	}

	public Color3f getColor(Scene scene, Hit hit) {
		return getColor(scene, hit, 1);
	}

	// Implements ambient light
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		for (Light light : scene.getLightSources()) {
			if (light instanceof AmbientLight) {
				return new Color3f(baseColor.getRed() * light.intensity() * light.color().getRed(),
									baseColor.getGreen() * light.intensity() * light.color().getGreen(),
									baseColor.getBlue() * light.intensity() * light.color().getBlue());
			}
		}
		
		return new Color3f(0, 0, 0);
	}
}
