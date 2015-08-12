package utils;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadMatrixf;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.nio.FloatBuffer;

import math.VecMath;
import matrix.Matrix4f;

import org.lwjgl.BufferUtils;

public class ViewFrustum {
	float halfHeight, halfWidth, zNear, zFar;
	Matrix4f matrix;
	FloatBuffer buf;

	public ViewFrustum(float width, float height, float zNear, float zFar,
			float fovY) {
		halfHeight = (float) (Math.tan(fovY / 360f * Math.PI) * zNear);
		halfWidth = halfHeight * width / height;
		this.zNear = zNear;
		this.zFar = zFar;
		matrix = ProjectionHelper.frustum(-halfWidth, halfWidth, -halfHeight, halfHeight, zNear, zFar);
		FloatBuffer buf = BufferUtils.createFloatBuffer(16 * 2);
		matrix.store(buf);
		buf.flip();
	}
	
	public Matrix4f getMatrix() {
		return matrix;
	}
	
	public FloatBuffer getMatrixBuffer() {
		return buf;
	}

//	public void apply() {
//		glMatrixMode(GL_PROJECTION);
//		glLoadMatrixf(identity);
//		glFrustum(-halfWidth, halfWidth, -halfHeight, halfHeight, zNear, zFar);
//		glMatrixMode(GL_MODELVIEW);
//	}
//
//	public void begin() {
//		glMatrixMode(GL_PROJECTION);
//		glPushMatrix();
//		glLoadMatrixf(identity);
//		glFrustum(-halfWidth, halfWidth, -halfHeight, halfHeight, zNear, zFar);
//		glMatrixMode(GL_MODELVIEW);
//	}
//
//	public void end() {
//		glMatrixMode(GL_PROJECTION);
//		glPopMatrix();
//		glMatrixMode(GL_MODELVIEW);
//	}
}