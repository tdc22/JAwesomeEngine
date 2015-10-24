package anim;

import quaternion.Quaternionf;
import vector.Vector3f;

public class Animation3 extends Animation<Vector3f, Quaternionf> {
	public Animation3() {
		super();
	}

	public Animation3(float animationspeed) {
		super(animationspeed);
	}

	public Animation3(float animationspeed, boolean loops) {
		super(animationspeed, loops);
	}
}
