package physicsSupportFunction;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import matrix.Matrix4f;
import objects.CollisionShape;
import objects.ShapedObject3;
import objects.SupportMap;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class SupportDifferenceObject extends ShapedObject3 {
	ShapedObject3 so1, so2;
	CollisionShape<Vector3f, ?, ?> rb1, rb2;

	public SupportDifferenceObject(ShapedObject3 s1, CollisionShape<Vector3f, ?, ?> r1, ShapedObject3 s2,
			CollisionShape<Vector3f, ?, ?> r2) {
		rendermode = GLConstants.POINTS;
		so1 = s1;
		rb1 = r1;
		so2 = s2;
		rb2 = r2;
		updateShape();
	}

	private Vector3f support(SupportMap<Vector3f> Sa, SupportMap<Vector3f> Sb, Vector3f dir) {
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

	public List<Vector3f> updateShape() {
		// List<Vector3f> v1 = so1.getVertices();
		// List<Vector3f> v2 = so2.getVertices();

		// Just for testing, so this is ok...
		deleteData();

		List<Vector3f> directions = new ArrayList<Vector3f>();
		List<Vector3f> result = new ArrayList<Vector3f>();
		// for(Vector3f v : v1) {
		// Vector3f res = support(rb1, rb2, v);
		// if(!result.contains(res))
		// result.add(res);
		// // for(Vector3f vec : v2) {
		// // Vector3f res = VecMath.substraction(rb1.supportPoint(v),
		// rb2.supportPoint(VecMath.negate(vec)));
		// // if(!result.contains(res))
		// // result.add(res);
		// // }
		// }
		// for(Vector3f v : v2) {
		// Vector3f res = support(rb1, rb2, v);
		// if(!result.contains(res))
		// result.add(res);
		// }

		// HashMap<Vector3f, Integer> tmp = new HashMap<Vector3f, Integer>();

		Vector3f vx = new Vector3f(1, 0, 0);
		Vector3f vy = new Vector3f(0, 1, 0);
		Vector3f vz = new Vector3f(0, 0, 1);
		Matrix4f mat = new Matrix4f();
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				for (int z = 0; z < 10; z++) {
					Vector3f dir = VecMath.transformVector(mat, vx);
					directions.add(dir);
					Vector3f res = support(rb1, rb2, dir);
					// System.out.println(VecMath.transformVector(mat,
					// vx).toString());
					if (!result.contains(res))
						result.add(res);

					// if (tmp.containsKey(res)) {
					// tmp.put(res, tmp.get(res) + 1);
					// } else {
					// tmp.put(res, 1);
					// }

					mat.rotate(36f, vz);
				}
				mat.rotate(36f, vy);
			}
			mat.rotate(36f, vx);
		}

		// for (Map.Entry<Vector3f, Integer> entry : tmp.entrySet()) {
		// System.out.println(entry.getKey() + ": " + entry.getValue());
		// }

		// Vector3f res = support(rb1, rb2, new Vector3f(1, 1, 1));

		// System.out.println(result.size());

		// System.out.println(v1.size() + "; " + v2.size() + "; " +
		// result.size());

		for (int r = 0; r < result.size(); r++) {
			addVertex(result.get(r), Color.GRAY, new Vector2f(), new Vector3f(0, 1, 0));
			addIndex(r);
		}
		this.prerender();

		return directions;
	}
}