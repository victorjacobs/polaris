package scene.material;

import scene.material.Color;
import java.util.HashSet;

import raytracer.Hit;
import scene.lighting.Light;

public interface Material {
	public Color getColor(HashSet<Light> lights, Hit hit);
}
