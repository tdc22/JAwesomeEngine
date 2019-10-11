package objects;

import gui.Color;
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

	public ShapedObject2(ShapedObject2 shape) {
		super(new Vector2f(shape.getRotationCenter()), new Vector2f(shape.getTranslation()),
				new Complexf(shape.getRotation()), new Vector2f(shape.getScale()));
		init();
		setRenderMode(shape.getRenderMode());
		copyShapeData(shape);
	}

	private void init() {
		vertices = new ObjectDataAttributesVectorf<Vector2f>(VERTEX_POSITION, 4, new float[] { 0, 1 }, true);
		normals = new ObjectDataAttributesVectorf<Vector2f>(NORMAL_POSITION, 4, new float[] { 0, 0 }, true);

		dataattributes.add(vertices);
		dataattributes.add(normals);

		rendermode = GLConstants.TRIANGLE_ADJACENCY;
	}

	public void computeNormals() {
		// int indicesnumber = indices.size();
		int vertexnumber = vertices.data.size();
		for (int n = 0; n < vertexnumber; n++) {
			if (n < normals.data.size())
				normals.data.set(n, new Vector2f(0, 0));
			else
				normals.data.add(new Vector2f(0, 0));
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
		buf.put(translation.getXf() + real * rotationcenter.x + imaginary * rotationcenter.y);
		buf.put(translation.getYf() - imaginary * rotationcenter.x + real * rotationcenter.y);
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
		float real = getRotation().getRealf();
		float imaginary = getRotation().getImaginaryf();
		return new Matrix4f(real * scale.x, -imaginary * scale.x, 0, 0, imaginary * scale.y, real * scale.y, 0, 0, 0, 0,
				0, 0, translation.getXf() + real * rotationcenter.x + imaginary * rotationcenter.y,
				translation.getYf() - imaginary * rotationcenter.x + real * rotationcenter.y, 0, 1);
	}

	public void addVertex(Vector2f vertex) {
		addVertex(vertex, Color.WHITE, vec2, vec2);
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
		setVertex(id, vertex, Color.WHITE, vec2, vec2);
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