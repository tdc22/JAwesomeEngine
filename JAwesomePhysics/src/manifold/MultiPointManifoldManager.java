package manifold;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.SupportMap;
import vector.Vector3f;

public class MultiPointManifoldManager extends ManifoldManager<Vector3f> {
	List<CollisionManifold<Vector3f>> collisionmanifolds;
	float offsetscale = 0.1f;

	public MultiPointManifoldManager() {
		collisionmanifolds = new ArrayList<CollisionManifold<Vector3f>>();
	}

	@Override
	public void add(CollisionManifold<Vector3f> cm) {
		Vector3f normal = cm.getCollisionNormal();

		Vector3f offsetA = VecMath.scale(cm.getContactTangentA(), offsetscale);
		Vector3f offsetB = VecMath.scale(cm.getContactTangentB(), offsetscale);

		Vector3f normalPOffsetA = VecMath.addition(normal, offsetA);
		Vector3f normalMOffsetA = VecMath.subtraction(normal, offsetA);
		Vector3f normalPOffsetB = VecMath.addition(normal, offsetB);
		Vector3f normalMOffsetB = VecMath.subtraction(normal, offsetB);
		Vector3f negNormalPOffsetA = VecMath.negate(normalMOffsetA);
		Vector3f negNormalMOffsetA = VecMath.negate(normalPOffsetA);
		Vector3f negNormalPOffsetB = VecMath.negate(normalMOffsetB);
		Vector3f negNormalMOffsetB = VecMath.negate(normalPOffsetB);

		SupportMap<Vector3f> Sa = cm.getObjects().getFirst();
		SupportMap<Vector3f> Sb = cm.getObjects().getSecond();

		Vector3f contactA = computeCenter(Sa.supportPoint(normalMOffsetA),
				Sa.supportPoint(normalPOffsetA),
				Sa.supportPoint(normalMOffsetB),
				Sa.supportPoint(normalPOffsetB));
		Vector3f contactB = computeCenter(Sb.supportPoint(negNormalMOffsetA),
				Sb.supportPoint(negNormalPOffsetA), negNormalMOffsetB,
				Sb.supportPoint(negNormalPOffsetB));
		Vector3f relativeContactA = computeCenter(
				Sa.supportPointRelative(normalMOffsetA),
				Sa.supportPointRelative(normalPOffsetA),
				Sa.supportPointRelative(normalMOffsetB),
				Sa.supportPointRelative(normalPOffsetB));
		Vector3f relativeContactB = computeCenter(
				Sb.supportPointRelative(negNormalMOffsetA),
				Sb.supportPointRelative(negNormalPOffsetA),
				Sb.supportPointRelative(negNormalMOffsetB),
				Sb.supportPointRelative(negNormalPOffsetB));
		Vector3f localContactA = computeCenter(
				Sa.supportPointLocal(normalMOffsetA),
				Sa.supportPointLocal(normalPOffsetA),
				Sa.supportPointLocal(normalMOffsetB),
				Sa.supportPointLocal(normalPOffsetB));
		Vector3f localContactB = computeCenter(
				Sb.supportPointLocal(negNormalMOffsetA),
				Sb.supportPointLocal(negNormalPOffsetA),
				Sb.supportPointLocal(negNormalMOffsetB),
				Sb.supportPointLocal(negNormalPOffsetB));
		collisionmanifolds
				.add(new CollisionManifold<Vector3f>(
						cm.getObjects().getFirst(),
						cm.getObjects().getSecond(), cm.getPenetrationDepth(),
						normal, contactA, contactB, relativeContactA,
						relativeContactB, localContactA, localContactB, cm
								.getContactTangentA(), cm.getContactTangentB()));
	}

	private Vector3f computeCenter(Vector3f a, Vector3f b, Vector3f c,
			Vector3f d) {
		return VecMath.scale(VecMath.addition(
				VecMath.addition(VecMath.addition(a, b), c), d), 0.25f);
	}

	@Override
	public List<CollisionManifold<Vector3f>> getManifolds() {
		return collisionmanifolds;
	}

	@Override
	public void start() {
		collisionmanifolds.clear();
	}
}
