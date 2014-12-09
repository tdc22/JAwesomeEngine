package physics;

import gui.Font;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyEvent;

import java.awt.Color;
import java.util.List;

import manifold.CollisionManifold;
import math.VecMath;
import matrix.Matrix1f;
import objects.RigidBody;
import objects.RigidBody2;
import objects.ShapedObject2;

import org.lwjgl.opengl.GL11;

import quaternion.Complexf;
import space.Space2;
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

	public PhysicsDebug2(InputManager inputs, Font f, Space2 physics) {
		font = f;
		this.physics = physics;
		setupControls(inputs);
	}

	private void setupControls(InputManager inputs) {
		toggleAABBs = new InputEvent("debug_physics2_showAABBs", new Input(
				Input.KEYBOARD_EVENT, "F5", KeyEvent.KEY_PRESSED));
		toggleContractPoints = new InputEvent(
				"debug_physics2_showContactPoints", new Input(
						Input.KEYBOARD_EVENT, "F6", KeyEvent.KEY_PRESSED));
		toggleCollisionNormals = new InputEvent(
				"debug_physics2_showCollisionNormals", new Input(
						Input.KEYBOARD_EVENT, "F7", KeyEvent.KEY_PRESSED));
		toggleVelocities = new InputEvent("debug_physics2_showVelocities",
				new Input(Input.KEYBOARD_EVENT, "F8", KeyEvent.KEY_PRESSED));

		inputs.addEvent(toggleAABBs);
		inputs.addEvent(toggleContractPoints);
		inputs.addEvent(toggleCollisionNormals);
		inputs.addEvent(toggleVelocities);
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

	public void setShowAABBs(boolean s) {
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