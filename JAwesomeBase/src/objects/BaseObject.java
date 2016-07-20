package objects;

import matrix.Matrix4f;
import quaternion.Rotation;
import vector.Vector;

/**
 * Base object that handles transformations for all objects of the engine.
 * 
 * @author Oliver Schall
 * 
 */

public abstract class BaseObject<L extends Vector, A extends Rotation> {
	protected L rotationcenter;
	protected L translation;
	protected A rotation;
	protected L scale;

	public BaseObject(L rotationcenter, L translation, A rotation, L scale) {
		this.rotationcenter = rotationcenter;
		this.translation = translation;
		this.rotation = rotation;
		this.scale = scale;
	}

	public void setRotationCenter(L rotationcenter) {
		this.rotationcenter = rotationcenter;
	}

	public void setTranslation(L translation) {
		this.translation = translation;
	}

	public void setRotation(A rotation) {
		this.rotation = rotation;
	}

	public void setScale(L scale) {
		this.scale = scale;
	}

	public L getRotationCenter() {
		return rotationcenter;
	}

	public L getTranslation() {
		return translation;
	}

	public A getRotation() {
		return rotation;
	}

	public L getScale() {
		return scale;
	}

	public void resetRotation() {
		rotation.setIdentity();
	}

	public void invertRotation() {
		rotation.invert();
	}

	public abstract void translate(L translate);

	public abstract void translateTo(L translate);

	public abstract void rotate(A rotate);

	public abstract void rotateTo(A rotate);

	public abstract void scale(L scale);

	public abstract void scaleTo(L scale);

	public abstract void scale(float scale);

	public abstract void scaleTo(float scale);

	public abstract Matrix4f getMatrix();
}