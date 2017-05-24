package loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import anim.BoneAnimation3;
import anim.BoneAnimationKeyframe3;
import anim.BoneAnimationSkeleton3;
import anim.BoneJoint;
import math.QuatMath;
import math.VecMath;
import matrix.Matrix;
import matrix.Matrix4f;
import objects.ShapedObject3;
import quaternion.Quaternionf;
import utils.GLConstants;
import utils.Pair;
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
				parseSource(line, values, null, vectorValues, null, null, sources, null, null, valueType);
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

	public static BoneAnimationSkeleton3 loadAnimationDAE(File f) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String line;

		inSource = false;
		inSourceTechnique = false;
		inSourceTechniqueAccessor = false;
		sourcename = "";
		stride = 0;
		boolean inControllers = false;
		boolean inVertexWeights = false;
		boolean inJoints = false;
		boolean inVisualScenes = false;
		boolean inAnimations = false;
		boolean inSampler = false;
		HashMap<String, List<Vector>> sources = new HashMap<String, List<Vector>>();
		HashMap<String, List<Matrix>> matrixSources = new HashMap<String, List<Matrix>>();
		HashMap<String, List<String>> nameSources = new HashMap<String, List<String>>();
		List<Float> values = new ArrayList<Float>();
		List<String> namevalues = new ArrayList<String>();
		List<Vector> vectorValues = new ArrayList<Vector>();
		List<Matrix> matrixValues = new ArrayList<Matrix>();
		List<String> stringValues = new ArrayList<String>();
		HashMap<String, Integer> valueType = new HashMap<String, Integer>();
		String jointJointSourceName = "";
		String invBindMatrixSourceName = "";
		String jointSourceName = "";
		String weightSourceName = "";
		String inputSourceName = "";
		String outputSourceName = "";
		String interpolationSourceName = "";
		List<Integer> jointCount = new ArrayList<Integer>();
		HashMap<String, Integer> jointNames = new HashMap<String, Integer>();
		LinkedList<Integer> nodestack = new LinkedList<Integer>();
		List<Vector> timestamps = null;
		List<Matrix> poses = null;
		List<String> targets = new ArrayList<String>();
		HashMap<String, Matrix4f> targetPose = new HashMap<String, Matrix4f>();
		HashMap<String, BoneAnimationKeyframe3> targetKeyframe = new HashMap<String, BoneAnimationKeyframe3>();

		List<Integer[]> jointIds = new ArrayList<Integer[]>();
		List<Vector4f> weights = new ArrayList<Vector4f>();
		List<BoneJoint> joints = new ArrayList<BoneJoint>();
		BoneJoint rootjoint = null;
		BoneAnimation3 animation = new BoneAnimation3();

		while ((line = reader.readLine()) != null) {
			if (line.contains("<library_controllers")) {
				inControllers = true;
			}
			if (inControllers) {
				parseSource(line, values, namevalues, vectorValues, matrixValues, stringValues, sources, matrixSources,
						nameSources, valueType);
				if (line.contains("<joints")) {
					inJoints = true;
				}
				if (inJoints) {
					if (line.contains("<input")) {
						String semantic = line.split("semantic=\"")[1].split("\"")[0];
						String semanticSource = line.split("source=\"")[1].split("\"")[0].replace("#", "");
						if (semantic.equals("JOINT"))
							jointJointSourceName = semanticSource;
						if (semantic.equals("INV_BIND_MATRIX"))
							invBindMatrixSourceName = semanticSource;
					}
				}
				if (line.contains("</joints")) {
					System.out.println("joint end" + "; " + jointJointSourceName + "; " + invBindMatrixSourceName);
					if (!jointJointSourceName.isEmpty() && !invBindMatrixSourceName.isEmpty()) {
						List<String> allJointNames = nameSources.get(jointJointSourceName);
						List<Matrix> invBindMatrices = matrixSources.get(invBindMatrixSourceName);
						for (String s : allJointNames)
							System.out.println(s);
						for (int i = 0; i < invBindMatrices.size(); i++) {
							Matrix4f mat = (Matrix4f) invBindMatrices.get(i);
							mat.invert(); // TODO: check
							joints.add(new BoneJoint(i, mat));

							jointNames.put(allJointNames.get(i), i);
							System.out.println("name: " + i + "; " + allJointNames.get(i));
						}

						jointJointSourceName = "";
						invBindMatrixSourceName = "";
					}

					inJoints = false;
				}
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
						int attributesPerJoint = 0;
						HashMap<Integer, String> attributeName = new HashMap<Integer, String>();
						int jointPos = -1;
						int weightPos = -1;
						List<String> jointSource = null;
						List<Vector> weightSource = null;
						if (!jointSourceName.isEmpty()) {
							attributeName.put(attributesPerJoint, jointSourceName);
							jointPos = attributesPerJoint;
							jointSource = nameSources.get(jointSourceName);
							attributesPerJoint++;
						}
						if (!weightSourceName.isEmpty()) {
							attributeName.put(attributesPerJoint, weightSourceName);
							weightPos = attributesPerJoint;
							weightSource = sources.get(weightSourceName);
							attributesPerJoint++;
						}

						int a = 0;
						int v = 0;
						String[] valuestrings = line.split(">")[1].split("<")[0].split(" ");
						for (int s = 0; s < valuestrings.length;) {
							int jointcount = jointCount.get(v);
							Integer[] currJoints = new Integer[jointcount];
							Float[] currWeights = new Float[jointcount];
							for (int i = 0; i < jointcount; i++) {
								for (int j = 0; j < attributesPerJoint; j++) {
									String value = valuestrings[s];
									int val = Integer.parseInt(value);
									System.out.println(value + "; " + val + "; " + a + "; " + weightPos + "; " + v
											+ "; " + jointcount);

									if (a == jointPos)
										currJoints[i] = val;
									else if (a == weightPos)
										currWeights[i] = ((Vector1f) weightSource.get(val)).x;

									a++;
									if (a == attributesPerJoint) {
										a = 0;
									}
									s++;
								}
							}

							for (Float w : currWeights) {
								System.out.println(w);
							}

							float totalWeight = 0;
							if (jointcount > 4) {
								Integer[] newJointIds = new Integer[4];
								Float[] newWeights = new Float[4];
								HashSet<Integer> addedWeights = new HashSet<Integer>();
								System.out.println("--------------");
								for (int i = 0; i < 4; i++) {
									float biggest = 0;
									int currID = -1;
									float currWeight = -1;
									for (int j = 0; j < jointcount; j++) {
										Float w = currWeights[j];
										if (w > biggest && !addedWeights.contains(j)) {
											currID = j;
											newJointIds[i] = currJoints[j];
											currWeight = w;
											System.out.println("Added: " + i + "; " + w);
										}
									}
									newWeights[i] = currWeight;
									totalWeight += currWeight;
									addedWeights.add(currID);
								}
								currJoints = newJointIds;
								currWeights = newWeights;
							} else {
								if (jointcount < 4) {
									Integer[] newJointIds = new Integer[4];
									for (int i = 0; i < 4; i++) {
										if (i < jointcount) {
											newJointIds[i] = currJoints[i];
										} else {
											newJointIds[i] = 0;
										}
									}
									currJoints = newJointIds;
								}
								for (Float w : currWeights) {
									totalWeight += w;
								}
							}

							System.out.println(currWeights.length);
							for (Float w : currWeights) {
								System.out.println(w);
							}

							System.out.println("norma: " + weights.size() + " " + totalWeight);
							if (totalWeight != 1.0f && totalWeight > 0) {
								for (int i = 0; i < currWeights.length; i++) {
									currWeights[i] = currWeights[i] / totalWeight;
								}
							}

							jointIds.add(currJoints);
							Vector4f weight = new Vector4f();
							for (int i = 0; i < currWeights.length; i++) {
								weight.setValue(i, currWeights[i]);
							}
							weights.add(weight);

							v++;
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
			if (line.contains("<library_visual_scenes")) {
				inVisualScenes = true;
			}
			if (inVisualScenes) {
				if (line.contains("<node")) {
					int nodeID = -1;
					if (line.contains("type=\"JOINT\"")) {
						String nid = line.split("id=\"")[1].split("\"")[0];
						System.out.println(nid + "; " + jointNames);
						nodeID = jointNames.get(nid);
					}
					if (nodestack.size() > 0) {
						int parentId = nodestack.peek();
						if (parentId != -1) {
							joints.get(parentId).addChild(joints.get(nodeID));
						} else {
							rootjoint = joints.get(nodeID);
						}
					}
					nodestack.push(nodeID);
				}
				// if (line.contains("<matrix")) {
				// String[] entries =
				// line.split("<matrix")[1].split(">")[1].split("<")[0].split("
				// ");
				// Matrix4f nodematrix = new Matrix4f();
				// for(int i = 0; i < 16; i++) {
				// nodematrix.set(i % 4, i / 4, Float.parseFloat(entries[i]));
				// }
				// joints.get(nodestack.peek()).
				// }
				if (line.contains("</node")) {
					nodestack.pop();
				}
				// jointNames.get(key)
			}
			if (line.contains("</library_visual_scenes")) {
				inVisualScenes = false;
			}
			if (line.contains("<library_animations")) {
				inAnimations = true;
			}
			if (inAnimations) {
				parseSource(line, values, namevalues, vectorValues, matrixValues, stringValues, sources, matrixSources,
						nameSources, valueType);
				if (line.contains("<sampler")) {
					inSampler = true;
				}
				if (inSampler) {
					if (line.contains("<input")) {
						String semantic = line.split("semantic=\"")[1].split("\"")[0];
						String semanticSource = line.split("source=\"")[1].split("\"")[0].replace("#", "");
						if (semantic.equals("INPUT"))
							inputSourceName = semanticSource;
						if (semantic.equals("OUTPUT"))
							outputSourceName = semanticSource;
						if (semantic.equals("INTERPOLATION"))
							interpolationSourceName = semanticSource;
					}
				}
				if (line.contains("</sampler")) {
					System.out.println("samplerend " + inputSourceName + "; " + outputSourceName);
					if (!inputSourceName.isEmpty() && !outputSourceName.isEmpty()) {
						timestamps = sources.get(inputSourceName);
						poses = matrixSources.get(outputSourceName);

						for (String s : sources.keySet())
							System.out.println(s);
						System.out.println(
								"H " + timestamps + "; " + poses + "; " + sources.size() + "; " + poses.size());

						inputSourceName = "";
						outputSourceName = "";
					}

					inSampler = false;
				}
				if (line.contains("<channel")) {
					System.out.println("channel " + timestamps);
					String target = line.split("target=\"")[1].split("\"")[0];
					target = target.split("/")[0];

					for (int i = 0; i < timestamps.size(); i++) {
						float timestamp = timestamps.get(i).getf(0);
						BoneAnimationKeyframe3 keyframe = null;
						if (animation.getKeyframes().size() == 0
								|| animation.getKeyframes().get(0).getTimestamp() > timestamp
								|| animation.getKeyframes().get(animation.getKeyframes().size() - 1)
										.getTimestamp() < timestamp) {
							keyframe = new BoneAnimationKeyframe3(timestamp, joints.size());
							animation.addKeyframe(keyframe);
						} else {
							Pair<BoneAnimationKeyframe3, BoneAnimationKeyframe3> currentKeyframes = animation
									.getCurrentKeyframes(timestamp);
							// TODO: delta?
							System.out.println("timestamp: " + i + "; " + timestamp);
							if (currentKeyframes.getFirst().getTimestamp() == timestamp) {
								keyframe = currentKeyframes.getFirst();
							} else if (currentKeyframes.getSecond().getTimestamp() == timestamp) {
								keyframe = currentKeyframes.getSecond();
							} else {
								keyframe = new BoneAnimationKeyframe3(timestamp, joints.size());
								animation.addKeyframe(keyframe);
							}
						}
						System.out.println(jointNames + "; " + jointNames.size());
						targets.add(target);
						targetPose.put(target, (Matrix4f) poses.get(i));
						targetKeyframe.put(target, keyframe);
					}
				}
			}
			if (line.contains("</library_animations")) {
				inAnimations = false;
			}
		}
		reader.close();

		System.out.println("final jointcount: " + joints.size());
		for (BoneAnimationKeyframe3 keyframe : animation.getKeyframes()) {
			keyframe.initializeKeyframeData(joints.size());
		}
		for (String target : targets) {
			int jointID = jointNames.get(target);
			Matrix4f pose = targetPose.get(target);
			BoneAnimationKeyframe3 keyframe = targetKeyframe.get(target);
			keyframe.getTranslations()[jointID] = (Vector3f) pose.getTranslation();
			keyframe.getRotations()[jointID] = pose.getSubMatrix().toQuaternionf();
		}

		// fill empty keyframe slots
		for (int i = 0; i < joints.size(); i++) {
			BoneAnimationKeyframe3 lastkeyframeTrans = null;
			BoneAnimationKeyframe3 lastkeyframeRot = null;
			System.out.println("Keycount " + animation.getKeyframes().size());
			for (int j = 0; j < animation.getKeyframes().size(); j++) {
				BoneAnimationKeyframe3 keyframe = animation.getKeyframes().get(j);
				if (keyframe.getTranslations()[i] == null) {
					if (j == 0 || lastkeyframeTrans == null) {
						keyframe.getTranslations()[i] = getNextKeyframe(animation.getKeyframes(),
								keyframe.getTimestamp(), i).getTranslations()[i];
					} else if (j == animation.getKeyframes().size()) {
						keyframe.getTranslations()[i] = lastkeyframeTrans.getTranslations()[i];
					} else {
						BoneAnimationKeyframe3 nextkeyframe = getNextKeyframe(animation.getKeyframes(),
								keyframe.getTimestamp(), i);
						System.out.println(keyframe + "; " + lastkeyframeTrans + "; " + nextkeyframe);
						float factor = (keyframe.getTimestamp() - lastkeyframeTrans.getTimestamp())
								/ (nextkeyframe.getTimestamp() - lastkeyframeTrans.getTimestamp());
						Vector3f translation = new Vector3f(lastkeyframeTrans.getTranslations()[i]);
						translation.scale(factor);
						translation.translate(VecMath.scale(nextkeyframe.getTranslations()[i], 1 - factor));
						keyframe.getTranslations()[i] = translation;
					}
				} else {
					lastkeyframeTrans = keyframe;
				}
				if (keyframe.getRotations()[i] == null) {
					if (j == 0 || lastkeyframeRot == null) {
						keyframe.getRotations()[i] = getNextKeyframe(animation.getKeyframes(), keyframe.getTimestamp(),
								i).getRotations()[i];
					} else if (j == animation.getKeyframes().size()) {
						keyframe.getRotations()[i] = lastkeyframeRot.getRotations()[i];
					} else {
						BoneAnimationKeyframe3 nextkeyframe = getNextKeyframe(animation.getKeyframes(),
								keyframe.getTimestamp(), i);
						float factor = (keyframe.getTimestamp() - lastkeyframeRot.getTimestamp())
								/ (nextkeyframe.getTimestamp() - lastkeyframeRot.getTimestamp());
						Quaternionf rotation = QuatMath.slerp(lastkeyframeRot.getRotations()[i],
								nextkeyframe.getRotations()[i], factor);
						keyframe.getRotations()[i] = rotation;
					}
				} else {
					lastkeyframeRot = keyframe;
				}
				System.out.println("Bone " + i + "; Keyframe " + j);
				System.out.println(keyframe.getTranslations()[i]);
				System.out.println(keyframe.getRotations()[i]);
			}
		}

		animation.normalizeTimestamps();

		BoneAnimationSkeleton3 animationSkeleton = new BoneAnimationSkeleton3(new BoneAnimation3(), rootjoint,
				joints.size());
		animationSkeleton.getJointIndicesDataAttributes().data = jointIds;
		animationSkeleton.getJointWeightsDataAttributes().data = weights;

		for (int i = 0; i < jointIds.size(); i++) {
			Integer[] ids = jointIds.get(i);
			System.out.print(i + ": ");
			for (int j = 0; j < ids.length; j++) {
				System.out.print(ids[j] + "; ");
			}
			System.out.println("Weights: " + weights.get(i));
		}

		System.out.println("FINAL");
		System.out.println(animationSkeleton.getJointIndicesDataAttributes().isActive() + "; "
				+ animationSkeleton.getJointWeightsDataAttributes().isActive());
		System.out.println(animationSkeleton.getJointIndicesDataAttributes().data.size() + "; "
				+ animationSkeleton.getJointWeightsDataAttributes().data.size());
		for (Integer[] v : animationSkeleton.getJointIndicesDataAttributes().data) {
			for (Integer i : v)
				System.out.print(i + " ");
			System.out.println();
		}

		// TODO: update active?
		animationSkeleton.getJointIndicesDataAttributes().updateActive();
		animationSkeleton.getJointWeightsDataAttributes().updateActive();
		animationSkeleton.getShape().prerender();
		animationSkeleton.setAnimation(animation);

		return animationSkeleton;
	}

	private static BoneAnimationKeyframe3 getNextKeyframe(List<BoneAnimationKeyframe3> keyframes, float timestamp,
			int jointId) {
		for (int i = 0; i < keyframes.size(); i++) {
			BoneAnimationKeyframe3 keyframe = keyframes.get(i);
			if (keyframe.getTimestamp() > timestamp && keyframe.getTranslations()[jointId] != null) {
				return keyframe;
			}
		}
		return null;
	}

	private static boolean stringvalues;

	private static void parseSource(String line, List<Float> values, List<String> namevalues, List<Vector> vectorValues,
			List<Matrix> matrixValues, List<String> stringValues, HashMap<String, List<Vector>> sources,
			HashMap<String, List<Matrix>> matrixSources, HashMap<String, List<String>> nameSources,
			HashMap<String, Integer> valueType) {
		if (line.contains("<source")) {
			inSource = true;
			sourcename = line.split("id=\"")[1].split("\"")[0];
		}
		if (inSource) {
			if (line.contains("<float_array")) {
				for (String value : line.split(">")[1].split("<")[0].split(" ")) {
					values.add(Float.parseFloat(value));
				}
				stringvalues = false;
			}
			if (line.contains("<Name_array")) {
				for (String value : line.split(">")[1].split("<")[0].split(" ")) {
					namevalues.add(value);
				}
				stringvalues = true;
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
						if (stringvalues) {
							for (int i = 0; i < namevalues.size(); i++)
								stringValues.add(namevalues.get(i));
							namevalues.clear();
						} else {
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
									vectorValues
											.add(new Vector3f(values.get(i3), values.get(i3 + 1), values.get(i3 + 2)));
								}
								break;
							case 4:
								for (int i = 0; i < values.size() / 4; i++) {
									int i4 = i * 4;
									vectorValues.add(new Vector4f(values.get(i4), values.get(i4 + 1),
											values.get(i4 + 2), values.get(i4 + 3)));
								}
								break;
							case 16:
								System.out.println("BINGO! " + values.size() + "; " + values.size() / 16 + "; "
										+ values.size() / 16f);
								for (Float f : values)
									System.out.print(f + " ");
								System.out.println();
								for (int i = 0; i < values.size() / 16; i++) {
									int i16 = i * 16;
									matrixValues.add(new Matrix4f(values.get(i16), values.get(i16 + 1),
											values.get(i16 + 2), values.get(i16 + 3), values.get(i16 + 4),
											values.get(i16 + 5), values.get(i16 + 6), values.get(i16 + 7),
											values.get(i16 + 8), values.get(i16 + 9), values.get(i16 + 10),
											values.get(i16 + 11), values.get(i16 + 12), values.get(i16 + 13),
											values.get(i16 + 14), values.get(i16 + 15)));
									// TODO: transpose???
								}
								break;
							default:
								System.err.println("Stride count has to be between 1 and 4 or 16 for matrices.");
							}
							values.clear();
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
				if (vectorValues.size() > 0) {
					sources.put(sourcename, new ArrayList<Vector>(vectorValues));
					vectorValues.clear();
					valueType.put(sourcename, new Integer(stride)); // TODO:
																	// check
				} else if (matrixValues.size() > 0) {
					System.out.println("matrixsource " + sourcename + "; " + matrixValues + "; " + matrixValues.size());
					matrixSources.put(sourcename, new ArrayList<Matrix>(matrixValues));
					matrixValues.clear();
				} else {
					nameSources.put(sourcename, new ArrayList<String>(stringValues));
					stringValues.clear();
				}
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

	public static BoneAnimationSkeleton3 loadAnimation(File f) throws IOException {
		BoneAnimationSkeleton3 object;

		if (f.getName().endsWith(".dae")) {
			object = loadAnimationDAE(f);
		} else {
			System.err.println("File extension not recognized. (Use *.dae)");
			return null;
		}

		return object;
	}
}