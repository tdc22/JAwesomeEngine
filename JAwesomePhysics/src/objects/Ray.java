package objects;

import vector.Vector;

public abstract class Ray<L extends Vector> {
	L pos, dir;

	public Ray(L pos, L dir) {
		this.pos = pos;
		this.dir = dir;
	}

	public L getPosition() {
		return pos;
	}

	public L getDirection() {
		return dir;
	}

	public void setPosition(L pos) {
		this.pos = pos;
	}

	public void setDirection(L dir) {
		this.dir = dir;
	}

	public void set(L pos, L dir) {
		this.pos = pos;
		this.dir = dir;
	}

	public abstract L pointOnRay(float lambda);

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(128);
		sb.append("Ray[");
		sb.append(pos.toString());
		sb.append(", ");
		sb.append(dir.toString());
		sb.append("]");
		return sb.toString();
	}

}
