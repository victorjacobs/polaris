package raytracer;
import scene.Scene;
import scene.geometry.Surface;

public class RayTracer {
	public static final int SCREEN_X = 640;
	public static final int SCREEN_Y = 480;
	
	private Scene scene;
	
	public RayTracer(Scene scene) {
		this.scene = scene;
	}
	
	public Hit trace(Ray ray) {
		float lowestT = Float.POSITIVE_INFINITY;
		Hit hit, closestHit = null;
		
		for (Surface surf : scene.getSurfaces()) {
			hit = surf.hit(ray, 0, lowestT);
			
			if (hit != null) {
				lowestT = hit.getT();
				closestHit = hit;
			}
		}
		
		return closestHit;
	}
	
	public void trace(int depth) {
		long start = System.currentTimeMillis();
		
		traceRecursiveStep(depth);
		
		long stop = System.currentTimeMillis();
		
		System.out.println("Render completed in " + Math.round((stop - start) / 1000) + "s");
	}
	
	public void traceRecursiveStep(int depth) {

	}
	
	
}
