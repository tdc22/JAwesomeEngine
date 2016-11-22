package utils;

import objects.AABB;
import objects.AABB2;
import objects.AABB3;
import objects.Ray;

public class IntersectionLibrary {
	public static boolean intersects(AABB2 aabb0, AABB2 aabb1) {
		return intersects(aabb0.getMin().x, aabb0.getMin().y, aabb0.getMax().x, aabb0.getMax().y, aabb1.getMin().x,
				aabb1.getMin().y, aabb1.getMax().x, aabb1.getMax().y);
	}

	public static boolean intersects(float aabb0MinX, float aabb0Miny, float aabb0MaxX, float aabb0MaxY,
			float aabb1MinX, float aabb1MinY, float aabb1MaxX, float aabb1MaxY) {
		return !(aabb0MaxX < aabb1MinX || aabb0MaxY < aabb1MinY || aabb0MinX > aabb1MaxX || aabb0Miny > aabb1MaxY);
	}

	public static boolean intersects(AABB3 aabb0, AABB3 aabb1) {
		return intersects(aabb0.getMin().x, aabb0.getMin().y, aabb0.getMin().z, aabb0.getMax().x, aabb0.getMax().y,
				aabb0.getMax().z, aabb1.getMin().x, aabb1.getMin().y, aabb1.getMin().z, aabb1.getMax().x,
				aabb1.getMax().y, aabb1.getMax().z);
	}

	public static boolean intersects(float aabb0MinX, float aabb0MinY, float aabb0MinZ, float aabb0MaxX,
			float aabb0MaxY, float aabb0MaxZ, float aabb1MinX, float aabb1MinY, float aabb1MinZ, float aabb1MaxX,
			float aabb1MaxY, float aabb1MaxZ) {
		return !(aabb0MaxX < aabb1MinX || aabb0MaxY < aabb1MinY || aabb0MaxZ < aabb1MinZ || aabb0MinX > aabb1MaxX
				|| aabb0MinY > aabb1MaxY || aabb0MinZ > aabb1MaxZ);
	}

	public static boolean intersects(Ray<?> ray, AABB<?> aabb) {
		float rayDirInv = (1 / ray.getDirection().getf(0));
		float t1 = (aabb.getMin().getf(0) - ray.getPosition().getf(0)) * rayDirInv;
		float t2 = (aabb.getMax().getf(0) - ray.getPosition().getf(0)) * rayDirInv;

		double tmin = Math.min(t1, t2);
		double tmax = Math.max(t1, t2);

		for (int i = 1; i < ray.getPosition().getDimensions(); ++i) {
			rayDirInv = (1 / ray.getDirection().getf(i));
			t1 = (aabb.getMin().getf(i) - ray.getPosition().getf(i)) * rayDirInv;
			t2 = (aabb.getMax().getf(i) - ray.getPosition().getf(i)) * rayDirInv;

			tmin = Math.max(tmin, Math.min(Math.min(t1, t2), tmax));
			tmax = Math.min(tmax, Math.max(Math.max(t1, t2), tmin));
		}

		return tmax > Math.max(tmin, 0.0);
	}
}