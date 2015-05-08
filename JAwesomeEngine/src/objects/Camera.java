package objects;

import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;
import math.QuatMath;
import matrix.Matrix4f;
import utils.DefaultValues;
import vector.Vector3f;

public class Camera extends GameObject {
	Vector3f direction;
	float hrot, vrot;

	public Camera() {
		super();
		init(DefaultValues.DEFAULT_CAMERA_POSITION,
				DefaultValues.DEFAULT_CAMERA_HORIZONTAL_ROTATION,
				DefaultValues.DEFAULT_CAMERA_VERTICAL_ROTATION);
	}

	public Camera(Vector3f pos) {
		init(pos, DefaultValues.DEFAULT_CAMERA_HORIZONTAL_ROTATION,
				DefaultValues.DEFAULT_CAMERA_VERTICAL_ROTATION);
	}

	public Camera(Vector3f pos, float hRotation, float vRotation) {
		init(pos, hRotation, vRotation);
	}

	public void begin() {
		glPushMatrix();
		glTranslatef(rotcenter.x, rotcenter.y, rotcenter.z);
		glMultMatrixf(buf);
		glTranslatef(-rotcenter.x, -rotcenter.y, -rotcenter.z);
	}

	public void end() {
		glPopMatrix();
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

	@Override
	public void updateBuffer() {
		direction = QuatMath.transform(rotation, new Vector3f(0, 0, -1));

		Matrix4f mat = new Matrix4f();
		mat.setSubMatrix(rotation.toMatrixf());
		mat.translate(getTranslation());
		mat.invert();
		mat.store(buf);
		buf.rewind();
	}
}