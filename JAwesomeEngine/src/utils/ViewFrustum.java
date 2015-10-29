package utils;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import matrix.Matrix4f;

public class ViewFrustum {
	float halfHeight, halfWidth, zNear, zFar;
	Matrix4f matrix;
	FloatBuffer buf;

	public ViewFrustum(float aspect, float zNear, float zFar, float fovY) {
		// halfHeight = height/2f;//(float) (Math.tan(fovY / 360f * Math.PI) *
		// zNear);
		// halfWidth = width/2f;//halfHeight * width / height;
		// this.zNear = zNear;
		// this.zFar = zFar;
		// halfHeight = (float) (zNear * Math.tan((fovY*Math.PI/180f)/2f));
		// halfWidth = aspect * halfHeight;
		// matrix = ProjectionHelper.frustum(-halfWidth, halfWidth, -halfHeight,
		// halfHeight, zNear, zFar);
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

	// public void apply() {
	// glMatrixMode(GL_PROJECTION);
	// glLoadMatrixf(identity);
	// glFrustum(-halfWidth, halfWidth, -halfHeight, halfHeight, zNear, zFar);
	// glMatrixMode(GL_MODELVIEW);
	// }
	//
	// public void begin() {
	// glMatrixMode(GL_PROJECTION);
	// glPushMatrix();
	// glLoadMatrixf(identity);
	// glFrustum(-halfWidth, halfWidth, -halfHeight, halfHeight, zNear, zFar);
	// glMatrixMode(GL_MODELVIEW);
	// }
	//
	// public void end() {
	// glMatrixMode(GL_PROJECTION);
	// glPopMatrix();
	// glMatrixMode(GL_MODELVIEW);
	// }
}