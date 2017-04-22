package anim;

import quaternion.Quaternionf;
import vector.Vector3f;

public class CurveAnimation3 extends CurveAnimation<Vector3f, Quaternionf> {
	public CurveAnimation3() {
		super();
	}

	public CurveAnimation3(float animationspeed) {
		super(animationspeed);
	}

	public CurveAnimation3(boolean loops) {
		super(loops);
	}

	public CurveAnimation3(float animationspeed, boolean loops) {
		super(animationspeed, loops);
	}

	public CurveAnimation3(CurveAnimation3 animation) {
		super(animation);
	}
}
