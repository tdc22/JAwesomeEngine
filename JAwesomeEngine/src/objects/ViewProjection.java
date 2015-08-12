package objects;

import java.nio.FloatBuffer;

import matrix.Matrix4f;

public interface ViewProjection extends Renderable {
	public void setViewMatrix(FloatBuffer buffer);
	public void setProjectionMatrix(FloatBuffer buffer);
	public void setViewProjectionMatrix(FloatBuffer viewBuffer, FloatBuffer projectionBuffer);
	
	public void setViewMatrix(Matrix4f matrix);
	public void setProjectionMatrix(Matrix4f matrix);
	public void setViewProjectionMatrix(Matrix4f viewMatrix, Matrix4f projectionMatrix);
	
	public FloatBuffer getViewMatrixBuffer();
	public FloatBuffer getProjectionMatrixBuffer();
}