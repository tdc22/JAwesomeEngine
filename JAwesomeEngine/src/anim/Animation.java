package anim;

import java.util.ArrayList;
import java.util.List;

import curves.SimpleAngularCurvePath;
import curves.SimpleCurvePath;
import quaternion.Rotation;
import vector.Vector;

public class Animation<L extends Vector, A extends Rotation> {
	List<SimpleCurvePath<L>> animationTranslationPaths;
	List<SimpleAngularCurvePath<A>> animationRotationPaths;
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

	private void init(float animationspeed, boolean loops) {
		this.speed = animationspeed;
		this.loops = loops;
		animationTranslationPaths = new ArrayList<SimpleCurvePath<L>>();
		animationRotationPaths = new ArrayList<SimpleAngularCurvePath<A>>();
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

	public void addAnimationPath(SimpleCurvePath<L> path, SimpleAngularCurvePath<A> angularpath) {
		animationTranslationPaths.add(path);
		animationRotationPaths.add(angularpath);
	}

	public void addAnimationTranslationPath(SimpleCurvePath<L> path) {
		animationTranslationPaths.add(path);
	}

	public void addAnimationRotationPath(SimpleAngularCurvePath<A> angularpath) {
		animationRotationPaths.add(angularpath);
	}

	public SimpleCurvePath<L> getAnimationTranslationPath(int id) {
		return animationTranslationPaths.get(id);
	}

	public SimpleAngularCurvePath<A> getAnimationRotationPath(int id) {
		return animationRotationPaths.get(id);
	}
}