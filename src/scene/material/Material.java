package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.RayTracer;
import scene.lighting.Light;

public interface Material {
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer);
}
