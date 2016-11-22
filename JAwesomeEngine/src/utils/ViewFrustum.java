package utils;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import matrix.Matrix4f;

public class ViewFrustum {
	Matrix4f matrix;
	FloatBuffer buf;

	public ViewFrustum(float aspect, float zNear, float zFar, float fovY) {
		matrix = ProjectionHelper.perspective(fovY, aspect, zNear, zFar);
		buf = BufferUtils.createFloatBuffer(16);
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