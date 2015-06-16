package objects;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import quaternion.Quaternionf;
import vector.Vector3f;
import broadphase.Broadphase;
import broadphase.SAPGeneric;

public class CompoundObject extends RigidBody3 {
	protected class CompoundSupport implements SupportCalculator<Vector3f> {

		public CompoundSupport() {
		}

		@Override
		public Vector3f supportPointLocal(Vector3f direction) {
			Vector3f closest = new Vector3f();
			float bestDot = -Float.MAX_VALUE;
			for (CollisionShape3 sc : collisionshapes) {
				Vector3f support = sc.supportPointLocal(direction);
				float dot = VecMath.dotproduct(support, direction);
				if (dot > bestDot) {
					closest = support;
					bestDot = dot;
				}
			}
			return closest;
		}

		@Override
		public Vector3f supportPointLocalNegative(Vector3f direction) {
			Vector3f closest = new Vector3f();
			float bestDot = Float.MAX_VALUE;
			for (CollisionShape3 sc : collisionshapes) {
				Vector3f support = sc.supportPointLocalNegative(direction);
				float dot = VecMath.dotproduct(support, direction);
				if (dot < bestDot) {
					closest = support;
					bestDot = dot;
				}
			}
			return closest;
		}

		@Override
		public List<Vector3f> supportPointLocalList(Vector3f direction) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Vector3f> supportPointLocalNegativeList(Vector3f direction) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasMultipleSupportPoints() {
			return true;
		}
	}

	List<CollisionShape3> collisionshapes;
	List<Vector3f> translations;
	List<Quaternionf> rotations;
	Broadphase<Vector3f, CollisionShape<Vector3f, ?, ?>> broadphase;

	public CompoundObject() {
		collisionshapes = new ArrayList<CollisionShape3>();
		translations = new ArrayList<Vector3f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = new SAPGeneric<CollisionShape<Vector3f, ?, ?>>();
	}

	public CompoundObject(
			Broadphase<Vector3f, CollisionShape<Vector3f, ?, ?>> broad) {
		collisionshapes = new ArrayList<CollisionShape3>();
		translations = new ArrayList<Vector3f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = broad;
	}

	public CompoundObject(CollisionShape3... shapes) {
		collisionshapes = new ArrayList<CollisionShape3>();
		translations = new ArrayList<Vector3f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = new SAPGeneric<CollisionShape<Vector3f, ?, ?>>();

		for (CollisionShape3 cs : shapes) {
			addCollisionShape(cs);
		}
	}

	public CompoundObject(
			Broadphase<Vector3f, CollisionShape<Vector3f, ?, ?>> broad,
			CollisionShape3... shapes) {
		collisionshapes = new ArrayList<CollisionShape3>();
		translations = new ArrayList<Vector3f>();
		rotations = new ArrayList<Quaternionf>();
		broadphase = broad;

		for (CollisionShape3 cs : shapes) {
			addCollisionShape(cs);
		}
	}

	public void addCollisionShape(CollisionShape3 collisionshape) {
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(new Vector3f());
		rotations.add(new Quaternionf());
		updateAABB();
	}

	public void addCollisionShape(CollisionShape3 collisionshape,
			Vector3f translation) {
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(translation);
		rotations.add(new Quaternionf());
		updateAABB();
	}

	public void addCollisionShape(CollisionShape3 collisionshape,
			Vector3f translation, Quaternionf rotation) {
		broadphase.add(collisionshape);
		collisionshapes.add(collisionshape);
		translations.add(translation);
		rotations.add(rotation);
		updateAABB();
	}

	private void updateAABB() {
		float maxLength = 0;
		for (int i = 0; i < collisionshapes.size(); i++) {
			CollisionShape3 cs = collisionshapes.get(i);
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
	public SupportCalculator<Vector3f> createSupportCalculator(
			CollisionShape<Vector3f, Quaternionf, Quaternionf> cs) {
		return new CompoundSupport();
	}
}