package broadphase;

import objects.AABB;
import objects.AABB2;
import objects.CollisionShape;
import quaternion.Complexf;
import utils.IntersectionLibrary;
import utils.RotationMath;
import vector.Vector2f;

public class DynamicAABBTree2Generic<ObjectType extends CollisionShape<Vector2f, Complexf, ?>>
		extends DynamicAABBTree<Vector2f, ObjectType> {

	public class Node2 extends Node {
		public Node2() {
			aabb = new AABB2();
		}

		public void updateAABB(float margin) {
			if (isLeaf()) {
				if (object.getRotationCenter().x != 0 || object.getRotationCenter().y != 0) {
					RotationMath.calculateRotationOffsetAABB2(object, margin, aabb);
				} else {
					Vector2f trans = object.getTranslation();
					AABB<Vector2f> objAABB = object.getAABB();
					aabb.getMin().set(trans.x + objAABB.getMin().x - margin, trans.y + objAABB.getMin().y - margin);
					aabb.getMax().set(trans.x + objAABB.getMax().x + margin, trans.y + objAABB.getMax().y + margin);
				}
			} else {
				aabb.getMin().set(Math.min(leftChild.aabb.getMin().x, rightChild.aabb.getMin().x),
						Math.min(leftChild.aabb.getMin().y, rightChild.aabb.getMin().y));
				aabb.getMax().set(Math.max(leftChild.aabb.getMax().x, rightChild.aabb.getMax().x),
						Math.max(leftChild.aabb.getMax().y, rightChild.aabb.getMax().y));
			}
		}
	}

	@Override
	public void add(ObjectType object) {
		if (root != null) {
			Node node = new Node2();
			node.setLeaf(object);
			node.updateAABB(margin);
			root = insertNode(node, root);
			nodemapping.put(object, node);
		} else {
			root = new Node2();
			root.setLeaf(object);
			root.updateAABB(margin);
			nodemapping.put(object, root);
		}
		objects.add(object);
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

			final float volumeDiff0 = mergedVolume(aabb0, node.aabb) - aabb0.volume();
			final float volumeDiff1 = mergedVolume(aabb1, node.aabb) - aabb1.volume();

			if (volumeDiff0 < volumeDiff1) {
				parent.leftChild = insertNode(node, parent.leftChild);
			} else {
				parent.rightChild = insertNode(node, parent.rightChild);
			}
		}
		parent.updateAABB(margin);
		return parent;
	}

	private float mergedVolume(AABB<Vector2f> aabb0, AABB<Vector2f> aabb1) {
		float newminx = Math.min(aabb0.getMin().x, aabb1.getMin().x);
		float newminy = Math.min(aabb0.getMin().y, aabb1.getMin().y);
		float newmaxx = Math.max(aabb0.getMax().x, aabb1.getMax().x);
		float newmaxy = Math.max(aabb0.getMax().y, aabb1.getMax().y);
		return (newmaxx - newminx) * (newmaxy - newminy);
	}

	private final AABB2 intersectAABB0 = new AABB2(), intersectAABB1 = new AABB2();

	@Override
	protected boolean intersect(Node node0, Node node1) {
		RotationMath.calculateRotationOffsetAABB2(node0.object, intersectAABB0);
		RotationMath.calculateRotationOffsetAABB2(node1.object, intersectAABB1);
		return IntersectionLibrary.intersects(intersectAABB0, intersectAABB1);
	}

	@Override
	protected boolean aabbContainsObject(AABB<Vector2f> aabb, ObjectType object) {
		RotationMath.calculateRotationOffsetAABB2(object, intersectAABB0);
		return aabb.contains(intersectAABB0);
	}
}