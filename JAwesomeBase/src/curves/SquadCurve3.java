package curves;

import math.QuatMath;
import quaternion.Quaternionf;

public class SquadCurve3 extends SquadCurve<Quaternionf> {

	public SquadCurve3(Quaternionf r0, Quaternionf r1, Quaternionf r2, Quaternionf r3) {
		super(r0, r1, r2, r3);
	}

	@Override
	public Quaternionf getRotation(float t) {
		return QuatMath.squad(r0, r1, r2, r3, t);
	}

}