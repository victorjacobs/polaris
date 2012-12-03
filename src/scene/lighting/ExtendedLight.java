package scene.lighting;

import raytracer.BoundingBox;
import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import scene.Scene;
import scene.data.Vector3f;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 19/11/12
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedLight extends PointLight {

	private BoundingBox volume;

	// TODO color
	// TODO extended light heeft niet echt een positie nodig!!
	public ExtendedLight(Vector3f position) {
		this(position, new BoundingBox(new Vector3f(position.x - 1, position.y - 1, position.z - 1), new Vector3f(position.x + 1, position.y + 1, position.z + 1)), 0.7f);
	}

	public ExtendedLight(Vector3f position, BoundingBox volume, float intensity) {
		super(position, intensity);

		this.volume = volume;
	}

	@Override
	public float getShadowPercentage(Scene scene, Vector3f point) {
		Random rand = new Random();

		Hit lightHit;
		int numOfHits = 0;
		float materialShadowPercentage = -1, x, y, z;

		for (int i = 0; i < Settings.SOFT_SHADOW_SAMPLES; i++) {
			x = volume.getMax().minus(volume.getMin()).x * rand.nextFloat();
			y = volume.getMax().minus(volume.getMin()).y * rand.nextFloat();
			z = volume.getMax().minus(volume.getMin()).z * rand.nextFloat();

			lightHit = scene.trace(new Ray(point, (new Vector3f(x, y, z)).sum(volume.getMin())), Settings.EPS);

			if (lightHit != null) {
				numOfHits++;
				if (materialShadowPercentage == -1) {
					materialShadowPercentage = lightHit.getSurface().getMaterial().getShadowPercentage();
				}
			}

		}

		materialShadowPercentage = (materialShadowPercentage == -1) ? 0 : materialShadowPercentage;

		return (numOfHits / Settings.SOFT_SHADOW_SAMPLES) * materialShadowPercentage;
	}
}
