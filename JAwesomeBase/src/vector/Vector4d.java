package vector;

import java.nio.FloatBuffer;

import matrix.Matrix4;

public class Vector4d extends Vector4 {
	public double x, y, z, w;

	public Vector4d() {
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}

	public Vector4d(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4d(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Vector4d(Vector2 v) {
		x = v.getX();
		y = v.getY();
		z = 0;
		w = 0;
	}

	public Vector4d(Vector3 v) {
		x = v.getX();
		y = v.getY();
		z = v.getZ();
		w = 0;
	}

	public Vector4d(Vector4 v) {
		x = v.getX();
		y = v.getY();
		z = v.getZ();
		w = v.getW();
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
			return (float) x;
		if (i == 1)
			return (float) y;
		if (i == 2)
			return (float) z;
		if (i == 3)
			return (float) w;
		return 0;
	}

	@Override
	public double getW() {
		return w;
	}

	@Override
	public float getWf() {
		return (float) w;
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
		w = 1 / w;
	}

	@Override
	public double lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}

	@Override
	public void load(FloatBuffer buf) {
		buf.put((float) x);
		buf.put((float) y);
		buf.put((float) z);
		buf.put((float) w);
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
		x *= scale.getX();
		y *= scale.getY();
		z *= scale.getZ();
		w *= scale.getW();
	}

	@Override
	public void set(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
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
		x = set.getX();
		y = set.getY();
		z = set.getZ();
		w = set.getW();
	}

	@Override
	public void setAll(double set) {
		x = set;
		y = set;
		z = set;
		w = set;
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
		this.w = w;
	}

	@Override
	public void setW(float w) {
		this.w = w;
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
		w = buf.get();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Vector4d[");
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
		x = transform.get(0, 0) * x + transform.get(0, 1) * y + transform.get(0, 2) * z + transform.get(0, 3) * w;
		y = transform.get(1, 0) * x + transform.get(1, 1) * y + transform.get(1, 2) * z + transform.get(1, 3) * w;
		z = transform.get(2, 0) * x + transform.get(2, 1) * y + transform.get(2, 2) * z + transform.get(2, 3) * w;
		w = transform.get(3, 0) * x + transform.get(3, 1) * y + transform.get(3, 2) * z + transform.get(3, 3) * w;
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
		x += trans.getX();
		y += trans.getY();
		z += trans.getZ();
		w += trans.getW();
	}

}
