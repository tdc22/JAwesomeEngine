package curves;

import vector.Vector2f;

public class BezierCurve2 extends BezierCurve<Vector2f> {

	public BezierCurve2(Vector2f p0, Vector2f p1, Vector2f p2, Vector2f p3) {
		super(p0, p1, p2, p3);
	}

	@Override
	public Vector2f getPoint(float t) {
		float u = 1 - t;
		float tt = t * t;
		float uu = u * u;
		float uuu = uu * u;
		float ttt = tt * t;

		Vector2f p = new Vector2f(p0.x * uuu + 3 * uu * t * p1.x + 3 * u * tt * p2.x + ttt * p3.x,
				p0.y * uuu + 3 * uu * t * p1.y + 3 * u * tt * p2.y + ttt * p3.y);
		return p;
	}

	@Override
	public void setStartPoint(Vector2f startpoint) {
		p0 = startpoint;
	}

}
