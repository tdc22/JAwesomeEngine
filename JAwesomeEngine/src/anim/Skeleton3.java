package anim;

import math.QuatMath;
import math.VecMath;
import objects.BaseObject;
import objects.BaseObject3;
import quaternion.Quaternionf;
import vector.Vector3f;

public class Skeleton3 extends Skeleton<Vector3f, Quaternionf> {

	public Skeleton3(Animation<Vector3f, Quaternionf> animation) {
		super(animation, new BaseObject3[0]);
		nullvec = new Vector3f();
		nullrot = new Quaternionf();
	}

	public Skeleton3(Animation<Vector3f, Quaternionf> animation, BaseObject<Vector3f, Quaternionf>[] bodypart) {
		super(animation, bodypart);
		nullvec = new Vector3f();
		nullrot = new Quaternionf();
	}

	@Override
	protected void updateAnimation(float animationTimer) {
		for (int i = 0; i < bodyparts.size(); i++) {
			BaseObject<Vector3f, Quaternionf> part = bodyparts.get(i);
			part.translate(animation.getAnimationTranslationPath(i).getPoint(animationTimer));
			part.rotate(animation.getAnimationRotationPath(i).getRotation(animationTimer));
		}
	}

	public void setDynamicAnimation(Animation<Vector3f, Quaternionf> animationparam) {
		this.animation = new Animation<Vector3f, Quaternionf>(animationparam);
		for (int i = 0; i < bodyparts.size(); i++) {
			BaseObject<Vector3f, Quaternionf> bodypart = bodyparts.get(i);
			animation.getAnimationTranslationPath(i).getCurves().get(0)
					.setStartPoint(VecMath.subtraction(bodypart.getTranslation(), attachedTo.getTranslation()));
			animation.getAnimationRotationPath(i).getCurves().get(0)
					.setStartRotation(QuatMath.substraction(bodypart.getRotation(), attachedTo.getRotation()));
		}
		animationTimer = 0;
	}

}
