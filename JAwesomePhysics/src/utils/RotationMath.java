package utils;

import math.QuatMath;
import objects.AABB;
import objects.CollisionShape;
import quaternion.Complexf;
import quaternion.Quaternionf;
import vector.Vector2f;
import vector.Vector3f;

public class RotationMath {
	public static AABB<Vector2f> calculateRotationOffsetAABB2(CollisionShape<Vector2f, Complexf, ?> collisionshape,
			float margin, AABB<Vector2f> result) {
		Vector2f trans = collisionshape.getTranslation();
		AABB<Vector2f> objectAABB = collisionshape.getAABB();
		float rcXRealYImaginary = collisionshape.getRotationCenter().x * collisionshape.getRotation().real
				- collisionshape.getRotationCenter().y * -collisionshape.getRotation().imaginary;
		float rcYRealXImaginary = collisionshape.getRotationCenter().y * collisionshape.getRotation().real
				+ collisionshape.getRotationCenter().x * -collisionshape.getRotation().imaginary;
		result.getMin().set(trans.x + objectAABB.getMin().x - margin + rcXRealYImaginary,
				trans.y + objectAABB.getMin().y - margin + rcYRealXImaginary);
		result.getMax().set(trans.x + objectAABB.getMax().x + margin + rcXRealYImaginary,
				trans.y + objectAABB.getMax().y + margin + rcYRealXImaginary);
		return result;
	}

	public static AABB<Vector2f> calculateRotationOffsetAABB2(CollisionShape<Vector2f, Complexf, ?> collisionshape,
			AABB<Vector2f> result) {
		Vector2f trans = collisionshape.getTranslation();
		AABB<Vector2f> objectAABB = collisionshape.getAABB();
		float rcXRealYImaginary = collisionshape.getRotationCenter().x * collisionshape.getRotation().real
				- collisionshape.getRotationCenter().y * -collisionshape.getRotation().imaginary;
		float rcYRealXImaginary = collisionshape.getRotationCenter().y * collisionshape.getRotation().real
				+ collisionshape.getRotationCenter().x * -collisionshape.getRotation().imaginary;
		result.getMin().set(trans.x + objectAABB.getMin().x + rcXRealYImaginary,
				trans.y + objectAABB.getMin().y + rcYRealXImaginary);
		result.getMax().set(trans.x + objectAABB.getMax().x + rcXRealYImaginary,
				trans.y + objectAABB.getMax().y + rcYRealXImaginary);
		return result;
	}

	public static Vector2f calculateRotationOffsetAABBMin2(CollisionShape<Vector2f, Complexf, ?> collisionshape) {
		Vector2f trans = collisionshape.getTranslation();
		AABB<Vector2f> objectAABB = collisionshape.getAABB();
		float rcXRealYImaginary = collisionshape.getRotationCenter().x * collisionshape.getRotation().real
				- collisionshape.getRotationCenter().y * -collisionshape.getRotation().imaginary;
		float rcYRealXImaginary = collisionshape.getRotationCenter().y * collisionshape.getRotation().real
				+ collisionshape.getRotationCenter().x * -collisionshape.getRotation().imaginary;
		return new Vector2f(trans.x + objectAABB.getMin().x + rcXRealYImaginary,
				trans.y + objectAABB.getMin().y + rcYRealXImaginary);
	}

	public static Vector2f calculateRotationOffsetAABBMax2(CollisionShape<Vector2f, Complexf, ?> collisionshape) {
		Vector2f trans = collisionshape.getTranslation();
		AABB<Vector2f> objectAABB = collisionshape.getAABB();
		float rcXRealYImaginary = collisionshape.getRotationCenter().x * collisionshape.getRotation().real
				- collisionshape.getRotationCenter().y * -collisionshape.getRotation().imaginary;
		float rcYRealXImaginary = collisionshape.getRotationCenter().y * collisionshape.getRotation().real
				+ collisionshape.getRotationCenter().x * -collisionshape.getRotation().imaginary;
		return new Vector2f(trans.x + objectAABB.getMax().x + rcXRealYImaginary,
				trans.y + objectAABB.getMax().y + rcYRealXImaginary);
	}

	public static AABB<Vector3f> calculateRotationOffsetAABB3(CollisionShape<Vector3f, Quaternionf, ?> collisionshape,
			float margin, AABB<Vector3f> result) {
		Vector3f trans = collisionshape.getTranslation();
		AABB<Vector3f> objectAABB = collisionshape.getAABB();

		Quaternionf rotation = collisionshape.getRotation();
		Vector3f transformedCenter = QuatMath.transform(rotation, collisionshape.getRotationCenter()); // TODO:
																										// optimize!!

		result.getMin().set(trans.x + objectAABB.getMin().x - margin + transformedCenter.x,
				trans.y + objectAABB.getMin().y - margin + transformedCenter.y,
				trans.z + objectAABB.getMin().z - margin + transformedCenter.z);
		result.getMax().set(trans.x + objectAABB.getMax().x + margin + transformedCenter.x,
				trans.y + objectAABB.getMax().y + margin + transformedCenter.y,
				trans.z + objectAABB.getMax().z + margin + transformedCenter.z);
		return result;
	}

	public static AABB<Vector3f> calculateRotationOffsetAABB3(CollisionShape<Vector3f, Quaternionf, ?> collisionshape,
			AABB<Vector3f> result) {
		Vector3f trans = collisionshape.getTranslation();
		AABB<Vector3f> objectAABB = collisionshape.getAABB();

		Vector3f transformedCenter = QuatMath.transform(collisionshape.getRotation(),
				collisionshape.getRotationCenter()); // TODO: optimize!!

		result.getMin().set(trans.x + objectAABB.getMin().x + transformedCenter.x,
				trans.y + objectAABB.getMin().y + transformedCenter.y,
				trans.z + objectAABB.getMin().z + transformedCenter.z);
		result.getMax().set(trans.x + objectAABB.getMax().x + transformedCenter.x,
				trans.y + objectAABB.getMax().y + transformedCenter.y,
				trans.z + objectAABB.getMax().z + transformedCenter.z);
		return result;
	}

	public static Vector3f calculateRotationOffsetAABBMin3(CollisionShape<Vector3f, Quaternionf, ?> collisionshape) {
		Vector3f trans = collisionshape.getTranslation();
		AABB<Vector3f> objectAABB = collisionshape.getAABB();

		Vector3f result = QuatMath.transform(collisionshape.getRotation(), collisionshape.getRotationCenter());
		result.translate(trans);
		result.translate(objectAABB.getMin());
		return result;
	}

	public static Vector3f calculateRotationOffsetAABBMax3(CollisionShape<Vector3f, Quaternionf, ?> collisionshape) {
		Vector3f trans = collisionshape.getTranslation();
		AABB<Vector3f> objectAABB = collisionshape.getAABB();

		Vector3f result = QuatMath.transform(collisionshape.getRotation(), collisionshape.getRotationCenter());
		result.translate(trans);
		result.translate(objectAABB.getMax());
		return result;
	}
}