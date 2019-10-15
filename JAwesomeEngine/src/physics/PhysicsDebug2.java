package physics;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import gui.Color;
import gui.Font;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;
import manifold.CollisionManifold;
import math.VecMath;
import matrix.Matrix1f;
import objects.AABB;
import objects.AABB2;
import objects.GhostObject;
import objects.RigidBody;
import objects.ShapedObject2;
import quaternion.Complexf;
import shader.Shader;
import space.Space2;
import utils.Pair;
import utils.RotationMath;
import vector.Vector1f;
import vector.Vector2f;

public class PhysicsDebug2 {
	Font font;
	Space2 physics;
	boolean showAABBs = false;
	boolean showVelocities = false;
	boolean showCollisionNormals = false;
	boolean showCollisionTangents = false;
	private InputEvent toggleAABBs, toggleCollisionNormals, toggleVelocities, toggleCollisionTangents;
	private List<Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2>> aabbObjects;
	private List<Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2>> velocityObjects;
	private List<ShapedObject2> lastNormals;

	Shader shader;
	ShapedObject2 normal1, normal2, tangent1, tangent2, velocity;

	public PhysicsDebug2(InputManager inputs, Shader s, Font f, Space2 physics) {
		font = f;
		shader = s;
		this.physics = physics;
		setupEvents(inputs);
		lastNormals = new ArrayList<ShapedObject2>();
	}

	private void clearAABBObjects() {
		for (Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2> obj : aabbObjects) {
			shader.removeObject(obj.getSecond());
			obj.getSecond().delete();
		}
		aabbObjects.clear();
	}

	private void clearVelocityObjects() {
		for (Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2> obj : velocityObjects) {
			shader.removeObject(obj.getSecond());
			obj.getSecond().delete();
		}
		velocityObjects.clear();
	}

	private void initAABBObjects() {
		aabbObjects = new ArrayList<Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2>>();
		Color c = Color.YELLOW;
		for (RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> rb : physics.getObjects()) {
			addAABB(rb, c);
		}
		c = Color.GREEN;
		for (GhostObject<Vector2f, Vector1f, Complexf, Matrix1f> rb : physics.getGhostObjects()) {
			addAABB(rb, c);
		}
	}

	private void addAABB(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> rb, Color c) {
		AABB<Vector2f> aabb = rb.getAABB();
		ShapedObject2 aabbobj = new ShapedObject2();
		aabbobj.setRenderMode(GL11.GL_LINE_LOOP);
		aabbobj.addVertex(new Vector2f(-1, -1), c);
		aabbobj.addVertex(new Vector2f(1, -1), c);
		aabbobj.addVertex(new Vector2f(1, 1), c);
		aabbobj.addVertex(new Vector2f(-1, 1), c);
		aabbobj.addIndices(0, 1, 2, 3);
		aabbobj.prerender();
		float axisX = aabb.getMax().x - aabb.getMin().x;
		float axisY = aabb.getMax().y - aabb.getMin().y;
		aabbobj.scale(axisX * 0.5f, axisY * 0.5f);
		shader.addObject(aabbobj);
		aabbObjects.add(new Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2>(rb, aabbobj));
	}

	private void initVelocityObjects() {
		velocityObjects = new ArrayList<Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2>>();
		Color c = Color.BLUE;
		for (RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> rb : physics.getObjects()) {
			Vector2f velocity = rb.getLinearVelocity();
			ShapedObject2 velocityObj = new ShapedObject2();
			velocityObj.setRenderMode(GL11.GL_LINES);
			velocityObj.translateTo(rb.getTranslation());
			velocityObj.addVertex(new Vector2f(), c);
			velocityObj.addVertex(velocity, c);
			velocityObj.addIndices(0, 1);
			velocityObj.prerender();
			shader.addObject(velocityObj);
			velocityObjects
					.add(new Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2>(rb, velocityObj));
		}
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

	public void setShowAABBs(boolean s) {
		if (s)
			initAABBObjects();
		if (!s)
			clearAABBObjects();
		showAABBs = s;
	}

	public void setShowVelocities(boolean s) {
		if (s)
			initVelocityObjects();
		if (!s)
			clearVelocityObjects();
		showVelocities = s;
	}

	public void setShowCollisionNormals(boolean s) {
		if (!s)
			clearLastNormals();
		showCollisionNormals = s;
	}

	public void setShowCollisionTangents(boolean s) {
		showCollisionTangents = s;
	}

	private void clearLastNormals() {
		for (ShapedObject2 normal : lastNormals) {
			normal.delete();
			shader.removeObject(normal);
		}
		lastNormals.clear();
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
		inputs.addEvent(toggleCollisionNormals);
		inputs.addEvent(toggleVelocities);
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
			for (Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2> aabbobj : aabbObjects) {
				if (aabbobj.getFirst().getRotationCenter().x != 0 || aabbobj.getFirst().getRotationCenter().y != 0) {
					AABB<Vector2f> aabb = RotationMath.calculateRotationOffsetAABB(aabbobj.getFirst(), 0.1f,
							new AABB2());
					float axisX = (aabb.getMax().x - aabb.getMin().x) * 0.5f;
					float axisY = (aabb.getMax().y - aabb.getMin().y) * 0.5f;
					System.out.println("A " + axisX + "; " + axisY + "; " + aabb.toString());
					aabbobj.getSecond().getScale().set(axisX, axisY);
					aabbobj.getSecond().translateTo(aabb.getMin().x + axisX, aabb.getMin().y + axisY);
				} else {
					aabbobj.getSecond().translateTo(aabbobj.getFirst().getTranslation());
				}
			}
		}
		if (showVelocities) {
			for (Pair<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>, ShapedObject2> velocityObj : velocityObjects) {
				velocityObj.getSecond().translateTo(velocityObj.getFirst().getTranslation());
				velocityObj.getSecond().removeVertex(1);
				velocityObj.getSecond().addVertex(velocityObj.getFirst().getLinearVelocity(), Color.BLUE);
				velocityObj.getSecond().prerender();
			}
		}
		if (showCollisionNormals) {
			clearLastNormals();
			List<CollisionManifold<Vector2f, Complexf>> manifolds = physics.getCollisionManifolds();
			for (CollisionManifold<Vector2f, Complexf> cm : manifolds) {
				Color c = Color.RED;
				ShapedObject2 normal1 = new ShapedObject2();
				ShapedObject2 normal2 = new ShapedObject2();
				normal1.setRenderMode(GL11.GL_LINES);
				normal2.setRenderMode(GL11.GL_LINES);

				Vector2f scalednormal = VecMath.scale(cm.getCollisionNormal(), 10f);
				normal1.addVertex(cm.getContactPointA(), c);
				normal1.addVertex(VecMath.addition(cm.getContactPointA(), VecMath.negate(scalednormal)), c);
				normal2.addVertex(cm.getContactPointB(), c);
				normal2.addVertex(VecMath.addition(cm.getContactPointB(), scalednormal), c);
				normal1.addIndices(0, 1);
				normal2.addIndices(0, 1);
				normal1.prerender();
				normal2.prerender();
				normal1.render();
				normal2.render();
				shader.addObject(normal1);
				shader.addObject(normal2);
				lastNormals.add(normal1);
				lastNormals.add(normal2);
			}
		}
	}
}