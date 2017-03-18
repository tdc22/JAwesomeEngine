package objects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
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
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;

import matrix.Matrix4f;
import vector.Vector2f;
import vector.Vector3f;

public class InstancedObject extends ShapedObject {
	protected static final int INSTANCE_POSITION = 4;

	protected List<Matrix4f> instances;

	protected int vboInstanceHandle;

	boolean renderInstances = true;

	protected int renderedInstanceCount = 0;

	protected int instancesize = 16;

	public InstancedObject() {
		super();

		instances = new ArrayList<Matrix4f>();
	}

	public void addInstance(Matrix4f instance) {
		instances.add(instance);
	}

	public void setRenderHints(boolean rendercolors, boolean rendertexturecoords, boolean rendernormals,
			boolean renderinstances) {
		renderColor = rendercolors;
		renderTexCoords = rendertexturecoords;
		renderNormals = rendernormals;
		renderInstances = renderinstances;
	}

	public void prerender() {
		// super.prerender();
		//
		// System.out.println(Debugger.getGLErrorName(GL11.glGetError()));
		//
		// if (vboInstanceHandle != 0)
		// glDeleteBuffers(vboInstanceHandle);
		//
		// renderedInstanceCount = instances.size();
		//
		// FloatBuffer instanceData =
		// BufferUtils.createFloatBuffer(renderedInstanceCount * instancesize);
		// for(Matrix4f mat : instances) {
		// instanceData.put(mat.getArrayf()[0]);
		// instanceData.put(mat.getArrayf()[1]);
		// instanceData.put(mat.getArrayf()[2]);
		// instanceData.put(mat.getArrayf()[3]);
		// // float[][] matrix = mat.getArrayf();
		// // instanceData.put(matrix[0][0]);
		// // instanceData.put(matrix[0][1]);
		// // instanceData.put(matrix[0][2]);
		// // instanceData.put(matrix[0][3]);
		// // instanceData.put(matrix[1][0]);
		// // instanceData.put(matrix[1][1]);
		// // instanceData.put(matrix[1][2]);
		// // instanceData.put(matrix[1][3]);
		// // instanceData.put(matrix[2][0]);
		// // instanceData.put(matrix[2][1]);
		// // instanceData.put(matrix[2][2]);
		// // instanceData.put(matrix[2][3]);
		// // instanceData.put(matrix[3][0]);
		// // instanceData.put(matrix[3][1]);
		// // instanceData.put(matrix[3][2]);
		// // instanceData.put(matrix[3][3]);
		// }
		// instanceData.flip();
		//
		// glBindVertexArray(vaoHandle);
		//
		// vboInstanceHandle = glGenBuffers();
		// glBindBuffer(GL_ARRAY_BUFFER, vboInstanceHandle);
		// glBufferData(GL_ARRAY_BUFFER, instanceData, GL_STATIC_DRAW);
		//
		// glVertexAttribPointer(INSTANCE_POSITION, vertexsize, GL_FLOAT, false,
		// instancesize, 0);
		// glVertexAttribDivisor(INSTANCE_POSITION, 1);
		// glVertexAttribPointer(INSTANCE_POSITION+1, vertexsize, GL_FLOAT,
		// false, instancesize, vertexsize);
		// glVertexAttribDivisor(INSTANCE_POSITION+1, 1);
		// glVertexAttribPointer(INSTANCE_POSITION+2, vertexsize, GL_FLOAT,
		// false, instancesize, 2*vertexsize);
		// glVertexAttribDivisor(INSTANCE_POSITION+2, 1);
		// glVertexAttribPointer(INSTANCE_POSITION+3, vertexsize, GL_FLOAT,
		// false, instancesize, 3*vertexsize);
		// glVertexAttribDivisor(INSTANCE_POSITION+3, 1);
		//
		// glBindBuffer(GL_ARRAY_BUFFER, 0);
		// glBindVertexArray(0);
		//
		// System.out.println(Debugger.getGLErrorName(GL11.glGetError()));

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
		if (vboInstanceHandle != 0)
			glDeleteBuffers(vboInstanceHandle);

		renderedIndexCount = indices.size();
		renderedInstanceCount = instances.size();
		int allVertices = vertices.size();

		IntBuffer indexData = BufferUtils.createIntBuffer(renderedIndexCount * polysize);
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(allVertices * vertexsize);
		FloatBuffer colorData = BufferUtils.createFloatBuffer(allVertices * colorsize);
		FloatBuffer textureData = BufferUtils.createFloatBuffer(allVertices * texsize);
		FloatBuffer normalData = BufferUtils.createFloatBuffer(allVertices * vertexsize);
		FloatBuffer instanceData = BufferUtils.createFloatBuffer(renderedInstanceCount * instancesize);

		for (int v = 0; v < allVertices; v++) {
			Vector3f vertex = vertices.get(v);
			vertexData.put(new float[] { vertex.x, vertex.y, vertex.z, 1 });
			Color vertcolor = colors.get(v);
			colorData.put(new float[] { vertcolor.getRed(), vertcolor.getGreen(), vertcolor.getBlue() });
			Vector2f tex = texturecoords.get(v);
			textureData.put(new float[] { tex.x, tex.y });
			Vector3f normal = normals.get(v);
			normalData.put(new float[] { normal.x, normal.y, normal.z, 0 });
		}
		for (int i = 0; i < renderedIndexCount; i++) {
			indexData.put(indices.get(i));
		}
		for (Matrix4f mat : instances) {
			instanceData.put(mat.getArrayf()[0]);
			instanceData.put(mat.getArrayf()[1]);
			instanceData.put(mat.getArrayf()[2]);
			instanceData.put(mat.getArrayf()[3]);
		}
		indexData.flip();
		vertexData.flip();
		colorData.flip();
		textureData.flip();
		normalData.flip();
		instanceData.flip();

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

		vboInstanceHandle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboInstanceHandle);
		glBufferData(GL_ARRAY_BUFFER, instanceData, GL_STATIC_DRAW);
		glVertexAttribPointer(INSTANCE_POSITION, vertexsize, GL_FLOAT, false, 0, instancesize);
		glVertexAttribDivisor(INSTANCE_POSITION, 1);
		glVertexAttribPointer(INSTANCE_POSITION + 1, vertexsize, GL_FLOAT, false, 0, 2 * instancesize);
		glVertexAttribDivisor(INSTANCE_POSITION + 1, 1);
		glVertexAttribPointer(INSTANCE_POSITION + 2, vertexsize, GL_FLOAT, false, 0, 3 * instancesize);
		glVertexAttribDivisor(INSTANCE_POSITION + 2, 1);
		glVertexAttribPointer(INSTANCE_POSITION + 3, vertexsize, GL_FLOAT, false, 0, 4 * instancesize);
		glVertexAttribDivisor(INSTANCE_POSITION + 3, 1);
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

