package utils;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glFrustum;
import static org.lwjgl.opengl.GL11.glLoadMatrix;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

import java.nio.FloatBuffer;

import math.VecMath;

import org.lwjgl.BufferUtils;

public class ViewFrustum {
	float halfWidth, halfHeight, zNear, zFar;
	private FloatBuffer identity;

	public ViewFrustum(float width, float height, float zNear, float zFar,
			float fovY) {
		halfHeight = (float) (Math.tan(fovY / 360f * Math.PI) * zNear);
		halfWidth = halfHeight * width / (float) height;
		this.zNear = zNear;
		this.zFar = zFar;

		identity = BufferUtils.createFloatBuffer(16 * 4);
		VecMath.identityMatrix().store(identity);
		identity.flip();
	}

	public void apply() {
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(identity);
		glFrustum(-halfWidth, halfWidth, -halfHeight, halfHeight, zNear, zFar);
		glMatrixMode(GL_MODELVIEW);
	}

	public void begin() {
		glMatrixMode(GL_PROJECTION);
		glPushMatrix();
		glLoadMatrix(identity);
		glFrustum(-halfWidth, halfWidth, -halfHeight, halfHeight, zNear, zFar);
		glMatrixMode(GL_MODELVIEW);
	}

	public void end() {
		glMatrixMode(GL_PROJECTION);
		glPopMatrix();
		glMatrixMode(GL_MODELVIEW);
	}
}