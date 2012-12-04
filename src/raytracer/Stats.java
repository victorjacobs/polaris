package raytracer;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 28/11/12
 * Time: 17:34
 */
public class Stats {
	private static int NUM_INTERSECTION_TESTS = 0;

	public static void incIntersections() {
		NUM_INTERSECTION_TESTS++;
	}

	public static void resetIntersections() {
		NUM_INTERSECTION_TESTS = 0;
	}

	public static int getNumIntersections() {
		return NUM_INTERSECTION_TESTS;
	}
}
