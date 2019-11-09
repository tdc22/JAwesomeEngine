package objects;

import gui.Color;
import math.VecMath;
import matrix.Matrix4f;
import quaternion.Quaternionf;
import utils.GLConstants;
import utils.VectorConstants;
import vector.Vector2f;
import vector.Vector3f;

public class ShapedObject3 extends ShapedObject<Vector3f, Quaternionf> implements InstancedBaseObject3 {
	final Vector2f vec2 = new Vector2f();
	final Vector3f vec3 = new Vector3f();

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

	public ShapedObject3(ShapedObject3 shape) {
		super(new Vector3f(shape.getRotationCenter()), new Vector3f(shape.getTranslation()),
				new Quaternionf(shape.getRotation()), new Vector3f(shape.getScale()));
		init();
		setRenderMode(shape.getRenderMode());
		setRenderHints(shape.colors.isActive(), shape.texturecoords.isActive(), shape.normals.isActive());
		copyShapeData(shape);
	}

	private void init() {
		vertices = new ObjectDataAttributesVectorf<Vector3f>(VERTEX_POSITION, 4, new float[] { 1 }, true);
		normals = new ObjectDataAttributesVectorf<Vector3f>(NORMAL_POSITION, 4, new float[] { 0 }, true);

		dataattributes.add(vertices);
		dataattributes.add(normals);

		rendermode = GLConstants.TRIANGLE_ADJACENCY;
	}

	public void computeNormals() {
		int indicesnumber = indices.data.size();
		int vertexnumber = vertices.data.size();
		for (int n = 0; n < vertexnumber; n++) {
			if (n < normals.data.size())
				normals.data.set(n, new Vector3f(0, 0, 0));
			else
				normals.data.add(new Vector3f(0, 0, 0));
		}
		int ci = 0;
		for (int i = 0; i < indicesnumber / 3; i++) {
			int index1 = indices.data.get(ci);
			int index2 = indices.data.get(ci + 1);
			int index3 = indices.data.get(ci + 2);
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
		float q10 = 2 * q1 * q0;
		float q20 = 2 * q2 * q0;
		float q30 = 2 * q3 * q0;
		float q11 = 2 * q1 * q1;
		float q12 = 2 * q1 * q2;
		float q13 = 2 * q1 * q3;
		float q22 = 2 * q2 * q2;
		float q23 = 2 * q2 * q3;
		float q33 = 2 * q3 * q3;
		float rot00 = 1 - q22 - q33;
		float rot01 = q12 + q30;
		float rot02 = q13 - q20;
		float rot10 = q12 - q30;
		float rot11 = 1 - q11 - q33;
		float rot12 = q23 + q10;
		float rot20 = q13 + q20;
		float rot21 = q23 - q10;
		float rot22 = 1 - q11 - q22;
		buf.put(rot00 * scale.x);
		buf.put(rot01 * scale.x);
		buf.put(rot02 * scale.x);
		buf.put(0);
		buf.put(rot10 * scale.y);
		buf.put(rot11 * scale.y);
		buf.put(rot12 * scale.y);
		buf.put(0);
		buf.put(rot20 * scale.z);
		buf.put(rot21 * scale.z);
		buf.put(rot22 * scale.z);
		buf.put(0);
		buf.put(translation.getXf() + rot00 * rotationcenter.x + rot10 * rotationcenter.y + rot20 * rotationcenter.z);
		buf.put(translation.getYf() + rot01 * rotationcenter.x + rot11 * rotationcenter.y + rot21 * rotationcenter.z);
		buf.put(translation.getZf() + rot02 * rotationcenter.x + rot12 * rotationcenter.y + rot22 * rotationcenter.z);
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
		rotation.rotate(rotZ, VectorConstants.AXIS_Z);
		rotation.rotate(rotY, VectorConstants.AXIS_Y);
		rotation.rotate(rotX, VectorConstants.AXIS_X);
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
		addVertex(vertex, Color.WHITE, vec2, vec3);
	}

	public void addVertex(Vector3f vertex, Color c) {
		addVertex(vertex, c, vec2, vec3);
	}

	public void addVertex(Vector3f vertex, Color c, Vector2f texturecoord) {
		addVertex(vertex, c, texturecoord, vec3);
	}

	public void addVertex(Vector3f vertex, Vector3f c) {
		addVertex(vertex, c, vec2, vec3);
	}

	public void addVertex(Vector3f vertex, Vector3f c, Vector2f texturecoord) {
		addVertex(vertex, c, texturecoord, vec3);
	}

	public void setVertex(int id, Vector3f vertex) {
		setVertex(id, vertex, Color.WHITE, vec2, vec3);
	}

	public void setVertex(int id, Vector3f vertex, Color c) {
		setVertex(id, vertex, c, vec2, vec3);
	}

	public void setVertex(int id, Vector3f vertex, Color c, Vector2f texturecoord) {
		setVertex(id, vertex, c, texturecoord, vec3);
	}

	public void setVertex(int id, Vector3f vertex, Vector3f c) {
		setVertex(id, vertex, c, vec2, vec3);
	}

	public void setVertex(int id, Vector3f vertex, Vector3f c, Vector2f texturecoord) {
		setVertex(id, vertex, c, texturecoord, vec3);
	}

	@Override
	public void rotate(float rotX, float rotY, float rotZ) {
		rotation.rotate(rotZ, VectorConstants.AXIS_Z);
		rotation.rotate(rotY, VectorConstants.AXIS_Y);
		rotation.rotate(rotX, VectorConstants.AXIS_X);
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

	@Override
	public void setRotationCenter(float x, float y, float z) {
		rotationcenter.set(x, y, z);
		updateBuffer();
	}
}