			if (renderInstances) {
				glEnableVertexAttribArray(INSTANCE_POSITION);
				glEnableVertexAttribArray(INSTANCE_POSITION + 1);
				glEnableVertexAttribArray(INSTANCE_POSITION + 2);
				glEnableVertexAttribArray(INSTANCE_POSITION + 3);
				glBindBuffer(GL_ARRAY_BUFFER, vboInstanceHandle);
			}

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboIndexHandle);

			glDrawElementsInstanced(rendermode, renderedIndexCount, GL_UNSIGNED_INT, 0, renderedInstanceCount);

			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glDisableVertexAttribArray(VERTEX_POSITION);
			if (renderInstances) {
				glDisableVertexAttribArray(INSTANCE_POSITION + 3);
				glDisableVertexAttribArray(INSTANCE_POSITION + 2);
				glDisableVertexAttribArray(INSTANCE_POSITION + 1);
				glDisableVertexAttribArray(INSTANCE_POSITION);
			}
			if (renderNormals)
				glDisableVertexAttribArray(NORMAL_POSITION);
			if (renderTexCoords)
				glDisableVertexAttribArray(TEXTURE_POSITION);
			if (renderColor)
				glDisableVertexAttribArray(COLOR_POSITION);

			glBindVertexArray(0);
		}
	}
}