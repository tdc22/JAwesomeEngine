package anim;

import objects.BaseObject;
import objects.Updateable;
import quaternion.Rotation;
import vector.Vector;

public abstract class Skeleton<L extends Vector, A extends Rotation, Anim extends Animation<L, A>>
		implements Updateable {
	BaseObject<L, A> attachedTo;
	Anim animation;
	float animationTimer = 0;
	DynamicAnimationTransition<L, A, Anim> dynamicAnimationTransition;

	public Skeleton(Anim animation) {
		setAnimation(animation);
	}

	public void setAnimation(Anim animationparam) {
		this.animation = animationparam;
		animationTimer = 0;
	}

	public abstract void setDynamicAnimation(Anim animationparam, float dynamicAnimationSpeed);

	public Anim getAnimation() {
		return animation;
	}

	public void attachTo(BaseObject<L, A> attach) {
		attachedTo = attach;
	}

	public BaseObject<L, A> attachedTo() {
		return attachedTo;
	}

	public DynamicAnimationTransition<L, A, Anim> getDynamicAnimationTransition() {
		return dynamicAnimationTransition;
	}

	public void setAnimationTimer(float timer) {
		animationTimer = timer;
	}

	public float getAnimationTimer() {
		return animationTimer;
	}

	protected void updateAnimationTimer(int delta) {
		if (dynamicAnimationTransition != null) {
			if (dynamicAnimationTransition.isInDynamicTransition(animationTimer)) {
				animationTimer += delta * dynamicAnimationTransition.getDynamicTransitionSpeed();
			} else {
				animationTimer += delta * dynamicAnimationTransition.getAnimation().getSpeed();
			}
		} else {
			animationTimer += delta * animation.getSpeed();
		}

		if (animationTimer > 1) {
			if (animation.loops) {
				if (dynamicAnimationTransition != null) {
					dynamicAnimationTransition = null;
				}
				animationTimer %= 1;
			} else {
				animationTimer = 1;
			}
		}
	}

	protected abstract void updateAnimation(float animationTimer);
}