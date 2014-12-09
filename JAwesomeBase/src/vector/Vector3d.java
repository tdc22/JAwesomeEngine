package vector;

import java.nio.FloatBuffer;

import matrix.Matrix3;

public class Vector3d extends Vector3 {
	public double x, y, z;

	public Vector3d() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d(Vector2 v) {
		x = v.getX();
		y = v.getY();
		z = 0;
	}

	public Vector3d(Vector3 v) {
		x = v.getX();
		y = v.getY();
		z = v.getZ();
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
			return (float) x;
		if (i == 1)
			return (float) y;
		if (i == 2)
			return (float) z;
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
	public double getZ() {
		return z;
	}

	@Override
	public float getZf() {
		return (float) z;
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
		buf.put((float) x);
		buf.put((float) y);
		buf.put((float) z);
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
		x *= scale.getX();
		y *= scale.getY();
		z *= scale.getZ();
	}

	@Override
	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void set(Vector3 set) {
		x = set.getX();
		y = set.getY();
		z = set.getZ();
	}

	@Override
	public void setAll(double set) {
		x = set;
		y = set;
		z = set;
	}

	@Override
	public void setAll(float set) {
		x = set;
		y = set;
		z = set;
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
	public void setZ(double z) {
		this.z = z;
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
		sb.append("Vector3d[");
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
		Vector3 v1 = transform.getRow(0);
		Vector3 v2 = transform.getRow(1);
		Vector3 v3 = transform.getRow(2);

		x = v1.getX() * x + v1.getY() * y + v1.getZ() * z;
		y = v2.getX() * x + v2.getY() * y + v2.getZ() * z;
		z = v3.getX() * x + v3.getY() * y + v3.getZ() * z;
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
		x += trans.getX();
		y += trans.getY();
		z += trans.getZ();
	}
}
