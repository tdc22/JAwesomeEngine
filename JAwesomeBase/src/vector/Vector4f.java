package vector;

import java.nio.FloatBuffer;

import matrix.Matrix4;

public class Vector4f extends Vector4 {
	public float x, y, z, w;

	public Vector4f() {
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}

	public Vector4f(double x, double y, double z, double w) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
		this.w = (float) w;
	}

	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4f(Vector2 v) {
		x = v.getXf();
		y = v.getYf();
		z = 0;
		w = 0;
	}

	public Vector4f(Vector3 v) {
		x = v.getXf();
		y = v.getYf();
		z = v.getZf();
		w = 0;
	}

	public Vector4f(Vector4 v) {
		x = v.getXf();
		y = v.getYf();
		z = v.getZf();
		w = v.getWf();
	}

	@Override
	public double get(int i) {
		if (i == 0)
			return x;
		if (i == 1)
			return y;
		if (i == 2)
			return z;
		if (i == 3)
			return w;
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
		if (i == 3)
			return w;
		return 0;
	}

	@Override
	public double getW() {
		return w;
	}

	@Override
	public float getWf() {
		return w;
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
		w = 1 / w;
	}

	@Override
	public double lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}

	@Override
	public void load(FloatBuffer buf) {
		buf.put(x);
		buf.put(y);
		buf.put(z);
		buf.put(w);
	}

	@Override
	public void negate() {
		x = -x;
		y = -y;
		z = -z;
		w = -w;
	}

	@Override
	public void scale(double scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		w *= scale;
	}

	@Override
	public void scale(double scalex, double scaley, double scalez, double scalew) {
		x *= scalex;
		y *= scaley;
		z *= scalez;
		w *= scalew;
	}

	@Override
	public void scale(float scale) {
		x *= scale;
		y *= scale;
		z *= scale;
		w *= scale;
	}

	@Override
	public void scale(float scalex, float scaley, float scalez, float scalew) {
		x *= scalex;
		y *= scaley;
		z *= scalez;
		w *= scalew;
	}

	@Override
	public void scale(Vector4 scale) {
		x *= scale.getXf();
		y *= scale.getYf();
		z *= scale.getZf();
		w *= scale.getWf();
	}

	@Override
	public void set(double x, double y, double z, double w) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
		this.w = (float) w;
	}

	@Override
	public void set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	@Override
	public void set(Vector4 set) {
		x = set.getXf();
		y = set.getYf();
		z = set.getZf();
		w = set.getWf();
	}

	@Override
	public void setAll(double set) {
		x = (float) set;
		y = (float) set;
		z = (float) set;
		w = (float) set;
	}

	@Override
	public void setAll(float set) {
		x = set;
		y = set;
		z = set;
		w = set;
	}

	@Override
	public void setW(double w) {
		this.w = (float) w;
	}

	@Override
	public void setW(float w) {
		this.w = w;
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
		w = buf.get();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Vector4f[");
		sb.append(x);
		sb.append(", ");
		sb.append(y);
		sb.append(", ");
		sb.append(z);
		sb.append(", ");
		sb.append(w);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public void transform(Matrix4 transform) {
		float xa = x;
		float ya = y;
		float za = z;
		x = transform.getf(0, 0) * xa + transform.getf(0, 1) * ya + transform.getf(0, 2) * za + transform.getf(0, 3) * w;
		y = transform.getf(1, 0) * xa + transform.getf(1, 1) * ya + transform.getf(1, 2) * za + transform.getf(1, 3) * w;
		z = transform.getf(2, 0) * xa + transform.getf(2, 1) * ya + transform.getf(2, 2) * za + transform.getf(2, 3) * w;
		w = transform.getf(3, 0) * xa + transform.getf(3, 1) * ya + transform.getf(3, 2) * za + transform.getf(3, 3) * w;
	}

	@Override
	public void translate(double transx, double transy, double transz, double transw) {
		x += transx;
		y += transy;
		z += transz;
		w += transw;
	}

	@Override
	public void translate(float transx, float transy, float transz, float transw) {
		x += transx;
		y += transy;
		z += transz;
		w += transw;
	}

	@Override
	public void translate(Vector4 trans) {
		x += trans.getXf();
		y += trans.getYf();
		z += trans.getZf();
		w += trans.getWf();
	}

}
