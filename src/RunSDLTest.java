import scene.parser.SceneBuilder;

import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA. User: victor Date: 18/11/12 Time: 23:19 To change this template use File | Settings |
 * File Templates.
 */
public class RunSDLTest {
	public static void main(String[] args) {
		SceneBuilder builder = new SceneBuilder();
		try {
			builder.loadScene("data/scenes/example.sdl");
		} catch (FileNotFoundException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
	}
}
