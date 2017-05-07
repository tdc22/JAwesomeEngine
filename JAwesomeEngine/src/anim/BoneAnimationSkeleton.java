package anim;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import matrix.Matrix4f;
import objects.ObjectDataAttributesVectorf;
import objects.ObjectDataAttributesVectori;
import objects.ShapedObject;
import quaternion.Rotation;
import utils.Pair;
import vector.Vector;
import vector.Vector4f;

public abstract class BoneAnimationSkeleton<L extends Vector, A extends Rotation> extends Skeleton<L, A, BoneAnimation<L, A>> {
	ShapedObject<L, A> shape;

	protected static final int JOINT_INDICES_POSITION = 4;
	protected static final int JOINT_WEIGHTS_POSITION = 5;

	protected ObjectDataAttributesVectori jointindices;
	protected ObjectDataAttributesVectorf<Vector4f> jointweights;

	private BoneJoint rootJoint;
	private int jointCount;
	
	protected List<Matrix4f> bonematrices;

	public BoneAnimationSkeleton(BoneAnimation<L, A> animation, ShapedObject<L, A> shape, BoneJoint rootJoint,
			int jointCount) {
		super(animation);
		this.shape = shape;
		this.rootJoint = rootJoint;
		this.jointCount = jointCount;

		jointindices = new ObjectDataAttributesVectori(JOINT_INDICES_POSITION, 4, new int[] { 0, 0, 0 }, true);
		jointweights = new ObjectDataAttributesVectorf<Vector4f>(JOINT_WEIGHTS_POSITION, 4, new float[] { 0, 0, 0 },
				true);

		shape.addDataAttribute(jointindices);
		shape.addDataAttribute(jointweights);

		bonematrices = new ArrayList<Matrix4f>();
		rootJoint.calculateInverseBindTransform(new Matrix4f());
	}

	@Override
	public void update(int delta) {
		updateAnimationTimer(delta);
		updateAnimation(animationTimer);
	}

	@Override
	public void setDynamicAnimation(BoneAnimation<L, A> animationparam, float dynamicAnimationSpeed) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateAnimation(float animationTimer) {
		Pair<BoneAnimationKeyframe<L, A>, BoneAnimationKeyframe<L, A>> currentKeyframes = animation.getCurrentKeyframes(animationTimer);
		float progression = (animationTimer - currentKeyframes.getFirst().getTimestamp()) / (currentKeyframes.getSecond().getTimestamp() - currentKeyframes.getFirst().getTimestamp());
		interpolate(currentKeyframes.getFirst(), currentKeyframes.getSecond(), progression);
		applyPoseToJoints(, rootJoint, new Matrix4f());
	}
	
	protected abstract void interpolate(BoneAnimationKeyframe<L, A> prevKeyframe, BoneAnimationKeyframe<L, A> nextKeyframe, float progression);

	private void addJointsToArray(BoneJoint parentJoint, Matrix4f[] jointMatrices) {
		jointMatrices[parentJoint.index] = parentJoint.getAnimatedTransform();
		for (BoneJoint childJoint : parentJoint.children)
			addJointsToArray(childJoint, jointMatrices);
	}
	
	private void applyPoseToJoints(Map<String, Matrix4f> currentPose, BoneJoint joint, Matrix4f parentTransform) {
		Matrix4f currentLocalTransform = currentPose.get(joint.name);
		Matrix4f currentTransform = VecMath.transformMatrix(parentTransform, currentLocalTransform);
		for (BoneJoint childJoint : joint.children) {
			applyPoseToJoints(currentPose, childJoint, currentTransform);
		}
		currentTransform.transform(joint.getInverseBindTransform());
		joint.setAnimatedTransform(currentTransform);
	}
}