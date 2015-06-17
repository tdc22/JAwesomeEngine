package objects;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import matrix.Matrix1f;
import quaternion.Complexf;
import quaternion.Quaternionf;
import vector.Vector2f;
import broadphase.Broadphase;
import broadphase.SAP2Generic;

public class CompoundObject2 extends RigidBody2 implements
		CompoundObject<Vector2f> {
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

	List<CollisionShape2> collisionshapes;
	List<Vector2f> translations;
	List<Quaternionf> rotations;
	Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> broadphase;
	boolean updated = false;

	public CompoundObject2() {
		collisionshapes = new ArrayList<CollisionShape2>();
		translations = new ArrayList<Vector2f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = new SAP2Generic<CollisionShape<Vector2f, ?, ?>>();
	}

	public CompoundObject2(
			Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> broad) {
		collisionshapes = new ArrayList<CollisionShape2>();
		translations = new ArrayList<Vector2f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = broad;
	}

	public CompoundObject2(CollisionShape2... shapes) {
		collisionshapes = new ArrayList<CollisionShape2>();
		translations = new ArrayList<Vector2f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = new SAP2Generic<CollisionShape<Vector2f, ?, ?>>();

		for (CollisionShape2 cs : shapes) {
			addCollisionShape(cs);
		}
	}

	public CompoundObject2(
			Broadphase<Vector2f, CollisionShape<Vector2f, ?, ?>> broad,
			CollisionShape2... shapes) {
		collisionshapes = new ArrayList<CollisionShape2>();
		translations = new ArrayList<Vector2f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = broad;

		for (CollisionShape2 cs : shapes) {
			addCollisionShape(cs);
		}
	}

	public void addCollisionShape(CollisionShape2 collisionshape) {
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(new Vector2f());
		rotations.add(new Quaternionf());
		updateAABB();
	}

	public void addCollisionShape(CollisionShape2 collisionshape,
			Vector2f translation) {
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(translation);
		rotations.add(new Quaternionf());
		updateAABB();
	}

	public void addCollisionShape(CollisionShape2 collisionshape,
			Vector2f translation, Quaternionf rotation) {
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(translation);
		rotations.add(rotation);
		updateAABB();
	}

	private void updateAABB() {
		float maxLength = 0;
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape2 cs = collisionshapes.get(i);
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
	public CompoundObject<Vector2f> getCompound() {
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
}