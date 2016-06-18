package objects;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import matrix.Matrix4f;
import quaternion.Complexf;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class ShapedObject2 extends ShapedObject<Vector2f, Complexf> implements InstancedBaseObject2 {
	final Vector2f vec2 = new Vector2f();

	public ShapedObject2() {
		super(new Vector2f(), new Vector2f(), new Complexf(), new Vector2f(1, 1));
		init();
	}

	public ShapedObject2(float x, float y) {
		super(new Vector2f(), new Vector2f(x, y), new Complexf(), new Vector2f(1, 1));
		init();
	}

	public ShapedObject2(Vector2f pos) {
		super(new Vector2f(), new Vector2f(pos), new Complexf(), new Vector2f(1, 1));
		init();
	}

	private void init() {
		vertices = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector2f>();

		rendermode = GLConstants.TRIANGLE_ADJACENCY;
	}

	public void computeNormals() {
		// int indicesnumber = indices.size();
		int vertexnumber = vertices.size();
		for (int n = 0; n < vertexnumber; n++) {
			if (n < normals.size())
				normals.set(n, new Vector2f(0, 0));
			else
				normals.add(new Vector2f(0, 0));
		}
		// int ci = 0;
		// for (int i = 0; i < indicesnumber / 3; i++) {
		// // TODO
		// // int index1 = indices.get(ci);
		// // int index2 = indices.get(ci + 1);
		// // int index3 = indices.get(ci + 2);
		// // Vector2f normal1 = getNormal(index1);
		// // Vector2f normal2 = getNormal(index2);
		// // Vector2f normal3 = getNormal(index3);
		// // Vector2f newnormal = VecMath.computeNormal(getVertex(index1),
		// // getVertex(index2), getVertex(index3));
		// // setNormal(index1, VecMath.normalize(VecMath.addition(normal1,
		// // newnormal)));
		// // setNormal(index2, VecMath.normalize(VecMath.addition(normal2,
		// // newnormal)));
		// // setNormal(index3, VecMath.normalize(VecMath.addition(normal3,
		// // newnormal)));
		// ci += 3;
		// }
	}

	public void updateBuffer() {
		float real = getRotation().getRealf();
		float imaginary = getRotation().getImaginaryf();
		buf.put(real * scale.x);
		buf.put(-imaginary * scale.x);
		buf.put(0);
		buf.put(0);
		buf.put(imaginary * scale.y);
		buf.put(real * scale.y);
		buf.put(0);
		buf.put(0);
		buf.put(0);
		buf.put(0);
		buf.put(0);
		buf.put(0);
		buf.put(translation.getXf());
		buf.put(translation.getYf());
		buf.put(0);
		buf.put(1);
		buf.flip();
	}

	public void translate(float transx, float transy) {
		translation.translate(transx, transy);
		updateBuffer();
	}

	public void translateTo(float x, float y) {
		translation.set(x, y);
		updateBuffer();
	}

	public void translateTo(Vector2f pos) {
		translation.set(pos);
		updateBuffer();
	}

	@Override
	public void translate(Vector2f translate) {
		translation.translate(translate);
		updateBuffer();
	}

	public void rotateTo(float rotX) {
		resetRotation();
		rotation.rotate(rotX);
		updateBuffer();
	}

	@Override
	public void rotate(Complexf rotate) {
		rotation.rotate(rotate);
		updateBuffer();
	}

	@Override
	public void scale(Vector2f scale) {
		scale.scale(scale);
		updateBuffer();
	}

	@Override
	public Matrix4f getMatrix() {
		float[][] mat = rotation.toMatrixf().getArrayf();
		return new Matrix4f(mat[0][0] * scale.x, mat[0][1] * scale.x, 0, 0, mat[1][0] * scale.y, mat[1][1] * scale.y, 0,
				0, 0, 0, 0, 0, translation.getXf(), translation.getYf(), 0, 1);
	}

	public void addVertex(Vector2f vertex) {
		addVertex(vertex, Color.GRAY, vec2, vec2);
	}

	public void addVertex(Vector2f vertex, Color c) {
		addVertex(vertex, c, vec2, vec2);
	}

	public void addVertex(Vector2f vertex, Color c, Vector2f texturecoord) {
		addVertex(vertex, c, texturecoord, vec2);
	}

	public void addVertex(Vector2f vertex, Vector3f c) {
		addVertex(vertex, c, vec2, vec2);
	}

	public void addVertex(Vector2f vertex, Vector3f c, Vector2f texturecoord) {
		addVertex(vertex, c, texturecoord, vec2);
	}

	public void setVertex(int id, Vector2f vertex) {
		setVertex(id, vertex, Color.GRAY, vec2, vec2);
	}

	public void setVertex(int id, Vector2f vertex, Color c) {
		setVertex(id, vertex, c, vec2, vec2);
	}

	public void setVertex(int id, Vector2f vertex, Color c, Vector2f texturecoord) {
		setVertex(id, vertex, c, texturecoord, vec2);
	}

	public void setVertex(int id, Vector2f vertex, Vector3f c) {
		setVertex(id, vertex, c, vec2, vec2);
	}

	public void setVertex(int id, Vector2f vertex, Vector3f c, Vector2f texturecoord) {
		setVertex(id, vertex, c, texturecoord, vec2);
	}

	@Override
	protected void fillBuffers(int allVertices, IntBuffer indexData, FloatBuffer vertexData, FloatBuffer colorData,
			FloatBuffer textureData, FloatBuffer normalData) {
		for (int v = 0; v < allVertices; v++) {
			Vector2f vertex = vertices.get(v);
			vertexData.put(new float[] { vertex.x, vertex.y, 0, 1 });
			Vector3f vertcolor = colors.get(v);
			colorData.put(new float[] { vertcolor.x, vertcolor.y, vertcolor.z });
			Vector2f tex = texturecoords.get(v);
			textureData.put(new float[] { tex.x, tex.y });
			Vector2f normal = normals.get(v);
			normalData.put(new float[] { normal.x, normal.y, 0, 0 });
		}
	}

	@Override
	public void rotate(float rotX) {
		rotation.rotate(rotX);
		updateBuffer();
	}

	@Override
	public void scale(float scaleX, float scaleY) {
		scale.scale(scaleX, scaleY);
		updateBuffer();
	}

	@Override
	public void scaleTo(float scaleX, float scaleY) {
		scale.set(scaleX, scaleY);
		updateBuffer();
	}

	@Override
	public void rotateTo(Complexf rotate) {
		resetRotation();
		rotation.rotate(rotate);
		updateBuffer();
	}

	@Override
	public void scaleTo(Vector2f scale) {
		this.scale.set(scale);
		updateBuffer();
	}

	@Override
	public void scale(float scale) {
		this.scale.scale(scale, scale);
		updateBuffer();
	}

	@Override
	public void scaleTo(float scale) {
		this.scale.set(scale, scale);
		updateBuffer();
	}
}