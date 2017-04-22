package anim;

import quaternion.Complexf;
import vector.Vector2f;

public class CurveAnimation2 extends CurveAnimation<Vector2f, Complexf> {
	public CurveAnimation2() {
		super();
	}

	public CurveAnimation2(float animationspeed) {
		super(animationspeed);
	}

	public CurveAnimation2(boolean loops) {
		super(loops);
	}

	public CurveAnimation2(float animationspeed, boolean loops) {
		super(animationspeed, loops);
	}

	public CurveAnimation2(CurveAnimation2 animation) {
		super(animation);
	}
}
