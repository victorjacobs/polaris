package scene.material;

import raytracer.Hit;
import scene.Scene;
import scene.data.Point2f;

/**
 * Class representing a material, the color that is contained herein is the so called "ambient color".
 * Normally the colors of all the effects are the same as this one.
 */
public abstract class Material {
	
	private Color3f baseColor;
	private Texture texture;

	public Material(Texture texture) {
		this.texture = texture;
	}

	public Material(Color3f baseColor) {
		this.baseColor = baseColor;
	}

	public final Color3f getUnshadedColorAt(Point2f point) {
		if (texture == null) {
			return baseColor;
		} else {
			return texture.getColor(point);
		}
	}

	public Color3f getColor(Scene scene, Hit hit) {
		return getColor(scene, hit, 1);
	}

	// Implements ambient light
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		if (scene.getBackground() != null) {
			return new Color3f(getUnshadedColorAt(hit.getTextureCoordinates()).getRed() * scene.getBackground().getRed(),
					getUnshadedColorAt(hit.getTextureCoordinates()).getGreen() * scene.getBackground().getGreen(),
					getUnshadedColorAt(hit.getTextureCoordinates()).getBlue() * scene.getBackground().getBlue());
		} else {
			return new Color3f(0, 0, 0);
		}
	}

	/**
	 * This method defines how other materials should calculate shadows if a shadow ray went through this material
	 * @return Percentage of decrease of light intensity through the object
	 */
	public abstract float getShadowPercentage();
}
