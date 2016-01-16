package curves;

import quaternion.Rotation;

public abstract class AngularCurve<A extends Rotation> {
	public abstract A getRotation(float t);

	public abstract void setStartRotation(A startrotation);
}
