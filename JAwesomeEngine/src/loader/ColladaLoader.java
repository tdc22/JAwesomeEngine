package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import objects.ShapedObject3;
import utils.GLConstants;
import vector.Vector;
import vector.Vector1f;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class ColladaLoader {
	private static boolean inSource;
	private static boolean inSourceTechnique;
	private static boolean inSourceTechniqueAccessor;
	private static String sourcename;
	private static int stride;

	public static ShapedObject3 loadDAE(File f) throws IOException {
		ShapedObject3 object = new ShapedObject3();

		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;

		inSource = false;
		inSourceTechnique = false;
		inSourceTechniqueAccessor = false;
		sourcename = "";
		stride = 0;
		HashMap<String, List<Vector>> sources = new HashMap<String, List<Vector>>();
		boolean inGeometries = false;
		boolean inVertices = false;
		boolean inPolylist = false;
		List<Float> values = new ArrayList<Float>();
		List<Vector> vectorValues = new ArrayList<Vector>();
		String vertexPositionChild = null;
		HashMap<String, Integer> valueType = new HashMap<String, Integer>();
		String vertexSourceName = "";
		String normalSourceName = "";
		String texcoordSourceName = "";
		String colorSourceName = "";
		List<Integer> vertexCount = new ArrayList<Integer>();

		List<Integer> indices = new ArrayList<Integer>();
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Vector2f> texturecoords = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Vector3f> colors = new ArrayList<Vector3f>();

		while ((line = reader.readLine()) != null) {
			if (line.contains("<library_geometries")) {
				values.clear();
				inGeometries = true;
			}
			if (inGeometries) {
				parseSource(line, values, vectorValues, sources, valueType);
				if (line.contains("<vertices")) {
					inVertices = true;
				}
				if (inVertices) {
					if (line.contains("<input") && line.contains("semantic=\"POSITION\"")) {
						vertexPositionChild = line.split("source=\"")[1].split("\"")[0].replace("#", "");
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
						String semanticSource = line.split("source=\"")[1].split("\"")[0].replace("#", "");
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
					if (line.contains("<p>")) {
						int attributesPerVertex = 0;
						HashMap<Integer, String> attributeName = new HashMap<Integer, String>();
						int vertexPos = -1;
						int normalPos = -1;
						int texCoordPos = -1;
						int colorPos = -1;
						List<Vector> vertexSource = null;
						List<Vector> normalSource = null;
						List<Vector> texCoordSource = null;
						List<Vector> colorSource = null;
						if (!vertexSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, vertexPositionChild);
							vertexPos = attributesPerVertex;
							vertexSource = sources.get(vertexPositionChild);
							attributesPerVertex++;
						}
						if (!normalSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, normalSourceName);
							normalPos = attributesPerVertex;
							normalSource = sources.get(normalSourceName);
							attributesPerVertex++;
						}
						if (!texcoordSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, texcoordSourceName);
							texCoordPos = attributesPerVertex;
							texCoordSource = sources.get(texcoordSourceName);
							attributesPerVertex++;
						}
						if (!colorSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, colorSourceName);
							colorPos = attributesPerVertex;
							colorSource = sources.get(colorSourceName);
							attributesPerVertex++;
						}

						int a = 0;
						int index = 0;
						for (String value : line.split(">")[1].split("<")[0].split(" ")) {
							int val = Integer.parseInt(value);

							if (a == vertexPos)
								vertices.add((Vector3f) vertexSource.get(val));
							else if (a == normalPos)
								normals.add((Vector3f) normalSource.get(val));
							else if (a == texCoordPos)
								texturecoords.add((Vector2f) texCoordSource.get(val));
							else if (a == colorPos)
								colors.add((Vector3f) colorSource.get(val));

							a++;
							if (a == attributesPerVertex) {
								a = 0;
								indices.add(index);
								index++;
							}
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
			if (line.contains("</library_geometries")) {
				inGeometries = false;
			}
		}
		reader.close();

		if (normals.size() < vertices.size()) {
			Vector3f normal = new Vector3f(0, 1, 0);
			int num = vertices.size() - normals.size();
			for (int i = 0; i < num; i++)
				normals.add(normal);
		}
		if (texturecoords.size() < vertices.size()) {
			Vector2f texcoord = new Vector2f(0, 0);
			int num = vertices.size() - texturecoords.size();
			for (int i = 0; i < num; i++)
				texturecoords.add(texcoord);
		}
		if (colors.size() < vertices.size()) {
			Vector3f color = new Vector3f(1, 1, 1);
			int num = vertices.size() - colors.size();
			for (int i = 0; i < num; i++)
				colors.add(color);
		}

		object.setIndices(indices);
		object.setVertices(vertices);
		object.setNormals(normals);
		object.setTextureCoordinates(texturecoords);
		object.setColors(colors);
		// TODO Adjacency variant?
		object.setRenderMode(GLConstants.TRIANGLES);
		object.prerender();

		return object;
	}

	public void loadAnimation(File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;

		inSource = false;
		inSourceTechnique = false;
		inSourceTechniqueAccessor = false;
		sourcename = "";
		stride = 0;
		boolean inControllers = false;
		boolean inVertexWeights = false;
		HashMap<String, List<Vector>> sources = new HashMap<String, List<Vector>>();
		List<Float> values = new ArrayList<Float>();
		List<Vector> vectorValues = new ArrayList<Vector>();
		HashMap<String, Integer> valueType = new HashMap<String, Integer>();
		String jointSourceName = "";
		String weightSourceName = "";
		List<Integer> jointCount = new ArrayList<Integer>();

		List<Integer> joints = new ArrayList<Integer>();
		List<Integer> weights = new ArrayList<Integer>();

		while ((line = reader.readLine()) != null) {
			if (line.contains("<library_controllers")) {
				inControllers = true;
			}
			if (inControllers) {
				parseSource(line, values, vectorValues, sources, valueType);
				if (line.contains("<vertex_weights")) {
					inVertexWeights = true;
				}
				if (inVertexWeights) {
					if (line.contains("<input")) {
						String semantic = line.split("semantic=\"")[1].split("\"")[0];
						String semanticSource = line.split("source=\"")[1].split("\"")[0].replace("#", "");
						if (semantic.equals("JOINT"))
							jointSourceName = semanticSource;
						if (semantic.equals("WEIGHT"))
							weightSourceName = semanticSource;
					}
					if (line.contains("<vcount")) {
						for (String value : line.split(">")[1].split("<")[0].split(" ")) {
							jointCount.add(Integer.parseInt(value));
						}
					}
					if (line.contains("<v>")) {
						int attributesPerVertex = 0;
						HashMap<Integer, String> attributeName = new HashMap<Integer, String>();
						int vertexPos = -1;
						int normalPos = -1;
						List<Vector> vertexSource = null;
						List<Vector> normalSource = null;
						if (!jointSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, jointSourceName);
							vertexPos = attributesPerVertex;
							vertexSource = sources.get(jointSourceName);
							attributesPerVertex++;
						}
						if (!weightSourceName.isEmpty()) {
							attributeName.put(attributesPerVertex, weightSourceName);
							normalPos = attributesPerVertex;
							normalSource = sources.get(weightSourceName);
							attributesPerVertex++;
						}

						int a = 0;
						int index = 0;
						for (String value : line.split(">")[1].split("<")[0].split(" ")) {
							int val = Integer.parseInt(value);

							if (a == vertexPos)
								vertices.add((Vector3f) vertexSource.get(val));
							else if (a == normalPos)
								normals.add((Vector3f) normalSource.get(val));

							a++;
							if (a == attributesPerVertex) {
								a = 0;
								indices.add(index);
								index++;
							}
						}
					}
				}
				if (line.contains("</vertex_weights")) {
					jointSourceName = "";
					weightSourceName = "";
					jointCount.clear();
					inVertexWeights = false;
				}
			}
			if (line.contains("</library_controllers")) {
				inControllers = false;
			}
		}
		reader.close();
	}

	private static void parseSource(String line, List<Float> values, List<Vector> vectorValues,
			HashMap<String, List<Vector>> sources, HashMap<String, Integer> valueType) {
		if (line.contains("<source")) {
			inSource = true;
			sourcename = line.split("id=\"")[1].split("\"")[0];
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
						stride = Integer.parseInt(line.split("stride=\"")[1].split("\"")[0]);
						switch (stride) {
						case 1:
							for (int i = 0; i < values.size(); i++)
								vectorValues.add(new Vector1f(values.get(i)));
							break;
						case 2:
							for (int i = 0; i < values.size() / 2; i++) {
								int i2 = i * 2;
								vectorValues.add(new Vector2f(values.get(i2), values.get(i2 + 1)));
							}
							break;
						case 3:
							for (int i = 0; i < values.size() / 3; i++) {
								int i3 = i * 3;
								vectorValues.add(new Vector3f(values.get(i3), values.get(i3 + 1), values.get(i3 + 2)));
							}
							break;
						case 4:
							for (int i = 0; i < values.size() / 4; i++) {
								int i4 = i * 4;
								vectorValues.add(new Vector4f(values.get(i4), values.get(i4 + 1), values.get(i4 + 2),
										values.get(i4 + 3)));
							}
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
				sources.put(sourcename, new ArrayList<Vector>(vectorValues));
				vectorValues.clear();
				valueType.put(sourcename, new Integer(stride)); // TODO:
																// check
				inSource = false;
			}
		}
	}

	public static ShapedObject3 loadModel(File f) throws IOException {
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