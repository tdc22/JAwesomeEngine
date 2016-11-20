package anim;

import quaternion.Rotation;
import vector.Vector;

public class DynamicAnimationTransition<L extends Vector, A extends Rotation>
		extends Animation<L, A> {
	float dynamicTransitionSpeed;

	public DynamicAnimationTransition(float transitionspeed) {
		super(0.001f, true);
		dynamicTransitionSpeed = transitionspeed;
	}

	public DynamicAnimationTransition(float animationspeed,
			float transitionspeed) {
		super(animationspeed, true);
		dynamicTransitionSpeed = transitionspeed;
	}

	public DynamicAnimationTransition(boolean loops, float transitionspeed) {
		super(0.001f, loops);
		dynamicTransitionSpeed = transitionspeed;
	}

	public DynamicAnimationTransition(float animationspeed, boolean loops,
			float transitionspeed) {
		super(animationspeed, loops);
		dynamicTransitionSpeed = transitionspeed;
	}

	public DynamicAnimationTransition(Animation<L, A> animation,
			float transitionspeed) {
		super(animation);
		dynamicTransitionSpeed = transitionspeed;
	}

	public float getDynamicTransitionSpeed() {
		return dynamicTransitionSpeed;
	}

	public void setDynamicTransitionSpeed(float speed) {
		dynamicTransitionSpeed = speed;
	}
}