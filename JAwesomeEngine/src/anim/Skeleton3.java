package anim;

import objects.BaseObject;
import vector.Vector3f;

public class Skeleton3 extends Skeleton<Vector3f> {

	public Skeleton3(Animation<Vector3f> animation, BaseObject... bodypart) {
		super(animation, bodypart);
	}

	@Override
	protected void updateAnimation(float animationTimer) {
		for (int i = 0; i < bodyparts.size(); i++) {
			BaseObject part = bodyparts.get(i);
			part.translate(animation.getAnimationPath(i).getPoint(animationTimer));
		}
	}

}
