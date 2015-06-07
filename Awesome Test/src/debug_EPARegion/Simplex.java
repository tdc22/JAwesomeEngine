package debug_EPARegion;

import math.VecMath;
import objects.ShapedObject;
import utils.GLConstants;
import vector.Vector3f;

public class Simplex extends ShapedObject {
	// Example values from epaDebugger
	public Vector3f a = new Vector3f(-0.117474556, 1.4255171, 1.4336302);
	public Vector3f b = new Vector3f(2.6647577, 1.6752844, -1.7089069);
	public Vector3f c = new Vector3f(0.009543419, 1.511261, -1.5328605);
	public Vector3f normal;

	public Simplex() {
		setRenderMode(GLConstants.TRIANGLES);

		addVertex(a);
		addVertex(b);
		addVertex(c);
		normal = VecMath.normalize(VecMath.computeNormal(a, b, c));
		System.out.println("normal: " + normal);

		addIndices(0, 1, 2);

		prerender();
	}
}
