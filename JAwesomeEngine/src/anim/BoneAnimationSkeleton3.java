package anim;

import math.QuatMath;
import matrix.Matrix4f;
import objects.ShapedObject;
import quaternion.Quaternionf;
import vector.Vector3f;

public class BoneAnimationSkeleton3 extends BoneAnimationSkeleton<Vector3f, Quaternionf> {

	public BoneAnimationSkeleton3(
			BoneAnimation<Vector3f, Quaternionf> animation,
			ShapedObject<Vector3f, Quaternionf> shape, BoneJoint rootJoint,
			int jointCount) {
		super(animation, shape, rootJoint, jointCount);
	}

	@Override
	protected void interpolate(
			BoneAnimationKeyframe<Vector3f, Quaternionf> prevKeyframe,
			BoneAnimationKeyframe<Vector3f, Quaternionf> nextKeyframe,
			float progression) {
		for (int i = 0; i < prevKeyframe.translations.size(); i++) {
			Vector3f start = prevKeyframe.translations.get(i);
			Vector3f end = nextKeyframe.translations.get(i);
			Matrix4f mat = bonematrices.get(i);
			mat.translateTo(start.x + (end.x - start.x) * progression, start.y + (end.y - start.y) * progression, start.z + (end.z - start.z) * progression);
			mat.setSubMatrix(QuatMath.slerp(prevKeyframe.rotations.get(i), nextKeyframe.rotations.get(i), progression).toMatrixf());
		}
	}

}
