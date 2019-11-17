package physics;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import broadphase.DynamicAABBTree3;
import broadphase.DynamicAABBTree3Generic.Node3;
import gui.Color;
import gui.Font;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;
import manifold.CollisionManifold;
import math.VecMath;
import objects.AABB;
import objects.AABB3;
import objects.GhostObject;
import objects.RigidBody;
import objects.ShapedObject3;
import quaternion.Quaternionf;
import shader.Shader;
import space.Space3;
import utils.Pair;
import utils.RotationMath;
import vector.Vector3f;

public class PhysicsDebug {
	Font font;
	Space3 physics;
	Shader defaultshader;
	boolean showAABBs = false;
	boolean showVelocities = false;
	boolean showCollisionNormals = false;
	boolean showCollisionTangents = false;
	private InputEvent toggleAABBs, toggleCollisionNormals, toggleVelocities, toggleCollisionTangents;
	private List<Pair<RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>, ShapedObject3>> aabbObjects;
	private List<Pair<Node3, ShapedObject3>> aabbNodeObjects;

	public PhysicsDebug(InputManager inputs, Shader defaultshader, Font f, Space3 physics) {
		font = f;
		this.physics = physics;
		this.defaultshader = defaultshader;
		setupEvents(inputs);
	}

	private void clearAABBObjects() {
		for (Pair<RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>, ShapedObject3> obj : aabbObjects) {
			defaultshader.removeObject(obj.getSecond());
			obj.getSecond().delete();
		}
		aabbObjects.clear();
		for (Pair<Node3, ShapedObject3> obj : aabbNodeObjects) {
			defaultshader.removeObject(obj.getSecond());
			obj.getSecond().delete();
		}
		aabbNodeObjects.clear();
	}

	private void initAABBObjects() {
		aabbObjects = new ArrayList<Pair<RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>, ShapedObject3>>();
		aabbNodeObjects = new ArrayList<Pair<Node3, ShapedObject3>>();
		Color c = Color.YELLOW;
		for (RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> rb : physics.getObjects()) {
			addAABB(rb, c);
		}
		c = Color.GREEN;
		for (GhostObject<Vector3f, Vector3f, Quaternionf, Quaternionf> rb : physics.getGhostObjects()) {
			addAABB(rb, c);
		}
		c = Color.BLUE;
		if (physics.getBroadphase() instanceof DynamicAABBTree3) {
			Node3 root = (Node3) ((DynamicAABBTree3) physics.getBroadphase()).getRoot();
			if (root != null) {
				traverseAABBTree(root, c);
			}
		}
	}

	private void traverseAABBTree(Node3 root, Color c) {
		if (!root.isLeaf()) {
			addAABB(root, c);
			traverseAABBTree((Node3) root.getLeftChild(), c);
			traverseAABBTree((Node3) root.getRightChild(), c);
		}
	}

	private void addAABB(RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> rb, Color c) {
		AABB<Vector3f> aabb = rb.getAABB();
		ShapedObject3 aabbobj = new ShapedObject3();
		addAABBShape(aabb, aabbobj, c);
		aabbObjects.add(new Pair<RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>, ShapedObject3>(rb, aabbobj));
	}

	private void addAABB(Node3 node, Color c) {
		AABB<Vector3f> aabb = node.getAABB();
		ShapedObject3 aabbobj = new ShapedObject3();
		addAABBShape(aabb, aabbobj, c);
		aabbNodeObjects.add(new Pair<Node3, ShapedObject3>(node, aabbobj));
	}

