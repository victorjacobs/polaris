package scene.material;

import raytracer.Hit;
import scene.Scene;
import scene.data.Vector3f;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 25/12/12
 * Time: 21:21
 */
public class BumpMap extends Material {
	private Texture bumpMap;
	private Material next;

	public BumpMap(Texture bumpMap, Material next) {
		this.bumpMap = bumpMap;
		this.next = next;
	}

	@Override
	public Color3f getColor(Scene scene, Hit hit, int recursionDepth) {
		Random rand = new Random();

		// Alter normal in hit with use of bump map
		Vector3f oldNormal = hit.getNormal();

		Vector3f delta = new Vector3f(rand.nextFloat() - 0.5f, rand.nextFloat() - 0.5f, rand.nextFloat() - 0.5f);
		delta = delta.divideBy(5);

		Vector3f newNormal = oldNormal.sum(delta);
		newNormal = newNormal.normalize();

		Hit newHit = new Hit(hit.getRay(), hit.getSurface(), hit.getPoint(), newNormal, hit.getTextureCoordinates(), hit.getT());

		return next.getColor(scene, newHit, recursionDepth);    //To change body of overridden methods use File | Settings | File Templates.
	}

	@Override
	public float getShadowPercentage() {
		return next.getShadowPercentage();
	}
}
