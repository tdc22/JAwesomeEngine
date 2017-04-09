package objects;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import matrix.Matrix4f;
import utils.ProjectionHelper;

public class ViewFrustum {
	Matrix4f matrix;
	FloatBuffer buf;

	public ViewFrustum(float aspect, float zNear, float zFar, float fovY) {
		buf = BufferUtils.createFloatBuffer(16);
		update(aspect, zNear, zFar, fovY);
	}

	public void update(float aspect, float zNear, float zFar, float fovY) {
		matrix = ProjectionHelper.perspective(fovY, aspect, zNear, zFar);
		matrix.store(buf);
		buf.flip();
	}

	public Matrix4f getMatrix() {
		return matrix;
	}

	public FloatBuffer getMatrixBuffer() {
		return buf;
	}
}