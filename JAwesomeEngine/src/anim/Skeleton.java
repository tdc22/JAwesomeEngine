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
	L nullvec;
	A nullrot;

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

	public void setAnimationTimer(float timer) {
		animationTimer = timer;
	}

	public float getAnimationTimer() {
		return animationTimer;
	}

	protected abstract void updateAnimation(float animationTimer);
}