package scene.geometry;

import raytracer.BoundingBox;
import raytracer.Hit;
import raytracer.Ray;
import scene.material.Material;
import scene.data.Matrix4f;

public interface Surface {
	
	public Hit hit(Ray ray, float t0, float t1);
	public BoundingBox boundingBox();
	public Material getMaterial();
	public void setMaterial(Material mat);
	public void applyTransformation(Matrix4f transformation);
	
	// TODO translate, rotate etc
	
}
