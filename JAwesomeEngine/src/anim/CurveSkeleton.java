package anim;

import java.util.ArrayList;
import java.util.List;

import objects.BaseObject;
import quaternion.Rotation;
import vector.Vector;

public abstract class CurveSkeleton<L extends Vector, A extends Rotation> extends Skeleton<L, A, CurveAnimation<L, A>> {
	List<BaseObject<L, A>> bodyparts;
	DynamicCurveAnimationTransition<L, A> dynamicAnimationTransition;

	public CurveSkeleton(CurveAnimation<L, A> animation, BaseObject<L, A>[] bodypart) {
		super(animation);
		bodyparts = new ArrayList<BaseObject<L, A>>();
		for (BaseObject<L, A> part : bodypart) {
			bodyparts.add(part);
		}
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

	public void addBodyPart(BaseObject<L, A> bodypart) {
		bodyparts.add(bodypart);
	}

	public DynamicCurveAnimationTransition<L, A> getDynamicAnimation() {
		return dynamicAnimationTransition;
	}
}