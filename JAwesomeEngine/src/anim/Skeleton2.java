package anim;

import objects.BaseObject;
import objects.BaseObject2;
import quaternion.Complexf;
import vector.Vector2f;

public class Skeleton2 extends Skeleton<Vector2f, Complexf> {
	boolean mirroredX = false;
	boolean mirroredY = false;
	boolean invertRotation = false;

	public Skeleton2(Animation<Vector2f, Complexf> animation) {
		super(animation, new BaseObject2[0]);
	}

	public Skeleton2(Animation<Vector2f, Complexf> animation, BaseObject<Vector2f, Complexf>[] bodypart) {
		super(animation, bodypart);
	}

	@Override
	protected void updateAnimation(float animationTimer) {
		for (int i = 0; i < bodyparts.size(); i++) {
			BaseObject<Vector2f, Complexf> part = bodyparts.get(i);
			Vector2f trans = animation.getAnimationTranslationPath(i).getPoint(animationTimer);
			if (mirroredX)
				trans.x = -trans.x;
			if (mirroredY)
				trans.x = -trans.x;
			part.translate(trans);
			Complexf rot = animation.getAnimationRotationPath(i).getRotation(animationTimer);
			if (invertRotation)
				rot.invert();
			part.rotate(rot);
		}
	}

	public void mirrorX() {
		mirroredX = !mirroredX;
	}

	public boolean isMirroredX() {
		return mirroredX;
	}

	public void setMirroredX(boolean mirroredX) {
		this.mirroredX = mirroredX;
	}

	public void mirrorY() {
		mirroredY = !mirroredY;
	}

	public boolean isMirroredY() {
		return mirroredY;
	}

	public void setMirroredY(boolean mirroredY) {
		this.mirroredY = mirroredY;
	}

	public void invertRotation() {
		invertRotation = !invertRotation;
	}

	public boolean isRotationInverted() {
		return invertRotation;
	}

	public void setRotationInverted(boolean invertRotation) {
		this.invertRotation = invertRotation;
	}
}