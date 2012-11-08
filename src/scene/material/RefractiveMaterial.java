package scene.material;

import java.util.HashSet;

import raytracer.Hit;
import raytracer.Ray;
import raytracer.RayTracer;
import scene.geometry.Vector3f;
import scene.lighting.Light;

public class RefractiveMaterial extends PhongMaterial {
	private float refractionCoefficient;
	private float n = 1;		// n where ray is travelling in
	
	public RefractiveMaterial(Color3f baseColor, float refractionCoefficient) {
		super(baseColor, 100);
		this.refractionCoefficient = refractionCoefficient;
	}
	
	@Override
	public Color3f getColor(HashSet<Light> lights, Hit hit, RayTracer tracer) {
		Color3f phong = super.getColor(lights, hit, tracer);

		Vector3f reflect = hit.getRay().getDirection().reflectOver(hit.getNormal());
		float c, kr, kg, kb;
		Vector3f innerRay = getRefractedRay(hit.getRay().getDirection(), hit.getNormal(), refractionCoefficient);

		if (hit.getRay().getDirection().dotProduct(hit.getNormal()) < 0) {
			c = - hit.getRay().getDirection().dotProduct(hit.getNormal());
			kr = kg = kb = 1;
		} else {
			
			kr = kg = kb = 1;		// TODO make distance dependant
			
			if (internalReflection(hit.getRay().getDirection(), hit.getNormal().negate(), 1 / refractionCoefficient)) {
				c = innerRay.dotProduct(hit.getNormal());
			} else {
				Hit hita = tracer.trace(new Ray(hit.getPoint(), reflect), 0.01f);
				if (hita != null) {
					Color3f otherColor = hita.getSurface().getMaterial().getColor(lights, hita, tracer);
					return new Color3f(kr * otherColor.getRed(), kg * otherColor.getGreen(), kb * otherColor.getBlue());
				} else {
					return new Color3f(0, 0, 0);
				}
			}
		}
		
		float R0 = ((refractionCoefficient - 1) * (refractionCoefficient - 1)) / ((refractionCoefficient + 1) * (refractionCoefficient + 1));
		float R = R0 + (1 - R0) * (float)Math.pow((1 - c), 5);
		
		Hit hitReflected = tracer.trace(new Ray(hit.getPoint(), reflect), 0.01f);
		Color3f colorReflected, colorRefracted;
		
		if (hitReflected != null) {
			colorReflected = hitReflected.getSurface().getMaterial().getColor(lights, hitReflected, tracer);
		} else {
			colorReflected = new Color3f(0, 0, 0);
		}
		
		Hit hitRefracted = tracer.trace(new Ray(hit.getPoint(), innerRay), 0.01f);
		
		if (hitRefracted != null) {
			colorRefracted = hitRefracted.getSurface().getMaterial().getColor(lights, hitRefracted, tracer);
		} else {
			colorRefracted = new Color3f(0, 0, 0);
		}
		
		R = 0.05f;
		
		return new Color3f(phong.getRed() + kr * (R * colorReflected.getRed() + (1 - R) * colorRefracted.getRed()),
				phong.getGreen() + kg * (R * colorReflected.getGreen() + (1 - R) * colorRefracted.getGreen()),
				phong.getBlue() + kb * (R * colorReflected.getBlue() + (1 - R) * colorRefracted.getBlue()));
	}
	
	private boolean internalReflection(Vector3f direction, Vector3f normal, float refractionIndex) {
		float dotProduct = direction.dotProduct(normal);
		return Math.sqrt(1 - ((n * n * (1 - dotProduct * dotProduct)) / (refractionIndex * refractionIndex))) < 0;
	}
	
	private Vector3f getRefractedRay(Vector3f direction, Vector3f normal, float refractionIndex) {
		Vector3f t1 = direction.minus(normal.multiply(direction.dotProduct(normal))).multiply(n / refractionIndex);
		
		float dotProduct = direction.dotProduct(normal);
		float preFactor = (float) Math.sqrt(1 - ((n * n * (1 - dotProduct * dotProduct)) / (refractionIndex * refractionIndex)));
		Vector3f t2 = normal.multiply(preFactor);
		
		return t1.minus(t2);
	}
	
//	private Vector3f getRefractedRay(Vector3f direction, Vector3f normal, float refractionIndex) {
//		float cosT = - normal.normalize().dotProduct(direction.normalize());
//		float cosT2 = 1 - refractionIndex * refractionIndex * (1 - 2 * cosT);
//		
//		Vector3f refractedRay = direction.multiply(refractionIndex).sum(normal.multiply(refractionIndex * cosT - (float)Math.sqrt(cosT2)));
//		return refractedRay;
//	}
}
