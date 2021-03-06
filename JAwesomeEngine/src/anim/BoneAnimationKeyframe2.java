package anim;

import quaternion.Complexf;
import vector.Vector2f;

public class BoneAnimationKeyframe2 extends BoneAnimationKeyframe<Vector2f, Complexf> {
	public BoneAnimationKeyframe2(float timestamp, int jointCount) {
		super(timestamp);
		initializeKeyframeData(jointCount);
	}

	@Override
	public void initializeKeyframeData(int jointCount) {
		translations = new Vector2f[jointCount];
		rotations = new Complexf[jointCount];
	}
}
