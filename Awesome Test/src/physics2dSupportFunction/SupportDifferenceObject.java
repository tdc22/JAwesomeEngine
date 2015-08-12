package physics2dSupportFunction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import matrix.Matrix4f;
import objects.CollisionShape;
import objects.ShapedObject;
import objects.ShapedObject2;
import objects.SupportMap;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class SupportDifferenceObject extends ShapedObject2 {
	ShapedObject so1, so2;
	CollisionShape<Vector2f, ?, ?> rb1, rb2;

	public SupportDifferenceObject(ShapedObject s1, CollisionShape<Vector2f, ?, ?> r1, ShapedObject s2,
			CollisionShape<Vector2f, ?, ?> r2) {
		rendermode = GLConstants.POINTS;
		so1 = s1;
		rb1 = r1;
		so2 = s2;
		rb2 = r2;
		updateShape();
	}

	private Vector2f support(SupportMap<Vector2f> Sa, SupportMap<Vector2f> Sb, Vector2f dir) {
		// System.out.println(Sa.supportPoint(dir).toString() + "; " +
		// Sb.supportPoint(VecMath.negate(dir)) + "; " +
		// VecMath.substraction(Sa.supportPoint(dir),
		// Sb.supportPoint(VecMath.negate(dir))));
		// System.out.println(dir.toString());
		// System.out.println(Sa.supportPoint(dir).toString());
		// System.out.println(Sb.supportPoint(dir).toString());
		// System.out.println(VecMath.substraction(Sa.supportPoint(dir),
		// Sb.supportPoint(VecMath.negate(dir))).toString());
		return VecMath.subtraction(Sa.supportPoint(dir), Sb.supportPointNegative(dir));
	}

	public List<Vector2f> updateShape() {
		// List<Vector2f> v1 = so1.getVertices();
		// List<Vector2f> v2 = so2.getVertices();

		// Just for testing, so this is ok...
		deleteData();

		List<Vector2f> directions = new ArrayList<Vector2f>();
		List<Vector2f> result = new ArrayList<Vector2f>();
		// for(Vector2f v : v1) {
		// Vector2f res = support(rb1, rb2, v);
		// if(!result.contains(res))
		// result.add(res);
		// // for(Vector2f vec : v2) {
		// // Vector2f res = VecMath.substraction(rb1.supportPoint(v),
		// rb2.supportPoint(VecMath.negate(vec)));
		// // if(!result.contains(res))
		// // result.add(res);
		// // }
		// }
		// for(Vector2f v : v2) {
		// Vector2f res = support(rb1, rb2, v);
		// if(!result.contains(res))
		// result.add(res);
		// }

		Vector2f vx1 = new Vector2f(1, 0);
		Vector3f vx = new Vector3f(1, 0, 0);
		Vector3f vy = new Vector3f(0, 1, 0);
		Matrix4f mat = new Matrix4f();
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				Vector2f dir = VecMath.transformVector(mat, vx1);
				directions.add(dir);
				Vector2f res = support(rb1, rb2, dir);
				// System.out.println(VecMath.transformVector(mat,
				// vx).toString());
				if (!result.contains(res))
					result.add(res);

				mat.rotate(36, vy);
			}
			mat.rotate(36, vx);
		}
		// Vector2f res = support(rb1, rb2, new Vector2f(1, 1, 1));

		// System.out.println(result.size());

		// System.out.println(v1.size() + "; " + v2.size() + "; " +
		// result.size());

		for (int r = 0; r < result.size(); r++) {
			addVertex(result.get(r), Color.GRAY, new Vector2f());
			addIndex(r);
		}
		this.prerender();

		return directions;
	}
}