package objects;

import java.nio.FloatBuffer;

import matrix.Matrix4f;

public interface ViewProjection2 extends Renderable2 {
	public void setViewMatrix2(FloatBuffer buffer);

	public void setProjectionMatrix2(FloatBuffer buffer);

	public void setViewProjectionMatrix2(FloatBuffer viewBuffer, FloatBuffer projectionBuffer);

	public void setViewMatrix2(Matrix4f matrix);

	public void setProjectionMatrix2(Matrix4f matrix);

	public void setViewProjectionMatrix2(Matrix4f viewMatrix, Matrix4f projectionMatrix);

	public FloatBuffer getViewMatrixBuffer2();

	public FloatBuffer getProjectionMatrixBuffer2();
}