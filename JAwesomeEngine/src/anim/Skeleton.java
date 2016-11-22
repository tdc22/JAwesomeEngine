package anim;

import java.util.ArrayList;
import java.util.List;

import objects.BaseObject;
import objects.Updateable;
import quaternion.Rotation;
import vector.Vector;

public abstract class Skeleton<L extends Vector, A extends Rotation> implements Updateable {
	List<BaseObject<L, A>> bodyparts;
	BaseObject<L, A> attachedTo;
	DynamicAnimationTransition<L, A> dynamicAnimationTransition;
	Animation<L, A> animation;
	float animationTimer = 0;
	L nullvec;
	A nullrot;

	public Skeleton(Animation<L, A> animation, BaseObject<L, A>[] bodypart) {
		bodyparts = new ArrayList<BaseObject<L, A>>();
		for (BaseObject<L, A> part : bodypart) {
			bodyparts.add(part);
		}
		setAnimation(animation);
	}

	public void setAnimation(Animation<L, A> animationparam) {
		this.animation = animationparam;
		animationTimer = 0;
	}

	public abstract void setDynamicAnimation(Animation<L, A> animationparam, float dynamicAnimationSpeed);

	public Animation<L, A> getAnimation() {
		return animation;
	}

	public DynamicAnimationTransition<L, A> getDynamicAnimation() {
		return dynamicAnimationTransition;
	}

	@Override
	public void update(int delta) {
		if (dynamicAnimationTransition != null) {
			if (dynamicAnimationTransition.getAnimationTranslationPath(0).getCurveNum(animationTimer) == 0) {
				animationTimer += delta * dynamicAnimationTransition.getDynamicTransitionSpeed();
			} else {
				animationTimer += delta * dynamicAnimationTransition.getSpeed();
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

		if (attachedTo != null) {
			for (BaseObject<L, A> part : bodyparts) {
				part.translateTo(attachedTo.getTranslation());
				part.rotateTo(attachedTo.getRotation());
			}
		} else {
			for (BaseObject<L, A> part : bodyparts) {
				part.translateTo(nullvec);
				part.rotateTo(nullrot);
			}
		}

		updateAnimation(animationTimer);
	}

	public void attachTo(BaseObject<L, A> attach) {
		attachedTo = attach;
	}

	public BaseObject<L, A> attachedTo() {
		return attachedTo;
	}

	public void addBodyPart(BaseObject<L, A> bodypart) {
		bodyparts.add(bodypart);
	}

	public void setAnimationTimer(float timer) {
		animationTimer = timer;
	}

	public float getAnimationTimer() {
		return animationTimer;
	}

	protected abstract void updateAnimation(float animationTimer);
}