package broadphase;

import java.util.Set;

import math.VecMath;
import objects.AABB;
import objects.RigidBody;
import utils.Pair;
import vector.Vector3f;

public class DynamicAABBTree implements Broadphase<Vector3f> {

	// main source:
	// http://allenchou.net/2014/02/game-physics-broadphase-dynamic-aabb-tree/

	protected class Node {
		Node parent, leftChild, rightChild;

		boolean childrenCrossed;
		AABB<Vector3f> aabb, data;

		public Node getSibling() {
			return this == parent.leftChild ? parent.rightChild
					: parent.leftChild;
		}

		public boolean isLeaf() {
			return leftChild == null;
		}

		public void setBranch(Node a, Node b) {
			a.parent = this;
			b.parent = this;

			leftChild = a;
			rightChild = b;
		}

		public void setLeaf(AABB<Vector3f> data) {
			this.data = data;

			leftChild = null;
			rightChild = null;
		}

		public void updateAABB(float margin) {
			if (isLeaf()) {
				Vector3f marginVector = new Vector3f(margin, margin, margin);
				aabb.setMin(VecMath.subtraction(data.getMin(), marginVector));
				aabb.setMax(VecMath.addition(data.getMax(), marginVector));
			} else
				aabb = leftChild.aabb.union(rightChild.aabb);
		}
	}

	@Override
	public void add(RigidBody<Vector3f, ?, ?, ?> object) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>> getOverlaps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<RigidBody<Vector3f, ?, ?, ?>> raycast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(RigidBody<Vector3f, ?, ?, ?> object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
}