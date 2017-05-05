package anim;

import quaternion.Rotation;
import vector.Vector;

public interface DynamicAnimationTransition<L extends Vector, A extends Rotation, Anim extends Animation<L, A>> {
	public void setDynamicTransitionSpeed(float speed);

	public float getDynamicTransitionSpeed();

	public Anim getAnimation();

	public boolean isInDynamicTransition(float animationTimer);
}
