package anim;

import quaternion.Quaternionf;
import vector.Vector3f;

public class BoneAnimationKeyframe3 extends BoneAnimationKeyframe<Vector3f, Quaternionf> {
	public BoneAnimationKeyframe3(float timestamp, int jointCount) {
		super(timestamp);
		initializeKeyframeData(jointCount);
	}

	@Override
	public void initializeKeyframeData(int jointCount) {
		translations = new Vector3f[jointCount];
		rotations = new Quaternionf[jointCount];
	}
}
