package physics;

import gui.Font;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import manifold.CollisionManifold;
import math.VecMath;
import objects.AABB;
import objects.RigidBody;
import objects.ShapedObject;

import org.lwjgl.opengl.GL11;

import quaternion.Quaternionf;
import space.Space3;
import utils.Pair;
import vector.Vector3f;

public class PhysicsDebug {
	Font font;
	Space3 physics;
	boolean showAABBs = false;
	boolean showCollisionNormals = false;
	boolean showVelocities = false;
	private InputEvent toggleAABBs, toggleContractPoints,
			toggleCollisionNormals, toggleVelocities;
	private List<Pair<ShapedObject, RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>>> aabbObjects;

	public PhysicsDebug(InputManager inputs, Font f, Space3 physics) {
		font = f;
		this.physics = physics;
		setupControls(inputs);
	}

	public boolean isAABBsShown() {
		return showAABBs;
	}

	public boolean isCollisionNormalsShown() {
		return showCollisionNormals;
	}

	public boolean isVelocitiesShown() {
		return showVelocities;
	}

	public void render2d() {

	}

	public void render3d() {
		if (showAABBs) {
			for (Pair<ShapedObject, RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>> aabbobj : aabbObjects) {
				aabbobj.getFirst().translateTo(
						aabbobj.getSecond().getTranslation());
				aabbobj.getFirst().render();
			}
		}
		if (showCollisionNormals) {
			List<CollisionManifold<Vector3f>> manifolds = physics
					.getCollisionManifolds();
			for (CollisionManifold<Vector3f> cm : manifolds) {
				Color c = Color.RED;
				ShapedObject normal1 = new ShapedObject();
				ShapedObject normal2 = new ShapedObject();
				normal1.setRenderMode(GL11.GL_LINES);
				normal2.setRenderMode(GL11.GL_LINES);
				normal1.addVertex(cm.getContactPointA(), c);
				normal1.addVertex(
						VecMath.addition(cm.getContactPointA(),
								VecMath.negate(cm.getCollisionNormal())), c);
				normal2.addVertex(cm.getContactPointB(), c);
				normal2.addVertex(
						VecMath.addition(cm.getContactPointB(),
								cm.getCollisionNormal()), c);
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
		if (showVelocities) {
			List<RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>> objs = physics
					.getObjects();
			for (RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> o : objs) {
				Color c = Color.BLUE;
				ShapedObject velocity = new ShapedObject();
				velocity.setRenderMode(GL11.GL_LINES);
				velocity.addVertex(o.getTranslation(), c);
				velocity.addVertex(
						VecMath.addition(o.getTranslation(),
								o.getLinearVelocity()), c);
				velocity.addIndices(0, 1);
				velocity.prerender();
				velocity.render();
				velocity.delete();
			}
		}
	}

	private void initAABBObjects() {
		aabbObjects = new ArrayList<Pair<ShapedObject, RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>>>();
		Color c = Color.YELLOW;
		for (RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> rb : physics
				.getObjects()) {
			AABB<Vector3f> aabb = rb.getAABB();
			ShapedObject aabbobj = new ShapedObject();
			Vector3f min = aabb.getMin();
			Vector3f max = aabb.getMax();
			aabbobj.setRenderMode(GL11.GL_LINES);
			aabbobj.addVertex(min, c);
			aabbobj.addVertex(new Vector3f(max.x, min.y, min.z), c);
			aabbobj.addVertex(new Vector3f(min.x, max.y, min.z), c);
			aabbobj.addVertex(new Vector3f(min.x, min.y, max.z), c);
			aabbobj.addVertex(new Vector3f(max.x, max.y, min.z), c);
			aabbobj.addVertex(new Vector3f(max.x, min.y, max.z), c);
			aabbobj.addVertex(new Vector3f(min.x, max.y, max.z), c);
			aabbobj.addVertex(max, c);
			aabbobj.addIndices(0, 1, 0, 2, 0, 3, 1, 4, 1, 5, 2, 4, 2, 6, 3, 6,
					3, 5, 4, 7, 5, 7, 6, 7);
			aabbobj.prerender();
			aabbObjects
					.add(new Pair<ShapedObject, RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>>(
							aabbobj, rb));
		}
	}

	private void clearAABBObjects() {
		for (Pair<ShapedObject, RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf>> obj : aabbObjects) {
			obj.getFirst().delete();
		}
		aabbObjects.clear();
	}

	public void setShowAABBs(boolean s) {
		if (s)
			initAABBObjects();
		if (!s)
			clearAABBObjects();
		showAABBs = s;
	}

	public void setShowCollisionNormals(boolean s) {
		showCollisionNormals = s;
	}

	public void setShowVelocities(boolean s) {
		showVelocities = s;
	}

	private void setupControls(InputManager inputs) {
		toggleAABBs = new InputEvent("debug_physics2_showAABBs", new Input(
				Input.KEYBOARD_EVENT, "F5", KeyInput.KEY_PRESSED));
		toggleContractPoints = new InputEvent(
				"debug_physics2_showContactPoints", new Input(
						Input.KEYBOARD_EVENT, "F6", KeyInput.KEY_PRESSED));
		toggleCollisionNormals = new InputEvent(
				"debug_physics2_showCollisionNormals", new Input(
						Input.KEYBOARD_EVENT, "F7", KeyInput.KEY_PRESSED));
		toggleVelocities = new InputEvent("debug_physics2_showVelocities",
				new Input(Input.KEYBOARD_EVENT, "F8", KeyInput.KEY_PRESSED));

		inputs.addEvent(toggleAABBs);
		inputs.addEvent(toggleContractPoints);
		inputs.addEvent(toggleCollisionNormals);
		inputs.addEvent(toggleVelocities);
	}

	public void toggleShowAABBs() {
		setShowAABBs(!showAABBs);
	}

	public void toggleShowCollisionNormals() {
		setShowCollisionNormals(!showCollisionNormals);
	}

	public void toggleShowVelocities() {
		setShowVelocities(!showVelocities);
	}

	public void update() {
		if (toggleAABBs.isActive())
			toggleShowAABBs();
		if (toggleCollisionNormals.isActive())
			toggleShowCollisionNormals();
		if (toggleVelocities.isActive())
			toggleShowVelocities();
	}
}