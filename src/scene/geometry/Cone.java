package scene.geometry;

import raytracer.BoundingBox;
import raytracer.Hit;
import raytracer.Ray;
import scene.data.Matrix4f;
import scene.material.Material;

/**
 * Created with IntelliJ IDEA. User: victor Date: 18/11/12 Time: 23:32 To change this template use File | Settings |
 * File Templates.
 */
public class Cone extends Surface {
	@Override
	public Hit hit(Ray ray, float t0, float t1) {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public BoundingBox boundingBox() {
		//To change body of implemented methods use File | Settings | File Templates.
		return null;
	}

	@Override
	public Material getMaterial() {
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setMaterial(Material mat) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void applyTransformation(Matrix4f transformation) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public float getProjectedSurfaceArea() {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}
}
