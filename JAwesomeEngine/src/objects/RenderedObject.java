package objects;

import quaternion.Rotation;
import vector.Vector;

public abstract class RenderedObject<L extends Vector, A extends Rotation> extends GameObject<L, A>
		implements RenderableObject {

	public RenderedObject(L rotcenter, L translation, A rotation, L scale) {
		super(rotcenter, translation, rotation, scale);
	}

}
