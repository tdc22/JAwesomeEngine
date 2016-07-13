package broadphase;

import math.VecMath;
import objects.AABB;
import objects.AABB2;
import objects.RigidBody;
import vector.Vector2f;

public class DynamicAABBTree2 extends DynamicAABBTree<Vector2f, RigidBody<Vector2f, ?, ?, ?>> {

	// main source:
	// http://allenchou.net/2014/02/game-physics-broadphase-dynamic-aabb-tree/
	public class Node2 extends Node {
		public Node2() {
			aabb = new AABB2();
		}

		public void updateAABB(float margin) {
			if (isLeaf()) {
				aabb.setMin(VecMath.subtraction(object.getGlobalMinAABB(), marginVector));
				aabb.setMax(VecMath.addition(object.getGlobalMaxAABB(), marginVector));
			} else
				aabb = leftChild.aabb.union(rightChild.aabb);
		}
	}

	public DynamicAABBTree2() {
		marginVector = new Vector2f(margin, margin);
	}

	@Override
	public void add(RigidBody<Vector2f, ?, ?, ?> object) {
		// System.out.println("Insert: " + object.getAABB());
		// System.out.println("Before: ");
		if (root != null) {
			// toString(root);
			Node node = new Node2();
			node.setLeaf(object);
			node.updateAABB(margin);
			root = insertNode(node, root);
		} else {
			root = new Node2();
			root.setLeaf(object);
			root.updateAABB(margin);
		}
		// System.out.println("After: ");
		// toString(root);
	}

	protected Node insertNode(Node node, Node parent) {
		if (parent.isLeaf()) {
			Node newParent = new Node2();
			newParent.parent = parent.parent;
			newParent.setBranch(node, parent);
			parent = newParent;
		} else {
			final AABB<Vector2f> aabb0 = parent.leftChild.aabb;
			final AABB<Vector2f> aabb1 = parent.rightChild.aabb;
			System.out.println(aabb0.volume() + "; " + aabb1.volume());
			final float volumeDiff0 = aabb0.union(node.aabb).volume() - aabb0.volume();
			final float volumeDiff1 = aabb1.union(node.aabb).volume() - aabb1.volume();

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