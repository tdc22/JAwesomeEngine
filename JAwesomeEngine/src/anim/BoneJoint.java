package anim;

import java.util.ArrayList;
import java.util.List;

import matrix.Matrix4f;

public class BoneJoint {
	int index;
	List<BoneJoint> children;

	Matrix4f animationTransform, localBindTransform, inverseBindTransform;

	public BoneJoint(int index, Matrix4f localBindTransform) {
		this.index = index;
		children = new ArrayList<BoneJoint>();
		this.localBindTransform = localBindTransform;
		inverseBindTransform = new Matrix4f();
	}

	public void addChild(BoneJoint child) {
		children.add(child);
	}

	public void setLocalBindTransform(Matrix4f localBindTransform) {
		this.localBindTransform = localBindTransform;
	}

	public void setAnimationTransform(Matrix4f animatedTransform) {
		this.animationTransform = animatedTransform;
	}

	public int getIndex() {
		return index;
	}

	public List<BoneJoint> getChildren() {
		return children;
	}

	public Matrix4f getAnimationTransform() {
		return animationTransform;
	}

	public Matrix4f getLocalBindTransform() {
		return localBindTransform;
	}

	public Matrix4f getInverseBindTransform() {
		return inverseBindTransform;
	}

	protected void calculateInverseBindTransform(Matrix4f parentBindTransform) {
		inverseBindTransform.set(localBindTransform);
		inverseBindTransform.transform(parentBindTransform);
		for (BoneJoint child : children) {
			child.calculateInverseBindTransform(inverseBindTransform);
		}
		inverseBindTransform.invert();
	}
}