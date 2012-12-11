import demo.ALotOfSpheres;
import demo.SceneGenerator;
import gui.Panel.StubPanel;
import gui.Renderer;
import raytracer.Settings;
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
	private static final int NUM_OF_STEPS = 4;
	private static final int STEP_SIZE = 10;

	private final long runID;

	private SceneGenerator sg;

	public static void main(String[] args) {
		new BenchmarkRun();
	}

	public BenchmarkRun() {
		runID = System.currentTimeMillis() % 1000;

		Renderer renderer = new Renderer(new StubPanel(), 1);
		sg =  new ALotOfSpheres();

		benchmarkAll(renderer);
		benchmarkGrid(renderer);
		benchmarkKD(renderer);
	}

	private void benchmarkAll(Renderer renderer) {
		StringBuilder sb;
		HashMap<String, Scene> scenes = new HashMap<String, Scene>();
		scenes.put("basic", new BasicScene());
		scenes.put("grid", new GridAcceleratedScene(new BasicScene()));
		scenes.put("KD", new BoundingBoxAcceleratedScene(new BasicScene()));

		// Compare all different acceleration structures with one another

		for (String sceneType : scenes.keySet()) {
			sb = new StringBuilder();
			sb.append("nbSpheres | nbIntersections" + sceneType + " | mem" + sceneType + " | buildtime" + sceneType + " | rendertime" + sceneType + '\n');

			System.err.println("------------------------");
			System.err.println("Starting " + sceneType + " benchmark");
			System.err.println("------------------------");

			Scene scene = scenes.get(sceneType);
			iterate(renderer, sb, scene);

			System.out.println("Writing stats to file");

			toFile(sb.toString(), sceneType);
		}
	}

	private void benchmarkKD(Renderer renderer) {
		// Experiment with different settings for parameters for grid

		StringBuilder sb;
		System.err.println("------------------------");
		System.err.println("Starting extended BVH benchmark");
		System.err.println("------------------------");

		Scene scene = new BoundingBoxAcceleratedScene(new BasicScene());

		for (int i = 20; i < 200; i+=50) {
			System.err.println("KD leaf contents: " + i);
			sb = new StringBuilder();
			sb.append("nbSpheres | nbIntersections" + i + " | mem" + i + " | buildtime" + i + " | rendertime" + i + '\n');

			Settings.KDTREE_ELEMENTS_IN_LEAF = i;

			iterate(renderer, sb, scene);

			toFile(sb.toString(), "kdtree" + i);
		}
	}

	private void benchmarkGrid(Renderer renderer) {
		// Experiment with different settings for parameters for grid

		StringBuilder sb;
		System.err.println("------------------------");
		System.err.println("Starting grid extended benchmark");
		System.err.println("------------------------");

		Scene scene = new GridAcceleratedScene(new BasicScene());

		for (int i = 2; i < 15; i+=3) {
			System.err.println("Grid density: " + i);
			sb = new StringBuilder();
			sb.append("nbSpheres | nbIntersections" + i + " | mem" + i + " | buildtime" + i + " | rendertime" + i + '\n');

			Settings.GRID_DENSITY = i;

			iterate(renderer, sb, scene);

			toFile(sb.toString(), "grid" + i);
		}
	}

	private void iterate(Renderer renderer, StringBuilder sb, Scene scene) {
		int nbSpheres = 1;

		for (int i = 0; i < NUM_OF_STEPS; i++) {
			Stats.resetTotalNumIntersections();

			System.err.println("\nIteration " + i + " " + nbSpheres + " spheres");

			scene.clear();
			sg.generateScene(scene, nbSpheres);
			renderer.loadScene(scene);

			long localStartTime = System.currentTimeMillis();

			renderer.render();

			renderer.join();

			sb.append(nbSpheres + " | " + Stats.getTotalNumIntersections() + " | " +
					Stats.getMemoryUsage() + " | " + Stats.getStructureBuildTime() +
					" | " + (System.currentTimeMillis() - localStartTime) + '\n');

			nbSpheres *= STEP_SIZE;
		}
	}

	private void toFile(String data, String ID) {
		System.out.println("Writing stats to file " + ID);

		try {
			String filePath = "./benchmarks/" + runID + "-" + ID + ".txt";
			File outFile = new File(filePath);
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));

			out.write(data);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
