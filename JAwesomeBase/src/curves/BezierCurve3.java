package curves;

import vector.Vector3f;

public class BezierCurve3 extends BezierCurve<Vector3f> {

	public BezierCurve3(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {
		super(p0, p1, p2, p3);
	}

	@Override
	public Vector3f getPoint(float t) {
		float u = 1 - t;
		float tt = t * t;
		float uu = u * u;
		float uuu = uu * u;
		float ttt = tt * t;

		Vector3f p = new Vector3f(p0.x * uuu + 3 * uu * t * p1.x + 3 * u * tt * p2.x + ttt * p3.x,
				p0.y * uuu + 3 * uu * t * p1.y + 3 * u * tt * p2.y + ttt * p3.y,
				p0.z * uuu + 3 * uu * t * p1.z + 3 * u * tt * p2.z + ttt * p3.z);
		return p;
	}

	@Override
	public void setStartPoint(Vector3f startpoint) {
		p0 = startpoint;
	}

}
