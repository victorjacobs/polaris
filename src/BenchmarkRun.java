import demo.RunALotOfSpheres;
import gui.Panel.StubPanel;
import gui.Renderer;
import raytracer.Stats;
import scene.BasicScene;
import scene.BoundingBoxAcceleratedScene;
import scene.GridAcceleratedScene;
import scene.Scene;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: victor
 * Date: 06/12/12
 * Time: 13:53
 */
public class BenchmarkRun {

	public static void main(String[] args) {
		long fileID = System.currentTimeMillis();

		StringBuilder sb;

		Renderer renderer = new Renderer(new StubPanel(), 1);

		HashMap<String, Scene> scenes = new HashMap<String, Scene>();
		scenes.put("basic", new BasicScene());
		scenes.put("grid", new GridAcceleratedScene(new BasicScene()));
		scenes.put("KD", new BoundingBoxAcceleratedScene(new BasicScene()));

		RunALotOfSpheres sceneGenerator = new RunALotOfSpheres();

		//

		for (String sceneType : scenes.keySet()) {
			sb = new StringBuilder();
			sceneGenerator.setNbOfSpheres(1);

			System.err.println("------------------------");
			System.err.println("Starting " + sceneType + " benchmark");
			System.err.println("------------------------");

			Scene scene = scenes.get(sceneType);
			for (int i = 0; i < 5; i++) {
				Stats.resetTotalNumIntersections();
				System.err.println("Iteration " + i + " " + sceneGenerator.getNbOfSpheres() + " spheres");

				scene.clear();
				sceneGenerator.generateScene(scene);
				renderer.loadScene(scene);

				renderer.render();

				renderer.join();

				sceneGenerator.setNbOfSpheres(sceneGenerator.getNbOfSpheres() * 10);
				sb.append(i + " | " + Stats.getTotalNumIntersections() + " | " + Stats.getMemoryUsage() + " | " + Stats.getStructureBuildTime() + '\n');
			}

			System.out.println("Writing stats to file");

			try {
				String filePath = "./benchmarks/" + fileID % 1000 + "-" + sceneType + ".txt";
				File outFile = new File(filePath);
				BufferedWriter out = new BufferedWriter(new FileWriter(outFile));

				out.write(sb.toString());
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
