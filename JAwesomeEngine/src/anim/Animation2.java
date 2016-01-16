package anim;

import quaternion.Complexf;
import vector.Vector2f;

public class Animation2 extends Animation<Vector2f, Complexf> {
	public Animation2() {
		super();
	}

	public Animation2(float animationspeed) {
		super(animationspeed);
	}

	public Animation2(boolean loops) {
		super(loops);
	}

	public Animation2(float animationspeed, boolean loops) {
		super(animationspeed, loops);
	}

	public Animation2(Animation2 animation) {
		super(animation);
	}
}
