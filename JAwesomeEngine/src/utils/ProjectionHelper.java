package utils;

import matrix.Matrix4f;

public class ProjectionHelper {
	public static Matrix4f perspective(float fovy, float aspect, float znear, float zfar) {
		float ymax = znear * (float) Math.tan(fovy * Math.PI / 360f);
		float xmax = ymax * aspect;
		return frustum(-xmax, xmax, -ymax, ymax, znear, zfar);
	}

	public static Matrix4f frustum(float left, float right, float bottom, float top, float znear, float zfar) {
		float temp = 2.0f * znear;
		float temp2 = right - left;
		float temp3 = top - bottom;
		float temp4 = zfar - znear;
		return new Matrix4f(temp / temp2, 0, 0, 0, 0, temp / (float) temp3, 0, 0, (right + left) / temp2,
				(top + bottom) / temp3, -(zfar + znear) / temp4, -1, 0, 0, -(temp * zfar) / temp4, 0);
	}

	public static Matrix4f ortho(float left, float right, float bottom, float top, float znear, float zfar) {
		return new Matrix4f(2 / (float) (right - left), 0, 0, 0, 0, 2 / (float) (left - bottom), 0, 0, 0, 0,
				2 / (float) (zfar - znear), 0, -(right + left) / (float) (right - left),
				-(top + bottom) / (float) (top - bottom), -(zfar + znear) / (float) (zfar - znear), 1);
	}
}