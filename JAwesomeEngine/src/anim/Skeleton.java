package anim;

import objects.BaseObject;
import objects.Updateable;
import quaternion.Rotation;
import vector.Vector;

public abstract class Skeleton<L extends Vector, A extends Rotation, Anim extends Animation<L, A>>
		implements Updateable {
	BaseObject<L, A> attachedTo;
	Anim animation;
	DynamicAnimationTransition<L, A, Anim> dynamicAnimationTransition;

	public Skeleton(Anim animation) {
		setAnimation(animation);
	}

	public void setAnimation(Anim animationparam) {
		animation = animationparam;
	}

	public abstract void setDynamicAnimation(Anim animationparam, float dynamicAnimationSpeed);
	
	public void clearDynamicAnimation() {
		dynamicAnimationTransition = null;
	}

	public Anim getAnimation() {
		return animation;
	}
	
	public Anim getCurrentAnimation() {
		return (dynamicAnimationTransition != null) ? dynamicAnimationTransition.getAnimation() : animation;
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

	public abstract void updateAnimation();
}