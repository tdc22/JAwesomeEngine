package objects;

import quaternion.Rotation;
import vector.Vector;

/**
 * Class which represents every Object in the physics space.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class CollisionShape<L extends Vector, A extends Rotation>
		extends BaseObject implements SupportMap<L> {
	AABB<L> aabb;
	A invrotation;
	protected SupportCalculator<L> supportcalculator;

	public CollisionShape() {

	}

	public CollisionShape(CollisionShape<L, A> cs) {
		aabb = cs.getAABB();
		invrotation = cs.getInverseRotation();
		supportcalculator = cs.createSupportCalculator(this);
	}

	public AABB<L> getAABB() {
		return aabb;
	}

	public SupportCalculator<L> getSupportCalculator() {
		return supportcalculator;
	}

	public L supportPointLocal(L direction) {
		return supportcalculator.supportPointLocal(direction);
	}

	public abstract L getGlobalMaxAABB();

	public abstract L getGlobalMinAABB();

	public abstract SupportCalculator<L> createSupportCalculator(
			CollisionShape<L, A> cs);

	public A getInverseRotation() {
		return invrotation;
	}

	public L getMaxAABB() {
		return aabb.getMax();
	}

	public L getMinAABB() {
		return aabb.getMin();
	}

	public void setAABB(AABB<L> aabb) {
		this.aabb = aabb;
	}

	public void setAABB(L minAABB, L maxAABB) {
		aabb.set(minAABB, maxAABB);
	}

	public void setMaxAABB(L maxAABB) {
		aabb.setMax(maxAABB);
	}

	public void setMinAABB(L minAABB) {
		aabb.setMin(minAABB);
	}

	public abstract void updateInverseRotation();
}