package curves;

import java.util.ArrayList;
import java.util.List;

import quaternion.Rotation;

public class SimpleAngularCurvePath<A extends Rotation> {
	List<AngularCurve<A>> curves;
	int numCurves = 0;
	float oneOverNum = 0;

	public SimpleAngularCurvePath() {
		curves = new ArrayList<AngularCurve<A>>();
	}

	public SimpleAngularCurvePath(SimpleAngularCurvePath<A> simpleangularcurvepath) {
		curves = new ArrayList<AngularCurve<A>>(simpleangularcurvepath.getCurves());
		numCurves = simpleangularcurvepath.getCurveCount();
		oneOverNum = simpleangularcurvepath.getOneOverCurveCount();
	}

	public void addCurve(AngularCurve<A> curve) {
		curves.add(curve);
		numCurves++;
		oneOverNum = 1 / (float) numCurves;
	}

	public List<AngularCurve<A>> getCurves() {
		return curves;
	}

	public int getCurveCount() {
		return numCurves;
	}

	public float getOneOverCurveCount() {
		return oneOverNum;
	}

	public A getRotation(float t) {
		int num = (int) (numCurves * t);
		t %= oneOverNum;
		t *= numCurves;
		if (num == numCurves) {
			num--;
			t = 1;
		}
		return curves.get(num).getRotation(t);
	}

	public int getCurveNum(float t) {
		int num = (int) (numCurves * t);
		if (num == numCurves) {
			num--;
		}
		return num;
	}
}
