package manifold;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.SupportMap;
import vector.Vector2f;
import vector.Vector3f;

public class MultiPointManifoldManager2 extends ManifoldManager<Vector2f> {
	List<CollisionManifold<Vector2f>> collisionmanifolds;
	float offsetscale = 0.04f;

	public MultiPointManifoldManager2() {
		collisionmanifolds = new ArrayList<CollisionManifold<Vector2f>>();
	}

	public MultiPointManifoldManager2(float offsetscale) {
		this.offsetscale = offsetscale;
		collisionmanifolds = new ArrayList<CollisionManifold<Vector2f>>();
	}

	@Override
	public void add(CollisionManifold<Vector2f> cm) {
		Vector2f normal = cm.getCollisionNormal();
		Vector3f offset3 = VecMath.crossproduct(new Vector3f(normal.x,
				normal.y, 0), new Vector3f(0, 0, 1));
		Vector2f offset = VecMath.scale(
				VecMath.normalize(new Vector2f(offset3.x, offset3.y)),
				offsetscale);

		Vector2f normalPOffset = VecMath.addition(normal, offset);
		Vector2f normalMOffset = VecMath.subtraction(normal, offset);
		Vector2f negNormalPOffset = VecMath.negate(normalMOffset);
		Vector2f negNormalMOffset = VecMath.negate(normalPOffset);

		SupportMap<Vector2f> Sa = cm.getObjects().getFirst();
		SupportMap<Vector2f> Sb = cm.getObjects().getSecond();

		Vector2f contactA = computeCenter(Sa.supportPoint(normalMOffset),
				Sa.supportPoint(normalPOffset));
		Vector2f contactB = computeCenter(Sb.supportPoint(negNormalMOffset),
				Sb.supportPoint(negNormalPOffset));
		Vector2f relativeContactA = computeCenter(
				Sa.supportPointRelative(normalMOffset),
				Sa.supportPointRelative(normalPOffset));
		Vector2f relativeContactB = computeCenter(
				Sb.supportPointRelative(negNormalMOffset),
				Sb.supportPointRelative(negNormalPOffset));
		Vector2f localContactA = computeCenter(
				Sa.supportPointLocal(normalMOffset),
				Sa.supportPointLocal(normalPOffset));
		Vector2f localContactB = computeCenter(
				Sb.supportPointLocal(negNormalMOffset),
				Sb.supportPointLocal(negNormalPOffset));
		collisionmanifolds
				.add(new CollisionManifold<Vector2f>(
						cm.getObjects().getFirst(),
						cm.getObjects().getSecond(), cm.getPenetrationDepth(),
						normal, contactA, contactB, relativeContactA,
						relativeContactB, localContactA, localContactB, cm
								.getContactTangentA(), cm.getContactTangentB()));
	}

	private Vector2f computeCenter(Vector2f a, Vector2f b) {
		return VecMath.scale(VecMath.addition(a, b), 0.5f);
	}

	@Override
	public List<CollisionManifold<Vector2f>> getManifolds() {
		return collisionmanifolds;
	}

	@Override
	public void start() {
		collisionmanifolds.clear();
	}
}
