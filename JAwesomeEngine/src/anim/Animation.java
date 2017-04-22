package anim;

import quaternion.Rotation;
import vector.Vector;

public abstract class Animation<L extends Vector, A extends Rotation> {
	boolean loops;
	float speed;

	public Animation() {
		init(0.001f, true);
	}

	public Animation(float animationspeed) {
		init(animationspeed, true);
	}

	public Animation(boolean loops) {
		init(0.001f, loops);
	}

	public Animation(float animationspeed, boolean loops) {
		init(animationspeed, loops);
	}

	public Animation(Animation<L, A> animation) {
		init(animation.getSpeed(), animation.isLooping());
	}

	private void init(float animationspeed, boolean loops) {
		this.speed = animationspeed;
		this.loops = loops;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getSpeed() {
		return speed;
	}

	public void setLooping(boolean loop) {
		this.loops = loop;
	}

	public boolean isLooping() {
		return loops;
	}
}