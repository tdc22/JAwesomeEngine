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

	public SimpleCurvePath(SimpleCurvePath<L> simplecurvepath) {
		curves = new ArrayList<Curve<L>>(simplecurvepath.getCurves());
		numCurves = simplecurvepath.getCurveCount();
		oneOverNum = simplecurvepath.getOneOverCurveCount();
	}

	public void addCurve(Curve<L> curve) {
		curves.add(curve);
		numCurves++;
		oneOverNum = 1 / (float) numCurves;
	}

	public List<Curve<L>> getCurves() {
		return curves;
	}

	public int getCurveCount() {
		return numCurves;
	}

	public float getOneOverCurveCount() {
		return oneOverNum;
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

	public int getCurveNum(float t) {
		int num = (int) (numCurves * t);
		if (num == numCurves) {
			num--;
		}
		return num;
	}
}