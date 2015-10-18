package curves;

import java.util.ArrayList;
import java.util.List;

import vector.Vector;

public class SimpleCurvePath<L extends Vector> {
	List<Curve<L>> curves;
	int numCurves = 0;
	float oneOverNum = 0;

	public SimpleCurvePath() {
		curves = new ArrayList<Curve<L>>();
	}

	public void addCurve(Curve<L> curve) {
		curves.add(curve);
		numCurves++;
		oneOverNum = 1 / (float) numCurves;
	}

	public L getPoint(float t) {
		int num = (int) (numCurves * t);
		t %= oneOverNum;
		t *= numCurves;
		if (num == numCurves) {
			num--;
			t = 1;
		}
		return curves.get(num).getPoint(t);
	}
}