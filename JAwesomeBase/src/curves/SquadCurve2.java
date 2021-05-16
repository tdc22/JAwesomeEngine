package curves;

import math.ComplexMath;
import quaternion.Complexf;

public class SquadCurve2 extends SquadCurve<Complexf> {

	public SquadCurve2(Complexf r0, Complexf r1, Complexf r2, Complexf r3) {
		super(r0, r1, r2, r3);
	}

	@Override
	public Complexf getRotation(float t) {
		return ComplexMath.lerp(r1, r2, t);
	}
}