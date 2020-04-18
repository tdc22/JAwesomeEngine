package tool_animationEditor3d;

import curves.BezierCurve3;
import objects.ShapedObject3;
import utils.GLConstants;

public class RenderedBezierCurve3 extends ShapedObject3 {
	public BezierCurve3 bezier;
	private final float numLines = 200;

	public RenderedBezierCurve3(BezierCurve3 bezier) {
		this.bezier = bezier;
		setRenderMode(GLConstants.LINE_STRIP);
		for (int i = 0; i < numLines + 1; i++) {
			addVertex(bezier.getPoint(i / numLines));
			addIndex(i);
		}
		prerender();
	}

	@Override
	public String toString() {
		return bezier.toString();
	}
}
