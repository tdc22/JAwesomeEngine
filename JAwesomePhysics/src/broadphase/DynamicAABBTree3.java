package broadphase;

import objects.AABB;
import objects.AABB3;
import objects.RigidBody;
import vector.Vector3f;

public class DynamicAABBTree3 extends DynamicAABBTree<Vector3f, RigidBody<Vector3f, ?, ?, ?>> {

	public class Node3 extends Node {
		public Node3() {
			aabb = new AABB3();
		}

		public void updateAABB(float margin) {
			if (isLeaf()) {
				Vector3f globalMinAABB = object.getGlobalMinAABB();
				Vector3f globalMaxAABB = object.getGlobalMaxAABB();
				aabb.getMin().set(globalMinAABB.x - margin, globalMinAABB.y - margin, globalMinAABB.z - margin);
				aabb.getMax().set(globalMaxAABB.x + margin, globalMaxAABB.y + margin, globalMaxAABB.z + margin);
			} else
				aabb = leftChild.aabb.union(rightChild.aabb);
		}
	}

	@Override
	public void add(RigidBody<Vector3f, ?, ?, ?> object) {
		if (root != null) {
			Node node = new Node3();
			node.setLeaf(object);
			node.updateAABB(margin);
			root = insertNode(node, root);
		} else {
			root = new Node3();
			root.setLeaf(object);
			root.updateAABB(margin);
		}
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