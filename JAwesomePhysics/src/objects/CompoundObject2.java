package objects;

import java.util.ArrayList;
import java.util.List;

import broadphase.Broadphase;
import broadphase.SAP2Generic;
import math.ComplexMath;
import math.VecMath;
import matrix.Matrix1f;
import quaternion.Complexf;
import vector.Vector2f;

public class CompoundObject2 extends RigidBody2 implements CompoundObject<Vector2f, Complexf> {
	protected class CompoundSupport implements SupportCalculator<Vector2f> {

		public CompoundSupport() {
		}

		@Override
		public Vector2f supportPointLocal(Vector2f direction) {
			return new Vector2f();
		}

		@Override
		public Vector2f supportPointLocalNegative(Vector2f direction) {
			return new Vector2f();
		}

		@Override
		public boolean isCompound() {
			return true;
		}
	}

	List<CollisionShape<Vector2f, Complexf, ?>> collisionshapes;
	Vector2f center;
	List<Vector2f> localtranslations;
	Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> broadphase;
	boolean updated = false;

	public CompoundObject2() {
		collisionshapes = new ArrayList<CollisionShape<Vector2f, Complexf, ?>>();
		localtranslations = new ArrayList<Vector2f>();
		broadphase = new SAP2Generic<CollisionShape<Vector2f, ?, ?>>();
		supportcalculator = createSupportCalculator(this);
		center = new Vector2f();
	}

	public CompoundObject2(Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> broad) {
		collisionshapes = new ArrayList<CollisionShape<Vector2f, Complexf, ?>>();
		localtranslations = new ArrayList<Vector2f>();
		broadphase = broad;
		supportcalculator = createSupportCalculator(this);
		center = new Vector2f();
	}

	public CompoundObject2(CollisionShape2... shapes) {
		collisionshapes = new ArrayList<CollisionShape<Vector2f, Complexf, ?>>();
		localtranslations = new ArrayList<Vector2f>();
		broadphase = new SAP2Generic<CollisionShape<Vector2f, ?, ?>>();

		for (CollisionShape2 cs : shapes) {
			addCollisionShape(cs);
		}
		supportcalculator = createSupportCalculator(this);
		center = new Vector2f();
	}

	public CompoundObject2(Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> broad, CollisionShape2... shapes) {
		collisionshapes = new ArrayList<CollisionShape<Vector2f, Complexf, ?>>();
		localtranslations = new ArrayList<Vector2f>();
		broadphase = broad;

		for (CollisionShape2 cs : shapes) {
			addCollisionShape(cs);
		}
		supportcalculator = createSupportCalculator(this);
		center = new Vector2f();
	}

	public void addCollisionShape(CollisionShape2 collisionshape) {
		addCollisionShape(collisionshape, new Vector2f(collisionshape.getTranslation()));
	}

	public void addCollisionShape(CollisionShape2 collisionshape, Vector2f translation) {
		translation.translate(-center.x, -center.y);
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		localtranslations.add(translation);
		updateCenterAndAABB();

		collisionshape.translateTo(VecMath.addition(getTranslation(), translation));
		collisionshape.setRotation(getRotation());
		collisionshape.invrotation = this.invrotation;
		for (CollisionShape<Vector2f, Complexf, ?> cs : collisionshapes)
			cs.setRotationCenter(aabb.getCenter());
	}

	public void updateTransformations() {
		for (int i = 0; i < collisionshapes.size(); i++) {
			collisionshapes.get(i).translateTo(VecMath.addition(getTranslation(),
					ComplexMath.transform(this.getRotation(), localtranslations.get(i))));
		}
	}

	private void updateCenterAndAABB() {

		/*
		 * Plan: 1. Calculate Center 1.1 Calculate global min and max for
		 * Collisionshape AABBs 1.2 Calculate center from global min and max 1.3
		 * Recalculate local translations 2. Update AABB
		 */

		Vector2f min = new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
		Vector2f max = new Vector2f(-Float.MAX_VALUE, -Float.MAX_VALUE);
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape<Vector2f, ?, ?> cs = collisionshapes.get(i);
			Vector2f trans = cs.getTranslation();
			Vector2f csmin = VecMath.addition(cs.getAABB().getMin(), trans);
			Vector2f csmax = VecMath.addition(cs.getAABB().getMax(), trans);
			if (csmin.x < min.x)
				min.x = csmin.x;
			if (csmin.y < min.y)
				min.y = csmin.y;
			if (csmax.x > max.x)
				max.x = csmax.x;
			if (csmax.y > max.y)
				max.y = csmax.y;
		}
		Vector2f newcenter = new Vector2f(min.x + (max.x - min.x) / 2f, min.y + (max.y - min.y) / 2f);
		Vector2f centerDifference = VecMath.subtraction(center, newcenter);
		center = newcenter;
		translateTo(center);
		for (Vector2f translation : localtranslations) {
			translation.translate(centerDifference);
		}

		float maxLength = 0;
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape<Vector2f, ?, ?> cs = collisionshapes.get(i);
			Vector2f trans = localtranslations.get(i);
			Vector2f csmin = VecMath.addition(cs.getAABB().getMin(), trans);
			Vector2f csmax = VecMath.addition(cs.getAABB().getMax(), trans);
			float minL = (float) csmin.lengthSquared();
			float maxL = (float) csmax.lengthSquared();
			if (minL > maxLength)
				maxLength = minL;
			if (maxL > maxLength)
				maxLength = maxL;
		}
		maxLength = (float) Math.sqrt(maxLength);

		setAABB(new Vector2f(-maxLength, -maxLength), new Vector2f(maxLength, maxLength));
	}

	@Override
	public CompoundObject<Vector2f, Complexf> getCompound() {
		return this;
	}

	@Override
	public SupportCalculator<Vector2f> createSupportCalculator(CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
		return new CompoundSupport();
	}

	@Override
	public Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> getCompoundBroadphase() {
		return broadphase;
	}

	@Override
	public RigidBody<Vector2f, ?, ?, ?> getRigidBody() {
		return this;
	}

	@Override
	public List<CollisionShape<Vector2f, Complexf, ?>> getCollisionShapes() {
		return collisionshapes;
	}

	@Override
	public void updateInverseRotation() {
		super.updateInverseRotation();
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape<Vector2f, Complexf, ?> cs = collisionshapes.get(i);
			cs.invrotation = this.invrotation;
		}
	}
}