package objects;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import quaternion.Quaternionf;
import vector.Vector3f;
import broadphase.Broadphase;
import broadphase.SAPGeneric;

public class CompoundObject3 extends RigidBody3 implements
		CompoundObject<Vector3f, Quaternionf> {
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
	List<Vector3f> translations;
	List<Quaternionf> rotations;
	Broadphase<Vector3f, CollisionShape<Vector3f, ?, ?>> broadphase;
	boolean updated = false;

	public CompoundObject3() {
		collisionshapes = new ArrayList<CollisionShape<Vector3f, Quaternionf, ?>>();
		translations = new ArrayList<Vector3f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = new SAPGeneric<CollisionShape<Vector3f, ?, ?>>();
		supportcalculator = createSupportCalculator(this);
	}

	public CompoundObject3(
			Broadphase<Vector3f, CollisionShape<Vector3f, ?, ?>> broad) {
		collisionshapes = new ArrayList<CollisionShape<Vector3f, Quaternionf, ?>>();
		translations = new ArrayList<Vector3f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = broad;
		supportcalculator = createSupportCalculator(this);
	}

	public CompoundObject3(CollisionShape3... shapes) {
		collisionshapes = new ArrayList<CollisionShape<Vector3f, Quaternionf, ?>>();
		translations = new ArrayList<Vector3f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = new SAPGeneric<CollisionShape<Vector3f, ?, ?>>();

		for (CollisionShape3 cs : shapes) {
			addCollisionShape(cs);
		}
		supportcalculator = createSupportCalculator(this);
	}

	public CompoundObject3(
			Broadphase<Vector3f, CollisionShape<Vector3f, ?, ?>> broad,
			CollisionShape3... shapes) {
		collisionshapes = new ArrayList<CollisionShape<Vector3f, Quaternionf, ?>>();
		translations = new ArrayList<Vector3f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = broad;

		for (CollisionShape3 cs : shapes) {
			addCollisionShape(cs);
		}
		supportcalculator = createSupportCalculator(this);
	}

	public void addCollisionShape(CollisionShape3 collisionshape) {
		collisionshape.setTranslation(getTranslation());
		collisionshape.setRotation(getRotation());
		collisionshape.invrotation = this.invrotation;
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(new Vector3f());
		rotations.add(new Quaternionf());
		updateAABB();
	}

	public void addCollisionShape(CollisionShape3 collisionshape,
			Vector3f translation) {
		collisionshape.setTranslation(getTranslation());
		collisionshape.setRotation(getRotation());
		collisionshape.invrotation = this.invrotation;
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(translation);
		rotations.add(new Quaternionf());
		updateAABB();
	}

	public void addCollisionShape(CollisionShape3 collisionshape,
			Vector3f translation, Quaternionf rotation) {
		collisionshape.setTranslation(getTranslation());
		collisionshape.setRotation(getRotation());
		collisionshape.invrotation = this.invrotation;
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(translation);
		rotations.add(rotation);
		updateAABB();
	}

	private void updateAABB() {
		float maxLength = 0;
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape<Vector3f, ?, ?> cs = collisionshapes.get(i);
			Vector3f trans = translations.get(i);
			Vector3f min = VecMath.addition(cs.getAABB().getMin(), trans);
			Vector3f max = VecMath.addition(cs.getAABB().getMax(), trans);
			float minL = (float) min.lengthSquared();
			float maxL = (float) max.lengthSquared();
			if (minL > maxLength)
				maxLength = minL;
			if (maxL > maxLength)
				maxLength = maxL;
		}
		maxLength = (float) Math.sqrt(maxLength);

		setAABB(new Vector3f(-maxLength, -maxLength, -maxLength), new Vector3f(
				maxLength, maxLength, maxLength));
	}

	@Override
	public CompoundObject<Vector3f, Quaternionf> getCompound() {
		return this;
	}

	@Override
	public SupportCalculator<Vector3f> createSupportCalculator(
			CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new CompoundSupport();
	}

	@Override
	public Broadphase<Vector3f, CollisionShape<Vector3f, ?, ?>> getCompoundBroadphase() {
		return broadphase;
	}

	@Override
	public RigidBody<Vector3f, ?, ?, ?> getRigidBody() {
		return this;
	}

	@Override
	public List<CollisionShape<Vector3f, Quaternionf, ?>> getCollisionShapes() {
		return collisionshapes;
	}

	@Override
	public void updateInverseRotation() {
		super.updateInverseRotation();
		for (CollisionShape<Vector3f, Quaternionf, ?> cs : collisionshapes) {
			cs.invrotation = this.invrotation;
		}
	}
}