package objects;

/**
 * Class which represents every Object in the physics space.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class CollisionObject extends BaseObject {
	// Vector3f angles;
	//
	// @Override
	// public void rotate(float angle) {
	// matrix.rotate((float) Math.toRadians(angle), new Vector3f(0.0f, 0.0f,
	// 1.0f));
	// angles.z += angle;
	// }
	//
	// @Override
	// public void rotate(float roll, float yaw, float pitch) {
	// matrix.rotate((float) Math.toRadians(roll), new Vector3f(0.0f, 0.0f,
	// 1.0f));
	// matrix.rotate((float) Math.toRadians(yaw), new Vector3f(0.0f, 1.0f,
	// 0.0f));
	// matrix.rotate((float) Math.toRadians(pitch), new Vector3f(1.0f, 0.0f,
	// 0.0f));
	// angles.translate(pitch, yaw, roll);
	// }
	//
	// @Override
	// protected void resetMatrix() {
	// matrix.setIdentity();
	// angles.set(0, 0, 0);
	// }
	//
	// public Vector3f getRotation() {
	// return angles;
	// }
}