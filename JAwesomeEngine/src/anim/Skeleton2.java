package anim;

import curves.BezierCurve2;
import curves.SimpleAngularCurvePath;
import curves.SimpleCurvePath;
import curves.SquadCurve2;
import math.ComplexMath;
import math.VecMath;
import objects.BaseObject;
import objects.BaseObject2;
import quaternion.Complexf;
import vector.Vector2f;

public class Skeleton2 extends Skeleton<Vector2f, Complexf> {
	boolean mirroredX = false;
	boolean mirroredY = false;
	boolean invertRotation = false;

	public Skeleton2(Animation<Vector2f, Complexf> animation) {
		super(animation, new BaseObject2[0]);
		nullvec = new Vector2f();
		nullrot = new Complexf();
	}

	public Skeleton2(Animation<Vector2f, Complexf> animation, BaseObject<Vector2f, Complexf>[] bodypart) {
		super(animation, bodypart);
		nullvec = new Vector2f();
		nullrot = new Complexf();
	}

	@Override
	protected void updateAnimation(float animationTimer) {
		for (int i = 0; i < bodyparts.size(); i++) {
			BaseObject<Vector2f, Complexf> part = bodyparts.get(i);
			Vector2f trans = animation.getAnimationTranslationPath(i).getPoint(animationTimer);
			if (mirroredX)
				trans.x = -trans.x;
			if (mirroredY)
				trans.y = -trans.y;
			part.translate(trans);
			Complexf rot = animation.getAnimationRotationPath(i).getRotation(animationTimer);
			if (invertRotation)
				rot.invert();
			part.rotate(rot);
		}
	}

	// TODO: Reset first point of animation after first path is done.
	// TODO: Optimize!
	public void setDynamicAnimation(Animation<Vector2f, Complexf> animationparam) {
		this.dynamicStoreAnimation = animationparam;
		System.out.println("-----------");
		for (int i = 0; i < bodyparts.size(); i++) {
			animation = new Animation<Vector2f, Complexf>(animationparam);
			BaseObject<Vector2f, Complexf> bodypart = bodyparts.get(i);
			Vector2f trans = VecMath.subtraction(bodypart.getTranslation(), attachedTo.getTranslation());
			if (mirroredX)
				trans.x = -trans.x;
			if (mirroredY)
				trans.y = -trans.y;
			SimpleCurvePath<Vector2f> translationpath = new SimpleCurvePath<Vector2f>(
					animation.getAnimationTranslationPath(i));
			SimpleAngularCurvePath<Complexf> rotationpath = new SimpleAngularCurvePath<Complexf>(
					animation.getAnimationRotationPath(i));
			BezierCurve2 oldcurve = (BezierCurve2) translationpath.getCurves().get(0);
			SquadCurve2 oldrotcurve = (SquadCurve2) rotationpath.getCurves().get(0);
			translationpath.getCurves().set(0,
					new BezierCurve2(new Vector2f(), new Vector2f(), new Vector2f(), new Vector2f()));
			rotationpath.getCurves().set(0,
					new SquadCurve2(ComplexMath.substraction(bodypart.getRotation(), attachedTo.getRotation()),
							oldrotcurve.getR1(), oldrotcurve.getR2(), oldrotcurve.getR3()));
			animation.getAnimationTranslationPaths().set(i, translationpath);
			animation.getAnimationRotationPaths().set(i, rotationpath);
			System.out.println(i);
			System.out.println(translationpath.getCurves());
		}
		animationTimer = 0;
	}

	public void mirrorX() {
		mirroredX = !mirroredX;
	}

	public boolean isMirroredX() {
		return mirroredX;
	}

	public void setMirroredX(boolean mirroredX) {
		this.mirroredX = mirroredX;
	}

	public void mirrorY() {
		mirroredY = !mirroredY;
	}

	public boolean isMirroredY() {
		return mirroredY;
	}

	public void setMirroredY(boolean mirroredY) {
		this.mirroredY = mirroredY;
	}

	public void invertRotation() {
		invertRotation = !invertRotation;
	}

	public boolean isRotationInverted() {
		return invertRotation;
	}

	public void setRotationInverted(boolean invertRotation) {
		this.invertRotation = invertRotation;
	}
}