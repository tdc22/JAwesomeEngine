package anim;

import java.util.List;

import quaternion.Rotation;
import utils.Pair;
import vector.Vector;

public class BoneAnimation<L extends Vector, A extends Rotation> extends Animation<L, A> {
	List<BoneAnimationKeyframe<L, A>> keyframes;

	public BoneAnimation() {
		super();
	}

	public BoneAnimation(float animationspeed) {
		super(animationspeed);
	}

	public BoneAnimation(boolean loops) {
		super(loops);
	}

	public BoneAnimation(float animationspeed, boolean loops) {
		super(animationspeed, loops);
	}

	public BoneAnimation(BoneAnimation<L, A> animation) {
		super();
		keyframes.addAll(animation.getKeyframes());
	}

	public void addKeyframe(BoneAnimationKeyframe<L, A> keyframe) {
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

	public List<BoneAnimationKeyframe<L, A>> getKeyframes() {
		return keyframes;
	}

	private final Pair<BoneAnimationKeyframe<L, A>, BoneAnimationKeyframe<L, A>> currentKeyframes = new Pair<BoneAnimationKeyframe<L, A>, BoneAnimationKeyframe<L, A>>(
			null, null);

	public Pair<BoneAnimationKeyframe<L, A>, BoneAnimationKeyframe<L, A>> getCurrentKeyframes(float animationTime) {
		BoneAnimationKeyframe<L, A> previousFrame = keyframes.get(0);
		BoneAnimationKeyframe<L, A> nextFrame = previousFrame;
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
}
