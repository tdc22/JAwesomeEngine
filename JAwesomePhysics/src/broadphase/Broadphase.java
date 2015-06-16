package broadphase;

import java.util.Set;

import objects.CollisionShape;
import utils.Pair;
import vector.Vector;

public interface Broadphase<L extends Vector, ObjectType extends CollisionShape<L, ?, ?>> {
	public void add(ObjectType object);

	public Set<Pair<ObjectType, ObjectType>> getOverlaps();

	public Set<ObjectType> raycast();

	public void remove(ObjectType object);

	public void update();
}