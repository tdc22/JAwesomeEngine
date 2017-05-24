package loader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gui.Color;
import objects.ShapedObject3;
import vector.Vector2f;
import vector.Vector3f;

public class OBJLoader {
	/*
	 * Converts a given *.obj-File to a *.mobj-File.
	 * 
	 * OBJ: v = vertex vt = texture coordinate vn = vertex normal vp = parameter
	 * space vertices (unsupported) f = faces (v/vt/vn)
	 * 
	 * MOBJ: vs = specific vertex (v, vt, vn) f = faces (vs..)
	 */
	public static File convertOBJ(File f) throws IOException {
		File f2 = new File(f.getPath().replace(".obj", ".mobj.tmp"));
		File f3 = new File(f.getPath().replace(".obj", ".mobj"));
		List<Integer[][]> facevertices = new ArrayList<Integer[][]>();
		// First run (checks all faces, if they are triangles, make list)
		BufferedReader reader = new BufferedReader(new FileReader(f));
		BufferedWriter writer = new BufferedWriter(new FileWriter(f2));
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("f ")) {
				// f vertexindex/textureindex/normalindex
				if (line.split(" ").length == 4) {
					Integer[][] indices = new Integer[3][3];
					String[] linesplit = line.split(" ");
					String[] firstindices = linesplit[1].split("/");
					indices[0][0] = Integer.parseInt(firstindices[0]);
					indices[0][1] = firstindices.length < 2 || firstindices[1].isEmpty() ? 0
							: Integer.parseInt(firstindices[1]);
					indices[0][2] = firstindices.length < 3 || firstindices[2].isEmpty() ? 0
							: Integer.parseInt(firstindices[2]);
					String[] secondindices = linesplit[2].split("/");
					indices[1][0] = Integer.parseInt(secondindices[0]);
					indices[1][1] = secondindices.length < 2 || secondindices[1].isEmpty() ? 0
							: Integer.parseInt(secondindices[1]);
					indices[1][2] = secondindices.length < 3 || secondindices[2].isEmpty() ? 0
							: Integer.parseInt(secondindices[2]);
					String[] thirdindices = linesplit[3].split("/");
					indices[2][0] = Integer.parseInt(thirdindices[0]);
					indices[2][1] = thirdindices.length < 2 || thirdindices[1].isEmpty() ? 0
							: Integer.parseInt(thirdindices[1]);
					indices[2][2] = thirdindices.length < 3 || thirdindices[2].isEmpty() ? 0
							: Integer.parseInt(thirdindices[2]);
					facevertices.add(indices);
				} else if (line.split(" ").length == 5) {
					Integer[][] indices = new Integer[3][3];
					String[] linesplit = line.split(" ");
					String[] firstindices = linesplit[1].split("/");
					indices[0][0] = Integer.parseInt(firstindices[0]);
					indices[0][1] = firstindices.length < 2 || firstindices[1].isEmpty() ? 0
							: Integer.parseInt(firstindices[1]);
					indices[0][2] = firstindices.length < 3 || firstindices[2].isEmpty() ? 0
							: Integer.parseInt(firstindices[2]);
					String[] secondindices = linesplit[2].split("/");
					indices[1][0] = Integer.parseInt(secondindices[0]);
					indices[1][1] = secondindices.length < 2 || secondindices[1].isEmpty() ? 0
							: Integer.parseInt(secondindices[1]);
					indices[1][2] = secondindices.length < 3 || secondindices[2].isEmpty() ? 0
							: Integer.parseInt(secondindices[2]);
					String[] thirdindices = linesplit[3].split("/");
					indices[2][0] = Integer.parseInt(thirdindices[0]);
					indices[2][1] = thirdindices.length < 2 || thirdindices[1].isEmpty() ? 0
							: Integer.parseInt(thirdindices[1]);
					indices[2][2] = thirdindices.length < 3 || thirdindices[2].isEmpty() ? 0
							: Integer.parseInt(thirdindices[2]);
					facevertices.add(indices);

					indices = new Integer[3][3];
					linesplit = line.split(" ");
					firstindices = linesplit[1].split("/");
					indices[0][0] = Integer.parseInt(firstindices[0]);
					indices[0][1] = firstindices.length < 2 || firstindices[1].isEmpty() ? 0
							: Integer.parseInt(firstindices[1]);
					indices[0][2] = firstindices.length < 3 || firstindices[2].isEmpty() ? 0
							: Integer.parseInt(firstindices[2]);
					secondindices = linesplit[3].split("/");
					indices[1][0] = Integer.parseInt(secondindices[0]);
					indices[1][1] = secondindices.length < 2 || secondindices[1].isEmpty() ? 0
							: Integer.parseInt(secondindices[1]);
					indices[1][2] = secondindices.length < 3 || secondindices[2].isEmpty() ? 0
							: Integer.parseInt(secondindices[2]);
					thirdindices = linesplit[4].split("/");
					indices[2][0] = Integer.parseInt(thirdindices[0]);
					indices[2][1] = thirdindices.length < 2 || thirdindices[1].isEmpty() ? 0
							: Integer.parseInt(thirdindices[1]);
					indices[2][2] = thirdindices.length < 3 || thirdindices[2].isEmpty() ? 0
							: Integer.parseInt(thirdindices[2]);
					facevertices.add(indices);
				} else {
					System.err.println("Number of vertices per face must be 3 or 4");
				}
			} else if (line.startsWith("v ") || line.startsWith("vt ") || line.startsWith("vn ") || line.startsWith("#")
					|| line.startsWith("mtllib") || line.startsWith("o ") // ||
																			// line.startsWith("s
																			// ")
					|| line.startsWith("usemtl")) {
				writer.write(line);
				writer.newLine();
			}
		}

		List<Integer[]> faces = new ArrayList<Integer[]>();
		List<Integer[]> vertices = new ArrayList<Integer[]>();
		for (Integer[][] a : facevertices) {
			Integer[] faceVertIds = new Integer[3];
			for (int i = 0; i < 3; i++) {
				Integer[] b = a[i];
				int pos = vertices.indexOf(b);
				if (pos == -1) {
					pos = vertices.size();
					vertices.add(b);
				}
				faceVertIds[i] = pos;
			}
			faces.add(faceVertIds);
			writer.write("f " + faceVertIds[0] + " " + faceVertIds[1] + " " + faceVertIds[2]);
			writer.newLine();
		}

		for (Integer[] a : vertices) {
			writer.write("vs " + a[0] + " " + a[1] + " " + a[2]);
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

	public static ShapedObject3 loadMOBJ(File f) throws IOException {
		ShapedObject3 object = new ShapedObject3();

		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texturecoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		vertices.add(new Vector3f());
		texturecoords.add(new Vector2f());
		normals.add(new Vector3f(0, 1, 0));
		while ((line = reader.readLine()) != null) {
			if (line.startsWith("v ")) {
				String[] vertexCoords = line.split(" ");
				float x = Float.parseFloat(vertexCoords[1]);
				float y = Float.parseFloat(vertexCoords[2]);
				float z = Float.parseFloat(vertexCoords[3]);
				vertices.add(new Vector3f(x, y, z));
			}
			if (line.startsWith("vt ")) {
				String[] textureCoords = line.split(" ");
				float tx = Float.parseFloat(textureCoords[1]);
				float ty = Float.parseFloat(textureCoords[2]);
				texturecoords.add(new Vector2f(tx, ty));
			}
			if (line.startsWith("vn ")) {
				String[] normalCoords = line.split(" ");
				float nx = Float.parseFloat(normalCoords[1]);
				float ny = Float.parseFloat(normalCoords[2]);
				float nz = Float.parseFloat(normalCoords[3]);
				normals.add(new Vector3f(nx, ny, nz));
			}
			if (line.startsWith("f ")) {
				String[] faceString = line.split(" ");
				int i1 = Integer.parseInt(faceString[1]);
				int i2 = Integer.parseInt(faceString[3]);
				int i3 = Integer.parseInt(faceString[5]);

				int adj1 = Integer.parseInt(faceString[2]);
				int adj2 = Integer.parseInt(faceString[4]);
				int adj3 = Integer.parseInt(faceString[6]);

				object.addTriangle(i1, adj1, i2, adj2, i3, adj3);
			}
			if (line.startsWith("vs ")) {
				String[] vertexString = line.split(" ");
				object.addVertex(vertices.get(Integer.parseInt(vertexString[1])), Color.WHITE,
						texturecoords.get(Integer.parseInt(vertexString[2])),
						normals.get(Integer.parseInt(vertexString[3])));
			}
		}
		reader.close();
		object.prerender();

		return object;
	}

	public static ShapedObject3 loadModel(File f) throws IOException {
		ShapedObject3 object;

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

		return object;
	}
}