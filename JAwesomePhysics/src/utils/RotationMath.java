package utils;

import objects.AABB;
import objects.CollisionShape;
import quaternion.Complexf;
import vector.Vector2f;

public class RotationMath {
	public static AABB<Vector2f> calculateRotationOffsetAABB(CollisionShape<Vector2f, Complexf, ?> collisionshape,
			float margin, AABB<Vector2f> result) {
		Vector2f trans = collisionshape.getTranslation();
		AABB<Vector2f> objectAABB = collisionshape.getAABB();
		float rcXRealYImaginary = collisionshape.getRotationCenter().x * collisionshape.getInverseRotation().real
				- collisionshape.getRotationCenter().y * collisionshape.getInverseRotation().imaginary;
		float rcYRealXImaginary = collisionshape.getRotationCenter().y * collisionshape.getInverseRotation().real
				+ collisionshape.getRotationCenter().x * collisionshape.getInverseRotation().imaginary;
		result.getMin().set(trans.x + objectAABB.getMin().x - margin + rcXRealYImaginary,
				trans.y + objectAABB.getMin().y - margin + rcYRealXImaginary);
		result.getMax().set(trans.x + objectAABB.getMax().x + margin + rcXRealYImaginary,
				trans.y + objectAABB.getMax().y + margin + rcYRealXImaginary);
		return result;
	}

	public static AABB<Vector2f> calculateRotationOffsetAABB(CollisionShape<Vector2f, Complexf, ?> collisionshape,
			AABB<Vector2f> result) {
		Vector2f trans = collisionshape.getTranslation();
		AABB<Vector2f> objectAABB = collisionshape.getAABB();
		float rcXRealYImaginary = collisionshape.getRotationCenter().x * collisionshape.getInverseRotation().real
				- collisionshape.getRotationCenter().y * collisionshape.getInverseRotation().imaginary;
		float rcYRealXImaginary = collisionshape.getRotationCenter().y * collisionshape.getInverseRotation().real
				+ collisionshape.getRotationCenter().x * collisionshape.getInverseRotation().imaginary;
		result.getMin().set(trans.x + objectAABB.getMin().x + rcXRealYImaginary,
				trans.y + objectAABB.getMin().y + rcYRealXImaginary);
		result.getMax().set(trans.x + objectAABB.getMax().x + rcXRealYImaginary,
				trans.y + objectAABB.getMax().y + rcYRealXImaginary);
		return result;
	}

	public static Vector2f calculateRotationOffsetAABBMin(CollisionShape<Vector2f, Complexf, ?> collisionshape) {
		Vector2f trans = collisionshape.getTranslation();
		AABB<Vector2f> objectAABB = collisionshape.getAABB();
		float rcXRealYImaginary = collisionshape.getRotationCenter().x * collisionshape.getInverseRotation().real
				- collisionshape.getRotationCenter().y * collisionshape.getInverseRotation().imaginary;
		float rcYRealXImaginary = collisionshape.getRotationCenter().y * collisionshape.getInverseRotation().real
				+ collisionshape.getRotationCenter().x * collisionshape.getInverseRotation().imaginary;
		return new Vector2f(trans.x + objectAABB.getMin().x + rcXRealYImaginary,
				trans.y + objectAABB.getMin().y + rcYRealXImaginary);
	}

	public static Vector2f calculateRotationOffsetAABBMax(CollisionShape<Vector2f, Complexf, ?> collisionshape) {
		Vector2f trans = collisionshape.getTranslation();
		AABB<Vector2f> objectAABB = collisionshape.getAABB();
		float rcXRealYImaginary = collisionshape.getRotationCenter().x * collisionshape.getInverseRotation().real
				- collisionshape.getRotationCenter().y * collisionshape.getInverseRotation().imaginary;
		float rcYRealXImaginary = collisionshape.getRotationCenter().y * collisionshape.getInverseRotation().real
				+ collisionshape.getRotationCenter().x * collisionshape.getInverseRotation().imaginary;
		return new Vector2f(trans.x + objectAABB.getMax().x + rcXRealYImaginary,
				trans.y + objectAABB.getMax().y + rcYRealXImaginary);
	}
}