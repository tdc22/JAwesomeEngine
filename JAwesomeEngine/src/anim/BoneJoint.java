package anim;

import java.util.List;

import matrix.Matrix4f;

public abstract class BoneJoint {
	int index;
	List<BoneJoint> children;

	Matrix4f animatedTransform, localBindTransform, inverseBindTransform;

	public BoneJoint(int index, Matrix4f localBindTransform) {
		this.index = index;
		this.localBindTransform = localBindTransform;
		inverseBindTransform = new Matrix4f();
	}

	public void addChild(BoneJoint child) {
		children.add(child);
	}

	public Matrix4f getAnimatedTransform() {
		return animatedTransform;
	}

	public Matrix4f getInverseBindTransform() {
		return inverseBindTransform;
	}

	protected void calculateInverseBindTransform(Matrix4f parentBindTransform) {
		inverseBindTransform.set(parentBindTransform);
		inverseBindTransform.transform(localBindTransform);
		for (BoneJoint child : children) {
			child.calculateInverseBindTransform(inverseBindTransform);
		}
		inverseBindTransform.invert();
	}
}