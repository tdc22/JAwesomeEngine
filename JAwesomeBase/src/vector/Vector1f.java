package vector;

import java.nio.FloatBuffer;

import matrix.Matrix1;

public class Vector1f extends Vector1 {
	public float x;

	public Vector1f() {
		x = 0;
	}

	public Vector1f(double x) {
		this.x = (float) x;
	}

	public Vector1f(float x) {
		this.x = x;
	}

	public Vector1f(Vector1 v) {
		x = v.getXf();
	}

	@Override
	public double get(int i) {
		if (i == 0)
			return x;
		return 0;
	}

	@Override
	public float getf(int i) {
		if (i == 0)
			return x;
		return 0;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public float getXf() {
		return x;
	}

	@Override
	public void invert() {
		x = 1 / x;
	}

	@Override
	public double lengthSquared() {
		return x * x;
	}

	@Override
	public void load(FloatBuffer buf) {
		// TODO Auto-generated method stub

	}

	@Override
	public void negate() {
		x = -x;
	}

	@Override
	public void scale(double scale) {
		x *= scale;
	}

	@Override
	public void scale(float scale) {
		x *= scale;
	}

	@Override
	public void scale(Vector1 scale) {
		x *= scale.getXf();
	}

	@Override
	public void set(double x) {
		this.x = (float) x;
	}

	@Override
	public void set(float x) {
		this.x = x;
	}

	@Override
	public void set(Vector1 set) {
		x = set.getXf();
	}

	@Override
	public void setAll(double set) {
		x = (float) set;
	}

	@Override
	public void setAll(float set) {
		x = set;
	}

	@Override
	public void setX(double x) {
		this.x = (float) x;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void store(FloatBuffer buf) {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Vector1f[");
		sb.append(x);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public void transform(Matrix1 transform) {
		x = transform.getf(0, 0) * x;
	}

	@Override
	public void translate(double transx) {
		x += transx;
	}

	@Override
	public void translate(float transx) {
		x += transx;
	}

	@Override
	public void translate(Vector1 trans) {
		x += trans.getXf();
	}

	@Override
	public void set(int i, double value) {
		if (i == 0) {
			x = (float) value;
		}
	}

	@Override
	public void set(int i, float value) {
		if (i == 0) {
			x = value;
		}
	}
}