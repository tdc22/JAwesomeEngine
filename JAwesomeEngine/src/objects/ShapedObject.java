package objects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

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

	protected List<Integer> indices;
	protected List<L> vertices;
	protected List<L> normals;
	protected List<Vector3f> colors;
	protected List<Vector2f> texturecoords;

	protected int vaoHandle;
	protected int vboIndexHandle;
	protected int vboVertexHandle;
	protected int vboColorHandle;
	protected int vboTextureCoordHandle;
	protected int vboNormalHandle;

	protected int rendermode;
	protected int renderedIndexCount = 0;

	boolean render = true;
	boolean renderColor = true;
	boolean renderTexCoords = false;
	boolean renderNormals = false;

	protected int vertexsize = 4;
	protected int polysize = 3;
	protected int colorsize = 3;
	protected int texsize = 2;

	public ShapedObject(L rotcenter, L translation, A rotation, L scale) {
		super(rotcenter, translation, rotation, scale);
		init();
	}

	private void init() {
		indices = new ArrayList<Integer>();
		colors = new ArrayList<Vector3f>();
		texturecoords = new ArrayList<Vector2f>();
	}

	public void copy(ShapedObject<L, A> original) {
		indices.addAll(original.getIndices());
		vertices.addAll(original.getVertices());
		normals.addAll(original.getNormals());
		colors.addAll(original.getColors());
		texturecoords.addAll(original.getTextureCoordinates());
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
	//
	public int getVAOHandle() {
		return vaoHandle;
	}

	public int getVBOIndexHandle() {
		return vboIndexHandle;
	}

	public int getVBOVertexHandle() {
		return vboVertexHandle;
	}

	public int getVBOColorHandle() {
		return vboColorHandle;
	}

	public int getVBOTextureCoordinateHandle() {
		return vboTextureCoordHandle;
	}

	public int getVBONormalHandle() {
		return vboNormalHandle;
	}

	public int getRenderedIndexCount() {
		return renderedIndexCount;
	}

	public void setVAOHandle(int vaoHandle) {
		this.vaoHandle = vaoHandle;
	}

	public void setVBOIndexHandle(int vboIndexHandle) {
		this.vboIndexHandle = vboIndexHandle;
	}

	public void setVBOVertexHandle(int vboVertexHandle) {
		this.vboVertexHandle = vboVertexHandle;
	}

	public void setVBOColorHandle(int vboColorHandle) {
		this.vboColorHandle = vboColorHandle;
	}

	public void setVBOTextureCoordinateHandle(int vboTextureCoordHandle) {
		this.vboTextureCoordHandle = vboTextureCoordHandle;
	}

	public void setVBONormalHandle(int vboNormalHandle) {
		this.vboNormalHandle = vboNormalHandle;
	}

	public void setRenderedIndexCount(int renderedIndexCount) {
		this.renderedIndexCount = renderedIndexCount;
	}

	public void addIndex(int index) {
		indices.add(index);
	}

	public void removeIndex(int id) {
		indices.remove(id);
	}

	public void addIndices(int... indices) {
		for (int index : indices)
			addIndex(index);
	}

	public abstract void addVertex(L vertex);

	public abstract void addVertex(L vertex, Color c);

	public abstract void addVertex(L vertex, Color c, Vector2f texturecoord);

	public void addVertex(L vertex, Color c, Vector2f texturecoord, L normal) {
		vertices.add(vertex);
		colors.add(new Vector3f(c.getRed(), c.getGreen(), c.getBlue()));
		texturecoords.add(texturecoord);
		normals.add(normal);
	}

	public void addVertex(L vertex, Vector3f c, Vector2f texturecoord, L normal) {
		vertices.add(vertex);
		colors.add(c);
		texturecoords.add(texturecoord);
		normals.add(normal);
	}

	public abstract void setVertex(int id, L vertex);

	public abstract void setVertex(int id, L vertex, Color c);

	public abstract void setVertex(int id, L vertex, Color c, Vector2f texturecoord);

	public void setVertex(int id, L vertex, Color c, Vector2f texturecoord, L normal) {
		vertices.set(id, vertex);
		colors.set(id, new Vector3f(c.getRed(), c.getGreen(), c.getBlue()));
		texturecoords.set(id, texturecoord);
		normals.set(id, normal);
	}

	public void setVertex(int id, L vertex, Vector3f c, Vector2f texturecoord, L normal) {
		vertices.set(id, vertex);
		colors.set(id, c);
		texturecoords.set(id, texturecoord);
		normals.set(id, normal);
	}

	public void removeVertex(int id) {
		vertices.remove(id);
		colors.remove(id);
		texturecoords.remove(id);
		normals.remove(id);
	}

	public abstract void computeNormals();

	@Override
	public void delete() {
		// matrix = null;
		deleteData();
		deleteGPUData();
		// buf.clear();
	}

	public void deleteData() {
		indices.clear();
		vertices.clear();
		colors.clear();
		normals.clear();
		texturecoords.clear();
	}

	public void deleteGPUData() {
		if (vaoHandle != 0)
			glDeleteVertexArrays(vaoHandle);
		if (vboIndexHandle != 0)
			glDeleteBuffers(vboIndexHandle);
		if (vboVertexHandle != 0)
			glDeleteBuffers(vboVertexHandle);
		if (vboColorHandle != 0)
			glDeleteBuffers(vboColorHandle);
		if (vboTextureCoordHandle != 0)
			glDeleteBuffers(vboTextureCoordHandle);
		if (vboNormalHandle != 0)
			glDeleteBuffers(vboNormalHandle);
	}

	public Integer getIndex(int indexid) {
		return indices.get(indexid);
	}

	public int getIndexCount() {
		return indices.size();
	}

	public List<Integer> getIndices() {
		return indices;
	}

	public L getNormal(int normalid) {
		return normals.get(normalid);
	}

	public L getVertex(int vertexid) {
		return vertices.get(vertexid);
	}

	public Vector2f getTextureCoordinate(int texcoordid) {
		return texturecoords.get(texcoordid);
	}

	public Vector3f getColor(int colorid) {
		return colors.get(colorid);
	}

	public int getVertexCount() {
		return vertices.size();
	}

	public List<L> getVertices() {
		return vertices;
	}

	public List<Vector2f> getTextureCoordinates() {
		return texturecoords;
	}

	public List<Vector3f> getColors() {
		return colors;
	}

	public List<L> getNormals() {
		return normals;
	}

	public void invertAllTriangles() {
		List<Integer> newIndices = new ArrayList<Integer>();
		if (rendermode == GLConstants.TRIANGLE_ADJACENCY) {
			for (int i = 0; i < indices.size(); i += 6) {
				newIndices.add(indices.get(i + 4));
				newIndices.add(indices.get(i + 5));
				newIndices.add(indices.get(i + 2));
				newIndices.add(indices.get(i + 3));
				newIndices.add(indices.get(i));
				newIndices.add(indices.get(i + 1));
			}
		} else {
			for (int i = 0; i < indices.size(); i += 3) {
				newIndices.add(indices.get(i));
				newIndices.add(indices.get(i + 2));
				newIndices.add(indices.get(i + 1));
			}
		}
		indices.clear();
		indices = newIndices;
		prerender();
	}

	protected abstract void fillBuffers(int allVertices, IntBuffer indexData, FloatBuffer vertexData,
			FloatBuffer colorData, FloatBuffer textureData, FloatBuffer normalData);

	public void prerender() {
		deleteGPUData();

		renderedIndexCount = indices.size();
		int allVertices = vertices.size();

		IntBuffer indexData = BufferUtils.createIntBuffer(renderedIndexCount * polysize);
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(allVertices * vertexsize);
		FloatBuffer colorData = BufferUtils.createFloatBuffer(allVertices * colorsize);
		FloatBuffer textureData = BufferUtils.createFloatBuffer(allVertices * texsize);
		FloatBuffer normalData = BufferUtils.createFloatBuffer(allVertices * vertexsize);

		fillBuffers(allVertices, indexData, vertexData, colorData, textureData, normalData);
		for (int i = 0; i < renderedIndexCount; i++) {
			indexData.put(indices.get(i));
		}
		indexData.flip();
		vertexData.flip();
		colorData.flip();
		textureData.flip();
		normalData.flip();

		vaoHandle = glGenVertexArrays();
		glBindVertexArray(vaoHandle);

		vboVertexHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(VERTEX_POSITION, vertexsize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboColorHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
		glVertexAttribPointer(COLOR_POSITION, colorsize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboTextureCoordHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboTextureCoordHandle);
		glBufferData(GL_ARRAY_BUFFER, textureData, GL_STATIC_DRAW);
		glVertexAttribPointer(TEXTURE_POSITION, texsize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboNormalHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
		glBufferData(GL_ARRAY_BUFFER, normalData, GL_STATIC_DRAW);
		glVertexAttribPointer(NORMAL_POSITION, vertexsize, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glEnableVertexAttribArray(VERTEX_POSITION);

		glBindVertexArray(0);

		vboIndexHandle = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexHandle);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		setRenderHints(renderColor, renderTexCoords, renderNormals);
	}

	@Override
	public void render() {
		if (render) {
			glBindVertexArray(vaoHandle);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexHandle);

			glDrawElements(rendermode, renderedIndexCount, GL_UNSIGNED_INT, 0);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

			glBindVertexArray(0);
		}
	}

	public void setColor(Color color) {
		setColor(new Vector3f(color.getRed(), color.getGreen(), color.getBlue()));
	}

	public void setColor(Vector3f color) {
		for (int c = 0; c < colors.size(); c++) {
			colors.set(c, color);
		}
		this.prerender();
	}

	public void setIndices(List<Integer> inds) {
		indices = inds;
	}

	public void setIndex(int indexid, int value) {
		indices.set(indexid, value);
	}

	public void setNormal(int normalid, L normal) {
		normals.set(normalid, normal);
	}

	public void setColor(int colorid, Vector3f color) {
		colors.set(colorid, color);
	}

	public void setTextureCoordinate(int texcoordid, Vector2f texturecoord) {
		texturecoords.set(texcoordid, texturecoord);
	}

	public void setRenderHints(boolean rendercolors, boolean rendertexturecoords, boolean rendernormals) {
		renderColor = rendercolors;
		renderTexCoords = rendertexturecoords;
		renderNormals = rendernormals;

		glBindVertexArray(vaoHandle);

		if (renderColor) {
			glEnableVertexAttribArray(COLOR_POSITION);
		} else {
			glDisableVertexAttribArray(COLOR_POSITION);
		}
		if (renderTexCoords) {
			glEnableVertexAttribArray(TEXTURE_POSITION);
		} else {
			glDisableVertexAttribArray(TEXTURE_POSITION);
		}
		if (renderNormals) {
			glEnableVertexAttribArray(NORMAL_POSITION);
		} else {
			glDisableVertexAttribArray(NORMAL_POSITION);
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

	public void setVertices(List<L> verts) {
		vertices = verts;
	}

	public void setColors(List<Vector3f> colors) {
		this.colors = colors;
	}

	public void setNormals(List<L> normals) {
		this.normals = normals;
	}

	public void setTextureCoordinates(List<Vector2f> texcoords) {
		texturecoords = texcoords;
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