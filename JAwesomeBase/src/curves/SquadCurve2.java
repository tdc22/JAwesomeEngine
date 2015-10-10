package curves;

import math.ComplexMath;
import quaternion.Complexf;

public class SquadCurve2 extends SquadCurve<Complexf> {

	public SquadCurve2(Complexf p0, Complexf p1, Complexf p2, Complexf p3) {
		super(p0, p1, p2, p3);
	}

	@Override
	public Complexf getRotation(float t) {
		return ComplexMath.squad(r0, r1, r2, r3, t);
	}

}