package miscTests;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import vector.Vector3f;

public class SpherePointMatcher {
	public static void main(String[] args) {
		List<Vector3f> points = new ArrayList<Vector3f>();
		float radius = 1;
		points.add(VecMath.scale(new Vector3f(0.5257311, 0.8506508, 0.0),
				radius));
		points.add(VecMath.scale(new Vector3f(-0.5257311, 0.8506508, 0.0),
				radius));
		points.add(VecMath.scale(new Vector3f(0.5257311, -0.8506508, 0.0),
				radius));
		points.add(VecMath.scale(new Vector3f(-0.5257311, -0.8506508, 0.0),
				radius));
		points.add(VecMath.scale(new Vector3f(0.0, 0.5257311, 0.8506508),
				radius));
		points.add(VecMath.scale(new Vector3f(0.0, -0.5257311, 0.8506508),
				radius));
		points.add(VecMath.scale(new Vector3f(0.0, 0.5257311, -0.8506508),
				radius));
		points.add(VecMath.scale(new Vector3f(0.0, -0.5257311, -0.8506508),
				radius));
		points.add(VecMath.scale(new Vector3f(0.8506508, 0.0, 0.5257311),
				radius));
		points.add(VecMath.scale(new Vector3f(-0.8506508, 0.0, 0.5257311),
				radius));
		points.add(VecMath.scale(new Vector3f(0.8506508, 0.0, -0.5257311),
				radius));
		points.add(VecMath.scale(new Vector3f(-0.8506508, 0.0, -0.5257311),
				radius));

		float distance = Float.MAX_VALUE;
		Vector3f a = points.get(0);
		for (int i = 1; i < points.size(); i++) {
			float tmpdist = VecMath
					.length(VecMath.subtraction(points.get(i), a));
			if (tmpdist < distance)
				distance = tmpdist;
		}

		float epsilon = 0f;
		List<Integer[]> faces = new ArrayList<Integer[]>();
		for (int i = 0; i < points.size(); i++) {
			for (int j = i; j < points.size(); j++) {
				if (j != i) {
					if (distance
							- VecMath.length(VecMath.subtraction(points.get(i),
									points.get(j))) < epsilon)
						for (int k = j; k < points.size(); k++) {
							if (k != j && k != i) {
								if (distance
										- VecMath.length(VecMath.subtraction(
												points.get(k), points.get(i))) < epsilon)
									if (distance
											- VecMath.length(VecMath
													.subtraction(points.get(k),
															points.get(j))) < epsilon)
										faces.add(new Integer[] { i, j, k });
							}
						}
				}
			}
		}
		System.out.print("facecount: " + faces.size() + "; distance: "
				+ distance);
		for (Integer[] f : faces)
			System.out.println(f[0] + "; " + f[1] + "; " + f[2]);
	}
}