package objects;

public interface InstancedBaseObject3 {
	public void translate(float x, float y, float z);

	public void translateTo(float x, float y, float z);

	public void rotate(float rotX, float rotY, float rotZ);

	public void rotateTo(float rotX, float rotY, float rotZ);

	public void scale(float scaleX, float scaleY, float scaleZ);

	public void scaleTo(float scaleX, float scaleY, float scaleZ);
}
