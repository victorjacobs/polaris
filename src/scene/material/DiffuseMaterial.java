package scene.material;

import raytracer.Hit;
import scene.Scene;
import scene.data.Point2f;
import scene.data.Point3f;
import scene.lighting.AmbientLight;
import scene.lighting.Light;

public class DiffuseMaterial extends Material {

	private Texture text;

	public DiffuseMaterial(Color3f baseColor) {
		super(baseColor);
	}

	// TODO: move this to Material
	public DiffuseMaterial(Texture text) {
		super(new Color3f(0, 0, 0));
		this.text = text;
	}
	
	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		Color3f ambientLight = super.getColor(scene, hit, recursionDepth);
		
		float sumR, sumG, sumB, dotProduct;

		sumR = ambientLight.getRed();
		sumG = ambientLight.getGreen();
		sumB = ambientLight.getBlue();

		for (Light light : scene.getLightSources()) {
			if (!(light instanceof AmbientLight) && !scene.isInShade(hit.getPoint(), light)) {
				dotProduct = Math.max(0, hit.getNormal().dotProduct(light.rayTo(hit.getPoint()).normalize()));
				sumR += (light.intensity() * light.color().getRed()) * dotProduct;
				sumG += (light.intensity() * light.color().getGreen()) * dotProduct;
				sumB += (light.intensity() * light.color().getBlue()) * dotProduct;
			}
		}
		
		if (text == null) {
			sumR *= baseColor.getRed();
			sumG *= baseColor.getGreen();
			sumB *= baseColor.getBlue();
		} else {
			Point2f coordinate = hit.getSurface().getLocalCoordinateFor(new Point3f(hit.getPoint()));

			sumR *= text.getColor(coordinate).getRed();
			sumG *= text.getColor(coordinate).getGreen();
			sumB *= text.getColor(coordinate).getBlue();
		}
		
		return new Color3f(ambientLight.getRed() + Math.min(1, sumR), ambientLight.getGreen() + Math.min(1, sumG), ambientLight.getRed() + Math.min(1, sumB));
	}
	
}
