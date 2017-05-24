package anim;

import quaternion.Rotation;
import vector.Vector;

public abstract class BoneAnimationKeyframe<L extends Vector, A extends Rotation> {
	float timestamp;
	L[] translations;
	A[] rotations;

	public BoneAnimationKeyframe(float timestamp) {
		this.timestamp = timestamp;
	}

	public void setTimestamp(float timestamp) {
		this.timestamp = timestamp;
	}

	public float getTimestamp() {
		return timestamp;
	}

	public L[] getTranslations() {
		return translations;
	}

	public A[] getRotations() {
		return rotations;
	}

	public abstract void initializeKeyframeData(int jointCount);
}
