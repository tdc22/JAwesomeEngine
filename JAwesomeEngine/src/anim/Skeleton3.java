package anim;

import objects.BaseObject;
import quaternion.Quaternionf;
import vector.Vector3f;

public class Skeleton3 extends Skeleton<Vector3f, Quaternionf> {

	public Skeleton3(Animation<Vector3f, Quaternionf> animation, BaseObject<Vector3f, Quaternionf>[] bodypart) {
		super(animation, bodypart);
	}

	@Override
	protected void updateAnimation(float animationTimer) {
		for (int i = 0; i < bodyparts.size(); i++) {
			BaseObject<Vector3f, Quaternionf> part = bodyparts.get(i);
			part.translate(animation.getAnimationTranslationPath(i).getPoint(animationTimer));
			part.rotate(animation.getAnimationRotationPath(i).getRotation(animationTimer));
		}
	}

}
