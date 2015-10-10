package anim;

import objects.BaseObject;
import quaternion.Complexf;
import vector.Vector2f;

public class Skeleton2 extends Skeleton<Vector2f, Complexf> {

	public Skeleton2(Animation<Vector2f, Complexf> animation, BaseObject... bodypart) {
		super(animation, bodypart);
	}

	@Override
	protected void updateAnimation(float animationTimer) {
		for (int i = 0; i < bodyparts.size(); i++) {
			BaseObject part = bodyparts.get(i);
			part.translate(animation.getAnimationTranslationPath(i).getPoint(animationTimer));
			part.rotate(animation.getAnimationRotationPath(i).getRotation(animationTimer));
		}
	}

}
