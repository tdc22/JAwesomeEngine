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

	public static HashMap<Integer, Integer> vertexIndexMap;

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

		vertexIndexMap = new HashMap<Integer, Integer>();

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

							if (a == vertexPos) {
								vertexIndexMap.put(vertices.size(), val);
								vertices.add((Vector3f) vertexSource.get(val));
							} else if (a == normalPos)
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

	public static HashMap<Integer, Integer> getLastVertexIndexMap() {
		return vertexIndexMap;
	}

	public static BoneAnimationSkeleton3 loadAnimationDAE(File f, ShapedObject3 skin,
			HashMap<Integer, Integer> vertexIndexMapping) throws IOException {
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
		List<Integer> jointCount = new ArrayList<Integer>();
		HashMap<String, Integer> jointNames = new HashMap<String, Integer>();
		LinkedList<Integer> nodestack = new LinkedList<Integer>();
		List<Vector> timestamps = null;
		List<Matrix> poses = null;
		List<String> targets = new ArrayList<String>();
		HashMap<String, List<Matrix4f>> targetPose = new HashMap<String, List<Matrix4f>>();
		HashMap<String, List<BoneAnimationKeyframe3>> targetKeyframe = new HashMap<String, List<BoneAnimationKeyframe3>>();

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
					if (!jointJointSourceName.isEmpty() && !invBindMatrixSourceName.isEmpty()) {
						List<String> allJointNames = nameSources.get(jointJointSourceName);
						List<Matrix> invBindMatrices = matrixSources.get(invBindMatrixSourceName);
						for (int i = 0; i < invBindMatrices.size(); i++) {
							Matrix4f localBindTransform = new Matrix4f();
							joints.add(new BoneJoint(i, localBindTransform));

							jointNames.put(allJointNames.get(i), i);
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

							float totalWeight = 0;
							if (jointcount > 4) {
								Integer[] newJointIds = new Integer[4];
								Float[] newWeights = new Float[4];
								HashSet<Integer> addedWeights = new HashSet<Integer>();
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

							if (totalWeight != 1.0f && totalWeight > 0) {
								for (int i = 0; i < currWeights.length; i++) {
									currWeights[i] = currWeights[i] / totalWeight;
								}
							}

							// SORT WEIGHTS BY VALUE
							// TODO: remove (?) probably just relevant for
							// debugging
							int finalNumberOfJoints = Math.min(jointcount, 4);
							for (int i = 0; i < finalNumberOfJoints; i++) {
								for (int j = i + 1; j < finalNumberOfJoints; j++) {
									if (currWeights[j] > currWeights[i]) {
										float w = currWeights[j];
										currWeights[j] = currWeights[i];
										currWeights[i] = w;
										int jid = currJoints[j];
										currJoints[j] = currJoints[i];
										currJoints[i] = jid;
									}
								}
							}
							// END SORT

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
				if (line.contains("<matrix")) {
					String[] entries = line.split("<matrix")[1].split(">")[1].split("<")[0].split(" ");
					Matrix4f nodematrix = new Matrix4f();
					for (int i = 0; i < 16; i++) {
						nodematrix.set(i % 4, i / 4, Float.parseFloat(entries[i]));
					}
					joints.get(nodestack.peek()).setLocalBindTransform(nodematrix);
				}
				if (line.contains("</node")) {
					nodestack.pop();
				}
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
						// if (semantic.equals("INTERPOLATION"))
						// interpolationSourceName = semanticSource;
					}
				}
				if (line.contains("</sampler")) {
					if (!inputSourceName.isEmpty() && !outputSourceName.isEmpty()) {
						timestamps = sources.get(inputSourceName);
						poses = matrixSources.get(outputSourceName);

						inputSourceName = "";
						outputSourceName = "";
					}

					inSampler = false;
				}
				if (line.contains("<channel")) {
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
							if (currentKeyframes.getFirst().getTimestamp() == timestamp) {
								keyframe = currentKeyframes.getFirst();
							} else if (currentKeyframes.getSecond().getTimestamp() == timestamp) {
								keyframe = currentKeyframes.getSecond();
							} else {
								keyframe = new BoneAnimationKeyframe3(timestamp, joints.size());
								animation.addKeyframe(keyframe);
							}
						}
						if (!targets.contains(target)) {
							targets.add(target);
							targetPose.put(target, new ArrayList<Matrix4f>());
							targetKeyframe.put(target, new ArrayList<BoneAnimationKeyframe3>());
						}
						targetPose.get(target).add((Matrix4f) poses.get(i));
						targetKeyframe.get(target).add(keyframe);
					}
				}
			}
			if (line.contains("</library_animations")) {
				inAnimations = false;
			}
		}
		reader.close();

		for (BoneAnimationKeyframe3 keyframe : animation.getKeyframes()) {
			keyframe.initializeKeyframeData(joints.size());
		}
		for (String target : targets) { // TODO: same targets multiple times in
										// list!!! (done?)
			List<Matrix4f> targetPoses = targetPose.get(target);
			List<BoneAnimationKeyframe3> targetKeyframes = targetKeyframe.get(target);
			for (int i = 0; i < targetPoses.size(); i++) {
				int jointID = jointNames.get(target);
				Matrix4f pose = targetPoses.get(i);
				BoneAnimationKeyframe3 keyframe = targetKeyframes.get(i);
				keyframe.getTranslations()[jointID] = (Vector3f) pose.getTranslation();
				keyframe.getRotations()[jointID] = pose.getSubMatrix().toQuaternionf();
			}
		}

		// fill empty keyframe slots
		for (int i = 0; i < joints.size(); i++) {
			BoneAnimationKeyframe3 lastkeyframeTrans = null;
			BoneAnimationKeyframe3 lastkeyframeRot = null;
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
			}
		}

		animation.normalizeTimestamps();

		// map jointIDs to vertices
		// HashMap<Vector3f, Integer> skinVertices = new HashMap<Vector3f,
		// Integer>();
		// List<Integer[]> vertexJointIDs = new ArrayList<Integer[]>();
		// List<Vector4f> vertexWeights = new ArrayList<Vector4f>();
		// int currIndex = 0;
		// for (Vector3f v : skin.getVertices()) {
		// Integer index = skinVertices.get(v);
		// if (index != null) {
		// vertexJointIDs.add(jointIds.get(index));
		// vertexWeights.add(weights.get(index));
		// } else {
		// skinVertices.put(v, currIndex);
		// vertexJointIDs.add(jointIds.get(currIndex));
		// vertexWeights.add(weights.get(currIndex));
		// currIndex++;
		// }
		// }

		// map using vertexIndexMap
		List<Integer[]> finalJointIDs = new ArrayList<Integer[]>();
		List<Vector4f> finalWeights = new ArrayList<Vector4f>();
		for (Integer i : vertexIndexMapping.keySet()) {
			int mapvalue = vertexIndexMapping.get(i);
			finalJointIDs.add(jointIds.get(mapvalue));
			finalWeights.add(weights.get(mapvalue));
		}

		BoneAnimationSkeleton3 animationSkeleton = new BoneAnimationSkeleton3(new BoneAnimation3(), skin, rootjoint,
				joints.size());
		animationSkeleton.getJointIndicesDataAttributes().data = finalJointIDs;
		animationSkeleton.getJointWeightsDataAttributes().data = finalWeights;

		// TODO: update active?
		// animationSkeleton.getJointIndicesDataAttributes().updateActive();
		// animationSkeleton.getJointWeightsDataAttributes().updateActive();
		animationSkeleton.getShape().prerender();
		animationSkeleton.getShape().updateRenderHints();
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
								for (int i = 0; i < values.size() / 16; i++) {
									int i16 = i * 16;
									Matrix4f m = new Matrix4f(values.get(i16), values.get(i16 + 1), values.get(i16 + 2),
											values.get(i16 + 3), values.get(i16 + 4), values.get(i16 + 5),
											values.get(i16 + 6), values.get(i16 + 7), values.get(i16 + 8),
											values.get(i16 + 9), values.get(i16 + 10), values.get(i16 + 11),
											values.get(i16 + 12), values.get(i16 + 13), values.get(i16 + 14),
											values.get(i16 + 15));
									m.transpose();
									matrixValues.add(m);
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
					valueType.put(sourcename, Integer.valueOf(stride)); // TODO:
																	// check
				} else if (matrixValues.size() > 0) {
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

	public static BoneAnimationSkeleton3 loadAnimation(File f, ShapedObject3 skin,
			HashMap<Integer, Integer> vertexIndexMapping) throws IOException {
		BoneAnimationSkeleton3 object;

		if (f.getName().endsWith(".dae")) {
			object = loadAnimationDAE(f, skin, vertexIndexMapping);
		} else {
			System.err.println("File extension not recognized. (Use *.dae)");
			return null;
		}
		object.updateAnimation();

		return object;
	}
}