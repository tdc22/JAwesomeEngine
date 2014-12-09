package vector;

import java.nio.FloatBuffer;

import matrix.Matrix2;

public class Vector2d extends Vector2 {
	public double x, y;

	public Vector2d() {
		x = 0;
		y = 0;
	}

	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2d(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2d(Vector2 v) {
		x = v.getX();
		y = v.getY();
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
			return (float) x;
		if (i == 1)
			return (float) y;
		return 0;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public float getXf() {
		return (float) x;
	}

	@Override
	public double getY() {
		return y;
	}

	@Override
	public float getYf() {
		return (float) y;
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
		buf.put((float) x);
		buf.put((float) y);
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
		x *= scale.getX();
		y *= scale.getY();
	}

	@Override
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void set(Vector2 set) {
		x = set.getX();
		y = set.getY();
	}

	@Override
	public void setAll(double set) {
		x = set;
		y = set;
	}

	@Override
	public void setAll(float set) {
		x = set;
		y = set;
	}

	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public void setX(float x) {
		this.x = x;
	}

	@Override
	public void setY(double y) {
		this.y = y;
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
		sb.append("Vector2d[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public void transform(Matrix2 transform) {
		Vector2 v1 = transform.getRow(0);
		Vector2 v2 = transform.getRow(1);

		x = v1.getX() * x + v1.getY() * y;
		y = v2.getX() * x + v2.getY() * y;
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
		x += trans.getX();
		y += trans.getY();
	}
}