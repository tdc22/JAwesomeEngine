package narrowphase;

import objects.Ray;
import objects.SupportMap;
import vector.Vector;

public interface RaycastNarrowphase<L extends Vector> {
	public boolean isColliding(SupportMap<L> Sa, Ray<L> ray);

	public float computeCollisionOnRay(SupportMap<L> Sa, Ray<L> ray);

	public L computeCollision(SupportMap<L> Sa, Ray<L> ray);
}