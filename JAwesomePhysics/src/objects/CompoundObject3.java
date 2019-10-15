package objects;

import java.util.ArrayList;
import java.util.List;

import broadphase.Broadphase;
import broadphase.SAPGeneric;
import math.QuatMath;
import math.VecMath;
import quaternion.Quaternionf;
import vector.Vector3f;

public class CompoundObject3 extends RigidBody3 implements CompoundObject<Vector3f, Quaternionf> {
	protected class CompoundSupport implements SupportCalculator<Vector3f> {

		public CompoundSupport() {
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			return new Vector3f();
		}

		@Override
		public Vector3f supportPointLocalNegative(Vector3f direction) {
			return new Vector3f();
		}

		@Override
		public boolean isCompound() {
			return true;
		}
	}

	List<CollisionShape<Vector3f, Quaternionf, ?>> collisionshapes;
	Vector3f center;
	List<Vector3f> localtranslations;
	Broadphase<Vector3f, CollisionShape<Vector3f, Quaternionf, ?>> broadphase;
	boolean updated = false;

	public CompoundObject3() {
		collisionshapes = new ArrayList<CollisionShape<Vector3f, Quaternionf, ?>>();
		localtranslations = new ArrayList<Vector3f>();
		broadphase = new SAPGeneric<CollisionShape<Vector3f, Quaternionf, ?>>();
		supportcalculator = createSupportCalculator(this);
		center = new Vector3f();
	}

	public CompoundObject3(Broadphase<Vector3f, CollisionShape<Vector3f, Quaternionf, ?>> broad) {
		collisionshapes = new ArrayList<CollisionShape<Vector3f, Quaternionf, ?>>();
		localtranslations = new ArrayList<Vector3f>();
		broadphase = broad;
		supportcalculator = createSupportCalculator(this);
		center = new Vector3f();
	}

	public CompoundObject3(CollisionShape3... shapes) {
		collisionshapes = new ArrayList<CollisionShape<Vector3f, Quaternionf, ?>>();
		localtranslations = new ArrayList<Vector3f>();
		broadphase = new SAPGeneric<CollisionShape<Vector3f, Quaternionf, ?>>();

		for (CollisionShape3 cs : shapes) {
			addCollisionShape(cs);
		}
		supportcalculator = createSupportCalculator(this);
		center = new Vector3f();
	}

	public CompoundObject3(Broadphase<Vector3f, CollisionShape<Vector3f, Quaternionf, ?>> broad,
			CollisionShape3... shapes) {
		collisionshapes = new ArrayList<CollisionShape<Vector3f, Quaternionf, ?>>();
		localtranslations = new ArrayList<Vector3f>();
		broadphase = broad;

		for (CollisionShape3 cs : shapes) {
			addCollisionShape(cs);
		}
		supportcalculator = createSupportCalculator(this);
		center = new Vector3f();
	}

	public void addCollisionShape(CollisionShape3 collisionshape) {
		addCollisionShape(collisionshape, new Vector3f(collisionshape.getTranslation()));
	}

	public void addCollisionShape(CollisionShape3 collisionshape, Vector3f translation) {
		translation.translate(-center.x, -center.y, -center.z);
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		localtranslations.add(translation);
		updateCenterAndAABB();

		collisionshape.translateTo(VecMath.addition(getTranslation(), translation));
		collisionshape.setRotation(getRotation());
		collisionshape.invrotation = this.invrotation;
		for (CollisionShape<Vector3f, Quaternionf, ?> cs : collisionshapes)
			cs.setRotationCenter(aabb.getCenter());
	}

	public void updateTransformations() {
		for (int i = 0; i < collisionshapes.size(); i++) {
			collisionshapes.get(i).translateTo(VecMath.addition(getTranslation(),
					QuatMath.transform(this.getRotation(), localtranslations.get(i))));
		}
	}

	private void updateCenterAndAABB() {

		/*
		 * Plan: 1. Calculate Center 1.1 Calculate global min and max for Collisionshape
		 * AABBs 1.2 Calculate center from global min and max 1.3 Recalculate local
		 * translations 2. Update AABB
		 */

		Vector3f min = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
		Vector3f max = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape<Vector3f, ?, ?> cs = collisionshapes.get(i);
			Vector3f trans = cs.getTranslation();
			Vector3f csmin = VecMath.addition(cs.getAABB().getMin(), trans);
			Vector3f csmax = VecMath.addition(cs.getAABB().getMax(), trans);
			if (csmin.x < min.x)
				min.x = csmin.x;
			if (csmin.y < min.y)
				min.y = csmin.y;
			if (csmin.z < min.z)
				min.z = csmin.z;
			if (csmax.x > max.x)
				max.x = csmax.x;
			if (csmax.y > max.y)
				max.y = csmax.y;
			if (csmax.z > max.z)
				max.z = csmax.z;
		}
		Vector3f newcenter = new Vector3f(min.x + (max.x - min.x) / 2f, min.y + (max.y - min.y) / 2f,
				min.z + (max.z - min.z) / 2f);
		Vector3f centerDifference = VecMath.subtraction(center, newcenter);
		center = newcenter;
		translateTo(center);
		for (Vector3f translation : localtranslations) {
			translation.translate(centerDifference);
		}

		float maxLength = 0;
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape<Vector3f, ?, ?> cs = collisionshapes.get(i);
			Vector3f trans = localtranslations.get(i);
			Vector3f csmin = VecMath.addition(cs.getAABB().getMin(), trans);
			Vector3f csmax = VecMath.addition(cs.getAABB().getMax(), trans);
			float minL = (float) csmin.lengthSquared();
			float maxL = (float) csmax.lengthSquared();
			if (minL > maxLength)
				maxLength = minL;
			if (maxL > maxLength)
				maxLength = maxL;
		}
		maxLength = (float) Math.sqrt(maxLength);

		setAABB(new Vector3f(-maxLength, -maxLength, -maxLength), new Vector3f(maxLength, maxLength, maxLength));
	}

	@Override
	public CompoundObject<Vector3f, Quaternionf> getCompound() {
		return this;
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new CompoundSupport();
	}

	@Override
	public Broadphase<Vector3f, CollisionShape<Vector3f, Quaternionf, ?>> getCompoundBroadphase() {
		return broadphase;
	}

	@Override
	public RigidBody<Vector3f, ?, Quaternionf, ?> getRigidBody() {
		return this;
	}

	@Override
	public List<CollisionShape<Vector3f, Quaternionf, ?>> getCollisionShapes() {
		return collisionshapes;
	}

	@Override
	public void updateInverseRotation() {
		super.updateInverseRotation();
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape<Vector3f, Quaternionf, ?> cs = collisionshapes.get(i);
			cs.invrotation = this.invrotation;
		}
	}
}