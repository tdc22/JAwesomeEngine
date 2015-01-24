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
import matrix.Matrix1f;
import objects.AABB;
import objects.RigidBody;
import objects.RigidBody2;
import objects.ShapedObject2;

import org.lwjgl.opengl.GL11;

import quaternion.Complexf;
import space.Space2;
import utils.Pair;
import vector.Vector1f;
import vector.Vector2f;

public class PhysicsDebug2 {
	Font font;
	Space2 physics;
	boolean showAABBs = false;
	boolean showContactPoints = false;
	boolean showCollisionNormals = false;
	boolean showVelocities = false;
	private InputEvent toggleAABBs, toggleContractPoints,
			toggleCollisionNormals, toggleVelocities;
	private List<Pair<ShapedObject2, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>>> aabbObjects;

	public PhysicsDebug2(InputManager inputs, Font f, Space2 physics) {
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

	public boolean isContactPointsShown() {
		return showContactPoints;
	}

	public boolean isVelocitiesShown() {
		return showVelocities;
	}

	private float pythagoreanSolve(float a, float b) {
		return (float) Math.sqrt(a * a + b * b);
	}

	public void render2d() {
		if (showAABBs) {
			for(Pair<ShapedObject2, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>> aabbobj : aabbObjects) {
				aabbobj.getFirst().translateTo(aabbobj.getSecond().getTranslation());
				aabbobj.getFirst().render();
			}
		}
		if (showCollisionNormals) {
			List<CollisionManifold<Vector2f>> manifolds = physics
					.getCollisionManifolds();
			for (CollisionManifold<Vector2f> cm : manifolds) {
				Color c = Color.RED;
				Vector2f normal = VecMath.scale(cm.getCollisionNormal(), 10);
				ShapedObject2 normal1 = new ShapedObject2();
				ShapedObject2 normal2 = new ShapedObject2();
				normal1.setRenderMode(GL11.GL_LINES);
				normal2.setRenderMode(GL11.GL_LINES);

				normal1.addVertex(cm.getContactPointA(), c);
				normal1.addVertex(
						VecMath.addition(
								cm.getContactPointA(),
								VecMath.scale(VecMath.negate(normal),
										cm.getPenetrationDepth())), c);
				normal2.addVertex(cm.getContactPointB(), c);
				normal2.addVertex(VecMath.addition(cm.getContactPointB(),
						VecMath.scale(normal, cm.getPenetrationDepth())), c);

				normal1.addIndices(0, 1);
				normal2.addIndices(0, 1);
				normal1.prerender();
				normal2.prerender();
				normal1.render();
				normal2.render();
				normal1.delete();
				normal2.delete();

				RigidBody2 A = (RigidBody2) cm.getObjects().getFirst();
				RigidBody2 B = (RigidBody2) cm.getObjects().getSecond();
				normal = cm.getCollisionNormal();
				Vector2f contactA = cm.getRelativeContactPointA();
				Vector2f contactB = cm.getRelativeContactPointB();
				Vector2f rv = VecMath.subtraction(B.getLinearVelocity(),
						A.getLinearVelocity());
				float velAlongNormal = VecMath.dotproduct(rv, normal);
				if (velAlongNormal <= 0) {
					float e = Math.min(A.getRestitution(), B.getRestitution());
					float ca = (float) Math.pow(
							VecMath.crossproduct(contactA, normal), 2)
							* A.getInverseInertia().getf(0, 0);
					float cb = (float) Math.pow(
							VecMath.crossproduct(contactB, normal), 2)
							* B.getInverseInertia().getf(0, 0);
					float j = (-(1 + e) * velAlongNormal)
							/ (A.getInverseMass() + B.getInverseMass() + ca + cb);

					// Friction
					// Re-calculate rv after normal impulse!
					rv = VecMath.subtraction(B.getLinearVelocity(),
							A.getLinearVelocity());
					Vector2f tangent = VecMath.subtraction(
							rv,
							VecMath.scale(normal,
									VecMath.dotproduct(rv, normal)));
					if (tangent.length() > 0)
						tangent.normalize();
					float jt = (-VecMath.dotproduct(rv, tangent))
							/ ((A.getInverseMass() + B.getInverseMass()));
					float mu = pythagoreanSolve(A.getStaticFriction(),
							B.getStaticFriction());
					Vector2f frictionImpulse = null;
					if (Math.abs(jt) < j * mu)
						frictionImpulse = VecMath.scale(tangent, jt);
					else {
						float dynamicFriction = pythagoreanSolve(
								A.getDynamicFriction(), B.getDynamicFriction());
						frictionImpulse = VecMath.scale(tangent, -j
								* dynamicFriction);
					}
					frictionImpulse = VecMath.scale(frictionImpulse, 100);

					c = Color.GREEN;
					ShapedObject2 friction = new ShapedObject2();
					friction.setRenderMode(GL11.GL_LINES);
					friction.addVertex(cm.getContactPointA(), c);
					friction.addVertex(
							VecMath.addition(cm.getContactPointA(),
									VecMath.negate(frictionImpulse)), c);
					friction.addVertex(cm.getContactPointB(), c);
					friction.addVertex(VecMath.addition(cm.getContactPointB(),
							frictionImpulse), c);
					friction.addIndices(0, 1);
					friction.prerender();
					friction.render();
					friction.delete();
				}
			}
		}
		if (showVelocities) {
			List<RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>> objs = physics
					.getObjects();
			for (RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> o : objs) {
				Color c = Color.BLUE;
				ShapedObject2 velocity = new ShapedObject2();
				velocity.setRenderMode(GL11.GL_LINES);
				velocity.addVertex(o.getTranslation(), c);
				velocity.addVertex(
						VecMath.addition(o.getTranslation2(),
								o.getLinearVelocity()), c);
				velocity.addIndices(0, 1);
				velocity.prerender();
				velocity.render();
				velocity.delete();
			}
		}
	}
	
	private void initAABBObjects() {
		aabbObjects = new ArrayList<Pair<ShapedObject2, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>>>();
		Color c = Color.YELLOW;
		for(RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> rb : physics.getObjects()) {
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
	
	private void clearAABBObjects() {
		for(Pair<ShapedObject2, RigidBody<Vector2f, Vector1f, Complexf, Matrix1f>> obj : aabbObjects) {
			obj.getFirst().delete();
		}
		aabbObjects.clear();
	}

	public void setShowAABBs(boolean s) {
		if(s) initAABBObjects();
		if(!s) clearAABBObjects();
		showAABBs = s;
	}

	public void setShowCollisionNormals(boolean s) {
		showCollisionNormals = s;
	}

	public void setShowContactPoints(boolean s) {
		showContactPoints = s;
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

	public void toggleShowContactPoints() {
		setShowContactPoints(!showContactPoints);
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