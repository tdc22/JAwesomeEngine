package anim;

import matrix.Matrix4f;
import objects.ObjectDataAttributesVectorf;
import objects.ObjectDataAttributesVectori;
import objects.ShapedObject;
import quaternion.Rotation;
import vector.Vector;
import vector.Vector4f;

public class BoneAnimationSkeleton<L extends Vector, A extends Rotation> extends Skeleton<L, A, BoneAnimation<L, A>> {
	ShapedObject<L, A> shape;

	protected static final int JOINT_INDICES_POSITION = 4;
	protected static final int JOINT_WEIGHTS_POSITION = 5;

	protected ObjectDataAttributesVectori jointindices;
	protected ObjectDataAttributesVectorf<Vector4f> jointweights;

	private BoneJoint rootJoint;
	private int jointCount;

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

	}

	private void addJointsToArray(BoneJoint parentJoint, Matrix4f[] jointMatrices) {
		jointMatrices[parentJoint.index] = parentJoint.getAnimatedTransform();
		for (BoneJoint childJoint : parentJoint.children)
			addJointsToArray(childJoint, jointMatrices);
	}
}
