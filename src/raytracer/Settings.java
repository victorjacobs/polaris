package raytracer;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 13/11/12
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
public class Settings {
	// Renderer
	public static int SCREEN_X = 640;
	public static int SCREEN_Y = 640;
	public static int MAX_RECURSION_DEPTH = 20;
	public static float EPS = 0.0001f;
	public static int NUMBER_OF_RENDER_PASSES = 1;

	// Effects
	public static boolean DISABLE_SHADOWS = false;
	public static int SOFT_SHADOW_SAMPLES = 1;
	public static int AA = 1;

	// Acceleration structures
	public static int GRID_DENSITY = 7;
	public static int KDTREE_ELEMENTS_IN_LEAF = 20;

	// Some debug settings
	public static boolean SHOULD_REPAINT_AFTER_EVERY_PIXEL = false;
	public static boolean FIX_SINGLE_THREAD = false;
	public static boolean RANDOM_COLOR_TRIANGLES = false;
	public static boolean COLLECT_STATS = false;
	public static boolean INTERSECTION_TESTS_FALSE_COLOR = false;
	public static boolean ENABLE_CAMERA_MOVE_IN_UI = true;
	public static boolean PAINT_AFTER_ALL_THREADS_FINISH = false;
}
