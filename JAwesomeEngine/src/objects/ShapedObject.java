package objects;

import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.util.ArrayList;
import java.util.List;

import gui.Color;
import quaternion.Rotation;
import utils.GLConstants;
import vector.Vector;
import vector.Vector2f;
import vector.Vector3f;

public abstract class ShapedObject<L extends Vector, A extends Rotation> extends RenderedObject<L, A> {
	protected static final int VERTEX_POSITION = 0;
	protected static final int COLOR_POSITION = 1;
	protected static final int TEXTURE_POSITION = 2;
	protected static final int NORMAL_POSITION = 3;

	protected int vaoHandle;

	protected ObjectDataAttributesInteger indices;
	protected ObjectDataAttributesVectorf<L> vertices, normals;
	protected ObjectDataAttributesVectorf<Vector3f> colors;
	protected ObjectDataAttributesVectorf<Vector2f> texturecoords;

	protected List<ObjectDataAttributes<?, ?>> dataattributes;

	protected int rendermode;
	protected int renderedIndexCount = 0;

	boolean render = true;

	public ShapedObject(L rotcenter, L translation, A rotation, L scale) {
		super(rotcenter, translation, rotation, scale);
		init();
	}

	private void init() {
		indices = new ObjectDataAttributesInteger(-1, 0, true, true);
		colors = new ObjectDataAttributesVectorf<Vector3f>(COLOR_POSITION, 3, new float[] {}, true);
		texturecoords = new ObjectDataAttributesVectorf<Vector2f>(TEXTURE_POSITION, 2, new float[] {}, false);

		dataattributes = new ArrayList<ObjectDataAttributes<?, ?>>();
		dataattributes.add(colors);
		dataattributes.add(texturecoords);
	}

	public void copyShapeData(ShapedObject<L, A> original) {
		indices.data.addAll(original.getIndexDataAttribute().data);
		for (ObjectDataAttributes<?, ?> dataattribute : original.getDataAttributes()) {
			if (dataattribute.equals(original.getVertexDataAttribute())) {
				vertices.data.addAll(original.getVertexDataAttribute().data);
			} else if (dataattribute.equals(original.getNormalDataAttribute())) {
				normals.data.addAll(original.getNormalDataAttribute().data);
			} else if (dataattribute.equals(original.getColorDataAttribute())) {
				colors.data.addAll(original.getColorDataAttribute().data);
			} else if (dataattribute.equals(original.getTextureCoordinateDataAttribute())) {
				texturecoords.data.addAll(original.getTextureCoordinateDataAttribute().data);
			} else {
				dataattributes.add(dataattribute);
			}
		}
		prerender();
	}

	// public void copyDirect(ShapedObject original) {
	// vaoHandle = original.getVAOHandle();
	// vboIndexHandle = original.getVBOIndexHandle();
	// vboVertexHandle = original.getVBOVertexHandle();
	// vboColorHandle = original.getVBOColorHandle();
	// vboTextureCoordHandle = original.getVBOTextureCoordinateHandle();
	// vboNormalHandle = original.getVBONormalHandle();
	// }

	public List<ObjectDataAttributes<?, ?>> getDataAttributes() {
		return dataattributes;
	}

	public ObjectDataAttributesInteger getIndexDataAttribute() {
		return indices;
	}

	public ObjectDataAttributesVectorf<L> getVertexDataAttribute() {
		return vertices;
	}

	public ObjectDataAttributesVectorf<L> getNormalDataAttribute() {
		return normals;
	}

	public ObjectDataAttributesVectorf<Vector3f> getColorDataAttribute() {
		return colors;
	}

	public ObjectDataAttributesVectorf<Vector2f> getTextureCoordinateDataAttribute() {
		return texturecoords;
	}

	public void addDataAttribute(ObjectDataAttributes<?, ?> dataattribute) {
		dataattributes.add(dataattribute);
	}

	public int getVAOHandle() {
		return vaoHandle;
	}

	public int getVBOIndexHandle() {
		return indices.getHandle();
	}

	public int getVBOVertexHandle() {
		return vertices.getHandle();
	}

	public int getVBOColorHandle() {
		return colors.getHandle();
	}

	public int getVBOTextureCoordinateHandle() {
		return texturecoords.getHandle();
	}

	public int getVBONormalHandle() {
		return normals.getHandle();
	}

