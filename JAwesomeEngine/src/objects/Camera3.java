package objects;

import matrix.Matrix4f;
import utils.DefaultValues;
import utils.VectorConstants;
import vector.Vector3f;

public class Camera3 extends GameObject3 implements Camera {
	Vector3f direction;
	float hrot, vrot;

	public Camera3() {
		super();
		init(DefaultValues.DEFAULT_CAMERA_POSITION, DefaultValues.DEFAULT_CAMERA_HORIZONTAL_ROTATION,
				DefaultValues.DEFAULT_CAMERA_VERTICAL_ROTATION);
	}

	public Camera3(Vector3f pos) {
		super();
		init(pos, DefaultValues.DEFAULT_CAMERA_HORIZONTAL_ROTATION, DefaultValues.DEFAULT_CAMERA_VERTICAL_ROTATION);
	}

	public Camera3(Vector3f pos, float hRotation, float vRotation) {
		super();
		init(pos, hRotation, vRotation);
	}

	public Vector3f getDirection() {
		return direction;
	}

	public float getHorizontalRotation() {
		return hrot;
	}

	public float getVerticalRotation() {
		return vrot;
	}

	private void init(Vector3f position, float hRotation, float vRotation) {
		hrot = 0;
		vrot = 0;
		translateTo(position);
		rotateTo(hRotation, vRotation);
	}

	public void rotate(float rotH, float rotV) {
		rotation(rotH, rotV);
	}

	public void rotateTo(float rotH, float rotV) {
		rotation(rotH - hrot, rotV - vrot);
	}

	public void rotation(float deltah, float deltav) {
		hrot += deltah;
		vrot += deltav;

		if (hrot > 360 || hrot < -360) {
			hrot %= 360;
		}
		if (vrot > 360 || vrot < -360) {
			vrot %= 360;
		}
		rotateTo(vrot, hrot, 0);
	}

	private Matrix4f mat;

	@Override
	public void updateBuffer() {
		if (mat == null) {
			mat = new Matrix4f();
			direction = new Vector3f();
		}

		direction.set(VectorConstants.BACK);
		direction.transform(rotation);

		mat.setIdentity();
		mat.setSubMatrix(rotation.toMatrixf());
		mat.scale(getScale());
		mat.translate(getTranslation());
		mat.invert();
		mat.store(buf);
		buf.flip();
	}
}