package broadphase;

import math.VecMath;
import objects.AABB;
import objects.AABB3;
import objects.RigidBody;
import vector.Vector3f;

public class DynamicAABBTree3 extends DynamicAABBTree<Vector3f, RigidBody<Vector3f, ?, ?, ?>> {

	// main source:
	// http://allenchou.net/2014/02/game-physics-broadphase-dynamic-aabb-tree/
	public class Node3 extends Node {
		public Node3() {
			aabb = new AABB3();
		}

		public void updateAABB(float margin) {
			if (isLeaf()) {
				aabb.setMin(VecMath.subtraction(object.getGlobalMinAABB(), marginVector));
				aabb.setMax(VecMath.addition(object.getGlobalMaxAABB(), marginVector));
			} else
				aabb = leftChild.aabb.union(rightChild.aabb);
		}
	}

	public DynamicAABBTree3() {
		marginVector = new Vector3f(margin, margin, margin);
	}

	@Override
	public void add(RigidBody<Vector3f, ?, ?, ?> object) {
		// System.out.println("Insert: " + object.getAABB());
		// System.out.println("Before: ");
		if (root != null) {
			// toString(root);
			Node node = new Node3();
			node.setLeaf(object);
			node.updateAABB(margin);
			root = insertNode(node, root);
		} else {
			root = new Node3();
			root.setLeaf(object);
			root.updateAABB(margin);
		}
		System.out.println("After: ");
		toString(root);
	}

	protected Node insertNode(Node node, Node parent) {
		if (parent.isLeaf()) {
			Node newParent = new Node3();
			newParent.parent = parent.parent;
			newParent.setBranch(node, parent);
			parent = newParent;
		} else {
			final AABB<Vector3f> aabb0 = parent.leftChild.aabb;
			final AABB<Vector3f> aabb1 = parent.rightChild.aabb;
			final float volumeDiff0 = aabb0.union(node.aabb).volume() - aabb0.volume();
			final float volumeDiff1 = aabb1.union(node.aabb).volume() - aabb1.volume();

			System.out.println(aabb0.volume() + "; " + aabb1.volume() + "; " + volumeDiff0 + "; " + volumeDiff1);

			if (volumeDiff0 < volumeDiff1) {
				parent.leftChild = insertNode(node, parent.leftChild);
			} else {
				parent.rightChild = insertNode(node, parent.rightChild);
			}
		}
		parent.updateAABB(margin);
		return parent;
	}
}