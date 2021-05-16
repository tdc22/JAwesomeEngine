package anim;

import curves.BezierCurve3;
import curves.SimpleAngularCurvePath;
import curves.SimpleCurvePath;
import curves.SquadCurve3;
import objects.BaseObject;
import objects.BaseObject3;
import quaternion.Quaternionf;
import vector.Vector3f;

public class CurveSkeleton3 extends CurveSkeleton<Vector3f, Quaternionf> {
	boolean mirroredX = false;
	boolean mirroredY = false;
	boolean mirroredZ = false;
	boolean invertRotation = false;

	public CurveSkeleton3() {
		super(null, new BaseObject3[0]);
		nullvec = new Vector3f();
		nullrot = new Quaternionf();
	}
	
	public CurveSkeleton3(CurveAnimation<Vector3f, Quaternionf> animation) {
		super(animation, new BaseObject3[0]);
		nullvec = new Vector3f();
		nullrot = new Quaternionf();
	}

	public CurveSkeleton3(CurveAnimation<Vector3f, Quaternionf> animation,
			BaseObject<Vector3f, Quaternionf>[] bodypart) {
		super(animation, bodypart);
		nullvec = new Vector3f();
		nullrot = new Quaternionf();
	}

	@Override
	public void updateAnimation() {
		CurveAnimation<Vector3f, Quaternionf> currentAnimation = getCurrentAnimation();
		for (int i = 0; i < bodyparts.size(); i++) {
			BaseObject<Vector3f, Quaternionf> part = bodyparts.get(i);
			Vector3f trans = currentAnimation.getAnimationTranslationPath(i).getPoint(currentAnimation.animationTimer);
			if (mirroredX)
				trans.x = -trans.x;
			if (mirroredY)
				trans.y = -trans.y;
			if (mirroredZ)
				trans.z = -trans.z;
			if(attachedTo != null)
				trans.transform(attachedTo.getRotation());
			part.translate(trans);
			Quaternionf rot = currentAnimation.getAnimationRotationPath(i).getRotation(currentAnimation.animationTimer);
			if (invertRotation)
				rot.invert();
			part.rotate(rot);
		}
	}
	
	// TODO: Optimize!
	public void setDynamicAnimation(CurveAnimation<Vector3f, Quaternionf> animationparam, float dynamicAnimationSpeed) {
		dynamicAnimationTransition = new DynamicCurveAnimationTransition<Vector3f, Quaternionf>(animationparam,
				dynamicAnimationSpeed);
		for (int i = 0; i < bodyparts.size(); i++) {
			Vector3f trans = animation.getAnimationTranslationPath(i).getPoint(animation.animationTimer);
			SimpleCurvePath<Vector3f> translationpath = new SimpleCurvePath<Vector3f>(
					dynamicAnimationTransition.getAnimation().getAnimationTranslationPath(i));
			SimpleAngularCurvePath<Quaternionf> rotationpath = new SimpleAngularCurvePath<Quaternionf>(
					dynamicAnimationTransition.getAnimation().getAnimationRotationPath(i));
			BezierCurve3 oldcurve = (BezierCurve3) translationpath.getCurves().get(0);
			SquadCurve3 oldrotcurve = (SquadCurve3) rotationpath.getCurves().get(0);
			translationpath.getCurves().set(0,
					new BezierCurve3(trans, oldcurve.getP1(), oldcurve.getP2(), oldcurve.getP3()));
			// TODO: fix, when squadcurves are properly fixed.
			Quaternionf newSquadrotation = animation.getAnimationRotationPath(i).getRotation(animation.animationTimer);
			rotationpath.getCurves().set(0,
					new SquadCurve3(newSquadrotation, newSquadrotation, oldrotcurve.getR2(), oldrotcurve.getR3()));
			dynamicAnimationTransition.getAnimation().getAnimationTranslationPaths().set(i, translationpath);
			dynamicAnimationTransition.getAnimation().getAnimationRotationPaths().set(i, rotationpath);
		}
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
	
	public void mirrorZ() {
		mirroredZ = !mirroredZ;
	}

	public boolean isMirroredZ() {
		return mirroredZ;
	}

	public void setMirroredZ(boolean mirroredZ) {
		this.mirroredZ = mirroredZ;
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
