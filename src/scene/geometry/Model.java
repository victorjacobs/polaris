package scene.geometry;

import raytracer.BoundingBox;
import raytracer.Hit;
import raytracer.Ray;
import scene.material.Material;
import scene.data.Matrix4f;
import scene.data.Vector3f;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that represents a model made up out of a number of triangles
 * 
 * @author victor
 *
 */
public class Model extends Surface {
	private ArrayList<Vector3f> points;
	private ArrayList<Vector3f> normalVectors;
	private ArrayList<Triangle> triangles;
	private Material material;
	
	private enum DataTags {
		V, VT, VN, F
	}

	public Model(String fileName) {
		this(fileName, null);
	}

	public Model(String fileName, Material mat) {
		System.out.println("Loading model from file " + fileName);
		points = new ArrayList<Vector3f>();
		normalVectors = new ArrayList<Vector3f>();
		triangles = new ArrayList<Triangle>();
		
		parseFile(fileName);
		this.material = mat;
	}
	
	private void parseFile(String fileName) {
		BufferedReader reader;
		String line;
		
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
	// NOTE: when creating triangles, set material to null, keep the material stored here
	private void parseLine(String line) {
		line = line.replaceAll("  ", " ");
		
		String[] lineTokenized = line.split(" ");
		
		if (line.trim().equals("")) return;
		
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
				// TODO CLEAN THIS CODE DUPLICATION
				String[] indices = lineTokenized[1].split("/");
				Triangle triag;
				
				if (lineTokenized.length == 4) {
					if (indices.length == 1) {
						triag = new Triangle(points.get(Integer.parseInt(lineTokenized[1]) - 1), points.get(Integer.parseInt(lineTokenized[2]) - 1), points.get(Integer.parseInt(lineTokenized[3]) - 1), material);
					} else {
						Vertex v1 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
						
						indices = lineTokenized[2].split("/");
						Vertex v2 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
						
						indices = lineTokenized[3].split("/");
						Vertex v3 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
						
						// Triangle
						triag = new Triangle(v1, v2, v3, material);
					}
					
					
					triangles.add(triag);
				} else if (lineTokenized.length == 5) {
					// Squares
					if (indices.length == 1) {
						triag = new Triangle(points.get(Integer.parseInt(lineTokenized[1]) - 1), points.get(Integer.parseInt(lineTokenized[2]) - 1), points.get(Integer.parseInt(lineTokenized[3]) - 1), material);
						triangles.add(triag);
						
						triag = new Triangle(points.get(Integer.parseInt(lineTokenized[2]) - 1), points.get(Integer.parseInt(lineTokenized[3]) - 1), points.get(Integer.parseInt(lineTokenized[4]) - 1), material);
						triangles.add(triag);
					} else {
						Vertex v1 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
						
						indices = lineTokenized[2].split("/");
						Vertex v2 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
						
						indices = lineTokenized[3].split("/");
						Vertex v3 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
						
						indices = lineTokenized[4].split("/");
						Vertex v4 = new Vertex(points.get(Integer.parseInt(indices[0]) - 1), normalVectors.get(Integer.parseInt(indices[2]) - 1));
						
						// Triangles
						triag = new Triangle(v1, v2, v3, material);
						triangles.add(triag);
						triag = new Triangle(v2, v3, v4, material);
						triangles.add(triag);
						triag = new Triangle(v1, v2, v4, material);
						triangles.add(triag);
						triag = new Triangle(v1, v4, v3, material);
						triangles.add(triag);
					}
				}
				
				break;
			}
		} catch (Exception e) {
			// Just ignore the message
			//e.printStackTrace();
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
		
		// Don't just return the hit gotten from one of the triangles, change the surface in Hit to the model
		// NIet heel mooi maar bon
		
		if (hit == null) {
			return null;
		} else {
			return new Hit(ray, this, hit.getPoint(), hit.getNormal(), hit.getT());
		}
	}

	@Override
	public BoundingBox boundingBox() {
		BoundingBox bb = null;

		for (Surface triag : triangles) {
			if (bb == null)
				bb = new BoundingBox(triag.boundingBox());

			bb = bb.add(triag.boundingBox());
		}

		return bb;
	}

	@Override
	public Material getMaterial() {
		return this.material;
	}
	
	@Override
	public void setMaterial(Material mat) {
		this.material = mat;
	}

	@Override
	public void applyTransformation(Matrix4f transformation) {
		for (Triangle triag : triangles) {
			triag.applyTransformation(transformation);
		}
	}

	@Override
	public List<Surface> getPrimitiveSurfaces() {
		return new LinkedList<Surface>(triangles);
	}
}
