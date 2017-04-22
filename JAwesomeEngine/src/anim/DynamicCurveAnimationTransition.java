package anim;

import quaternion.Rotation;
import vector.Vector;

public class DynamicCurveAnimationTransition<L extends Vector, A extends Rotation> extends CurveAnimation<L, A> {
	float dynamicTransitionSpeed;

	public DynamicCurveAnimationTransition(float transitionspeed) {
		super(0.001f, true);
		dynamicTransitionSpeed = transitionspeed;
	}

	public DynamicCurveAnimationTransition(float animationspeed, float transitionspeed) {
		super(animationspeed, true);
		dynamicTransitionSpeed = transitionspeed;
	}

	public DynamicCurveAnimationTransition(boolean loops, float transitionspeed) {
		super(0.001f, loops);
		dynamicTransitionSpeed = transitionspeed;
	}

	public DynamicCurveAnimationTransition(float animationspeed, boolean loops, float transitionspeed) {
		super(animationspeed, loops);
		dynamicTransitionSpeed = transitionspeed;
	}

	public DynamicCurveAnimationTransition(CurveAnimation<L, A> animation, float transitionspeed) {
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