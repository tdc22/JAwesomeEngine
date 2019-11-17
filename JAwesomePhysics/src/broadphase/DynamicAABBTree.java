package broadphase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import objects.AABB;
import objects.CollisionShape;
import objects.Ray;
import utils.IntersectionLibrary;
import utils.Pair;
import vector.Vector;

public abstract class DynamicAABBTree<L extends Vector, ObjectType extends CollisionShape<L, ?, ?>>
		implements Broadphase<L, ObjectType> {
	// main source:
	// http://allenchou.net/2014/02/game-physics-broadphase-dynamic-aabb-tree/

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

	Node root;

	final List<ObjectType> objects;
	
	final HashMap<ObjectType, Node> nodemapping;

	final List<Node> invalidNodes;

	final List<Pair<ObjectType, ObjectType>> overlaps;

	final Set<Pair<ObjectType, ObjectType>> overlapSet;

	final List<BroadphaseListener<L, ObjectType>> listeners;

	public DynamicAABBTree() {
		objects = new ArrayList<ObjectType>();
		nodemapping = new HashMap<ObjectType, Node>();
		invalidNodes = new ArrayList<Node>();
		overlaps = new ArrayList<Pair<ObjectType, ObjectType>>();
		overlapSet = new HashSet<Pair<ObjectType, ObjectType>>();
		listeners = new ArrayList<BroadphaseListener<L, ObjectType>>();
	}

	protected abstract Node insertNode(Node node, Node parent);

	protected void clearChildrenCrossFlagHelper(Node node) {
		node.childrenCrossed = false;
		if (!node.isLeaf()) {
			clearChildrenCrossFlagHelper(node.leftChild);
			clearChildrenCrossFlagHelper(node.rightChild);
		}
	}

	final Pair<ObjectType, ObjectType> tempPair = new Pair<ObjectType, ObjectType>(null, null);

	protected void computePairsHelper(Node node0, Node node1) {
		if (node0.isLeaf()) {
			if (node1.isLeaf()) {
				tempPair.set(node0.object, node1.object);
				if (intersect(node0, node1)) {
					if (!overlapSet.contains(tempPair)) {
						Pair<ObjectType, ObjectType> overlap = new Pair<ObjectType, ObjectType>(node0.object,
								node1.object);
						overlaps.add(overlap);
						overlapSet.add(overlap);

						for (BroadphaseListener<L, ObjectType> listener : listeners) {
							listener.overlapStarted(overlap.getFirst(), overlap.getSecond());
						}
					}
				} else {
					if (overlapSet.contains(tempPair)) {
						overlaps.remove(tempPair);
						overlapSet.remove(tempPair);

						for (BroadphaseListener<L, ObjectType> listener : listeners) {
							listener.overlapEnded(tempPair.getFirst(), tempPair.getSecond());
						}
					}
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

	protected abstract boolean intersect(Node node0, Node node1);

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
		Node remove = nodemapping.remove(object);
		if (remove != null)
			removeNode(remove);

		Iterator<Pair<ObjectType, ObjectType>> it = overlapSet.iterator();
		while (it.hasNext()) {
			if (it.next().contains(object)) {
				it.remove();
			}
		}

		for (int i = 0; i < overlaps.size();)
			if (overlaps.get(i).contains(object))
				overlaps.remove(i);
			else
				i++;
		objects.remove(object);
	}

	protected void removeNode(Node node) {
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
			System.out.print("; " + n.object.getAABB() + "; " + n.object.getTranslation());
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
	
	protected abstract boolean aabbContainsObject(AABB<L> aabb, ObjectType object);

	private void updateNodeHelper(Node node, List<Node> invalidNodes) {
		if (node.isLeaf()) {
			if (!aabbContainsObject(node.aabb, node.object)) {
				invalidNodes.add(node);
			}
		} else {
			updateNodeHelper(node.leftChild, invalidNodes);
			updateNodeHelper(node.rightChild, invalidNodes);
		}
	}

	private final LinkedList<Node> raycastQueue = new LinkedList<Node>(); // TODO:
																			// different
																			// (faster?)
																			// implementation?
	HashSet<ObjectType> raycastResults = new HashSet<ObjectType>(); // TODO:
																	// other
																	// set-implementation?

	@Override
	public ObjectType raycast(Ray<L> ray) {
		// TODO
		return null;
	}

	@Override
	public Set<ObjectType> raycastAll(Ray<L> ray) {
		raycastQueue.clear();
		if (root != null) {
			raycastQueue.push(root);
		}
		raycastResults.clear();

		while (!raycastQueue.isEmpty()) {
			Node node = raycastQueue.pop();
			AABB<L> aabb = node.isLeaf() ? node.object.getGlobalAABB() : node.getAABB();

			if (IntersectionLibrary.intersects(ray, aabb)) {
				if (node.isLeaf()) {
					raycastResults.add(node.object);
				} else {
					raycastQueue.push(node.leftChild);
					raycastQueue.push(node.rightChild);
				}
			}
		}

		return raycastResults;
	}

	@Override
	public List<ObjectType> getObjects() {
		return objects;
	}

	@Override
	public boolean contains(ObjectType obj) {
		return objects.contains(obj);
	}

	@Override
	public void addListener(BroadphaseListener<L, ObjectType> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(BroadphaseListener<L, ObjectType> listener) {
		listeners.remove(listener);
	}
}