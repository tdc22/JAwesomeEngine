package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import objects.ShapedObject3;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class ColladaLoader {

	public static ShapedObject3 loadDAE(File f) throws IOException {
		ShapedObject3 object = new ShapedObject3();

		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;

		HashMap<String, List<Float>> sources = new HashMap<String, List<Float>>();
		boolean inGeometries = false;
		boolean inSource = false;
		boolean inSourceTechnique = false;
		boolean inSourceTechniqueAccessor = false;
		boolean inVertices = false;
		boolean inPolylist = false;
		List<Float> values = new ArrayList<Float>();
		String sourcename = "";
		String positionSourceName = "";
		String vertexSourceName = "";
		String normalSourceName = "";
		String texcoordSourceName = "";
		String colorSourceName = "";
		List<Integer> vertexCount = new ArrayList<Integer>();

		while ((line = reader.readLine()) != null) {
			if (line.contains("</library_geometries")) {
				inGeometries = false;
			}
			if (inGeometries) {
				if (line.contains("<source")) {
					inSource = true;
					System.out.println(line);
					sourcename = line.split("id=\"")[1].split("\"")[0];
					System.out.println(sourcename);
				}
				if (inSource) {
					if (line.contains("<float_array")) {
						for (String value : line.split(">")[1].split("<")[0].split(" ")) {
							values.add(Float.parseFloat(value));
						}
					}
					if (line.contains("<technique_common")) {
						inSourceTechnique = true;
					}
					if (inSourceTechnique) {
						if (line.contains("<accessor")) {
							inSourceTechniqueAccessor = true;
						}
						if (inSourceTechniqueAccessor) {
							if (line.contains("stride=\"")) {
								int stride = Integer.parseInt(line.split("stride=\"")[1].split("\"")[0]);
								switch (stride) {
								case 1:
									List<Vector1f> valueVectors1 = new ArrayList<Vector1f>(); // TODO
									for (int i = 0; i < values.size(); i++)
										valueVectors1.add(new Vector1f(values.get(i))); // TODO
									break;
								case 2:
									List<Vector2f> valueVectors2 = new ArrayList<Vector2f>();
									for (int i = 0; i < values.size() / 2; i++)
										valueVectors2.add(new Vector2f(values.get(i), values.get(i + 1)));
									break;
								case 3:
									List<Vector3f> valueVectors3 = new ArrayList<Vector3f>();
									for (int i = 0; i < values.size() / 3; i++)
										valueVectors3
												.add(new Vector3f(values.get(i), values.get(i + 1), values.get(i + 2)));
									break;
								case 4:
									List<Vector4f> valueVectors4 = new ArrayList<Vector4f>();
									for (int i = 0; i < values.size() / 4; i++)
										valueVectors4.add(new Vector4f(values.get(i), values.get(i + 1),
												values.get(i + 2), values.get(i + 3)));
									break;
								default:
									System.err.println("Stride count has to be between 1 and 4.");
								}
							}
							if (line.contains("</accessor")) {
								inSourceTechniqueAccessor = false;
							}
						}
						if (line.contains("</technique_common")) {
							inSourceTechnique = false;
						}
					}
					if (line.contains("</source")) {
						sources.put(sourcename, values);
						inSource = false;
					}
				}
				if (line.contains("<vertices")) {
					inVertices = true;
				}
				if (inVertices) {
					if (line.contains("<input") && line.contains("semantic=\"POSITION\"")) {
						positionSourceName = line.split("source=\"")[1].split("\"")[0];
					}
					if (line.contains("</vertices")) {
						inVertices = false;
					}
				}
				if (line.contains("<polylist")) {
					inPolylist = true;
				}
				if (inPolylist) {
					if (line.contains("<input")) {
						String semantic = line.split("semantic=\"")[1].split("\"")[0];
						String semanticSource = line.split("source=\"")[1].split("\"")[0];
						if (semantic.equals("VERTEX"))
							vertexSourceName = semanticSource;
						if (semantic.equals("NORMAL"))
							normalSourceName = semanticSource;
						if (semantic.equals("TEXCOORD"))
							texcoordSourceName = semanticSource;
						if (semantic.equals("COLOR"))
							colorSourceName = semanticSource;
					}
					if (line.contains("<vcount")) {
						for (String value : line.split(">")[1].split("<")[0].split(" ")) {
							int vcount = Integer.parseInt(value);
							if (vcount == 3 || vcount == 4) {
								vertexCount.add(vcount);
							} else {
								System.err.println("Number of vertices per face must be 3 or 4");
							}
						}
					}
					if (line.contains("<p")) {
						int attributesPerVertex = 0;
						HashMap<Integer, String> attributeName = new HashMap<Integer, String>();
						if (!vertexSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, vertexSourceName);
							attributesPerVertex++;
						}
						if (!normalSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, normalSourceName);
							attributesPerVertex++;
						}
						if (!texcoordSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, texcoordSourceName);
							attributesPerVertex++;
						}
						if (!colorSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, colorSourceName);
						}

						int a = 0;
						for (String value : line.split(">")[1].split("<")[0].split(" ")) {
							// TODO
							a++;
							if (a == attributesPerVertex)
								a = 0;
						}
					}
					if (line.contains("</polylist")) {
						vertexSourceName = "";
						normalSourceName = "";
						texcoordSourceName = "";
						colorSourceName = "";
						vertexCount.clear();
						inPolylist = false;
					}
				}
			}
			if (line.contains("<library_geometries")) {
				values.clear();
				inGeometries = true;
			}
		}
		reader.close();

		// private void readPositions() {
		// String positionsId =
		// meshData.getChild("vertices").getChild("input").getAttribute("source").substring(1);
		// = Cube-mesh-positions
		// XmlNode positionsData = meshData.getChildWithAttribute("source",
		// "id", positionsId).getChild("float_array");

		// int count = Integer.parseInt(positionsData.getAttribute("count"));
		// String[] posData = positionsData.getData().split(" ");
		// for (int i = 0; i < count/3; i++) {
		// float x = Float.parseFloat(posData[i * 3]);
		// float y = Float.parseFloat(posData[i * 3 + 1]);
		// float z = Float.parseFloat(posData[i * 3 + 2]);
		// Vector4f position = new Vector4f(x, y, z, 1);
		// Matrix4f.transform(CORRECTION, position, position);
		// vertices.add(new Vertex(vertices.size(), new Vector3f(position.x,
		// position.y, position.z), vertexWeights.get(vertices.size())));
		// }
		// }

		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texturecoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		vertices.add(new Vector3f());
		texturecoords.add(new Vector2f());
		normals.add(new Vector3f(0, 1, 0));

		return object;
	}

	// TODO: rename to loadModel?
	public static ShapedObject3 loadGeometry(File f) throws IOException {
		ShapedObject3 object;

		if (f.getName().endsWith(".dae")) {
			object = loadDAE(f);
		} else {
			System.err.println("File extension not recognized. (Use *.dae)");
			return null;
		}

		return object;
	}
}