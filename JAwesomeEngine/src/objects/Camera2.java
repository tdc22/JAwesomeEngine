package objects;

import matrix.Matrix4f;
import vector.Vector2f;

public class Camera2 extends Camera {
	public Camera2() {
		super();
		init(new Vector2f());
	}

	public Camera2(Vector2f pos) {
		init(pos);
	}

	private void init(Vector2f position) {
		translateTo(position);
	}

	@Override
	public void updateBuffer() {
		Matrix4f mat = new Matrix4f();
		mat.setSubMatrix(rotation.toMatrixf());
		mat.translate(getTranslation());
		mat.invert();
		mat.store(buf);
		buf.flip();
	}
}