package objects;

public abstract class DataObject extends GameObject implements ObjectData {
	protected int shapetype;

	@Override
	public int getShapeType() {
		return shapetype;
	}
}