	private void addAABBShape(AABB<Vector3f> aabb, ShapedObject3 aabbobj, Color c) {
		aabbobj.setRenderMode(GL11.GL_LINES);
		aabbobj.addVertex(new Vector3f(-1, -1, -1), c);
		aabbobj.addVertex(new Vector3f(1, -1, -1), c);
		aabbobj.addVertex(new Vector3f(-1, 1, -1), c);
		aabbobj.addVertex(new Vector3f(-1, -1, 1), c);
		aabbobj.addVertex(new Vector3f(1, 1, -1), c);
		aabbobj.addVertex(new Vector3f(1, -1, 1), c);
		aabbobj.addVertex(new Vector3f(-1, 1, 1), c);
		aabbobj.addVertex(new Vector3f(1, 1, 1), c);
		aabbobj.addIndices(0, 1, 0, 2, 0, 3, 1, 4, 1, 5, 2, 4, 2, 6, 3, 6, 3, 5, 4, 7, 5, 7, 6, 7);
		aabbobj.prerender();
		float axisX = aabb.getMax().x - aabb.getMin().x;
		float axisY = aabb.getMax().y - aabb.getMin().y;
		float axisZ = aabb.getMax().z - aabb.getMin().z;
		aabbobj.scale(axisX * 0.5f, axisY * 0.5f, axisZ * 0.5f);
		defaultshader.addObject(aabbobj);
	}

	public boolean isAABBsShown() {
		return showAABBs;
	}

	public boolean isVelocitiesShown() {
		return showVelocities;
	}

	public boolean isCollisionNormalsShown() {
		return showCollisionNormals;
	}

	public boolean isCollisionTangentsShown() {
		return showCollisionTangents;
	}

	public void render3d() {
		defaultshader.bind();
		if (showCollisionNormals) {
			List<CollisionManifold<Vector3f, Quaternionf>> manifolds = physics.getCollisionManifolds();
			for (CollisionManifold<Vector3f, Quaternionf> cm : manifolds) {
				Color c = Color.RED;
				ShapedObject3 normal1 = new ShapedObject3();
				ShapedObject3 normal2 = new ShapedObject3();
				normal1.setRenderMode(GL11.GL_LINES);
				normal2.setRenderMode(GL11.GL_LINES);
				normal1.addVertex(cm.getContactPointA(), c);
				normal1.addVertex(VecMath.addition(cm.getContactPointA(), VecMath.negate(cm.getCollisionNormal())), c);
				normal2.addVertex(cm.getContactPointB(), c);
				normal2.addVertex(VecMath.addition(cm.getContactPointB(), cm.getCollisionNormal()), c);
				normal1.addIndices(0, 1);
				normal2.addIndices(0, 1);
				normal1.prerender();
				normal2.prerender();
				normal1.render();
				normal2.render();
				normal1.delete();
				normal2.delete();
			}
		}
		if (showCollisionTangents) {
			List<CollisionManifold<Vector3f, Quaternionf>> manifolds = physics.getCollisionManifolds();
			for (CollisionManifold<Vector3f, Quaternionf> cm : manifolds) {
				Color c = Color.GREEN;
				ShapedObject3 tangent1 = new ShapedObject3();
				ShapedObject3 tangent2 = new ShapedObject3();
				tangent1.setRenderMode(GL11.GL_LINES);
				tangent2.setRenderMode(GL11.GL_LINES);
				tangent1.addVertex(cm.getContactPointA(), c);
				tangent1.addVertex(VecMath.addition(cm.getContactPointA(), VecMath.negate(cm.getContactTangentA())), c);
				tangent2.addVertex(cm.getContactPointB(), c);
				tangent2.addVertex(VecMath.addition(cm.getContactPointB(), cm.getContactTangentB()), c);
				tangent1.addIndices(0, 1);
				tangent2.addIndices(0, 1);
				tangent1.prerender();
				tangent2.prerender();
				tangent1.render();
				tangent2.render();
				tangent1.delete();
				tangent2.delete();
			}
		}
		if (showVelocities) {
			List<RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>> objs = physics.getObjects();
			for (RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> o : objs) {
				Color c = Color.BLUE;
				ShapedObject3 velocity = new ShapedObject3();
				velocity.setRenderMode(GL11.GL_LINES);
				velocity.addVertex(o.getTranslation(), c);
				velocity.addVertex(VecMath.addition(o.getTranslation(), o.getLinearVelocity()), c);
				velocity.addIndices(0, 1);
				velocity.prerender();
				velocity.render();
				velocity.delete();
			}
		}
		defaultshader.unbind();
	}

