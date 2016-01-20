package broadphase;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import math.VecMath;
import objects.AABB;
import objects.AABB3;
import objects.RigidBody;
import utils.Pair;
import vector.Vector3f;

public class DynamicAABBTree implements Broadphase<Vector3f, RigidBody<Vector3f, ?, ?, ?>> {

	// main source:
	// http://allenchou.net/2014/02/game-physics-broadphase-dynamic-aabb-tree/

	protected class Node {
		Node parent, leftChild, rightChild;

		boolean childrenCrossed;
		AABB<Vector3f> aabb;
		RigidBody<Vector3f, ?, ?, ?> object;

		public Node() {
			aabb = new AABB3();
		}

		public Node getSibling() {
			return this == parent.leftChild ? parent.rightChild : parent.leftChild;
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

		public void setLeaf(RigidBody<Vector3f, ?, ?, ?> object) {
			this.object = object;

			leftChild = null;
			rightChild = null;
		}

		public void updateAABB(float margin) {
			if (isLeaf()) {
				Vector3f marginVector = new Vector3f(margin, margin, margin);
				aabb.setMin(VecMath.subtraction(object.getGlobalMinAABB(), marginVector));
				aabb.setMax(VecMath.addition(object.getGlobalMaxAABB(), marginVector));
			} else
				aabb = leftChild.aabb.union(rightChild.aabb);
		}
	}

	Node root;
	private final float margin = 0.1f;
	List<Node> invalidNodes;
	List<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>> overlaps;

	public DynamicAABBTree() {
		invalidNodes = new ArrayList<Node>();
		overlaps = new ArrayList<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>>();
	}

	@Override
	public void add(RigidBody<Vector3f, ?, ?, ?> object) {
		System.out.println("Before: ");
		if (root != null)
			toString(root);
		if (root != null) {
			Node node = new Node();
			node.setLeaf(object);
			node.updateAABB(margin);
			root = insertNode(node, root);
		} else {
			root = new Node();
			root.setLeaf(object);
			root.updateAABB(margin);
		}
		System.out.println("After: ");
		toString(root);
	}

	private void clearChildrenCrossFlagHelper(Node node) {
		node.childrenCrossed = false;
		if (!node.isLeaf()) {
			clearChildrenCrossFlagHelper(node.leftChild);
			clearChildrenCrossFlagHelper(node.rightChild);
		}
	}

	private void computePairsHelper(Node node0, Node node1) {
		if (node0.isLeaf()) {
			if (node1.isLeaf()) {
				if (node0.object.getGlobalAABB().intersects(node1.object.getGlobalAABB())) {
					overlaps.add(new Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>(node0.object,
							node1.object));
				}
			} else {
				crossChildren(node1);
				computePairsHelper(node0, node1.leftChild);
				computePairsHelper(node0, node1.rightChild);
			}
		} else {
			if (node1.isLeaf()) {
				crossChildren(node0);
				computePairsHelper(node0.leftChild, node1);
				computePairsHelper(node0.rightChild, node1);
			} else {
				crossChildren(node0);
				crossChildren(node1);
				computePairsHelper(node0.leftChild, node1.leftChild);
				computePairsHelper(node0.leftChild, node1.rightChild);
				computePairsHelper(node0.rightChild, node1.leftChild);
				computePairsHelper(node0.rightChild, node1.rightChild);
			}
		}
	}

	private void crossChildren(Node node) {
		if (!node.childrenCrossed) {
			computePairsHelper(node.leftChild, node.rightChild);
			node.childrenCrossed = true;
		}
	}

	@Override
	public Set<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>> getOverlaps() {
		return new LinkedHashSet<Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>>(overlaps);
	}

	private Node insertNode(Node node, Node parent) {
		if (parent.isLeaf()) {
			Node newParent = new Node();
			newParent.parent = parent.parent;
			System.out.println("1: " + parent.isLeaf());
			newParent.setBranch(node, parent);
			parent = newParent;
			System.out.println("2: " + parent.isLeaf());
		} else {
			final AABB<Vector3f> aabb0 = parent.leftChild.aabb;
			final AABB<Vector3f> aabb1 = parent.rightChild.aabb;
			final float volumeDiff0 = aabb0.union(node.aabb).volume() - aabb0.volume();
			final float volumeDiff1 = aabb1.union(node.aabb).volume() - aabb1.volume();

			if (volumeDiff0 < volumeDiff1)
				insertNode(node, parent.leftChild);
			else
				insertNode(node, parent.rightChild);
		}
		parent.updateAABB(margin);
		return parent;
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

	private void removeNode(Node node) {
		Node parent = node.parent;
		if (parent != null) {
			Node sibling = node.getSibling();
			if (parent.parent != null) {
				sibling.parent = parent.parent;
				if (parent == parent.parent.leftChild)
					parent.parent.leftChild = sibling;
				else
					parent.parent.rightChild = sibling;
			} else {
				root = sibling;
				sibling.parent = null;
			}
			node = null;
			parent = null;
		} else {
			root = null;
			node = null;
		}
	}

	private void toString(Node n) {
		System.out.println(n.aabb + "; " + n.isLeaf() + "; " + n.leftChild + "; " + n.rightChild);
		if (!n.isLeaf()) {
			toString(n.leftChild);
			toString(n.rightChild);
		}
	}

	@Override
	public void update() {
		updateAABBTree();

		overlaps.clear();

		System.out.println("-----------------------------");
		toString(root);

		if (root == null || root.isLeaf())
			return;

		clearChildrenCrossFlagHelper(root);

		computePairsHelper(root.leftChild, root.rightChild);
	}

	private void updateAABBTree() {
		if (root != null) {
			if (root.isLeaf())
				root.updateAABB(margin);
			else {
				invalidNodes.clear();
				updateNodeHelper(root, invalidNodes);

				for (Node node : invalidNodes) {
					Node parent = node.parent;
					Node sibling = node.getSibling();
					sibling.parent = (parent.parent != null) ? parent.parent : null;
					if (parent.parent != null) {
						if (parent == parent.parent.leftChild)
							parent.parent.leftChild = sibling;
						else
							parent.parent.rightChild = sibling;
					} else
						root = sibling;

					node.updateAABB(margin);
					insertNode(node, root);
				}
				invalidNodes.clear();
			}
		}
	}

	private void updateNodeHelper(Node node, List<Node> invalidNodes) {
		if (node.isLeaf()) {
			if (!node.aabb.contains(node.object.getGlobalAABB())) {
				invalidNodes.add(node);
			}
		} else {
			updateNodeHelper(node.leftChild, invalidNodes);
			updateNodeHelper(node.rightChild, invalidNodes);
		}
	}

	@Override
	public List<RigidBody<Vector3f, ?, ?, ?>> getObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(RigidBody<Vector3f, ?, ?, ?> obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addListener(BroadphaseListener<Vector3f, RigidBody<Vector3f, ?, ?, ?>> listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(BroadphaseListener<Vector3f, RigidBody<Vector3f, ?, ?, ?>> listener) {
		// TODO Auto-generated method stub

	}
}