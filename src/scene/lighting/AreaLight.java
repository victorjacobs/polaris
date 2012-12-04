package scene.lighting;

import raytracer.BoundingBox;
import raytracer.Hit;
import raytracer.Ray;
import raytracer.Settings;
import scene.Scene;
import scene.data.Point3f;
import scene.data.Vector3f;
import scene.material.Color3f;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 19/11/12
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */
public class AreaLight extends PointLight {

	private BoundingBox volume;

	public AreaLight(Point3f position) {
		this(position, new Color3f(1, 1, 1), 0.7f, 1);
	}

	public AreaLight(Point3f position, Color3f color, float intensity, float size) {
		super(new Vector3f(position), intensity, color);

		volume = new BoundingBox(new Vector3f(position.x - 1f, position.y - 1f, position.z - 1f), new Vector3f(position.x + 1f, position.y + 1f, position.z + 1f));
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

			lightHit = scene.trace(new Ray(point, (new Vector3f(x, y, z).sum(volume.getMin())).minus(point)), Settings.EPS);

			if (lightHit != null && lightHit.getT() < 1) {
				numOfHits++;
				if (materialShadowPercentage == -1) {
					materialShadowPercentage = lightHit.getSurface().getMaterial().getShadowPercentage();
				}
			}

		}

		materialShadowPercentage = (materialShadowPercentage == -1) ? 0 : materialShadowPercentage;

		return (numOfHits / (float)Settings.SOFT_SHADOW_SAMPLES) * materialShadowPercentage;
	}
}
