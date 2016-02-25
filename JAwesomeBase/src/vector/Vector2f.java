package vector;

import java.nio.FloatBuffer;

import matrix.Matrix2;
import quaternion.Complex;

public class Vector2f extends Vector2 {
	public float x, y;

	public Vector2f() {
		x = 0;
		y = 0;
	}

	public Vector2f(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2 v) {
		x = v.getXf();
		y = v.getYf();
	}

	@Override
	public double get(int i) {
		if (i == 0)
			return x;
		if (i == 1)
			return y;
		return 0;
	}

	@Override
	public float getf(int i) {
		if (i == 0)
			return x;
		if (i == 1)
			return y;
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
	public double getY() {
		return y;
	}

	@Override
	public float getYf() {
		return y;
	}

	@Override
	public void invert() {
		x = 1 / x;
		y = 1 / y;
	}

	@Override
	public double lengthSquared() {
		return x * x + y * y;
	}

	@Override
	public void load(FloatBuffer buf) {
		buf.put(x);
		buf.put(y);
	}

	@Override
	public void negate() {
		x = -x;
		y = -y;
	}

	@Override
	public void scale(double scale) {
		x *= scale;
		y *= scale;
	}

	@Override
	public void scale(double scalex, double scaley) {
		x *= scalex;
		y *= scaley;
	}

	@Override
	public void scale(float scale) {
		x *= scale;
		y *= scale;
	}

	@Override
	public void scale(float scalex, float scaley) {
		x *= scalex;
		y *= scaley;
	}

	@Override
	public void scale(Vector2 scale) {
		x *= scale.getXf();
		y *= scale.getYf();
	}

	@Override
	public void set(double x, double y) {
		this.x = (float) x;
		this.y = (float) y;
	}

	@Override
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(Vector2 set) {
		x = set.getXf();
		y = set.getYf();
	}

	@Override
	public void setAll(double set) {
		x = (float) set;
		y = (float) set;
	}

	@Override
	public void setAll(float set) {
		x = set;
		y = set;
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
	public void setY(double y) {
		this.y = (float) y;
	}

	@Override
	public void setY(float y) {
		this.y = y;
	}

	@Override
	public void store(FloatBuffer buf) {
		x = buf.get();
		y = buf.get();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Vector2f[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public void transform(Matrix2 transform) {
		float xa = x;
		x = transform.getf(0, 0) * xa + transform.getf(0, 1) * y;
		y = transform.getf(1, 0) * xa + transform.getf(1, 1) * y;
	}
	
	@Override
	public void transform(Complex transform) {
		float xa = x;
		x = xa * transform.getRealf() + y * transform.getImaginaryf();
		y = xa * -transform.getImaginaryf() + y * transform.getRealf();
	}

	@Override
	public void translate(double transx, double transy) {
		x += transx;
		y += transy;
	}

	@Override
	public void translate(float transx, float transy) {
		x += transx;
		y += transy;
	}

	@Override
	public void translate(Vector2 trans) {
		x += trans.getXf();
		y += trans.getYf();
	}
}