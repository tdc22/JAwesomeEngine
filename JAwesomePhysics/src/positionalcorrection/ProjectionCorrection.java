package positionalcorrection;

import manifold.CollisionManifold;
import math.VecMath;
import objects.RigidBody2;
import objects.RigidBody3;
import vector.Vector2f;
import vector.Vector3f;

public class ProjectionCorrection implements PositionalCorrection {
	float correctionPercent, slop;

	public ProjectionCorrection() {
		this.correctionPercent = 0.2f;
		this.slop = 0.1f;
	}

	public ProjectionCorrection(float slop) {
		this.correctionPercent = 0.2f;
		this.slop = slop;
	}

	public ProjectionCorrection(float correctionPercent, float slop) {
		this.correctionPercent = correctionPercent;
		this.slop = slop;
	}

	@Override
	public void correct(CollisionManifold<Vector3f, ?> manifold) {
		RigidBody3 A = (RigidBody3) manifold.getObjects().getFirst();
		RigidBody3 B = (RigidBody3) manifold.getObjects().getSecond();
		Vector3f correction = VecMath.scale(manifold.getCollisionNormal(),
				(Math.max(manifold.getPenetrationDepth() - slop, 0) / (A.getInverseMass() + B.getInverseMass()))
						* correctionPercent);
		A.translate(correction.x * -A.getInverseMass(), correction.y * -A.getInverseMass(),
				correction.z * -A.getInverseMass());
		B.translate(correction.x * B.getInverseMass(), correction.y * B.getInverseMass(),
				correction.z * B.getInverseMass());
	}

	@Override
	public void correct2(CollisionManifold<Vector2f, ?> manifold) {
		RigidBody2 A = (RigidBody2) manifold.getObjects().getFirst();
		RigidBody2 B = (RigidBody2) manifold.getObjects().getSecond();
		Vector2f correction = VecMath.scale(manifold.getCollisionNormal(),
				(Math.max(manifold.getPenetrationDepth() - slop, 0) / (A.getInverseMass() + B.getInverseMass()))
						* correctionPercent);
		A.translate(correction.x * -A.getInverseMass(), correction.y * -A.getInverseMass());
		B.translate(correction.x * B.getInverseMass(), correction.y * B.getInverseMass());
	}

	public float getCorrectionPercent() {
		return correctionPercent;
	}

	public float getSlop() {
		return slop;
	}

	public void setCorrectionPercent(float correctionPercent) {
		this.correctionPercent = correctionPercent;
	}

	public void setSlop(float slop) {
		this.slop = slop;
	}
}