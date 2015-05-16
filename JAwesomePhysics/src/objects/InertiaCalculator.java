package objects;

import quaternion.Rotation;

public interface InertiaCalculator<A extends Rotation> {
	public A calculateInertia(float mass);
}