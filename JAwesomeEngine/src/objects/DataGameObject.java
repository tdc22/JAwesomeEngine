package objects;

public abstract class DataGameObject extends GameObject implements ObjectData {
	protected int shapetype;

	@Override
	public int getShapeType() {
		return shapetype;
	}
}
