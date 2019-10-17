package positionalcorrection;

import manifold.CollisionManifold;
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

	private final Vector3f tmpCorrection3 = new Vector3f();

	@Override
	public void correct(CollisionManifold<Vector3f, ?> manifold) {
		RigidBody3 A = (RigidBody3) manifold.getObjects().getFirst();
		RigidBody3 B = (RigidBody3) manifold.getObjects().getSecond();
		tmpCorrection3.set(manifold.getCollisionNormal());
		tmpCorrection3
				.scale((Math.max(manifold.getPenetrationDepth() - slop, 0) / (A.getInverseMass() + B.getInverseMass()))
						* correctionPercent);
		A.translate(tmpCorrection3.x * -A.getInverseMass(), tmpCorrection3.y * -A.getInverseMass(),
				tmpCorrection3.z * -A.getInverseMass());
		B.translate(tmpCorrection3.x * B.getInverseMass(), tmpCorrection3.y * B.getInverseMass(),
				tmpCorrection3.z * B.getInverseMass());
	}

	private final Vector2f tmpCorrection2 = new Vector2f();

	@Override
	public void correct2(CollisionManifold<Vector2f, ?> manifold) {
		RigidBody2 A = (RigidBody2) manifold.getObjects().getFirst();
		RigidBody2 B = (RigidBody2) manifold.getObjects().getSecond();
		tmpCorrection2.set(manifold.getCollisionNormal());
		tmpCorrection2
				.scale((Math.max(manifold.getPenetrationDepth() - slop, 0) / (A.getInverseMass() + B.getInverseMass()))
						* correctionPercent);
		A.translate(tmpCorrection2.x * -A.getInverseMass(), tmpCorrection2.y * -A.getInverseMass());
		B.translate(tmpCorrection2.x * B.getInverseMass(), tmpCorrection2.y * B.getInverseMass());
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