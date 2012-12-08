package raytracer;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 28/11/12
 * Time: 17:34
 */
public class Stats {
	private static long NUM_INTERSECTION_TESTS = 0;
	private static long TOTAL_INTERSECTION_TESTS = 0;
	private static int MEM_USAGE_DATA_STRUCTURE;
	private static long STRUCTURE_BUILD_TIME;

	public static void incIntersections() {
		NUM_INTERSECTION_TESTS++;
	}

	public static void resetIntersections() {
		TOTAL_INTERSECTION_TESTS += NUM_INTERSECTION_TESTS;
		NUM_INTERSECTION_TESTS = 0;
	}

	public static long getNumIntersections() {
		return NUM_INTERSECTION_TESTS;
	}

	public static long getTotalNumIntersections() {
		return TOTAL_INTERSECTION_TESTS;
	}

	public static void resetTotalNumIntersections() {
		TOTAL_INTERSECTION_TESTS = 0;
	}

	public static void setMemoryUsage(int usage) {
		MEM_USAGE_DATA_STRUCTURE = usage;
	}

	public static int getMemoryUsage() {
		return MEM_USAGE_DATA_STRUCTURE;
	}

	public static void setStructureBuildTime(long time) {
		STRUCTURE_BUILD_TIME = time;
	}

	public static long getStructureBuildTime() {
		return STRUCTURE_BUILD_TIME;
	}
}
