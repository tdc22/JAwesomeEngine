package anim;

import java.util.ArrayList;
import java.util.List;

import curves.LinearCurvePath;
import vector.Vector;

public class Animation<L extends Vector> {
	List<LinearCurvePath<L>> animationPaths;
	boolean loops = true;
	float speed = 0.001f;

	public Animation() {
		animationPaths = new ArrayList<LinearCurvePath<L>>();
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

	public void addAnimationPath(LinearCurvePath<L> path) {
		animationPaths.add(path);
	}

	public LinearCurvePath<L> getAnimationPath(int id) {
		return animationPaths.get(id);
	}
}