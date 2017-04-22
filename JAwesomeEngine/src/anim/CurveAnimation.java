package anim;

import java.util.ArrayList;
import java.util.List;

import curves.SimpleAngularCurvePath;
import curves.SimpleCurvePath;
import quaternion.Rotation;
import vector.Vector;

public class CurveAnimation<L extends Vector, A extends Rotation> extends Animation<L, A> {
	List<SimpleCurvePath<L>> animationTranslationPaths = new ArrayList<SimpleCurvePath<L>>();;
	List<SimpleAngularCurvePath<A>> animationRotationPaths = new ArrayList<SimpleAngularCurvePath<A>>();;

	public CurveAnimation() {
		super();
	}

	public CurveAnimation(float animationspeed) {
		super(animationspeed);
	}

	public CurveAnimation(boolean loops) {
		super(loops);
	}

	public CurveAnimation(float animationspeed, boolean loops) {
		super(animationspeed, loops);
	}

	public CurveAnimation(CurveAnimation<L, A> animation) {
		super();
		animationTranslationPaths.addAll(animation.getAnimationTranslationPaths());
		animationRotationPaths.addAll(animation.getAnimationRotationPaths());
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

	public List<SimpleCurvePath<L>> getAnimationTranslationPaths() {
		return animationTranslationPaths;
	}

	public List<SimpleAngularCurvePath<A>> getAnimationRotationPaths() {
		return animationRotationPaths;
	}
}