	public int getRenderedIndexCount() {
		return renderedIndexCount;
	}

	public void setVAOHandle(int vaoHandle) {
		this.vaoHandle = vaoHandle;
	}

	public void setVBOIndexHandle(int vboIndexHandle) {
		this.indices.handle = vboIndexHandle;
	}

	public void setVBOVertexHandle(int vboVertexHandle) {
		this.vertices.handle = vboVertexHandle;
	}

	public void setVBOColorHandle(int vboColorHandle) {
		this.colors.handle = vboColorHandle;
	}

	public void setVBOTextureCoordinateHandle(int vboTextureCoordHandle) {
		this.texturecoords.handle = vboTextureCoordHandle;
	}

	public void setVBONormalHandle(int vboNormalHandle) {
		this.normals.handle = vboNormalHandle;
	}

	public void setRenderedIndexCount(int renderedIndexCount) {
		this.renderedIndexCount = renderedIndexCount;
	}

	public void addIndex(int index) {
		indices.data.add(index);
	}

	public void removeIndex(int id) {
		indices.data.remove(id);
	}

	public void addIndices(int... indices) {
		for (int index : indices)
			addIndex(index);
	}

	public abstract void addVertex(L vertex);

	public abstract void addVertex(L vertex, Color c);

	public abstract void addVertex(L vertex, Color c, Vector2f texturecoord);

	public void addVertex(L vertex, Color c, Vector2f texturecoord, L normal) {
		vertices.data.add(vertex);
		colors.data.add(new Vector3f(c.getRed(), c.getGreen(), c.getBlue()));
		texturecoords.data.add(texturecoord);
		normals.data.add(normal);
	}

	public void addVertex(L vertex, Vector3f c, Vector2f texturecoord, L normal) {
		vertices.data.add(vertex);
		colors.data.add(c);
		texturecoords.data.add(texturecoord);
		normals.data.add(normal);
	}

	public abstract void setVertex(int id, L vertex);

	public abstract void setVertex(int id, L vertex, Color c);

	public abstract void setVertex(int id, L vertex, Color c, Vector2f texturecoord);

	public void setVertex(int id, L vertex, Color c, Vector2f texturecoord, L normal) {
		vertices.data.set(id, vertex);
		colors.data.set(id, new Vector3f(c.getRed(), c.getGreen(), c.getBlue()));
		texturecoords.data.set(id, texturecoord);
		normals.data.set(id, normal);
	}

	public void setVertex(int id, L vertex, Vector3f c, Vector2f texturecoord, L normal) {
		vertices.data.set(id, vertex);
		colors.data.set(id, c);
		texturecoords.data.set(id, texturecoord);
		normals.data.set(id, normal);
	}

	public void removeVertex(int id) {
		vertices.data.remove(id);
		colors.data.remove(id);
		texturecoords.data.remove(id);
		normals.data.remove(id);
	}

	public abstract void computeNormals();

	@Override
	public void delete() {
		// matrix = null;
		indices.delete();
		for (ObjectDataAttributes<?, ?> dataattribute : dataattributes) {
			dataattribute.delete();
		}
		// buf.clear();
	}

	public void deleteData() {
		indices.deleteData();
		for (ObjectDataAttributes<?, ?> dataattribute : dataattributes) {
			dataattribute.deleteData();
		}
	}

	public void deleteGPUData() {
		if (vaoHandle != 0)
			glDeleteVertexArrays(vaoHandle);
		indices.deleteGPUData();
		for (ObjectDataAttributes<?, ?> dataattribute : dataattributes) {
			dataattribute.deleteGPUData();
		}
	}

	public Integer getIndex(int indexid) {
		return indices.data.get(indexid);
	}

	public int getIndexCount() {
		return indices.data.size();
	}

	public List<Integer> getIndices() {
		return indices.data;
	}

	public L getNormal(int normalid) {
		return normals.data.get(normalid);
	}

	public L getVertex(int vertexid) {
		return vertices.data.get(vertexid);
	}

	public Vector2f getTextureCoordinate(int texcoordid) {
		return texturecoords.data.get(texcoordid);
	}

	public Vector3f getColor(int colorid) {
		return colors.data.get(colorid);
	}

	public int getVertexCount() {
		return vertices.data.size();
	}

	public List<L> getVertices() {
		return vertices.data;
	}

