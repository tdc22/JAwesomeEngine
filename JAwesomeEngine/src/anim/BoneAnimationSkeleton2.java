package anim;

import math.ComplexMath;
import matrix.Matrix2f;
import matrix.Matrix4f;
import objects.ShapedObject;
import objects.ShapedObject2;
import quaternion.Complexf;
import vector.Vector2f;

public class BoneAnimationSkeleton2 extends BoneAnimationSkeleton<Vector2f, Complexf, BoneAnimationKeyframe2> {

	public BoneAnimationSkeleton2(BoneAnimation<Vector2f, Complexf, BoneAnimationKeyframe2> animation,
			ShapedObject<Vector2f, Complexf> shape, BoneJoint rootJoint, int jointCount) {
		super(animation, shape, rootJoint, jointCount);
	}

	public BoneAnimationSkeleton2(BoneAnimation<Vector2f, Complexf, BoneAnimationKeyframe2> animation,
			BoneJoint rootJoint, int jointCount) {
		super(animation, new ShapedObject2(), rootJoint, jointCount);
	}

	@Override
	protected void interpolate(BoneAnimationKeyframe2 prevKeyframe, BoneAnimationKeyframe2 nextKeyframe,
			float progression) {
		for (int i = 0; i < prevKeyframe.translations.length; i++) {
			Vector2f start = prevKeyframe.translations[i];
			Vector2f end = nextKeyframe.translations[i];
			Matrix4f mat = bonematrices[i];
			mat.translateTo(start.x + (end.x - start.x) * progression, start.y + (end.y - start.y) * progression);
			Matrix2f rotationmat = ComplexMath.slerp(prevKeyframe.rotations[i], nextKeyframe.rotations[i], progression)
					.toMatrixf();
			rotationmat.transpose();
			mat.setSubMatrix2(rotationmat);
		}
	}

}
