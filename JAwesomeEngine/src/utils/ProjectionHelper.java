package utils;

import matrix.Matrix4f;

public class ProjectionHelper {
	public static Matrix4f frustum(float left, float right, float bottom, float top, float znear, float zfar) {
		return new Matrix4f((2 * znear) / (float) (right - left), 0, 0, 0, 0, (2 * znear) / (float) (top - bottom), 0,
				0, (right + left) / (float) (right - left), (top + bottom) / (float) (top - bottom),
				-(zfar + znear) / (float) (zfar - znear), -1, 0, 0, -(2 * zfar * znear) / (float) (zfar - znear), 0);
	}

	public static Matrix4f ortho(float left, float right, float bottom, float top, float znear, float zfar) {
		return new Matrix4f(2 / (float) (right - left), 0, 0, 0, 0, 2 / (float) (left - bottom), 0, 0, 0, 0,
				2 / (float) (zfar - znear), 0, -(right + left) / (float) (right - left),
				-(top + bottom) / (float) (top - bottom), -(zfar + znear) / (float) (zfar - znear), 1);
	}
}