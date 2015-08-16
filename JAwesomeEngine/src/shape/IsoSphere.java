package shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.ShapedObject;
import shapedata.SphereStructure;
import vector.Vector2f;
import vector.Vector3f;

public class IsoSphere extends ShapedObject implements SphereStructure {
	public final static int POINT_SUBDIVISION = 0;

	public final static int TRIANGLE_SUBDIVISION = 1;
	float radius;

	public IsoSphere(float x, float y, float z, float radius, int subdivisions) {
		super();
		translateTo(x, y, z);
		init(radius, subdivisions);
	}

	public IsoSphere(Vector3f pos, float radius, int subdivisions) {
		super();
		translateTo(pos);
		init(radius, subdivisions);
	}

	private Vector3f getCenter(Vector3f a, Vector3f b) {
		return VecMath.setScale(VecMath.addition(a, b), radius);
	}

	@Override
	public float getRadius() {
		return radius;
	}

	private void init(float radius, int subdivisions) {
		this.radius = radius;
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		List<Integer[]> faces = new ArrayList<Integer[]>();

		// vertices.add(VecMath.scale(new Vector3f(0.81649655, 0.0,
		// -0.57735026), radius));
		// vertices.add(VecMath.scale(new Vector3f(-0.81649655, 0.0,
		// -0.57735026), radius));
		// vertices.add(VecMath.scale(new Vector3f(0.0, 0.81649655, 0.57735026),
		// radius));
		// vertices.add(VecMath.scale(new Vector3f(0.0, -0.81649655,
		// 0.57735026), radius));
		//
		// faces.add(new Integer[] {1, 0, 3, 0, 2, 0});
		// faces.add(new Integer[] {0, 1, 2, 1, 3, 1});
		// faces.add(new Integer[] {0, 2, 3, 2, 1, 2});
		// faces.add(new Integer[] {0, 3, 1, 3, 2, 3});

		vertices.add(VecMath.scale(new Vector3f(0.5257311, 0.8506508, 0.0),
				radius));
		vertices.add(VecMath.scale(new Vector3f(-0.5257311, 0.8506508, 0.0),
				radius));
		vertices.add(VecMath.scale(new Vector3f(0.5257311, -0.8506508, 0.0),
				radius));
		vertices.add(VecMath.scale(new Vector3f(-0.5257311, -0.8506508, 0.0),
				radius));
		vertices.add(VecMath.scale(new Vector3f(0.0, 0.5257311, 0.8506508),
				radius));
		vertices.add(VecMath.scale(new Vector3f(0.0, -0.5257311, 0.8506508),
				radius));
		vertices.add(VecMath.scale(new Vector3f(0.0, 0.5257311, -0.8506508),
				radius));
		vertices.add(VecMath.scale(new Vector3f(0.0, -0.5257311, -0.8506508),
				radius));
		vertices.add(VecMath.scale(new Vector3f(0.8506508, 0.0, 0.5257311),
				radius));
		vertices.add(VecMath.scale(new Vector3f(-0.8506508, 0.0, 0.5257311),
				radius));
		vertices.add(VecMath.scale(new Vector3f(0.8506508, 0.0, -0.5257311),
				radius));
		vertices.add(VecMath.scale(new Vector3f(-0.8506508, 0.0, -0.5257311),
				radius));

		faces.add(new Integer[] { 0, 0, 2, 0, 11, 0 });
		faces.add(new Integer[] { 0, 0, 5, 0, 7, 0 });
		faces.add(new Integer[] { 0, 0, 5, 0, 11, 0 });
		faces.add(new Integer[] { 0, 0, 7, 0, 9, 0 });
		faces.add(new Integer[] { 1, 0, 3, 0, 8, 0 });
		faces.add(new Integer[] { 1, 0, 3, 0, 10, 0 });
		faces.add(new Integer[] { 1, 0, 5, 0, 7, 0 });
		faces.add(new Integer[] { 1, 0, 5, 0, 10, 0 });
		faces.add(new Integer[] { 1, 0, 7, 0, 8, 0 });
		faces.add(new Integer[] { 2, 0, 4, 0, 6, 0 });
		faces.add(new Integer[] { 2, 0, 4, 0, 11, 0 });
		faces.add(new Integer[] { 2, 0, 6, 0, 9, 0 });
		faces.add(new Integer[] { 3, 0, 4, 0, 6, 0 });
		faces.add(new Integer[] { 3, 0, 4, 0, 10, 0 });
		faces.add(new Integer[] { 3, 0, 6, 0, 8, 0 });
		faces.add(new Integer[] { 4, 0, 10, 0, 11, 0 });
		faces.add(new Integer[] { 5, 0, 10, 0, 11, 0 });
		faces.add(new Integer[] { 6, 0, 8, 0, 9, 0 });
		faces.add(new Integer[] { 7, 0, 8, 0, 9, 0 });

		for (int i = 0; i < subdivisions; i++) {
			List<Integer[]> currfaces = new ArrayList<Integer[]>(faces);
			// switch(divisionmethod) {
			// case POINT_SUBDIVISION:
			// for(int j = currfaces.size()-1; j >= 0; j--) {
			// Integer[] f = currfaces.get(j);
			//
			// Vector3f a = vertices.get(f[0]);
			// Vector3f b = vertices.get(f[2]);
			// Vector3f c = vertices.get(f[4]);
			//
			// Vector3f d =
			// VecMath.setScale(VecMath.addition(VecMath.addition(a, b), c),
			// radius);
			// int vc = vertices.size();
			// vertices.add(d);
			//
			// faces.remove(j);
			// faces.add(new Integer[] {f[0], 0, f[2], 0, vc, 0});
			// faces.add(new Integer[] {f[0], 0, vc, 0, f[4], 0});
			// faces.add(new Integer[] {vc, 0, f[2], 0, f[4], 0});
			// }
			// break;
			// case TRIANGLE_SUBDIVISION:
			for (Integer[] f : currfaces) {
				Vector3f v = getCenter(vertices.get(f[2]), vertices.get(f[4]));
				if (!vertices.contains(v)) {
					vertices.add(v);
				}
				v = getCenter(vertices.get(f[0]), vertices.get(f[4]));
				if (!vertices.contains(v)) {
					vertices.add(v);
				}
			}

			for (int j = currfaces.size() - 1; j >= 0; j--) {
				Integer[] f = currfaces.get(j);

				int a = vertices.indexOf(getCenter(vertices.get(f[0]),
						vertices.get(f[2])));
				int b = vertices.indexOf(getCenter(vertices.get(f[4]),
						vertices.get(f[0])));
				int c = vertices.indexOf(getCenter(vertices.get(f[2]),
						vertices.get(f[4])));

				System.out.println(a + "; " + b + "; " + c);

				faces.remove(j);
				faces.add(new Integer[] { f[0], 0, a, 0, b, 0 });
				faces.add(new Integer[] { b, 0, c, 0, f[4], 0 });
				faces.add(new Integer[] { a, 0, f[2], 0, c, 0 });
				faces.add(new Integer[] { a, 0, c, 0, b, 0 });
			}
			// break;
			// }
		}

		for (Vector3f v : vertices)
			addVertex(v, Color.GRAY, new Vector2f(0, 0), VecMath.normalize(v));
		vertices.clear();
		// edges.clear();

		for (Integer[] f : faces)
			addTriangle(f[0], f[1], f[2], f[3], f[4], f[5]);
		faces.clear();

		this.prerender();
	}
}
