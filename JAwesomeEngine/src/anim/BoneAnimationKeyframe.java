package anim;

import java.util.ArrayList;
import java.util.List;

import quaternion.Rotation;
import vector.Vector;

public class BoneAnimationKeyframe<L extends Vector, A extends Rotation> {
	float timestamp;
	List<L> translations;
	List<A> rotations;

	public BoneAnimationKeyframe(float timestamp) {
		this.timestamp = timestamp;
		translations = new ArrayList<L>();
		rotations = new ArrayList<A>();
	}

	public float getTimestamp() {
		return timestamp;
	}
}
