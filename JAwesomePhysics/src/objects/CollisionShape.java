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

	public abstract SupportCalculator<L> createSupportCalculator(
			CollisionShape<L, A> cs);

	public AABB<L> getAABB() {
		return aabb;
	}

	public abstract AABB<L> getGlobalAABB();

	public abstract L getGlobalMaxAABB();

	public abstract L getGlobalMinAABB();

	public A getInverseRotation() {
		return invrotation;
	}

	public L getMaxAABB() {
		return aabb.getMax();
	}

	public L getMinAABB() {
		return aabb.getMin();
	}

	public SupportCalculator<L> getSupportCalculator() {
		return supportcalculator;
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

	public L supportPointLocal(L direction) {
		return supportcalculator.supportPointLocal(direction);
	}

	public abstract void updateInverseRotation();
}