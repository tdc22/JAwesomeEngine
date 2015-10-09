package curves;

import java.util.ArrayList;
import java.util.List;

import vector.Vector;

public class LinearCurvePath<L extends Vector> {
	List<Curve<L>> curves;
	int numBezierCurves = 0;
	float oneOverNum = 0;

	public LinearCurvePath() {
		curves = new ArrayList<Curve<L>>();
	}

	public void addCurve(Curve<L> curve) {
		curves.add(curve);
		numBezierCurves++;
		oneOverNum = 1 / (float) numBezierCurves;
	}

	public L getPoint(float t) {
		int num = (int) (numBezierCurves * t);
		t %= oneOverNum;
		t *= numBezierCurves;
		return curves.get(num).getPoint(t);
	}
}