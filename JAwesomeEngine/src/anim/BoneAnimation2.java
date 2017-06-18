package anim;

import quaternion.Complexf;
import vector.Vector2f;

public class BoneAnimation2 extends BoneAnimation<Vector2f, Complexf, BoneAnimationKeyframe2> {
	public BoneAnimation2() {
		super();
	}

	public BoneAnimation2(float animationspeed) {
		super(animationspeed);
	}

	public BoneAnimation2(boolean loops) {
		super(loops);
	}

	public BoneAnimation2(float animationspeed, boolean loops) {
		super(animationspeed, loops);
	}

	public BoneAnimation2(BoneAnimation2 animation) {
		super(animation);
	}
}
