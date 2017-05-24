package anim;

import math.QuatMath;
import matrix.Matrix4f;
import objects.ShapedObject;
import objects.ShapedObject3;
import quaternion.Quaternionf;
import vector.Vector3f;

public class BoneAnimationSkeleton3 extends BoneAnimationSkeleton<Vector3f, Quaternionf, BoneAnimationKeyframe3> {

	public BoneAnimationSkeleton3(BoneAnimation<Vector3f, Quaternionf, BoneAnimationKeyframe3> animation,
			ShapedObject<Vector3f, Quaternionf> shape, BoneJoint rootJoint, int jointCount) {
		super(animation, shape, rootJoint, jointCount);
	}

	public BoneAnimationSkeleton3(BoneAnimation<Vector3f, Quaternionf, BoneAnimationKeyframe3> animation,
			BoneJoint rootJoint, int jointCount) {
		super(animation, new ShapedObject3(), rootJoint, jointCount);
	}

	@Override
	protected void interpolate(BoneAnimationKeyframe3 prevKeyframe, BoneAnimationKeyframe3 nextKeyframe,
			float progression) {
		for (int i = 0; i < prevKeyframe.translations.length; i++) {
			Vector3f start = prevKeyframe.translations[i];
			Vector3f end = nextKeyframe.translations[i];
			Matrix4f mat = bonematrices[i];
			mat.translateTo(start.x + (end.x - start.x) * progression, start.y + (end.y - start.y) * progression,
					start.z + (end.z - start.z) * progression);
			mat.setSubMatrix(
					QuatMath.slerp(prevKeyframe.rotations[i], nextKeyframe.rotations[i], progression).toMatrixf());
		}
	}

}
