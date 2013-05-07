package scene.material;

import raytracer.Hit;
import scene.Scene;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 27/01/13
 * Time: 21:10
 */
public class Perlin extends Material {

	private final PerlinNoiseGenerator perlinGenerator;

	public Perlin() {
		perlinGenerator = new PerlinNoiseGenerator();
	}

	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		double noiseVal = perlinGenerator.improvedNoise(hit.getPoint().x, hit.getPoint().y, hit.getPoint().z);
		return new Color3f((float) noiseVal, (float) noiseVal, (float) noiseVal);
	}

	@Override
	public float getShadowPercentage() {
		return 1;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
