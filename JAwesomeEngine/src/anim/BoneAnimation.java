package anim;

import java.util.ArrayList;
import java.util.List;

import quaternion.Rotation;
import utils.Pair;
import vector.Vector;

public class BoneAnimation<L extends Vector, A extends Rotation, B extends BoneAnimationKeyframe<L, A>>
		extends Animation<L, A> {
	List<B> keyframes;

	public BoneAnimation() {
		super();
		keyframes = new ArrayList<B>();
	}

	public BoneAnimation(float animationspeed) {
		super(animationspeed);
		keyframes = new ArrayList<B>();
	}

	public BoneAnimation(boolean loops) {
		super(loops);
		keyframes = new ArrayList<B>();
	}

	public BoneAnimation(float animationspeed, boolean loops) {
		super(animationspeed, loops);
		keyframes = new ArrayList<B>();
	}

	public BoneAnimation(BoneAnimation<L, A, B> animation) {
		super();
		keyframes = new ArrayList<B>();
		keyframes.addAll(animation.getKeyframes());
	}

	public void addKeyframe(B keyframe) {
		boolean inserted = false;
		for (int i = keyframes.size() - 1; i >= 0; i--) {
			if (keyframes.get(i).timestamp < keyframe.timestamp) {
				keyframes.add(i + 1, keyframe);
				inserted = true;
				break;
			}
		}
		if (!inserted) {
			keyframes.add(0, keyframe);
		}
	}

	public List<B> getKeyframes() {
		return keyframes;
	}

	private final Pair<B, B> currentKeyframes = new Pair<B, B>(null, null);

	public Pair<B, B> getCurrentKeyframes(float animationTime) {
		B previousFrame = keyframes.get(0);
		B nextFrame = previousFrame;
		for (int i = 1; i < keyframes.size(); i++) {
			nextFrame = keyframes.get(i);
			if (nextFrame.getTimestamp() >= animationTime) {
				break;
			}
			previousFrame = nextFrame;
		}
		currentKeyframes.set(previousFrame, nextFrame);
		return currentKeyframes;
	}

	public void normalizeTimestamps() {
		float lastKeyframeTime = keyframes.get(keyframes.size() - 1).getTimestamp();
		for (B keyframe : keyframes) {
			keyframe.setTimestamp(keyframe.getTimestamp() / lastKeyframeTime);
		}
	}
}
