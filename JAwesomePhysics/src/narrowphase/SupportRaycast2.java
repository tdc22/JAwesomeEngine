package narrowphase;

import objects.Ray;
import objects.SupportMap;
import vector.Vector2f;

public class SupportRaycast2 implements RaycastNarrowphase<Vector2f> {

	@Override
	public boolean isColliding(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		// TODO Auto-generated method stub
		
		// check mid, check side, check other side, k?
		
		return false;
	}

	@Override
	public float computeCollisionOnRay(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		// TODO Auto-generated method stub
		
		// Take line from before, make triangle, refine iteratively, k?
		
		return 0;
	}

	@Override
	public Vector2f computeCollision(SupportMap<Vector2f> Sa, Ray<Vector2f> ray) {
		// TODO Auto-generated method stub
		return null;
	}

}
