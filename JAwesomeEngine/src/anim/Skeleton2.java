package anim;

import objects.BaseObject;
import vector.Vector2f;

public class Skeleton2 extends Skeleton<Vector2f> {

	public Skeleton2(Animation<Vector2f> animation, BaseObject... bodypart) {
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
