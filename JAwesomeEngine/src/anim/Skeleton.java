package anim;

import java.util.ArrayList;
import java.util.List;

import objects.BaseObject;
import objects.Updateable;
import vector.Vector;
import vector.Vector3f;

public abstract class Skeleton<L extends Vector> implements Updateable {
	List<BaseObject> bodyparts;
	BaseObject attachedTo;
	Animation<L> animation;
	float animationTimer = 0;

	public Skeleton(Animation<L> animation, BaseObject... bodypart) {
		bodyparts = new ArrayList<BaseObject>();
		for (BaseObject part : bodypart) {
			bodyparts.add(part);
		}
		setAnimation(animation);
	}

	public void setAnimation(Animation<L> animation) {
		this.animation = animation;
		animationTimer = 0;
	}

	@Override
	public void update(int delta) {
		animationTimer += delta * animation.getSpeed();
		if (animationTimer > 1) {
			if (animation.loops) {
				animationTimer %= 1;
			} else {
				animationTimer = 1;
			}
		}

		if (attachedTo != null) {
			for (BaseObject part : bodyparts) {
				part.translateTo(attachedTo.getTranslation());
			}
		} else {
			Vector3f nullvec = new Vector3f();
			for (BaseObject part : bodyparts) {
				part.translateTo(nullvec);
			}
		}

		updateAnimation(animationTimer);
	}

	public void attachTo(BaseObject attach) {
		attachedTo = attach;
	}

	public BaseObject attachedTo() {
		return attachedTo;
	}

	public void addBodyPart(BaseObject bodypart) {
		bodyparts.add(bodypart);
	}

	protected abstract void updateAnimation(float animationTimer);
}