	public void setShowAABBs(boolean s) {
		if (s)
			initAABBObjects();
		if (!s)
			clearAABBObjects();
		showAABBs = s;
	}

	public void setShowVelocities(boolean s) {
		showVelocities = s;
	}

	public void setShowCollisionNormals(boolean s) {
		showCollisionNormals = s;
	}

	public void setShowCollisionTangents(boolean s) {
		showCollisionTangents = s;
	}

	private void setupEvents(InputManager inputs) {
		toggleAABBs = new InputEvent("debug_physics2_showAABBs",
				new Input(Input.KEYBOARD_EVENT, "F5", KeyInput.KEY_PRESSED));
		toggleVelocities = new InputEvent("debug_physics2_showVelocities",
				new Input(Input.KEYBOARD_EVENT, "F6", KeyInput.KEY_PRESSED));
		toggleCollisionNormals = new InputEvent("debug_physics2_showCollisionNormals",
				new Input(Input.KEYBOARD_EVENT, "F7", KeyInput.KEY_PRESSED));
		toggleCollisionTangents = new InputEvent("debug_physics2_showCollisionTangents",
				new Input(Input.KEYBOARD_EVENT, "F8", KeyInput.KEY_PRESSED));

		inputs.addEvent(toggleAABBs);
		inputs.addEvent(toggleVelocities);
		inputs.addEvent(toggleCollisionNormals);
		inputs.addEvent(toggleCollisionTangents);
	}

	public void toggleShowAABBs() {
		setShowAABBs(!showAABBs);
	}

	public void toggleShowVelocities() {
		setShowVelocities(!showVelocities);
	}

	public void toggleShowCollisionNormals() {
		setShowCollisionNormals(!showCollisionNormals);
	}

	public void toggleShowCollisionTangents() {
		setShowCollisionTangents(!showCollisionTangents);
	}

	final AABB3 aabb = new AABB3();

	public void update() {
		if (toggleAABBs.isActive())
			toggleShowAABBs();
		if (toggleVelocities.isActive())
			toggleShowVelocities();
		if (toggleCollisionNormals.isActive())
			toggleShowCollisionNormals();
		if (toggleCollisionTangents.isActive())
			toggleShowCollisionTangents();

		if (showAABBs) {
			for (Pair<RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>, ShapedObject3> aabbobj : aabbObjects) {
				if (aabbobj.getFirst().getRotationCenter().x != 0 || aabbobj.getFirst().getRotationCenter().y != 0) {
					RotationMath.calculateRotationOffsetAABB3(aabbobj.getFirst(), 0.1f, aabb);
					float axisX = (aabb.getMax().x - aabb.getMin().x) * 0.5f;
					float axisY = (aabb.getMax().y - aabb.getMin().y) * 0.5f;
					float axisZ = (aabb.getMax().z - aabb.getMin().z) * 0.5f;
					aabbobj.getSecond().getScale().set(axisX, axisY, axisZ);
					aabbobj.getSecond().translateTo(aabb.getMin().x + axisX, aabb.getMin().y + axisY,
							aabb.getMin().z + axisZ);
				} else {
					aabbobj.getSecond().translateTo(aabbobj.getFirst().getTranslation());
				}
			}
			/*
			 * for (Pair<ShapedObject3, Node3> aabbobj : aabbNodeObjects) {
			 * System.out.println(aabbobj.getSecond().getAABB().getCenter());
			 * aabbobj.getFirst().translateTo((Vector3f)
			 * aabbobj.getSecond().getAABB().getCenter()); }
			 */
		}
	}
}