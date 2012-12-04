package raytracer;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 13/11/12
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
public class Settings {
	public static final int SCREEN_X = 1280;
	public static final int SCREEN_Y = 900;
	public static final int MAX_RECURSION_DEPTH = 10;
	public static final float EPS = 0.01f;
	public static final int SOFT_SHADOW_SAMPLES = 300;
	public static final int AA = 1;

	public static final int GRID_DENSITY = 7;
	public static final int KDTREE_ELEMENTS_IN_LEAF = 20;

	// Some debug settings
	public static final boolean SHOULD_REPAINT_AFTER_EVERY_PIXEL = true;
	public static final boolean FIX_SINGLE_THREAD = false;
	public static final boolean RANDOM_COLOR_TRIANGLES = false;
	public static final boolean COLLECT_STATS = false;
	public static final boolean GRID_ENABLE_MAILBOXING = true;
}
