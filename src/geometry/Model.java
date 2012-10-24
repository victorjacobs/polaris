package geometry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Class that represents a model made up out of a number of triangles
 * 
 * @author victor
 *
 */
public class Model {
	private ArrayList<Vector3f> vertices;
	
	private enum DataTags {
		V, VT, VN, F
	}
	
	// TODO eventueel parser uit halen en ergens anders zetten
	public Model(String fileName) {
		vertices = new ArrayList<Vector3f>();
		// Add dummy on index 0 because data is 1-indexed
		vertices.add(new Vector3f(0, 0, 0));
		
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
	}
	
	private void parseLine(String line) {
		String[] lineTokenized = line.split(" ");
		
		switch (DataTags.valueOf(lineTokenized[0])) {
		case V:
			//Vector3f vertex = new Vector3f(lineTokenized[1], lineTokenized[2], lineTokenized[3]).
			//vertices.add(vertex);
			break;
			
		case VT:
			
			break;
			
		case VN:
			
			break;
			
		case F:
			
			break;
		}
	}
}
