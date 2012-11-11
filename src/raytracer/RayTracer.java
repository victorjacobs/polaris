package raytracer;
import scene.Scene;
import scene.geometry.Surface;

public class RayTracer {
	public static final int SCREEN_X = 640;
	public static final int SCREEN_Y = 480;
	public static final int MAX_RECURSION_DEPTH = 5;
	public static final float EPS = 0.001f;
	
	private Scene scene;
	
	public RayTracer(Scene scene) {
		this.scene = scene;
	}
	
	public Hit trace(Ray ray) {
		return trace(ray, 0);
	}
	
	public Hit trace(Ray ray, float eps) {
		float lowestT = Float.POSITIVE_INFINITY;
		Hit hit, closestHit = null;
		
		for (Surface surf : scene.getSurfaces()) {
			hit = surf.hit(ray, eps, lowestT);
			
			if (hit != null) {
				lowestT = hit.getT();
				closestHit = hit;
			}
		}
		
		return closestHit;
	}
	
	public Hit traceAny(Ray ray, float eps) {
		Hit hit;
		
		for (Surface surf : scene.getSurfaces()) {
			hit = surf.hit(ray, eps, Float.POSITIVE_INFINITY);
			
			if (hit != null) return hit;
		}
		
		return null;
	}
	
	
}
