package scene.geometry;

import raytracer.BoundingBox;
import raytracer.Hit;
import raytracer.Ray;
import scene.material.Material;
import scene.data.Matrix4f;

import java.util.LinkedList;
import java.util.List;

public abstract class Surface {
	
	public abstract Hit hit(Ray ray, float t0, float t1);
	public abstract BoundingBox boundingBox();
	public abstract Material getMaterial();
	public abstract void setMaterial(Material mat);
	public abstract void applyTransformation(Matrix4f transformation);

	public List<Surface> getPrimitiveSurfaces() {
		LinkedList<Surface> out = new LinkedList<Surface>();
		out.add(this);
		return out;
	}
	
}
