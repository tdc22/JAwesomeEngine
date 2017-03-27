package manifold;

import vector.Vector;

public class RaycastHitResult<L extends Vector> {
	float hitDistance;
	L hitPosition, hitNormal;

	public RaycastHitResult(RaycastHitResult<L> hitResult) {
		this.hitDistance = hitResult.getHitDistance();
		this.hitPosition = hitResult.getHitPosition();
		this.hitNormal = hitResult.getHitNormal();
	}

	public RaycastHitResult(float hitDistance, L hitPosition, L hitNormal) {
		this.hitDistance = hitDistance;
		this.hitPosition = hitPosition;
		this.hitNormal = hitNormal;
	}

	public float getHitDistance() {
		return hitDistance;
	}

	public L getHitPosition() {
		return hitPosition;
	}

	public L getHitNormal() {
		return hitNormal;
	}
}
