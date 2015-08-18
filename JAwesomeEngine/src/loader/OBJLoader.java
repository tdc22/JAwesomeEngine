package loader;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import objects.ShapedObject;
import vector.Vector2f;
import vector.Vector3f;

public class OBJLoader {
	/*
	 * Converts a given *.obj-File to a *.mobj-File.
	 */
	public static File convertOBJ(File f) throws IOException {
		File f2 = new File(f.getPath().replace(".obj", ".mobj.tmp"));
		File f3 = new File(f.getPath().replace(".obj", ".mobj"));
		List<Integer[]> faces = new ArrayList<Integer[]>();
		// First run (checks all faces, if they are triangles, make list)
		BufferedReader reader = new BufferedReader(new FileReader(f));
		BufferedWriter writer = new BufferedWriter(new FileWriter(f2));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("f ")) {
				// f vertexindex/textureindex/normalindex
				String writeline = "";
				if (line.split(" ").length == 4) {
					Integer[] indices = new Integer[3];
					indices[0] = Integer.parseInt(line.split(" ")[1].split("/")[0]) - 1;
					indices[1] = Integer.parseInt(line.split(" ")[2].split("/")[0]) - 1;
					indices[2] = Integer.parseInt(line.split(" ")[3].split("/")[0]) - 1;
					faces.add(indices);
					writeline = line.split(" ")[0] + " " + indices[0] + " " + indices[1] + " " + indices[2];
				} else if (line.split(" ").length == 5) {
					Integer[] indices1 = new Integer[3];
					indices1[0] = Integer.parseInt(line.split(" ")[1].split("/")[0]) - 1;
					indices1[1] = Integer.parseInt(line.split(" ")[2].split("/")[0]) - 1;
					indices1[2] = Integer.parseInt(line.split(" ")[3].split("/")[0]) - 1;
					faces.add(indices1);
					Integer[] indices2 = new Integer[3];
					indices2[0] = Integer.parseInt(line.split(" ")[1].split("/")[0]) - 1;
					indices2[1] = Integer.parseInt(line.split(" ")[3].split("/")[0]) - 1;
					indices2[2] = Integer.parseInt(line.split(" ")[4].split("/")[0]) - 1;
					faces.add(indices2);
					writeline = line.split(" ")[0] + " " + indices1[0] + " " + indices1[1] + " " + indices1[2] + " "
							+ "\n" + line.split(" ")[0] + " " + indices2[0] + " " + indices2[1] + " " + indices2[2];
				} else {
					System.err.println("Number of vertices per face must be 3 or 4");
				}
				writer.write(writeline);
			}
			if (line.startsWith("v ") || line.startsWith("vn ") || line.startsWith("#") || line.startsWith("mtllib")
					|| line.startsWith("o ") || line.startsWith("s ") || line.startsWith("usemtl")) {
				writer.write(line);
			}
			writer.newLine();
		}
		reader.close();
		writer.close();

		// Second run
		reader = new BufferedReader(new FileReader(f2));
		writer = new BufferedWriter(new FileWriter(f3));
		int facenumber = 0;
		System.out.println(faces.size());
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("f ")) {
				String writeline = "";
				// find face
				Integer[] faceindices = faces.get(facenumber);
				int v1 = faceindices[0];
				int v2 = faceindices[1];
				int v3 = faceindices[2];
				int adj1 = 0, adj2 = 0, adj3 = 0;
				// find same placed vertices with other ids

				for (int d = 0; d < faces.size(); d++) {
					// Search for neighbor face
					Integer[] indices = faces.get(d);
					int i1 = indices[0];
					int i2 = indices[1];
					int i3 = indices[2];
					int numsame = 0;
					boolean s1 = false, s2 = false, s3 = false;
					if (v1 == i1 || v1 == i2 || v1 == i3) {
						numsame++;
						s1 = true;
					}
					if (v2 == i1 || v2 == i2 || v2 == i3) {
						numsame++;
						s2 = true;
					}
					if (v3 == i1 || v3 == i2 || v3 == i3) {
						numsame++;
						s3 = true;
					}
					if (numsame == 2) {
						int adj = 0;
						if (i1 != v1 && i1 != v2 && i1 != v3)
							adj = i1;
						if (i2 != v1 && i2 != v2 && i2 != v3)
							adj = i2;
						if (i3 != v1 && i3 != v2 && i3 != v3)
							adj = i3;
						if (s1 && s2)
							adj1 = adj;
						if (s2 && s3)
							adj2 = adj;
						if (s3 && s1)
							adj3 = adj;
					}
				}
				String[] lineparts = line.split(" ");
				writeline = lineparts[0] + " " + lineparts[1] + " " + adj1 + " " + lineparts[2] + " " + adj2 + " "
						+ lineparts[3] + " " + adj3;
				writer.write(writeline);
				facenumber++;
			} else {
				writer.write(line);
			}
			writer.newLine();
		}
		reader.close();
		writer.close();

		f2.delete();

		return f3;
	}

	/*
	 * Converts a given *.obj-File to a *.mobj-File.
	 */
	public static ShapedObject loadMOBJ(File f) throws IOException {
		ShapedObject object = new ShapedObject();

		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		int normalindex = 0;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("v ")) {
				float x = Float.parseFloat(line.split(" ")[1]);
				float y = Float.parseFloat(line.split(" ")[2]);
				float z = Float.parseFloat(line.split(" ")[3]);
				Vector3f vertex = new Vector3f(x, y, z);

				object.addVertex(vertex, Color.WHITE, new Vector2f(0, 0), new Vector3f(0, 1, 0));
			}
			if (line.startsWith("vn")) {
				float nx = Float.parseFloat(line.split(" ")[1]);
				float ny = Float.parseFloat(line.split(" ")[2]);
				float nz = Float.parseFloat(line.split(" ")[3]);

				object.setNormal(normalindex, new Vector3f(nx, ny, nz));
				normalindex++;
			}
			if (line.startsWith("f ")) {
				int i1 = Integer.parseInt(line.split(" ")[1].split("/")[0]);
				int i2 = Integer.parseInt(line.split(" ")[3].split("/")[0]);
				int i3 = Integer.parseInt(line.split(" ")[5].split("/")[0]);

				int adj1 = Integer.parseInt(line.split(" ")[2]);
				int adj2 = Integer.parseInt(line.split(" ")[4]);
				int adj3 = Integer.parseInt(line.split(" ")[6]);

				object.addTriangle(i1, adj1, i2, adj2, i3, adj3);
			}
		}
		reader.close();

		return object;
	}

	public static ShapedObject loadModel(File f) throws FileNotFoundException, IOException {
		ShapedObject object = new ShapedObject();
		if (f.getName().endsWith(".mobj")) {
			object = loadMOBJ(f);
		} else {
			if (f.getName().endsWith(".obj")) {
				f = convertOBJ(f);
				object = loadMOBJ(f);
			} else {
				System.err.println("File extension not recognized. (Use *.obj or *.mobj)");
				return null;
			}
		}

		/*
		 * BufferedReader reader = new BufferedReader(new FileReader(f));
		 * ShapedObject object = new ShapedObject(); //List<Vector3f> normals =
		 * new ArrayList<Vector3f>(); String line; /*while ((line =
		 * reader.readLine()) != null) { if (line.startsWith("vn ")) { float x =
		 * Float.valueOf(line.split(" ")[1]); float y =
		 * Float.valueOf(line.split(" ")[2]); float z =
		 * Float.valueOf(line.split(" ")[3]); normals.add(new Vector3f(x, y,
		 * z)); } } reader = new BufferedReader(new FileReader(f)); int
		 * normalsize = normals.size()-1; int currentvert = 0;
		 */
		/*
		 * Vector3f nullvector = new Vector3f(0,0,0); while ((line =
		 * reader.readLine()) != null) { if (line.startsWith("v ")) { float x =
		 * Float.valueOf(line.split(" ")[1]); float y =
		 * Float.valueOf(line.split(" ")[2]); float z =
		 * Float.valueOf(line.split(" ")[3]); object.addVertex(new Vector3f(x,
		 * y, z), new Color(Color.GREY), new Vector2f(0,0),
		 * nullvector);//normals.get((int) normalIndices.x)); } else if
		 * (line.startsWith("f ")) { int index1 =
		 * (int)Integer.valueOf(line.split(" ")[1].split("/")[0])-1; int index2
		 * = (int)Integer.valueOf(line.split(" ")[2].split("/")[0])-1; int
		 * index3 = (int)Integer.valueOf(line.split(" ")[3].split("/")[0])-1;
		 * Vector3f normal1 = object.getNormal(index1); Vector3f normal2 =
		 * object.getNormal(index2); Vector3f normal3 =
		 * object.getNormal(index3); Vector3f newnormal =
		 * VecMath.computeNormal(object.getVertex(index1),
		 * object.getVertex(index3), object.getVertex(index2));
		 * object.setNormal(index1,
		 * VecMath.vectorNormalize(VecMath.vectorAddition(normal1, newnormal)));
		 * object.setNormal(index2,
		 * VecMath.vectorNormalize(VecMath.vectorAddition(normal2, newnormal)));
		 * object.setNormal(index3,
		 * VecMath.vectorNormalize(VecMath.vectorAddition(normal3, newnormal)));
		 * //object.addTriangle(index1, index2, index3); } } reader.close();
		 */
		return object;
	}
}