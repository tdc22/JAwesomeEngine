package broadphase;

import objects.AABB;
import objects.AABB3;
import objects.CollisionShape;
import utils.IntersectionLibrary;
import vector.Vector3f;

public class DynamicAABBTree3Generic<ObjectType extends CollisionShape<Vector3f, ?, ?>>
		extends DynamicAABBTree<Vector3f, ObjectType> {

	public class Node3 extends Node {
		public Node3() {
			aabb = new AABB3();
		}

		public void updateAABB(float margin) {
			if (isLeaf()) {
				Vector3f trans = object.getTranslation();
				AABB<Vector3f> objAABB = object.getAABB();
				aabb.getMin().set(trans.x + objAABB.getMin().x - margin,
						trans.y + objAABB.getMin().y - margin,
						trans.z + objAABB.getMin().z - margin);
				aabb.getMax().set(trans.x + objAABB.getMax().x + margin,
						trans.y + objAABB.getMax().y + margin,
						trans.z + objAABB.getMax().z + margin);
			} else {
				aabb.getMin().set(
						Math.min(leftChild.aabb.getMin().x,
								rightChild.aabb.getMin().x),
						Math.min(leftChild.aabb.getMin().y,
								rightChild.aabb.getMin().y),
						Math.min(leftChild.aabb.getMin().z,
								rightChild.aabb.getMin().z));
				aabb.getMax().set(
						Math.max(leftChild.aabb.getMax().x,
								rightChild.aabb.getMax().x),
						Math.max(leftChild.aabb.getMax().y,
								rightChild.aabb.getMax().y),
						Math.max(leftChild.aabb.getMax().z,
								rightChild.aabb.getMax().z));
			}
		}
	}

	@Override
	public void add(ObjectType object) {
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
			final float volumeDiff0 = mergedVolume(aabb0, node.aabb)
					- aabb0.volume();
			final float volumeDiff1 = mergedVolume(aabb1, node.aabb)
					- aabb1.volume();

			if (volumeDiff0 < volumeDiff1) {
				parent.leftChild = insertNode(node, parent.leftChild);
			} else {
				parent.rightChild = insertNode(node, parent.rightChild);
			}
		}
		parent.updateAABB(margin);
		return parent;
	}

	private float mergedVolume(AABB<Vector3f> aabb0, AABB<Vector3f> aabb1) {
		float newminx = Math.min(aabb0.getMin().x, aabb1.getMin().x);
		float newminy = Math.min(aabb0.getMin().y, aabb1.getMin().y);
		float newminz = Math.min(aabb0.getMin().z, aabb1.getMin().z);
		float newmaxx = Math.max(aabb0.getMax().x, aabb1.getMax().x);
		float newmaxy = Math.max(aabb0.getMax().y, aabb1.getMax().y);
		float newmaxz = Math.max(aabb0.getMax().z, aabb1.getMax().z);
		return (newmaxx - newminx) * (newmaxy - newminy) * (newmaxz - newminz);
	}

	@Override
	protected boolean intersect(Node node0, Node node1) {
		Vector3f trans0 = node0.object.getTranslation();
		Vector3f trans1 = node1.object.getTranslation();
		AABB<Vector3f> aabb0 = node0.object.getAABB();
		AABB<Vector3f> aabb1 = node1.object.getAABB();
		return IntersectionLibrary.intersects(trans0.x + aabb0.getMin().x,
				trans0.y + aabb0.getMin().y, trans0.z + aabb0.getMin().z,
				trans0.x + aabb0.getMax().x, trans0.y + aabb0.getMax().y,
				trans0.z + aabb0.getMax().z, trans1.x + aabb1.getMin().x,
				trans1.y + aabb1.getMin().y, trans1.z + aabb1.getMin().z,
				trans1.x + aabb1.getMax().x, trans1.y + aabb1.getMax().y,
				trans1.z + aabb1.getMax().z);
	}
}