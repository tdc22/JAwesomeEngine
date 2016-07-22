package manifold;

import objects.RigidBody;
import vector.Vector;

public class RaycastResult<L extends Vector> {
	L hitPosition;
	float hitDistance;
	RigidBody<L, ?, ?, ?> hitObject;
}
