package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import objects.ShapedObject;

public class X3DLoader {
	public static ShapedObject loadModel(File f) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		ShapedObject object = new ShapedObject();
		// List<Vector3f> verts = new ArrayList<Vector3f>();
		// List<Vector3f> normals = new ArrayList<Vector3f>();
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("v ")) {

			}
		}
		reader.close();
		return object;
	}
}
