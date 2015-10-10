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
}