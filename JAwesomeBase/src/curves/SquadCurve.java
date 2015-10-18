package curves;

import quaternion.Rotation;

public abstract class SquadCurve<A extends Rotation> extends AngularCurve<A> {
	A r0, r1, r2, r3;

	public SquadCurve(A r0, A r1, A r2, A r3) {
		this.r0 = r0;
		this.r1 = r1;
		this.r2 = r2;
		this.r3 = r3;
	}

	public A getR0() {
		return r0;
	}

	public A getR1() {
		return r1;
	}

	public A getR2() {
		return r2;
	}

	public A getR3() {
		return r3;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("SquadCurve[");
		sb.append(r0);
		sb.append(", ");
		sb.append(r1);
		sb.append(", ");
		sb.append(r2);
		sb.append(", ");
		sb.append(r3);
		sb.append(']');
		return sb.toString();
	}
}