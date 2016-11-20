package objects;

import quaternion.Rotation;
import vector.Vector;

public abstract class GhostObject<L extends Vector, A1 extends Vector, A2 extends Rotation, A3 extends Rotation>
		extends RigidBody<L, A1, A2, A3> {

	public GhostObject(L rotationcenter, L translation, A2 rotation, L scale) {
		super(rotationcenter, translation, rotation, scale);
		setMass(1);
	}

	public GhostObject(CollisionShape<L, A2, A3> cs, L rotationcenter,
			L translation, A2 rotation, L scale) {
		super(cs, rotationcenter, translation, rotation, scale);
		setMass(1);
	}

	@Override
	public boolean isGhost() {
		return true;
	}
}
