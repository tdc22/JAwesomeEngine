package animationEditor;

import curves.BezierCurve2;
import objects.ShapedObject2;
import utils.GLConstants;

public class RenderedBezierCurve extends ShapedObject2 {
	public BezierCurve2 bezier;
	private final float numLines = 200;

	public RenderedBezierCurve(BezierCurve2 bezier) {
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
