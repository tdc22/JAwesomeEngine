package objects;

import static org.lwjgl.opengl.GL11.GL_COLOR_ARRAY;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glColorPointer;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glMultMatrix;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import math.VecMath;

import org.lwjgl.BufferUtils;

import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class ShapedObject extends RenderedObject {
	private List<Integer> indices;
	private List<Vector3f> vertices;
	private List<Vector3f> normals;
	private List<Color> vertcolors;
	private List<Vector2f> texturecoords;

	private int vboIndexHandle;
	private int vboVertexHandle;
	private int vboColorHandle;
	private int vboTextureCoordHandle;
	private int vboNormalHandle;

	protected int rendermode;

	boolean renderColor = true;
	boolean renderTexCoords = false;
	boolean renderNormals = false;

	protected int vertexsize = 3;
	protected int polysize = 3;
	protected int colorsize = 3;
	protected int texsize = 2;

	public ShapedObject() {
		super();

		indices = new ArrayList<Integer>();
		vertices = new ArrayList<Vector3f>();
		normals = new ArrayList<Vector3f>();
		vertcolors = new ArrayList<Color>();
		texturecoords = new ArrayList<Vector2f>();

		rendermode = GLConstants.TRIANGLE_ADJACENCY;
	}

	public void addIndex(int index) {
		indices.add(index);
	}

	public void addIndices(int... indices) {
		for (int index : indices)
			addIndex(index);
	}

	public void addQuad(int index1, int adjacency1, int index2, int adjacency2,
			int index3, int adjacency3, int index4, int adjacency4) {
		addTriangle(index1, adjacency1, index2, adjacency2, index3, index4);
		addTriangle(index1, index2, index3, adjacency3, index4, adjacency4);

		// addTriangle(index4, adjacency4, index1, adjacency1, index2, index3);
		// addTriangle(index2, adjacency2, index3, adjacency3, index4, index1);
	}

	public void addTriangle(int index1, int adjacency1, int index2,
			int adjacency2, int index3, int adjacency3) {
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

	public void addVertex(Vector3f vertex, Color c, Vector2f texturecoord,
			Vector3f normal) {
		vertices.add(vertex);
		vertcolors.add(c);
		texturecoords.add(texturecoord);
		normals.add(normal);
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
			Vector3f newnormal = VecMath.computeNormal(getVertex(index1),
					getVertex(index2), getVertex(index3));
			setNormal(index1,
					VecMath.normalize(VecMath.addition(normal1, newnormal)));
			setNormal(index2,
					VecMath.normalize(VecMath.addition(normal2, newnormal)));
			setNormal(index3,
					VecMath.normalize(VecMath.addition(normal3, newnormal)));
			ci += 3;
		}
	}

	@Override
	public void delete() {
		// matrix = null;
		deleteData();
		deleteGPUData();
	}

	public void deleteData() {
		indices.clear();
		vertices.clear();
		vertcolors.clear();
		normals.clear();
		texturecoords.clear();
	}

	public void deleteGPUData() {
		glDeleteBuffers(vboIndexHandle);
		glDeleteBuffers(vboVertexHandle);
		glDeleteBuffers(vboColorHandle);
		glDeleteBuffers(vboTextureCoordHandle);
		glDeleteBuffers(vboNormalHandle);
	}

	protected void endRender() {
		if (shadered && shaderactive)
			shader.unbind();

		glPopMatrix();
		// if(isAttached()) glPopMatrix();
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

	protected void initRender() {
		if (shadered && shaderactive)
			shader.bind();

		// if(isAttached()) {
		// glPushMatrix();
		// Vector3f attachedrotcenter = attachedTo.getRotationCenter();
		// glTranslatef(attachedrotcenter.x, attachedrotcenter.y,
		// attachedrotcenter.z);
		// glMultMatrix(attachedTo.getMatrixBuffer());
		// glTranslatef(-attachedrotcenter.x, -attachedrotcenter.y,
		// -attachedrotcenter.z);
		// }

		glPushMatrix();
		glTranslatef(rotcenter.x, rotcenter.y, rotcenter.z);
		glMultMatrix(buf);
		glTranslatef(-rotcenter.x, -rotcenter.y, -rotcenter.z);
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

		int allIndices = indices.size();
		int allVertices = vertices.size();

		IntBuffer indexData = BufferUtils
				.createIntBuffer(allIndices * polysize);
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(allVertices
				* vertexsize);
		FloatBuffer colorData = BufferUtils.createFloatBuffer(allVertices
				* colorsize);
		FloatBuffer textureData = BufferUtils.createFloatBuffer(allVertices
				* texsize);
		FloatBuffer normalData = BufferUtils.createFloatBuffer(allVertices
				* vertexsize);

		for (int v = 0; v < allVertices; v++) {
			Vector3f vertex = vertices.get(v);
			vertexData.put(new float[] { vertex.x, vertex.y, vertex.z });
			Color vertcolor = vertcolors.get(v);
			colorData.put(new float[] { vertcolor.getRed(),
					vertcolor.getGreen(), vertcolor.getBlue() });
			Vector2f tex = texturecoords.get(v);
			textureData.put(new float[] { tex.x, tex.y });
			Vector3f normal = normals.get(v);
			normalData.put(new float[] { normal.x, normal.y, normal.z });
		}
		for (int i = 0; i < allIndices; i++) {
			indexData.put(indices.get(i));
		}
		indexData.flip();
		vertexData.flip();
		colorData.flip();
		textureData.flip();
		normalData.flip();

		vboIndexHandle = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexHandle);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

		vboVertexHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboColorHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboTextureCoordHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboTextureCoordHandle);
		glBufferData(GL_ARRAY_BUFFER, textureData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		vboNormalHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
		glBufferData(GL_ARRAY_BUFFER, normalData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	@Override
	public void render() {
		initRender();

		glEnableClientState(GL_VERTEX_ARRAY);
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glVertexPointer(vertexsize, GL_FLOAT, 0, 0L);

		if (renderColor) {
			glEnableClientState(GL_COLOR_ARRAY);
			glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
			glColorPointer(colorsize, GL_FLOAT, 0, 0L);
		}

		if (renderTexCoords) {
			glEnableClientState(GL_TEXTURE_COORD_ARRAY);
			glBindBuffer(GL_ARRAY_BUFFER, vboTextureCoordHandle);
			glTexCoordPointer(texsize, GL_FLOAT, 0, 0L);
		}

		if (renderNormals) {
			glEnableClientState(GL_NORMAL_ARRAY);
			glBindBuffer(GL_ARRAY_BUFFER, vboNormalHandle);
			glNormalPointer(GL_FLOAT, 0, 0L);
		}

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexHandle);

		glDrawElements(rendermode, indices.size(), GL_UNSIGNED_INT, 0);

		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
		if (renderNormals)
			glDisableClientState(GL_NORMAL_ARRAY);
		if (renderTexCoords)
			glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		if (renderColor)
			glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);

		endRender();
	}

	public void setColor(Color color) {
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

	public void setRenderHints(boolean rendercolors,
			boolean rendertexturecoords, boolean rendernormals) {
		renderColor = rendercolors;
		renderTexCoords = rendertexturecoords;
		renderNormals = rendernormals;
	}

	public void setRenderMode(int mode) {
		rendermode = mode;
	}

	public void setVertices(List<Vector3f> verts) {
		vertices = verts;
	}
}