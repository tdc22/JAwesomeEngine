package anim;

import java.util.ArrayList;
import java.util.List;

import objects.BaseObject;
import quaternion.Rotation;
import vector.Vector;

public abstract class CurveSkeleton<L extends Vector, A extends Rotation> extends Skeleton<L, A, CurveAnimation<L, A>> {
	List<BaseObject<L, A>> bodyparts;
	L nullvec;
	A nullrot;

	public CurveSkeleton(CurveAnimation<L, A> animation, BaseObject<L, A>[] bodypart) {
		super(animation);
		bodyparts = new ArrayList<BaseObject<L, A>>();
		for (BaseObject<L, A> part : bodypart) {
			bodyparts.add(part);
		}
	}

	@Override
	public void update(int delta) {
		updateAnimationTimer(delta);

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
	
	public List<BaseObject<L, A>> getBodyParts() {
		return bodyparts;
	}
}