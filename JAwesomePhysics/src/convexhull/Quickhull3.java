package convexhull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import convexhull.Quickhull3Old.Triangle;
import math.VecMath;
import collisionshape.ConvexShape;
import vector.Vector3f;

public class Quickhull3 {
	public static ConvexShape computeConvexHull(List<Vector3f> points, int iterations) {
		// source: http://thomasdiewald.com/blog/?p=1888
		
		// Initial Phase
		// 1. Create initial simplex
		Vector3f[] EPs = getExtremePoints(points);
		
		// 1.1 Get two most distant points as baseline of base triangle
		int a, b;
		float distance = -1;
		for(int i = 0; i < EPs.length - 1; i++) {
			for(int j = i; j < EPs.length; j++) {
				float dist = (float) VecMath.subtraction(EPs[i], EPs[j]).lengthSquared();
				if(dist > distance) {
					distance = dist;
					a = i;
					b = j;
				}
			}
		}
		Vector3f A = EPs[a];
		Vector3f B = EPs[b];
		
		// 1.2 Get furthest point from base line to create base triangle
		Vector3f AB = VecMath.subtraction(B, A);
		AB.normalize();
		int c;
		distance = -1;
		for(int i = 0; i < EPs.length; i++) {
			if(i != a && i != b) {
				Vector3f IA = VecMath.subtraction(EPs[i], A);
				float dist = (float) VecMath.crossproduct(AB, IA).lengthSquared();
				if(dist > distance) {
					distance = dist;
					c = i;
				}
			}
		}
		Vector3f C = EPs[c];
		
		// 1.3 Get furthest point from base triangle
		Vector3f ABCnormal = VecMath.computeNormal(A, B, C);
		boolean keepOrientation = true;
		int d;
		distance = 0;
		for(int i = 0; i < EPs.length; i++) {
			if(i != a && i != b && i != c) {
				Vector3f IA = VecMath.subtraction(EPs[i], A);
				float dist = VecMath.dotproduct(IA, ABCnormal);
				if(Math.abs(dist) > distance) {
					keepOrientation = (dist > 0); // TODO: check
					distance = Math.abs(dist);
					d = i;
				}
			}
		}
		Vector3f D = EPs[d];
		
		// 2. Assign points to faces
		// 2.1 Create faces and add them to queue
		LinkedList<Triangle> faces = new LinkedList<Triangle>();
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		vertices.add(A);
		vertices.add(B);
		vertices.add(C);
		vertices.add(D);
		if(keepOrientation) {
			faces.add(new Triangle(0, 1, 2, VecMath.computeNormal(A, B, C)));
			faces.add(new Triangle(0, 3, 1, VecMath.computeNormal(A, D, B)));
			faces.add(new Triangle(3, 2, 1, VecMath.computeNormal(D, C, B)));
			faces.add(new Triangle(0, 2, 3, VecMath.computeNormal(A, C, D)));
		}
		else {
			faces.add(new Triangle(0, 2, 1, VecMath.computeNormal(A, C, B)));
			faces.add(new Triangle(0, 1, 3, VecMath.computeNormal(A, B, D)));
			faces.add(new Triangle(0, 3, 2, VecMath.computeNormal(A, D, C)));
			faces.add(new Triangle(3, 1, 2, VecMath.computeNormal(D, B, C)));
		}
		// 2.2 Find lighting points for initial triangles
		LinkedList<List<Vector3f>> listsOfFacePoints = new LinkedList<List<Vector3f>>();
		for(int i = 0; i < faces.size(); i++) {
			listsOfFacePoints.add(getLightPoints(faces.get(i), points));
		}
		// 3. Push the 4 faces on the stack (done above)
		
		// Iteration Phase
		
		// 1. If stack not empty Pop Face from Stack
		Triangle t;
		while((t = faces.poll()) != null) {
			// 2. Get most distant point of the face's point set
			Vector3f furthestPoint;
			int furthestPointID;
			A = vertices.get(t.a);
			List<Vector3f> facepoints = listsOfFacePoints.poll();
			distance = -1;
			for(int i = 0; i < facepoints.size(); i++) {
				Vector3f P = facepoints.get(i);
				Vector3f PA = VecMath.subtraction(A, P);
				float dist = VecMath.dotproduct(PA, t.normal);
				if(dist > distance) {
					distance = dist;
					furthestPoint = P;
					furthestPointID = i;
				}
			}
			facepoints.remove(furthestPointID);
			
			// 3. Find all faces that can be seen from this point
			List<Triangle> lightFaces = new ArrayList<Triangle>();
			for(int i = faces.size()-1; i >= 0; i--) {
				Triangle tri = faces.get(i);
				Vector3f triP = VecMath.subtraction(furthestPoint, vertices.get(tri.a));
				if(VecMath.dotproduct(tri.normal, triP) >= 0) {
					lightFaces.add(tri);
					faces.remove(i);
				}
			}
			
			// 4. Extract horizon edges of light-faces and extrude to Point
			
			// 5. Assign all points of all light-faces to the new created faces
			
			// 6. Push new created faces on the stack and start at (1)
		}
			
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
	
	private static List<Vector3f> getLightPoints(Triangle triangle, List<Vector3f> points) {
		LinkedList<Vector3f> result = new LinkedList<Vector3f>();
		for(int i = points.size() - 1; i >= 0; i--) {
			Vector3f p = points.get(i);
			if(VecMath.dotproduct(triangle.normal, p) > 0) {
				result.add(p);
				points.remove(i);
			}
		}
		return result;
	}
	
	private class Triangle {
		int a, b, c;
		Vector3f normal;
		
		public Triangle(int a, int b, int c, Vector3f normal) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.normal = normal;
		}
	}
}