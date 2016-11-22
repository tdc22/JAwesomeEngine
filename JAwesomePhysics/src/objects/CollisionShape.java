package objects;

import quaternion.Rotation;
import vector.Vector;

/**
 * Class which represents every Object in the physics space.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class CollisionShape<L extends Vector, A1 extends Rotation, A2 extends Rotation>
		extends BaseObject<L, A1> implements SupportMap<L> {
	AABB<L> aabb;
	A1 invrotation;
	protected SupportCalculator<L> supportcalculator;

	public CollisionShape(L rotationcenter, L translation, A1 rotation, L scale) {
		super(rotationcenter, translation, rotation, scale);
	}

	public CollisionShape(CollisionShape<L, A1, A2> cs, L rotationcenter, L translation, A1 rotation, L scale) {
		super(rotationcenter, translation, rotation, scale);
		aabb = cs.getAABB();
		translation = cs.getTranslation();
		invrotation = cs.getInverseRotation();
		supportcalculator = cs.createSupportCalculator(this);
	}

	public abstract SupportCalculator<L> createSupportCalculator(CollisionShape<L, A1, A2> cs);

	public AABB<L> getAABB() {
		return aabb;
	}

	public abstract AABB<L> getGlobalAABB();

	public abstract L getGlobalMaxAABB();

	public abstract L getGlobalMinAABB();

	public A1 getInverseRotation() {
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

	@Override
	public L supportPointLocal(L direction) {
		return supportcalculator.supportPointLocal(direction);
	}

	@Override
	public L supportPointLocalNegative(L direction) {
		return supportcalculator.supportPointLocalNegative(direction);
	}

	@Override
	public L getSupportCenter() {
		return getTranslation();
	}

	@Override
	public boolean isCompound() {
		return supportcalculator.isCompound();
	}

	public CompoundObject<L, A1> getCompound() {
		return null;
	}

	public abstract void updateInverseRotation();
}