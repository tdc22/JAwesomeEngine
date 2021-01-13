package anim;

import math.QuatMath;
import math.VecMath;
import objects.BaseObject;
import objects.BaseObject3;
import quaternion.Quaternionf;
import vector.Vector3f;

public class CurveSkeleton3 extends CurveSkeleton<Vector3f, Quaternionf> {

	public CurveSkeleton3() {
		super(null, new BaseObject3[0]);
		nullvec = new Vector3f();
		nullrot = new Quaternionf();
	}
	
	public CurveSkeleton3(CurveAnimation<Vector3f, Quaternionf> animation) {
		super(animation, new BaseObject3[0]);
		nullvec = new Vector3f();
		nullrot = new Quaternionf();
	}

	public CurveSkeleton3(CurveAnimation<Vector3f, Quaternionf> animation,
			BaseObject<Vector3f, Quaternionf>[] bodypart) {
		super(animation, bodypart);
		nullvec = new Vector3f();
		nullrot = new Quaternionf();
	}

	@Override
	public void updateAnimation(float animationTimer) {
		for (int i = 0; i < bodyparts.size(); i++) {
			BaseObject<Vector3f, Quaternionf> part = bodyparts.get(i);
			Vector3f point = animation.getAnimationTranslationPath(i).getPoint(animationTimer);
			if(attachedTo != null) {
				point.transform(attachedTo.getRotation());
			}
			part.translate(point);
			part.rotate(animation.getAnimationRotationPath(i).getRotation(animationTimer));
		}
	}

	public void setDynamicAnimation(CurveAnimation<Vector3f, Quaternionf> animationparam, float dynamicAnimationSpeed) {
		this.animation = new CurveAnimation<Vector3f, Quaternionf>(animationparam);
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
