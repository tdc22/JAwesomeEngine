package vector;

import java.nio.FloatBuffer;

import math.VecMath;
import matrix.Matrix3;
import quaternion.Quaternion;

public class Vector3f extends Vector3 {
	public float x, y, z;

	public Vector3f() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Vector3f(double x, double y, double z) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(Vector2 v) {
		x = v.getXf();
		y = v.getYf();
		z = 0;
	}

	public Vector3f(Vector3 v) {
		x = v.getXf();
		y = v.getYf();
		z = v.getZf();
	}

	@Override
	public double get(int i) {
		if (i == 0)
			return x;
		if (i == 1)
			return y;
		if (i == 2)
			return z;
		return 0;
	}

	@Override
	public float getf(int i) {
		if (i == 0)
			return x;
		if (i == 1)
			return y;
		if (i == 2)
			return z;
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
	public double getZ() {
		return z;
	}

	@Override
	public float getZf() {
		return z;
	}

	@Override
	public void invert() {
		x = 1 / x;
		y = 1 / y;
		z = 1 / z;
	}

	@Override
	public double lengthSquared() {
		return x * x + y * y + z * z;
	}

	@Override
	public void load(FloatBuffer buf) {
		buf.put(x);
		buf.put(y);
		buf.put(z);
	}

	@Override
	public void negate() {
		x = -x;
		y = -y;
		z = -z;
	}

	@Override
	public void scale(double scale) {
		x *= scale;
		y *= scale;
		z *= scale;
	}

	@Override
	public void scale(double scalex, double scaley, double scalez) {
		x *= scalex;
		y *= scaley;
		z *= scalez;
	}

	@Override
	public void scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
	}

	@Override
	public void scale(float scalex, float scaley, float scalez) {
		x *= scalex;
		y *= scaley;
		z *= scalez;
	}

	@Override
	public void scale(Vector3 scale) {
		x *= scale.getXf();
		y *= scale.getYf();
		z *= scale.getZf();
	}

	@Override
	public void set(double x, double y, double z) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}

	@Override
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void set(Vector3 set) {
		x = set.getXf();
		y = set.getYf();
		z = set.getZf();
	}

	@Override
	public void setAll(double set) {
		x = (float) set;
		y = (float) set;
		z = (float) set;
	}

	@Override
	public void setAll(float set) {
		x = set;
		y = set;
		z = set;
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
	public void setZ(double z) {
		this.z = (float) z;
	}

	@Override
	public void setZ(float z) {
		this.z = z;
	}

	@Override
	public void store(FloatBuffer buf) {
		x = buf.get();
		y = buf.get();
		z = buf.get();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Vector3f[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(", ");
		sb.append(z);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public void transform(Matrix3 transform) {
		float xa = x;
		float ya = y;
		x = transform.getf(0, 0) * xa + transform.getf(0, 1) * ya + transform.getf(0, 2) * z;
		y = transform.getf(1, 0) * xa + transform.getf(1, 1) * ya + transform.getf(1, 2) * z;
		z = transform.getf(2, 0) * xa + transform.getf(2, 1) * ya + transform.getf(2, 2) * z;
	}

	@Override
	public void transform(Quaternion transform) {
		Vector3f u = new Vector3f(transform.getQ1f(), transform.getQ2f(), transform.getQ3f());
		float s = transform.getQ0f();
		float dotUV = VecMath.dotproduct(u, this);
		float dotUU = VecMath.dotproduct(u, u);
		float xa = x;
		float ya = y;
		x = u.getXf() * 2 * dotUV + xa * (s * s - dotUU) + (u.getYf() * z - u.getZf() * ya) * 2 * s;
		y = u.getYf() * 2 * dotUV + ya * (s * s - dotUU) + (u.getZf() * xa - u.getXf() * z) * 2 * s;
		z = u.getZf() * 2 * dotUV + z * (s * s - dotUU) + (u.getXf() * ya - u.getYf() * xa) * 2 * s;
	}

	@Override
	public void translate(double transx, double transy, double transz) {
		x += transx;
		y += transy;
		z += transz;
	}

	@Override
	public void translate(float transx, float transy, float transz) {
		x += transx;
		y += transy;
		z += transz;
	}

	@Override
	public void translate(Vector3 trans) {
		x += trans.getXf();
		y += trans.getYf();
		z += trans.getZf();
	}
}