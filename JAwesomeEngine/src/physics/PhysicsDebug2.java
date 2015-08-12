package physics;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import gui.Font;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;
import manifold.CollisionManifold;
import math.VecMath;
import matrix.Matrix1f;
import objects.AABB;
import objects.RigidBody;
import objects.ShapedObject2;
import quaternion.Complexf;
import space.Space2;
import utils.Pair;
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
	private List<Pair<ShapedObject2, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>>> aabbObjects;

	public PhysicsDebug2(InputManager inputs, Font f, Space2 physics) {
		font = f;
		this.physics = physics;
		setupEvents(inputs);
	}

	private void clearAABBObjects() {
		for (Pair<ShapedObject2, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>> obj : aabbObjects) {
			obj.getFirst().delete();
		}
		aabbObjects.clear();
	}

	private void initAABBObjects() {
		aabbObjects = new ArrayList<Pair<ShapedObject2, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>>>();
		Color c = Color.YELLOW;
		for (RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> rb : physics.getObjects()) {
			AABB<Vector2f> aabb = rb.getAABB();
			ShapedObject2 aabbobj = new ShapedObject2();
			aabbobj.setRenderMode(GL11.GL_LINE_STRIP);
			aabbobj.addVertex(aabb.getMin(), c);
			aabbobj.addVertex(new Vector2f(aabb.getMin().x, aabb.getMax().y), c);
			aabbobj.addVertex(aabb.getMax(), c);
			aabbobj.addVertex(new Vector2f(aabb.getMax().x, aabb.getMin().y), c);
			aabbobj.addIndices(0, 1, 2, 3, 0);
			aabbobj.prerender();
			aabbObjects.add(new Pair<ShapedObject2, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>>(aabbobj, rb));
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

	public void render2d() {
		if (showAABBs) {
			for (Pair<ShapedObject2, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>> aabbobj : aabbObjects) {
				aabbobj.getFirst().translateTo(aabbobj.getSecond().getTranslation());
				aabbobj.getFirst().render();
			}
		}
		if (showCollisionNormals) {
			List<CollisionManifold<Vector2f>> manifolds = physics.getCollisionManifolds();
			for (CollisionManifold<Vector2f> cm : manifolds) {
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
				normal1.delete();
				normal2.delete();
			}
		}
		if (showCollisionTangents) {
			List<CollisionManifold<Vector2f>> manifolds = physics.getCollisionManifolds();
			for (CollisionManifold<Vector2f> cm : manifolds) {
				Color c = Color.GREEN;
				ShapedObject2 tangent1 = new ShapedObject2();
				ShapedObject2 tangent2 = new ShapedObject2();
				tangent1.setRenderMode(GL11.GL_LINES);
				tangent2.setRenderMode(GL11.GL_LINES);

				tangent1.addVertex(cm.getContactPointA(), c);
				tangent1.addVertex(VecMath.addition(cm.getContactPointA(),
						VecMath.negate(VecMath.scale(cm.getContactTangentA(), 10f))), c);
				tangent2.addVertex(cm.getContactPointB(), c);
				tangent2.addVertex(VecMath.addition(cm.getContactPointB(), VecMath.scale(cm.getContactTangentB(), 10f)),
						c);
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
			List<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>> objs = physics.getObjects();
			for (RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> o : objs) {
				Color c = Color.BLUE;
				ShapedObject2 velocity = new ShapedObject2();
				velocity.setRenderMode(GL11.GL_LINES);
				velocity.addVertex(o.getTranslation(), c);
				velocity.addVertex(VecMath.addition(o.getTranslation2(), o.getLinearVelocity()), c);
				velocity.addIndices(0, 1);
				velocity.prerender();
				velocity.render();
				velocity.delete();
			}
		}
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
	}
}