package narrowphase;

import manifold.RaycastHitResult;
import objects.Ray;
import objects.SupportMap;
import vector.Vector;

public interface RaycastNarrowphase<L extends Vector> {
	public boolean isColliding(SupportMap<L> Sa, Ray<L> ray);

	public RaycastHitResult<L> computeCollision(SupportMap<L> Sa, Ray<L> ray);
}