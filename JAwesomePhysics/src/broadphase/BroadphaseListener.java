package broadphase;

import java.util.EventListener;

import objects.CollisionShape;
import vector.Vector;

public interface BroadphaseListener<L extends Vector, ObjectType extends CollisionShape<L, ?, ?>>
		extends EventListener {
	public abstract void overlapStarted(ObjectType objA, ObjectType objB);

	public abstract void overlapEnded(ObjectType objA, ObjectType objB);
}