	public List<Vector2f> getTextureCoordinates() {
		return texturecoords.data;
	}

	public List<Vector3f> getColors() {
		return colors.data;
	}

	public List<L> getNormals() {
		return normals.data;
	}

	public void invertAllTriangles() {
		List<Integer> newIndices = new ArrayList<Integer>();
		if (rendermode == GLConstants.TRIANGLE_ADJACENCY) {
			for (int i = 0; i < indices.data.size(); i += 6) {
				newIndices.add(indices.data.get(i + 4));
				newIndices.add(indices.data.get(i + 5));
				newIndices.add(indices.data.get(i + 2));
				newIndices.add(indices.data.get(i + 3));
				newIndices.add(indices.data.get(i));
				newIndices.add(indices.data.get(i + 1));
			}
		} else {
			for (int i = 0; i < indices.data.size(); i += 3) {
				newIndices.add(indices.data.get(i));
				newIndices.add(indices.data.get(i + 2));
				newIndices.add(indices.data.get(i + 1));
			}
		}
		indices.data.clear();
		indices.data = newIndices;
		prerender();
	}

	public void prerender() {
		deleteGPUData();

		renderedIndexCount = indices.data.size();

		indices.updateData();
		for (ObjectDataAttributes<?, ?> dataattribute : dataattributes) {
			dataattribute.updateData();
		}

		vaoHandle = glGenVertexArrays();
		glBindVertexArray(vaoHandle);

		for (ObjectDataAttributes<?, ?> dataattribute : dataattributes) {
			dataattribute.pushBuffer();
		}

		glEnableVertexAttribArray(VERTEX_POSITION);

		glBindVertexArray(0);

		indices.pushBuffer();

		setRenderHints(colors.isActive(), texturecoords.isActive(), normals.isActive());
	}

	@Override
	public void render() {
		if (render) {
			glBindVertexArray(vaoHandle);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indices.handle);

			glDrawElements(rendermode, renderedIndexCount, GL_UNSIGNED_INT, 0);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

			glBindVertexArray(0);
		}
	}

	public void setColor(Color color) {
		setColor(new Vector3f(color.getRed(), color.getGreen(), color.getBlue()));
	}

	public void setColor(Vector3f color) {
		for (int c = 0; c < colors.data.size(); c++) {
			colors.data.set(c, color);
		}
		this.prerender();
	}

	public void setIndices(List<Integer> inds) {
		indices.data = inds;
	}

	public void setIndex(int indexid, int value) {
		indices.data.set(indexid, value);
	}

	public void setNormal(int normalid, L normal) {
		normals.data.set(normalid, normal);
	}

	public void setColor(int colorid, Vector3f color) {
		colors.data.set(colorid, color);
	}

	public void setTextureCoordinate(int texcoordid, Vector2f texturecoord) {
		texturecoords.data.set(texcoordid, texturecoord);
	}

	public void setRenderHints(boolean rendercolors, boolean rendertexturecoords, boolean rendernormals) {
		glBindVertexArray(vaoHandle);

		colors.setActive(rendercolors);
		texturecoords.setActive(rendertexturecoords);
		normals.setActive(rendernormals);

		glBindVertexArray(0);
	}

	public void updateRenderHints() {
		glBindVertexArray(vaoHandle);

		for (ObjectDataAttributes<?, ?> dataattribute : dataattributes) {
			dataattribute.updateActive();
		}

		glBindVertexArray(0);
	}

	public void setRendered(boolean render) {
		this.render = render;
	}

	public boolean isRendered() {
		return render;
	}

	public void setRenderMode(int mode) {
		rendermode = mode;
	}

	public int getRenderMode() {
		return rendermode;
	}

	public void setVertices(List<L> verts) {
		vertices.data = verts;
	}

	public void setColors(List<Vector3f> colors) {
		this.colors.data = colors;
	}

	public void setNormals(List<L> normals) {
		this.normals.data = normals;
	}

	public void setTextureCoordinates(List<Vector2f> texcoords) {
		texturecoords.data = texcoords;
	}

	public void addQuad(int index1, int index2, int index3, int index4) {
		addTriangle(index1, index2, index3);
		addTriangle(index1, index3, index4);
	}

	public void addTriangle(int index1, int index2, int index3) {
		addIndex(index1);
		addIndex(index2);
		addIndex(index3);
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
}