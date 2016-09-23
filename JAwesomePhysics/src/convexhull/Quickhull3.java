package convexhull;

import java.util.List;

import collisionshape.ConvexShape;
import vector.Vector3f;

public class Quickhull3 {
	public static ConvexShape computeConvexHull(List<Vector3f> points, int iterations) {
		
		// Initialization
		Vector3f[] EPs = getExtremePoints(points);
		
		
		ConvexShape shape = new ConvexShape(0, 0, 0, vertices, adjacentsMap);
		return shape;
	}
	
	private static Vector3f[] getExtremePoints(List<Vector3f> points) {
		Vector3f[] result = new Vector3f[6];
		
		float minX = Float.MAX_VALUE;
		float minY = Float.MAX_VALUE;
		float minZ = Float.MAX_VALUE;
		float maxX = Float.MIN_VALUE;
		float maxY = Float.MIN_VALUE;
		float maxZ = Float.MIN_VALUE;
		
		for(Vector3f p : points) {
			if(p.x < minX) {
				result[0] = p;
				minX = p.x;
			}
			if(p.y < minY) {
				result[1] = p;
				minY = p.y;
			}
			if(p.z < minZ) {
				result[2] = p;
				minZ = p.z;
			}
			if(p.x > maxX) {
				result[3] = p;
				maxX = p.x;
			}
			if(p.y > maxY) {
				result[4] = p;
				maxY = p.y;
			}
			if(p.z > maxZ) {
				result[5] = p;
				maxZ = p.z;
			}
		}
		
		return result;
	}
}