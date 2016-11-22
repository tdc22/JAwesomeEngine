package objects;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import quaternion.Rotation;
import vector.Vector;

public abstract class GameObject<L extends Vector, A extends Rotation> extends DataObject<L, A> {
	protected FloatBuffer buf;

	public GameObject(L rotcenter, L translation, A rotation, L scale) {
		super(rotcenter, translation, rotation, scale);

		buf = BufferUtils.createFloatBuffer(16);
		updateBuffer();
	}

	public void delete() {
		buf.clear();
	}

	public FloatBuffer getMatrixBuffer() {
		return buf;
	}

	public abstract void updateBuffer();
}
