package anim;

import quaternion.Quaternionf;
import vector.Vector3f;

public class BoneAnimation3 extends BoneAnimation<Vector3f, Quaternionf, BoneAnimationKeyframe3> {
	public BoneAnimation3() {
		super();
	}

	public BoneAnimation3(float animationspeed) {
		super(animationspeed);
	}

	public BoneAnimation3(boolean loops) {
		super(loops);
	}

	public BoneAnimation3(float animationspeed, boolean loops) {
		super(animationspeed, loops);
	}

	public BoneAnimation3(BoneAnimation3 animation) {
		super(animation);
	}
}
