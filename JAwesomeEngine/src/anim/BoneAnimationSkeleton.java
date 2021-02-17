package anim;

import math.VecMath;
import matrix.Matrix4f;
import objects.ObjectDataAttributesVectorf;
import objects.ObjectDataAttributesVectori;
import objects.ShapedObject;
import quaternion.Rotation;
import utils.Pair;
import vector.Vector;
import vector.Vector4f;

public abstract class BoneAnimationSkeleton<L extends Vector, A extends Rotation, B extends BoneAnimationKeyframe<L, A>>
		extends Skeleton<L, A, BoneAnimation<L, A, B>> {
	ShapedObject<L, A> shape;

	protected static final int JOINT_INDICES_POSITION = 4;
	protected static final int JOINT_WEIGHTS_POSITION = 5;

	protected ObjectDataAttributesVectori jointindices;
	protected ObjectDataAttributesVectorf<Vector4f> jointweights;

	private BoneJoint rootJoint;
	private int jointCount;

	protected Matrix4f[] bonematrices;

	public BoneAnimationSkeleton(BoneAnimation<L, A, B> animation, ShapedObject<L, A> shape, BoneJoint rootJoint,
			int jointCount) {
		super(animation);
		this.shape = shape;
		this.rootJoint = rootJoint;
		this.jointCount = jointCount;

		jointindices = new ObjectDataAttributesVectori(JOINT_INDICES_POSITION, 4, new int[] {}, true);
		jointweights = new ObjectDataAttributesVectorf<Vector4f>(JOINT_WEIGHTS_POSITION, 4, new float[] {}, true);

		shape.addDataAttribute(jointindices);
		shape.addDataAttribute(jointweights);

		bonematrices = new Matrix4f[jointCount];
		for (int i = 0; i < jointCount; i++) {
			bonematrices[i] = new Matrix4f();
		}
		rootJoint.calculateInverseBindTransform(new Matrix4f());
	}

	@Override
	public void update(int delta) {
		animation.updateAnimationTimer(delta);
		updateAnimation();
	}

	@Override
	public void setDynamicAnimation(BoneAnimation<L, A, B> animationparam, float dynamicAnimationSpeed) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAnimation() {
		Pair<B, B> currentKeyframes = animation.getCurrentKeyframes(animation.animationTimer);
		float currentTime = animation.animationTimer - currentKeyframes.getFirst().getTimestamp();
		float keyframeDifference = currentKeyframes.getSecond().getTimestamp()
				- currentKeyframes.getFirst().getTimestamp();
		float progression = currentTime / keyframeDifference;
		interpolate(currentKeyframes.getFirst(), currentKeyframes.getSecond(), progression);
		applyPoseToJoints(rootJoint, new Matrix4f());
	}

	protected abstract void interpolate(B prevKeyframe, B nextKeyframe, float progression);

	public int getJointCount() {
		return jointCount;
	}

	public BoneJoint getRootJoint() {
		return rootJoint;
	}

	public Matrix4f[] getBoneMatrices() {
		return bonematrices;
	}

	public Matrix4f[] getJointTransforms() {
		Matrix4f[] jointMatrices = new Matrix4f[jointCount];
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
	}

	private void addJointsToArray(BoneJoint headJoint, Matrix4f[] jointMatrices) {
		jointMatrices[headJoint.index] = headJoint.getAnimationTransform();
		for (BoneJoint childJoint : headJoint.children) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}

	public ObjectDataAttributesVectori getJointIndicesDataAttributes() {
		return jointindices;
	}

	public ObjectDataAttributesVectorf<Vector4f> getJointWeightsDataAttributes() {
		return jointweights;
	}

	public ShapedObject<L, A> getShape() {
		return shape;
	}

	private void applyPoseToJoints(BoneJoint joint, Matrix4f parentTransform) {
		Matrix4f currentLocalTransform = bonematrices[joint.getIndex()];
		Matrix4f currentTransform = VecMath.transformMatrix(currentLocalTransform, parentTransform);
		for (BoneJoint childJoint : joint.children) {
			applyPoseToJoints(childJoint, currentTransform);
		}
		// TODO: optimize!
		currentTransform = VecMath.transformMatrix(joint.getInverseBindTransform(), currentTransform);
		joint.setAnimationTransform(currentTransform);
	}
}