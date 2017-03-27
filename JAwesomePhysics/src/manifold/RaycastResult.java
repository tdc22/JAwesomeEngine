package manifold;

import objects.RigidBody;
import vector.Vector;

public class RaycastResult<L extends Vector> extends RaycastHitResult<L> {
	RigidBody<L, ?, ?, ?> hitObject;

	public RaycastResult(RigidBody<L, ?, ?, ?> hitObject, RaycastHitResult<L> hitResult) {
		super(hitResult);
		this.hitObject = hitObject;
	}

	public RaycastResult(RigidBody<L, ?, ?, ?> hitObject, float hitDistance, L hitPosition, L hitNormal) {
		super(hitDistance, hitPosition, hitNormal);
		this.hitObject = hitObject;
	}

	public RigidBody<L, ?, ?, ?> getHitObject() {
		return hitObject;
	}
}
