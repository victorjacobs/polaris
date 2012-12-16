package scene.geometry;

import raytracer.BoundingBox;
import raytracer.Hit;
import raytracer.Ray;
import scene.data.Matrix4f;
import scene.data.Point2f;
import scene.data.Vector3f;
import scene.material.Material;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Class that represents a model made up out of a number of triangles
 * 
 * @author victor
 *
 */
public class Model extends Surface {
	// These are only needed for loading of file
	private List<Vector3f> points;
	private List<Vector3f> normalVectors;
	private List<Point2f> textures;
	// Actual data of model is here
	private List<Triangle> triangles;
	private Material material;
	
	private enum DataTags {
		V, VT, VN, F
	}

	public Model(String fileName) {
		this(fileName, null);
	}

	public Model(String fileName, Material mat) {
		long startTime = System.currentTimeMillis();
		System.out.println("Loading model from file " + fileName);
		points = new ArrayList<Vector3f>();
		normalVectors = new ArrayList<Vector3f>();
		textures = new ArrayList<Point2f>();
		triangles = new ArrayList<Triangle>();
		
		parseFile(fileName);
		setMaterial(mat);

		System.out.println("Loaded in " + (System.currentTimeMillis() - startTime) + "ms");
	}

	private Model(List<Triangle> triangles, Material mat) {
		this.triangles = triangles;
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
		System.out.println("  " + points.size() + " points");
		System.out.println("  " + triangles.size() + " triangles");
	}
	
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
				Point2f texture = new Point2f(Float.parseFloat(lineTokenized[1]), Float.parseFloat(lineTokenized[2]));
				textures.add(texture);
				break;
				
			case VN:
				// Normal vector
				Vector3f normal = new Vector3f(Float.parseFloat(lineTokenized[1]), Float.parseFloat(lineTokenized[2]), Float.parseFloat(lineTokenized[3]));
				normalVectors.add(normal);
				break;
				
			case F:
				// Generate vertices
				// Parsevertices only needs the strings with vertices in them, data tag not needed
				parseVertices(Arrays.copyOfRange(lineTokenized, 1, lineTokenized.length));

				break;
			}
		} catch (Exception e) {
			// Just ignore the message
			//e.printStackTrace();
		}
		
	}

	private Vertex generateVertex(String[] params) {
		int pointIndex = Integer.parseInt(params[0]) - 1;
		if (params.length == 1) {
			return new Vertex(points.get(pointIndex));
		} else {
			int normalIndex = Integer.parseInt(params[2]) - 1;

			if (params[1].isEmpty()) {
				return new Vertex(points.get(pointIndex), normalVectors.get(normalIndex));
			} else {
				int textureIndex = Integer.parseInt(params[1]) - 1;

				return new Vertex(points.get(pointIndex), normalVectors.get(normalIndex), textures.get(textureIndex));
			}
		}
	}

	private void parseVertices(String[] vertices) {
		String[] indices = vertices[0].split("/");
		Triangle triag;

		int numOfPoints = vertices.length;

		Vertex v1, v2, v3;

		// Fix one point, then rotate around rest:
		v1 = generateVertex(indices);

		// To make sure normals are ok: build triangles counterclockwise
		for (int i = 2; i < numOfPoints; i++) {
			indices = vertices[i - 1].split("/");
			v2 = generateVertex(indices);
			indices = vertices[i].split("/");
			v3 = generateVertex(indices);

			triangles.add(new Triangle(v1, v2, v3, material));
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
			return new Hit(ray, this, hit.getPoint(), hit.getNormal(), hit.getTextureCoordinates(), hit.getT());
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

		for (Triangle triag : triangles) {
			triag.setMaterial(mat);
		}
	}

	@Override
	public void applyTransformation(Matrix4f transformation) {
		for (Triangle triag : triangles) {
			triag.applyTransformation(transformation);
		}
	}

	@Override
	public float getProjectedSurfaceArea() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Surface> getPrimitiveSurfaces() {
		return new LinkedList<Surface>(triangles);
	}

	// TODO naar interface?
	public Model duplicate() {
		// Deep copy triangles
		List<Triangle> clonedTriangles = new LinkedList<Triangle>();

		for (Triangle triag : triangles) {
			clonedTriangles.add(new Triangle(triag));
		}

		return new Model(clonedTriangles, material);
	}

	public void moveToOrigin() {
		BoundingBox bb = boundingBox();
		Vector3f translate = bb.getMin().sum(bb.getMax()).divideBy(2).negate();

		Matrix4f trans = AffineTransformation.translate(translate);

		applyTransformation(trans);
	}
}
