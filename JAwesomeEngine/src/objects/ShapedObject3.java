package objects;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import math.VecMath;
import matrix.Matrix4f;
import quaternion.Quaternionf;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class ShapedObject3 extends ShapedObject<Vector3f, Quaternionf> implements InstancedBaseObject3 {

	public ShapedObject3() {
		super(new Vector3f(), new Vector3f(), new Quaternionf(), new Vector3f(1, 1, 1));
		init();
	}

	public ShapedObject3(float x, float y, float z) {
		super(new Vector3f(), new Vector3f(x, y, z), new Quaternionf(), new Vector3f(1, 1, 1));
		init();
	}

	public ShapedObject3(Vector3f pos) {
		super(new Vector3f(), new Vector3f(pos), new Quaternionf(), new Vector3f(1, 1, 1));
		init();
	}

	private void init() {
		vertices = new ArrayList<Vector3f>();
		normals = new ArrayList<Vector3f>();

		rendermode = GLConstants.TRIANGLE_ADJACENCY;
	}

	public void addQuad(int index1, int adjacency1, int index2, int adjacency2, int index3, int adjacency3, int index4,
			int adjacency4) {
		addTriangle(index1, adjacency1, index2, adjacency2, index3, index4);
		addTriangle(index1, index2, index3, adjacency3, index4, adjacency4);
	}

	public void addTriangle(int index1, int adjacency1, int index2, int adjacency2, int index3, int adjacency3) {
		addIndex(index1);
		addIndex(adjacency1);
		addIndex(index2);
		addIndex(adjacency2);
		addIndex(index3);
		addIndex(adjacency3);
	}

	public void computeNormals() {
		int indicesnumber = indices.size();
		int vertexnumber = vertices.size();
		for (int n = 0; n < vertexnumber; n++) {
			if (n < normals.size())
				normals.set(n, new Vector3f(0, 0, 0));
			else
				normals.add(new Vector3f(0, 0, 0));
		}
		int ci = 0;
		for (int i = 0; i < indicesnumber / 3; i++) {
			int index1 = indices.get(ci);
			int index2 = indices.get(ci + 1);
			int index3 = indices.get(ci + 2);
			Vector3f normal1 = getNormal(index1);
			Vector3f normal2 = getNormal(index2);
			Vector3f normal3 = getNormal(index3);
			Vector3f newnormal = VecMath.computeNormal(getVertex(index1), getVertex(index2), getVertex(index3));
			setNormal(index1, VecMath.normalize(VecMath.addition(normal1, newnormal)));
			setNormal(index2, VecMath.normalize(VecMath.addition(normal2, newnormal)));
			setNormal(index3, VecMath.normalize(VecMath.addition(normal3, newnormal)));
			ci += 3;
		}
	}

	public void updateBuffer() {
		float q0 = rotation.getQ0f();
		float q1 = rotation.getQ1f();
		float q2 = rotation.getQ2f();
		float q3 = rotation.getQ3f();
		buf.put((1 - 2 * q2 * q2 - 2 * q3 * q3) * scale.x);
		buf.put((2 * q1 * q2 + 2 * q3 * q0) * scale.x);
		buf.put((2 * q1 * q3 - 2 * q2 * q0) * scale.x);
		buf.put(0);
		buf.put((2 * q1 * q2 - 2 * q3 * q0) * scale.y);
		buf.put((1 - 2 * q1 * q1 - 2 * q3 * q3) * scale.y);
		buf.put((2 * q2 * q3 + 2 * q1 * q0) * scale.y);
		buf.put(0);
		buf.put((2 * q1 * q3 + 2 * q2 * q0) * scale.z);
		buf.put((2 * q2 * q3 - 2 * q1 * q0) * scale.z);
		buf.put((1 - 2 * q1 * q1 - 2 * q2 * q2) * scale.z);
		buf.put(0);
		buf.put(translation.getXf());
		buf.put(translation.getYf());
		buf.put(translation.getZf());
		buf.put(1);
		buf.flip();
	}

	public void translate(float transx, float transy, float transz) {
		translation.translate(transx, transy, transz);
		updateBuffer();
	}

	public void translateTo(float x, float y, float z) {
		translation.set(x, y, z);
		updateBuffer();
	}

	public void translateTo(Vector3f pos) {
		translation.set(pos);
		updateBuffer();
	}

	@Override
	public void translate(Vector3f translate) {
		translation.translate(translate);
		updateBuffer();
	}

	public void rotateTo(float rotX, float rotY, float rotZ) {
		resetRotation();
		rotation.rotate(rotZ, new Vector3f(0.0f, 0.0f, 1.0f));
		rotation.rotate(rotY, new Vector3f(0.0f, 1.0f, 0.0f));
		rotation.rotate(rotX, new Vector3f(1.0f, 0.0f, 0.0f));
		updateBuffer();
	}

	@Override
	public void rotate(Quaternionf rotate) {
		rotation.rotate(rotate);
		updateBuffer();
	}

	@Override
	public void scale(Vector3f scale) {
		scale.scale(scale);
		updateBuffer();
	}

	@Override
	public Matrix4f getMatrix() {
		float[][] mat = rotation.toMatrixf().getArrayf();
		return new Matrix4f(mat[0][0] * scale.x, mat[0][1] * scale.x, mat[0][2] * scale.x, 0, mat[1][0] * scale.y,
				mat[1][1] * scale.y, mat[1][2] * scale.y, 0, mat[2][0] * scale.z, mat[2][1] * scale.z,
				mat[2][2] * scale.z, 0, translation.getXf(), translation.getYf(), translation.getZf(), 1);
	}

	public void addVertex(Vector3f vertex) {
		addVertex(vertex, Color.GRAY, new Vector2f(), new Vector3f());
	}

	public void addVertex(Vector3f vertex, Color c) {
		addVertex(vertex, c, new Vector2f(), new Vector3f());
	}

	public void addVertex(Vector3f vertex, Color c, Vector2f texturecoord) {
		addVertex(vertex, c, texturecoord, new Vector3f());
	}

	public void addVertex(Vector3f vertex, Vector3f c) {
		addVertex(vertex, c, new Vector2f(), new Vector3f());
	}

	public void addVertex(Vector3f vertex, Vector3f c, Vector2f texturecoord) {
		addVertex(vertex, c, texturecoord, new Vector3f());
	}

	public void setVertex(int id, Vector3f vertex) {
		setVertex(id, vertex, Color.GRAY, new Vector2f(), new Vector3f());
	}

	public void setVertex(int id, Vector3f vertex, Color c) {
		setVertex(id, vertex, c, new Vector2f(), new Vector3f());
	}

	public void setVertex(int id, Vector3f vertex, Color c, Vector2f texturecoord) {
		setVertex(id, vertex, c, texturecoord, new Vector3f());
	}

	public void setVertex(int id, Vector3f vertex, Vector3f c) {
		setVertex(id, vertex, c, new Vector2f(), new Vector3f());
	}

	public void setVertex(int id, Vector3f vertex, Vector3f c, Vector2f texturecoord) {
		setVertex(id, vertex, c, texturecoord, new Vector3f());
	}

	@Override
	protected void fillBuffers(int allVertices, IntBuffer indexData, FloatBuffer vertexData, FloatBuffer colorData,
			FloatBuffer textureData, FloatBuffer normalData) {
		for (int v = 0; v < allVertices; v++) {
			Vector3f vertex = vertices.get(v);
			vertexData.put(new float[] { vertex.x, vertex.y, vertex.z, 1 });
			Vector3f vertcolor = colors.get(v);
			colorData.put(new float[] { vertcolor.x, vertcolor.y, vertcolor.z });
			Vector2f tex = texturecoords.get(v);
			textureData.put(new float[] { tex.x, tex.y });
			Vector3f normal = normals.get(v);
			normalData.put(new float[] { normal.x, normal.y, normal.z, 0 });
		}
	}

	@Override
	public void rotate(float rotX, float rotY, float rotZ) {
		rotation.rotate(rotZ, new Vector3f(0.0f, 0.0f, 1.0f));
		rotation.rotate(rotY, new Vector3f(0.0f, 1.0f, 0.0f));
		rotation.rotate(rotX, new Vector3f(1.0f, 0.0f, 0.0f));
		updateBuffer();
	}

	@Override
	public void scale(float scaleX, float scaleY, float scaleZ) {
		scale.scale(scaleX, scaleY, scaleZ);
		updateBuffer();
	}

	@Override
	public void scaleTo(float scaleX, float scaleY, float scaleZ) {
		scale.set(scaleX, scaleY, scaleZ);
		updateBuffer();
	}

	@Override
	public void rotateTo(Quaternionf rotate) {
		resetRotation();
		rotation.rotate(rotate);
		updateBuffer();
	}

	@Override
	public void scaleTo(Vector3f scale) {
		this.scale.set(scale);
		updateBuffer();
	}

	@Override
	public void scale(float scale) {
		this.scale.scale(scale, scale, scale);
		updateBuffer();
	}

	@Override
	public void scaleTo(float scale) {
		this.scale.set(scale, scale, scale);
		updateBuffer();
	}
}