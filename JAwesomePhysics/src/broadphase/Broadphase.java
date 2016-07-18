package broadphase;

import java.util.List;
import java.util.Set;

import objects.CollisionShape;
import objects.Ray;
import utils.Pair;
import vector.Vector;

public interface Broadphase<L extends Vector, ObjectType extends CollisionShape<L, ?, ?>> {
	public void add(ObjectType object);

	public Set<Pair<ObjectType, ObjectType>> getOverlaps();

	public List<ObjectType> getObjects();

	public boolean contains(ObjectType obj);

	public ObjectType raycast(Ray<L> ray);

	public Set<ObjectType> raycastAll(Ray<L> ray);

	public void remove(ObjectType object);

	public void addListener(BroadphaseListener<L, ObjectType> listener);

	public void removeListener(BroadphaseListener<L, ObjectType> listener);

	public void update();
}