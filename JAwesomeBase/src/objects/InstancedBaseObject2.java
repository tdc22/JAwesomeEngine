package objects;

public interface InstancedBaseObject2 {
	public void translate(float x, float y);

	public void translateTo(float x, float y);

	public void rotate(float rotX);

	public void rotateTo(float rotX);

	public void scale(float scaleX, float scaleY);

	public void scaleTo(float scaleX, float scaleY);
	
	public void setRotationCenter(float x, float y);
}
