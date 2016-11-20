package narrowphase;

import objects.Ray;
import objects.SupportMap;
import vector.Vector3f;

public class SupportRaycast implements RaycastNarrowphase<Vector3f> {

	@Override
	public boolean isColliding(SupportMap<Vector3f> Sa, Ray<Vector3f> ray) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float computeCollisionOnRay(SupportMap<Vector3f> Sa,
			Ray<Vector3f> ray) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Vector3f computeCollision(SupportMap<Vector3f> Sa, Ray<Vector3f> ray) {
		// TODO Auto-generated method stub
		return null;
	}

}
