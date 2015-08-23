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
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import math.VecMath;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class ShapedObject extends RenderedObject {
	protected static final int VERTEX_POSITION = 0;
	protected static final int COLOR_POSITION = 1;
	protected static final int TEXTURE_POSITION = 2;
	protected static final int NORMAL_POSITION = 3;

	protected List<Integer> indices;
	protected List<Vector3f> vertices;
	protected List<Vector3f> normals;
	protected List<Vector3f> vertcolors;
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

	public ShapedObject() {
		super();
		init();
	}

	public ShapedObject(float x, float y, float z) {
		super();
		translateTo(x, y, z);
		init();
	}

	public ShapedObject(Vector3f pos) {
		super();
		translateTo(pos);
		init();
	}

	private void init() {
		indices = new ArrayList<Integer>();
		vertices = new ArrayList<Vector3f>();
		normals = new ArrayList<Vector3f>();
		vertcolors = new ArrayList<Vector3f>();
		texturecoords = new ArrayList<Vector2f>();

		rendermode = GLConstants.TRIANGLE_ADJACENCY;
	}

	public void copy(ShapedObject original) {
		indices.addAll(original.getIndices());
		vertices.addAll(original.getVertices());
		normals.addAll(original.getNormals());
		vertcolors.addAll(original.getVertexColors());
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
	// public int getVAOHandle() {
	// return vaoHandle;
	// }
	//
	// public int getVBOIndexHandle() {
	// return vboIndexHandle;
	// }
	//
	// public int getVBOVertexHandle() {
	// return vboVertexHandle;
	// }
	//
	// public int getVBOColorHandle() {
	// return vboColorHandle;
	// }
	//
	// public int getVBOTextureCoordinateHandle() {
	// return vboTextureCoordHandle;
	// }
	//
	// public int getVBONormalHandle() {
	// return vboNormalHandle;
	// }

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

	public void addVertex(Vector3f vertex) {
		addVertex(vertex, Color.GRAY, new Vector2f(), new Vector3f());
	}

	public void addVertex(Vector3f vertex, Color c) {
		addVertex(vertex, c, new Vector2f(), new Vector3f());
	}

	public void addVertex(Vector3f vertex, Color c, Vector2f texturecoord, Vector3f normal) {
		vertices.add(vertex);
		vertcolors.add(new Vector3f(c.getRed(), c.getGreen(), c.getBlue()));
		texturecoords.add(texturecoord);
		normals.add(normal);
	}

	public void addVertex(Vector3f vertex, Vector3f c, Vector2f texturecoord, Vector3f normal) {
		vertices.add(vertex);
		vertcolors.add(c);
		texturecoords.add(texturecoord);
		normals.add(normal);
	}

	public void removeVertex(int id) {
		vertices.remove(id);
		vertcolors.remove(id);
		texturecoords.remove(id);
		normals.remove(id);
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
		vertcolors.clear();
		normals.clear();
		texturecoords.clear();
	}

	public void deleteGPUData() {
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

	public Vector3f getNormal(int normalid) {
		return normals.get(normalid);
	}

	public Vector3f getVertex(int vertexid) {
		return vertices.get(vertexid);
	}

	public int getVertexCount() {
		return vertices.size();
	}

	public List<Vector3f> getVertices() {
		return vertices;
	}

	public List<Vector2f> getTextureCoordinates() {
		return texturecoords;
	}

	public List<Vector3f> getVertexColors() {
		return vertcolors;
	}

	public List<Vector3f> getNormals() {
		return normals;
	}

	public void invertAllTriangles() {
		List<Integer> newIndices = new ArrayList<Integer>();
		for (int i = 0; i < indices.size(); i += 6) {
			newIndices.add(indices.get(i + 5));
			newIndices.add(indices.get(i + 4));
			newIndices.add(indices.get(i + 3));
			newIndices.add(indices.get(i + 2));
			newIndices.add(indices.get(i + 1));
			newIndices.add(indices.get(i));
		}
		indices.clear();
		indices = newIndices;
		prerender();
	}

	public void prerender() {
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

		renderedIndexCount = indices.size();
		int allVertices = vertices.size();

		IntBuffer indexData = BufferUtils.createIntBuffer(renderedIndexCount * polysize);
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(allVertices * vertexsize);
		FloatBuffer colorData = BufferUtils.createFloatBuffer(allVertices * colorsize);
		FloatBuffer textureData = BufferUtils.createFloatBuffer(allVertices * texsize);
		FloatBuffer normalData = BufferUtils.createFloatBuffer(allVertices * vertexsize);

		for (int v = 0; v < allVertices; v++) {
			Vector3f vertex = vertices.get(v);
			vertexData.put(new float[] { vertex.x, vertex.y, vertex.z, 1 });
			Vector3f vertcolor = vertcolors.get(v);
			colorData.put(new float[] { vertcolor.x, vertcolor.y, vertcolor.z });
			Vector2f tex = texturecoords.get(v);
			textureData.put(new float[] { tex.x, tex.y });
			Vector3f normal = normals.get(v);
			normalData.put(new float[] { normal.x, normal.y, normal.z, 0 });
		}
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

		glBindVertexArray(0);

		vboIndexHandle = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexHandle);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
	}

	@Override
	public void render() {
		if (render) {
			glBindVertexArray(vaoHandle);

			glEnableVertexAttribArray(VERTEX_POSITION);
			glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);

			if (renderColor) {
				glEnableVertexAttribArray(COLOR_POSITION);
				glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
			}

			if (renderTexCoords) {
				glEnableVertexAttribArray(TEXTURE_POSITION);
				glBindBuffer(GL_ARRAY_BUFFER, vboTextureCoordHandle);
			}

			if (renderNormals) {
				glEnableVertexAttribArray(NORMAL_POSITION);
				glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
			}

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexHandle);

			glDrawElements(rendermode, renderedIndexCount, GL_UNSIGNED_INT, 0);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glDisableVertexAttribArray(VERTEX_POSITION);
			if (renderNormals)
				glDisableVertexAttribArray(NORMAL_POSITION);
			if (renderTexCoords)
				glDisableVertexAttribArray(TEXTURE_POSITION);
			if (renderColor)
				glDisableVertexAttribArray(COLOR_POSITION);

			glBindVertexArray(0);
		}
	}

	public void setColor(Color color) {
		setColor(new Vector3f(color.getRed(), color.getGreen(), color.getBlue()));
	}

	public void setColor(Vector3f color) {
		for (int c = 0; c < vertcolors.size(); c++) {
			vertcolors.set(c, color);
		}
		this.prerender();
	}

	public void setIndices(List<Integer> inds) {
		indices = inds;
	}

	public void setNormal(int normalid, Vector3f normal) {
		normals.set(normalid, normal);
	}

	public void setRenderHints(boolean rendercolors, boolean rendertexturecoords, boolean rendernormals) {
		renderColor = rendercolors;
		renderTexCoords = rendertexturecoords;
		renderNormals = rendernormals;
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

	public void setVertices(List<Vector3f> verts) {
		vertices = verts;
	}

	public void setColors(List<Vector3f> colors) {
		vertcolors = colors;
	}

	public void setNormals(List<Vector3f> normals) {
		this.normals = normals;
	}

	public void setTextureCoordinates(List<Vector2f> texcoords) {
		texturecoords = texcoords;
	}
}