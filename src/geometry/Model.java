package geometry;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import raytracer.Hit;
import raytracer.Ray;

/**
 * Class that represents a model made up out of a number of triangles
 * 
 * @author victor
 *
 */
public class Model implements Surface {
	private ArrayList<Vector3f> points;
	private ArrayList<Vector3f> normalVectors;
	private ArrayList<Triangle> triangles;
	
	private enum DataTags {
		V, VT, VN, F
	}
	
	// TODO eventueel parser uit halen en ergens anders zetten
	public Model(String fileName) {
		System.out.println("Loading model from file " + fileName);
		points = new ArrayList<Vector3f>();
		normalVectors = new ArrayList<Vector3f>();
		triangles = new ArrayList<Triangle>();
		
		parseFile(fileName);
	}
	
	private void parseFile(String fileName) {
		BufferedReader reader;
		String line = "";
		
		try {
			File data = new File(fileName);
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(data)));
			
			while ((line = reader.readLine()) != null) {
				parseLine(line);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Did not find file " + fileName);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Something went wrong reading out file " + fileName);
			e.printStackTrace();
		}
		
		System.out.println("File loaded");
		System.out.println("Some stats:");
		System.out.println(" " + points.size() + " points");
		System.out.println(" " + triangles.size() + " triangles");
	}
	
	// TODO: now we assume that all planes are triangles
	private void parseLine(String line) {
		String[] lineTokenized = line.split(" ");
		
		try {
			switch (DataTags.valueOf(lineTokenized[0].toUpperCase())) {
			case V:
				// Points
				Vector3f point = new Vector3f(Float.parseFloat(lineTokenized[1]), Float.parseFloat(lineTokenized[2]), Float.parseFloat(lineTokenized[3]));
				points.add(point);
				break;
				
			case VT:
				//System.out.println("Not yet implemented");
				break;
				
			case VN:
				// Normal vector
				Vector3f normal = new Vector3f(Float.parseFloat(lineTokenized[1]), Float.parseFloat(lineTokenized[2]), Float.parseFloat(lineTokenized[3]));
				normalVectors.add(normal);
				break;
				
			case F:
				// Generate vertices
				// TODO make more general?
				// TODO als geen textuur/normaal
				String[] indices = lineTokenized[1].split("/");
				Triangle triag;
				
				if (indices.length == 1) {
					triag = new Triangle(points.get(Integer.parseInt(indices[0]) - 1), points.get(Integer.parseInt(indices[0]) - 1), points.get(Integer.parseInt(indices[0]) - 1), Color.GREEN);
				} else {
					Vertex v1 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
					
					indices = lineTokenized[2].split("/");
					Vertex v2 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
					
					indices = lineTokenized[3].split("/");
					Vertex v3 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
					
					// Triangle
					// TODO: color
					triag = new Triangle(v1, v2, v3, Color.WHITE);
				}
				
				
				triangles.add(triag);
				
				break;
			}
		} catch (Exception e) {
			// Just ignore the message
			//System.err.println("Unsupported message");
		}
		
	}

	// NOTE: since this is a bag full of triangles, find all hits, then return closest
	@Override
	public Hit hit(Ray ray, float t0, float t1) {
		Hit hit = null;
		Hit curHit;
		float smallestT = t1;
		
		for (Triangle triag : triangles) {
			curHit = triag.hit(ray, t0, smallestT);
			if (curHit != null) {
				smallestT = curHit.getT();
				hit = curHit;
			}
		}
		
		return hit;
	}

	@Override
	public void boundingBox() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Color getColor() {
		return Color.WHITE;
	}
	
	public void move(float x, float y, float z) {
		
	}
}
