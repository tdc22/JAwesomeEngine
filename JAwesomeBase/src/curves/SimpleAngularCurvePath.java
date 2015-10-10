package curves;

import java.util.ArrayList;
import java.util.List;

import quaternion.Rotation;

public class SimpleAngularCurvePath<A extends Rotation> {
	List<AngularCurve<A>> curves;
	int numBezierCurves = 0;
	float oneOverNum = 0;

	public SimpleAngularCurvePath() {
		curves = new ArrayList<AngularCurve<A>>();
	}

	public void addCurve(AngularCurve<A> curve) {
		curves.add(curve);
		numBezierCurves++;
		oneOverNum = 1 / (float) numBezierCurves;
	}

	public A getRotation(float t) {
		int num = (int) (numBezierCurves * t);
		t %= oneOverNum;
		t *= numBezierCurves;
		return curves.get(num).getRotation(t);
	}
}
