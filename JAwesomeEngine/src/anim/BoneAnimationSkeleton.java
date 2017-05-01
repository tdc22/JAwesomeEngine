package anim;

import objects.ShapedObject;
import quaternion.Rotation;
import vector.Vector;

public class BoneAnimationSkeleton<L extends Vector, A extends Rotation> extends Skeleton<L, A, BoneAnimation<L, A>> {
	ShapedObject<L, A> shape;

	public BoneAnimationSkeleton(BoneAnimation<L, A> animation, ShapedObject<L, A> shape) {
		super(animation);
		this.shape = shape;
	}

	@Override
	public void update(int delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDynamicAnimation(BoneAnimation<L, A> animationparam, float dynamicAnimationSpeed) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateAnimation(float animationTimer) {
		// TODO Auto-generated method stub

	}

}
