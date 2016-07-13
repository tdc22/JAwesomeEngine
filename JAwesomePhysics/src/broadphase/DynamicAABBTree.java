package broadphase;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import objects.AABB;
import objects.CollisionShape;
import utils.Pair;
import vector.Vector;

public abstract class DynamicAABBTree<L extends Vector, ObjectType extends CollisionShape<L, ?, ?>>
		implements Broadphase<L, ObjectType> {
	public abstract class Node {
		Node parent, leftChild, rightChild;

		boolean childrenCrossed;
		AABB<L> aabb;
		ObjectType object;

		public Node getLeftChild() {
			return leftChild;
		}

		public Node getRightChild() {
			return rightChild;
		}

		public AABB<L> getAABB() {
			return aabb;
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

		public void setLeaf(ObjectType object) {
			this.object = object;

			leftChild = null;
			rightChild = null;
		}

		public abstract void updateAABB(float margin);
	}

	protected final float margin = 0.1f;
	protected L marginVector;

	Node root;

	final List<Node> invalidNodes;

	final List<Pair<ObjectType, ObjectType>> overlaps;

	public DynamicAABBTree() {
		invalidNodes = new ArrayList<Node>();
		overlaps = new ArrayList<Pair<ObjectType, ObjectType>>();
	}

	protected abstract Node insertNode(Node node, Node parent);

	protected void clearChildrenCrossFlagHelper(Node node) {
		node.childrenCrossed = false;
		if (!node.isLeaf()) {
			clearChildrenCrossFlagHelper(node.leftChild);
			clearChildrenCrossFlagHelper(node.rightChild);
		}
	}

	protected void computePairsHelper(Node node0, Node node1) {
		if (node0.isLeaf()) {
			if (node1.isLeaf()) {
				if (node0.object.getGlobalAABB().intersects(node1.object.getGlobalAABB())) {
					overlaps.add(new Pair<ObjectType, ObjectType>(node0.object, node1.object));
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

	protected void crossChildren(Node node) {
		if (!node.childrenCrossed) {
			computePairsHelper(node.leftChild, node.rightChild);
			node.childrenCrossed = true;
		}
	}

	@Override
	public Set<Pair<ObjectType, ObjectType>> getOverlaps() {
		return new LinkedHashSet<Pair<ObjectType, ObjectType>>(overlaps);
	}

	@Override
	public void remove(ObjectType object) {
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

	public Node getRoot() {
		return root;
	}

	protected void toString(Node n) {
		System.out.print(n.aabb + "; " + n.isLeaf() + "; " + n.leftChild + "; " + n.rightChild + "; " + n);
		if (n.isLeaf()) {
			System.out.print("; " + n.object.getAABB());
		}
		System.out.println();
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
		// toString(root);

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
					root = insertNode(node, root);
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
	public Set<ObjectType> raycast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ObjectType> getObjects() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(ObjectType obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addListener(BroadphaseListener<L, ObjectType> listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeListener(BroadphaseListener<L, ObjectType> listener) {
		// TODO Auto-generated method stub

	}
}