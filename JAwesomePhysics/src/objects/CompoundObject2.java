package objects;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import matrix.Matrix1f;
import quaternion.Complexf;
import quaternion.Quaternionf;
import vector.Vector2f;
import vector.Vector3f;
import broadphase.Broadphase;
import broadphase.SAP2Generic;

public class CompoundObject2 extends RigidBody2 implements
		CompoundObject<Vector2f, Complexf> {
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
	CollisionShape<Vector2f, Complexf, ?> base;
	List<Vector2f> translations;
	List<Quaternionf> rotations;
	Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> broadphase;
	boolean updated = false;

	public CompoundObject2() {
		collisionshapes = new ArrayList<CollisionShape<Vector2f, Complexf, ?>>();
		translations = new ArrayList<Vector2f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = new SAP2Generic<CollisionShape<Vector2f, ?, ?>>();
		supportcalculator = createSupportCalculator(this);
	}

	public CompoundObject2(
			Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> broad) {
		collisionshapes = new ArrayList<CollisionShape<Vector2f, Complexf, ?>>();
		translations = new ArrayList<Vector2f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = broad;
		supportcalculator = createSupportCalculator(this);
	}

	public CompoundObject2(CollisionShape2... shapes) {
		collisionshapes = new ArrayList<CollisionShape<Vector2f, Complexf, ?>>();
		translations = new ArrayList<Vector2f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = new SAP2Generic<CollisionShape<Vector2f, ?, ?>>();

		for (CollisionShape2 cs : shapes) {
			addCollisionShape(cs);
		}
		supportcalculator = createSupportCalculator(this);
	}

	public CompoundObject2(
			Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> broad,
			CollisionShape2... shapes) {
		collisionshapes = new ArrayList<CollisionShape<Vector2f, Complexf, ?>>();
		translations = new ArrayList<Vector2f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = broad;

		for (CollisionShape2 cs : shapes) {
			addCollisionShape(cs);
		}
		supportcalculator = createSupportCalculator(this);
	}

	public void addCollisionShape(CollisionShape2 collisionshape) {
		Vector2f translation;
		if (base == null) {
			base = collisionshape;
			translateTo(base.getTranslation());
			base.setTranslation(getTranslation());
			rotateTo(base.getRotation());
			translation = new Vector2f();
		} else {
			translation = VecMath.subtraction(collisionshape.getTranslation2(),
					base.getTranslation2());
			Vector2f negtranslation = VecMath.negate(translation);
			collisionshape.setRotationCenter(new Vector3f(negtranslation.x,
					negtranslation.y, 0));
			collisionshape.translateTo(VecMath.addition(getTranslation2(),
					translation));
		}
		collisionshape.setRotation(getRotation());
		collisionshape.invrotation = this.invrotation;
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(translation);
		rotations.add(new Quaternionf());
		updateAABB();
	}

	public void updateTransformations() {
		for (int i = 1; i < collisionshapes.size(); i++) {
			CollisionShape<Vector2f, Complexf, ?> cs = collisionshapes.get(i);
			System.out.println(translations.get(i));
			cs.translateTo(VecMath.addition(getTranslation2(),
					translations.get(i)));
		}
	}

	// public void addCollisionShape(CollisionShape2 collisionshape,
	// Vector2f translation) {
	// collisionshape.setTranslation(getTranslation());
	// collisionshape.setRotation(getRotation());
	// collisionshape.invrotation = this.invrotation;
	// broadphase.add(collisionshape);
	// collisionshapes.add(collisionshape);
	// translations.add(translation);
	// rotations.add(new Quaternionf());
	// updateAABB();
	// }
	//
	// public void addCollisionShape(CollisionShape2 collisionshape,
	// Vector2f translation, Quaternionf rotation) {
	// collisionshape.setTranslation(getTranslation());
	// collisionshape.setRotation(getRotation());
	// collisionshape.invrotation = this.invrotation;
	// broadphase.add(collisionshape);
	// collisionshapes.add(collisionshape);
	// translations.add(translation);
	// rotations.add(rotation);
	// updateAABB();
	// }

	private void updateAABB() {
		float maxLength = 0;
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape<Vector2f, ?, ?> cs = collisionshapes.get(i);
			Vector2f trans = translations.get(i);
			Vector2f min = VecMath.addition(cs.getAABB().getMin(), trans);
			Vector2f max = VecMath.addition(cs.getAABB().getMax(), trans);
			float minL = (float) min.lengthSquared();
			float maxL = (float) max.lengthSquared();
			if (minL > maxLength)
				maxLength = minL;
			if (maxL > maxLength)
				maxLength = maxL;
		}
		maxLength = (float) Math.sqrt(maxLength);

		setAABB(new Vector2f(-maxLength, -maxLength), new Vector2f(maxLength,
				maxLength));
	}

	@Override
	public CompoundObject<Vector2f, Complexf> getCompound() {
		return this;
	}

	@Override
	public SupportCalculator<Vector2f> createSupportCalculator(
			CollisionShape<Vector2f, Complexf, Matrix1f> cs) {
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
		for (CollisionShape<Vector2f, Complexf, ?> cs : collisionshapes) {
			cs.invrotation = this.invrotation;
		}
	}
}