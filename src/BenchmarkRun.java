import demo.RunALotOfSpheres;
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
	private static final int NUM_OF_STEPS = 5;
	private final long runID;

	public static void main(String[] args) {
		new BenchmarkRun();
	}

	public BenchmarkRun() {
		runID = System.currentTimeMillis() % 1000;

		Renderer renderer = new Renderer(new StubPanel(), 1);
		RunALotOfSpheres sceneGenerator = new RunALotOfSpheres();

		//benchmarkAll(renderer, sceneGenerator);
		benchmarkGrid(renderer, sceneGenerator);
		//benchmarkKD(renderer, sceneGenerator);
	}

	private void benchmarkKD(Renderer renderer, RunALotOfSpheres sceneGenerator) {
		// Experiment with different settings for parameters for grid

		StringBuilder sb;
		System.err.println("------------------------");
		System.err.println("Starting extended BVH benchmark");
		System.err.println("------------------------");

		Scene scene = new BoundingBoxAcceleratedScene(new BasicScene());

		for (int i = 20; i < 200; i+=50) {
			System.err.println("KD leaf contents: " + i);
			sb = new StringBuilder();
			sb.append("nbSpheres | nbIntersections" + i + " | mem" + i + " | buildtime" + i + '\n');
			sceneGenerator.setNbOfSpheres(1);

			Settings.KDTREE_ELEMENTS_IN_LEAF = i;

			for (int j = 0; j < NUM_OF_STEPS; j++) {
				Stats.resetTotalNumIntersections();
				System.err.println("Iteration " + j + " " + sceneGenerator.getNbOfSpheres() + " spheres");

				scene.clear();
				sceneGenerator.generateBareScene(scene);
				renderer.loadScene(scene);

				renderer.render();

				renderer.join();

				sceneGenerator.setNbOfSpheres(sceneGenerator.getNbOfSpheres() * 10);
				sb.append(sceneGenerator.getNbOfSpheres() + " | " + Stats.getTotalNumIntersections() + " | " + Stats.getMemoryUsage() + " | " + Stats.getStructureBuildTime() + '\n');
			}

			System.out.println("Writing stats to file");

			try {
				String filePath = "./benchmarks/" + runID + "-kdtree" + i + ".txt";
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

	private void benchmarkGrid(Renderer renderer, RunALotOfSpheres sceneGenerator) {
		// Experiment with different settings for parameters for grid

		StringBuilder sb;
		System.err.println("------------------------");
		System.err.println("Starting grid extended benchmark");
		System.err.println("------------------------");

		Scene scene = new GridAcceleratedScene(new BasicScene());

		for (int i = 2; i < 15; i+=3) {
			System.err.println("Grid density: " + i);
			sb = new StringBuilder();
			sb.append("nbSpheres | nbIntersections" + i + " | mem" + i + " | buildtime" + i + '\n');
			sceneGenerator.setNbOfSpheres(1);

			Settings.GRID_DENSITY = i;

			for (int j = 0; j < NUM_OF_STEPS; j++) {
				Stats.resetTotalNumIntersections();
				System.err.println("Iteration " + j + " " + sceneGenerator.getNbOfSpheres() + " spheres");

				scene.clear();
				sceneGenerator.generateBareScene(scene);
				renderer.loadScene(scene);

				renderer.render();

				renderer.join();

				sceneGenerator.setNbOfSpheres(sceneGenerator.getNbOfSpheres() * 10);
				sb.append(sceneGenerator.getNbOfSpheres() + " | " + Stats.getTotalNumIntersections() + " | " + Stats.getMemoryUsage() + " | " + Stats.getStructureBuildTime() + '\n');
			}

			System.out.println("Writing stats to file");

			try {
				String filePath = "./benchmarks/" + runID + "-grid" + i + ".txt";
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

	private void benchmarkAll(Renderer renderer, RunALotOfSpheres sceneGenerator) {
		StringBuilder sb;
		HashMap<String, Scene> scenes = new HashMap<String, Scene>();
		scenes.put("basic", new BasicScene());
		scenes.put("grid", new GridAcceleratedScene(new BasicScene()));
		scenes.put("KD", new BoundingBoxAcceleratedScene(new BasicScene()));

		// Compare all different acceleration structures with one another

		for (String sceneType : scenes.keySet()) {
			sb = new StringBuilder();
			sb.append("nbSpheres | nbIntersections" + sceneType + " | mem" + sceneType + " | buildtime" + sceneType + '\n');
			sceneGenerator.setNbOfSpheres(1);

			System.err.println("------------------------");
			System.err.println("Starting " + sceneType + " benchmark");
			System.err.println("------------------------");

			Scene scene = scenes.get(sceneType);
			for (int i = 0; i < NUM_OF_STEPS; i++) {
				Stats.resetTotalNumIntersections();
				System.err.println("Iteration " + i + " " + sceneGenerator.getNbOfSpheres() + " spheres");

				scene.clear();
				sceneGenerator.generateBareScene(scene);
				renderer.loadScene(scene);

				renderer.render();

				renderer.join();

				sceneGenerator.setNbOfSpheres(sceneGenerator.getNbOfSpheres() * 10);
				sb.append(sceneGenerator.getNbOfSpheres() + " | " + Stats.getTotalNumIntersections() + " | " + Stats.getMemoryUsage() + " | " + Stats.getStructureBuildTime() + '\n');
			}

			System.out.println("Writing stats to file");

			try {
				String filePath = "./benchmarks/" + runID + "-" + sceneType + ".txt";
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
