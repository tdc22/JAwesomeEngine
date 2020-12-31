package tool_animationEditor3d;

import java.util.ArrayList;
import java.util.List;

import curves.BezierCurve3;
import curves.SquadCurve3;
import math.VecMath;
import objects.ShapedObject3;
import quaternion.Quaternionf;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import utils.GLConstants;
import utils.VectorConstants;
import vector.Vector3f;

public class AnimationPath3 {
	List<ShapedObject3> markers;
	List<ShapedObject3> rotationreferences;
	List<ShapedObject3> secondaryrotationreferences;
	List<RenderedBezierCurve3> beziercurves;
	List<SquadCurve3> squadcurves;
	ShapedObject3 bodypart;

	Shader markershader, defaultshader;

	int numCurves = 0;
	float oneOverNum = 0;

	boolean skipPress = false;
	boolean dragging = false;
	int temppoint = -1, temppoint2 = -1, tempreference = -1;
	int dragID;
	ShapedObject3 draggedMarker;
	int draggedMarkerType;
	final float maxDraggingDistanceSqr = 0.001f;
	float animationTimer = 0;
	boolean closed = false;
	final Vector3f startpos = new Vector3f();

	public AnimationPath3(Shader defaultshader, Shader markershader, ShapedObject3 bodypart) {
		init(defaultshader, markershader, bodypart);
	}

	public AnimationPath3(AnimationPath3 ap) {
		init(ap.defaultshader, ap.markershader, ap.bodypart);
	}

	private void init(Shader defaultshader, Shader markershader, ShapedObject3 bodypart) {
		this.defaultshader = defaultshader;
		this.markershader = markershader;
		markers = new ArrayList<ShapedObject3>();
		rotationreferences = new ArrayList<ShapedObject3>();
		secondaryrotationreferences = new ArrayList<ShapedObject3>();
		beziercurves = new ArrayList<RenderedBezierCurve3>();
		squadcurves = new ArrayList<SquadCurve3>();
		this.bodypart = bodypart;
	}

	public Vector3f getPoint(float t) {
		if (beziercurves.size() > 0) {
			int num = (int) (numCurves * t);
			t %= oneOverNum;
			t *= numCurves;
			if (num == numCurves) {
				num--;
				t = 1;
			}
			return beziercurves.get(num).bezier.getPoint(t);
		}
		return null;
	}

	public Quaternionf getRotation(float t) {
		int num = (int) (numCurves * t);
		t %= oneOverNum;
		t *= numCurves;
		if (num == numCurves) {
			num--;
			t = 1;
		}
		return squadcurves.get(num).getRotation(t);
	}
	
	float maxproject;
	private void checkMarkers(List<ShapedObject3> markerlist, int markertype, Vector3f campos, Vector3f clickdir) {
		double dist;
		for(int i = 0; i < markerlist.size(); i++) {
			ShapedObject3 marker = markerlist.get(i);
			Vector3f camToMarker = VecMath.subtraction(marker.getTranslation(), campos);
			camToMarker.normalize();
			System.out.println(camToMarker);
			if ((dist = VecMath.dotproduct(camToMarker, clickdir)) > maxproject) {
				dragID = i;
				draggedMarker = marker;
				maxproject = (float) dist;
				draggedMarkerType = markertype;
			}
			System.out.println("Dist " + dist);
		}
	}

	public void clickLeft(Vector3f campos, Vector3f clickdir, Vector3f projectedpos) {
		System.out.println("Dir " + clickdir.length() + "; " + clickdir);
		maxproject = 0;
		checkMarkers(markers, 0, campos, clickdir);
		checkMarkers(rotationreferences, 1, campos, clickdir);
		checkMarkers(secondaryrotationreferences, 2, campos, clickdir);
		System.out.println("Proj " + (1 - maxproject));
		if(draggedMarker != null) {
			startpos.set(draggedMarker.getTranslation());
		}
		if ((1 - maxproject) <= maxDraggingDistanceSqr) {
			System.out.println("Start drag");
			dragging = true;
		} else {
			System.out.println("A");
			if (!closed) {
				System.out.println("B");
				if (!skipPress) {
					System.out.println("Add marker 1");
					if (temppoint == -1) {
						temppoint = markers.size();
					} else {
						temppoint2 = markers.size();
					}
					addBoxMarker(projectedpos);

					projectedpos.x += 0.2;
					addRotationMarker(projectedpos);
					
					projectedpos.x -= 0.2;
					projectedpos.y += 0.2;
					addSecondaryRotationMarker(projectedpos);
				} else {
					System.out.println("Add marker 2");
					markers.add(markers.get(markers.size() - 2));
					skipPress = false;
				}
			}
			else {
				dragging = false;
			}
		}
	}

	public void addBoxMarker(Vector3f pos) {
		Box m = new Box(pos, 0.1f, 0.1f, 0.1f);
		markers.add(m);
		markershader.addObject(m);
	}

	public void addSphereMarker(Vector3f pos) {
		Sphere m = new Sphere(pos, 0.1f, 16, 16);
		markers.add(m);
		markershader.addObject(m);
	}

	public void addRotationMarker(Vector3f pos) {
		RotationMarker rm = new RotationMarker(pos);
		rotationreferences.add(rm);
		markershader.addObject(rm);
	}
	
	public void addSecondaryRotationMarker(Vector3f pos) {
		SecondaryRotationMarker srm = new SecondaryRotationMarker(pos);
		secondaryrotationreferences.add(srm);
		markershader.addObject(srm);
	}

	public void downLeft(Vector3f pos) {
		if (dragging) {
			System.out.println("DRAG");
			draggedMarker.translateTo(pos);
		}
	}

	public void releaseLeft(Vector3f pos) {
		if (dragging) {
			if (draggedMarkerType == 0) {
				markers.get(dragID).translateTo(pos);

				int bezierid = dragID / 4;
				int pointid = dragID % 4;
				if (bezierid < beziercurves.size()) {
					RenderedBezierCurve3 rb = beziercurves.get(bezierid);
					BezierCurve3 b = rb.bezier;
					rb.delete();
					System.out.println(pos + "; " + startpos);
					if (pointid == 0) {
						rb = new RenderedBezierCurve3(new BezierCurve3(pos, b.getP1(), b.getP2(), b.getP3()));
						rotationreferences.get(bezierid).translate(VecMath.subtraction(pos, startpos));
						if (closed) {
							RenderedBezierCurve3 rbLast = beziercurves.get(beziercurves.size() - 1);
							BezierCurve3 b1 = rbLast.bezier;
							rbLast.delete();
							rbLast = new RenderedBezierCurve3(new BezierCurve3(b1.getP0(), b1.getP1(), b1.getP2(), pos));
							beziercurves.set(beziercurves.size() - 1, rbLast);
						}
					} else if (pointid == 1) {
						rb = new RenderedBezierCurve3(new BezierCurve3(b.getP0(), pos, b.getP2(), b.getP3()));
					} else if (pointid == 3) {
						rb = new RenderedBezierCurve3(new BezierCurve3(b.getP0(), b.getP1(), pos, b.getP3()));
					} else if (pointid == 2) {
						rb = new RenderedBezierCurve3(new BezierCurve3(b.getP0(), b.getP1(), b.getP2(), pos));
						rotationreferences.get(bezierid + 1).translate(VecMath.subtraction(pos, startpos));
						if (beziercurves.size() > bezierid + 1) {
							RenderedBezierCurve3 rbNext = beziercurves.get(bezierid + 1);
							BezierCurve3 b1 = rbNext.bezier;
							rbNext.delete();
							rbNext = new RenderedBezierCurve3(new BezierCurve3(pos, b1.getP1(), b1.getP2(), b1.getP3()));
							beziercurves.set(bezierid + 1, rbNext);
						}
					}
					beziercurves.set(bezierid, rb);
					updatePathMarker();
				} else {
					markers.get(dragID).translateTo(pos);
				}
			} else if(draggedMarkerType == 1) {
				rotationreferences.get(dragID).translateTo(pos);
				if (closed) {
					updateSquad();
					updatePathMarker();
				}
				System.out.println(rotationreferences.size());
			} else if(draggedMarkerType == 2) {
				secondaryrotationreferences.get(dragID).translateTo(pos);
				if (closed) {
					updateSquad();
					updatePathMarker();
				}
				System.out.println(secondaryrotationreferences.size());
			}

			dragging = false;
		} else {
			if (!closed) {
				if (tempreference == -1) {
					tempreference = markers.size();
					addSphereMarker(pos);
				} else {
					int p = markers.size();
					addSphereMarker(pos);
					addBezierCurve(new BezierCurve3(markers.get(temppoint).getTranslation(),
							markers.get(tempreference).getTranslation(), markers.get(p).getTranslation(),
							markers.get(temppoint2).getTranslation()));
					updatePathMarker();

					temppoint = temppoint2;
					temppoint2 = -1;
					tempreference = -1;
					skipPress = true;
				}
			}
		}
	}

	public void addBezierCurve(RenderedBezierCurve3 rbc) {
		beziercurves.add(rbc);
		numCurves++;
		oneOverNum = 1 / (float) numCurves;
		defaultshader.addObject(rbc);
	}

	public void addBezierCurve(BezierCurve3 bez) {
		addBezierCurve(new RenderedBezierCurve3(bez));
	}

	public void closePath() {
		if (temppoint2 != -1) {
			System.out.println("Set second reference first!");
			return;
		}
		if (beziercurves.size() == 0) {
			System.out.println("Add curves first!");
			return;
		}
		if (closed) {
			System.out.println("Already closed.");
			return;
		}

		RenderedBezierCurve3 bezier = null;
		Vector3f closevector = VecMath.subtraction(beziercurves.get(0).bezier.getP0(),
				markers.get(temppoint).getTranslation());
		closevector.scale(0.3f);
		Vector3f pos1 = null;
		Vector3f ref1 = null;
		Vector3f ref2 = VecMath.subtraction(markers.get(0).getTranslation(), closevector);
		Vector3f pos2 = markers.get(0).getTranslation();
		if (tempreference != -1) {
			pos1 = markers.get(temppoint).getTranslation();
			ref1 = markers.get(tempreference).getTranslation();

			markers.add(markers.get(temppoint));
			markers.add(markers.get(tempreference));

			bezier = new RenderedBezierCurve3(new BezierCurve3(pos1, ref1, ref2, pos2));
		} else if (temppoint != -1) {
			pos1 = markers.get(temppoint).getTranslation();
			ref1 = VecMath.addition(markers.get(temppoint).getTranslation(), closevector);

			markers.add(markers.get(temppoint));
			addSphereMarker(ref1);

			bezier = new RenderedBezierCurve3(new BezierCurve3(pos1, ref1, ref2, pos2));
		} else {
			pos1 = markers.get(markers.size() - 1).getTranslation();
			ref1 = VecMath.addition(markers.get(temppoint).getTranslation(), closevector);

			markers.add(markers.get(markers.size() - 1));
			addSphereMarker(ref1);

			bezier = new RenderedBezierCurve3(new BezierCurve3(pos1, ref1, ref2, pos2));
		}

		markers.add(markers.get(0));
		addSphereMarker(ref2);

		addBezierCurve(bezier);

		closed = true;
		updateSquad();
		updatePathMarker();

		temppoint = -1;
		tempreference = -1;
		temppoint2 = -1;
	}

	public void deleteCurve() {

	}

	public void setAnimationTimer(float timer) {
		animationTimer = timer;
		updatePathMarker();
	}

	public void updatePathMarker() {
		Vector3f pos = getPoint(animationTimer);
		if (pos != null) {
			bodypart.translateTo(pos);
		}
		if (closed) {
			Quaternionf rot = getRotation(animationTimer);
			if (rot != null) {
				bodypart.rotateTo(rot);
			}
		}
	}

	public void updateSquad() {
		List<Quaternionf> rotations = new ArrayList<Quaternionf>();
		squadcurves.clear();
		System.out.println("AAA " + rotationreferences.size() + "; " + markers.size());
		for (int i = 0; i < rotationreferences.size(); i++) {
			rotations.add(getRotation(rotationreferences.get(i).getTranslation(), markers.get(i * 4).getTranslation()));
		}
		int rs = rotations.size();
		for (int i = 0; i < rs; i++) {
			// Complexf r0 = null;
			Quaternionf r1 = rotations.get(i);
			Quaternionf r2 = rotations.get((i + 1) % rs);
			// Complexf r3 = null;
			// if(i == 0) {
			// r0 = rotations.get(rs-1);
			// r3 = rotations.get(2%rs);
			// }
			// else {
			// r0 = rotations.get(i-1);
			// r3 = rotations.get((i+2)%rs);
			// }
			System.out.println(i + "; " + r1 + "; " + r2);
			squadcurves.add(new SquadCurve3(r1, r1, r2, r2));
		}
	}

	// TODO: private
	public Quaternionf getRotation(Vector3f rotationmarker, Vector3f pathmarker) {
		Vector3f direction = VecMath.subtraction(rotationmarker, pathmarker);
		direction.normalize();
		Quaternionf result = new Quaternionf();
		/*float projY = VecMath.dotproduct(direction, VectorConstants.AXIS_Y);
		if(projY < 0.9) {
			Vector3f a = VecMath.crossproduct(direction, VectorConstants.AXIS_Y);
			result.q0 = a.x;
			result.q1 = a.y;
			result.q2 = a.z;
			result.q3 = projY;
		}
		else {*/
			// TODO
			result.q0 = 1 + VecMath.dotproduct(direction, VectorConstants.AXIS_X);
			Vector3f a = VecMath.crossproduct(VectorConstants.AXIS_X, direction);
			result.q1 = a.x;
			result.q2 = a.y;
			result.q3 = a.z;/*
		}*/
		result.normalize(); // TODO: needed?
		System.out.println("GetRot " + rotationmarker + "; " + pathmarker + "; " + direction + "; " + result);
		return result;
	}

	private class RotationMarker extends ShapedObject3 {
		public RotationMarker(Vector3f pos) {
			super(pos);
			setRenderMode(GLConstants.TRIANGLES);
			addVertex(new Vector3f(0, 0, -1));
			addVertex(new Vector3f(-1, 0, 1));
			addVertex(new Vector3f(1, 0, 1));
			addVertex(new Vector3f(0, 1, 0));
			addIndices(0, 2, 1, 0, 1, 3, 1, 2, 3, 2, 0, 3);
			scale(0.2f);
			prerender();
		}
	}
	
	private class SecondaryRotationMarker extends ShapedObject3 {
		public SecondaryRotationMarker(Vector3f pos) {
			super(pos);
			setRenderMode(GLConstants.TRIANGLES);
			addVertex(new Vector3f(0, 0, -1));
			addVertex(new Vector3f(0, 0, 1));
			addVertex(new Vector3f(0, 1, 0));
			addIndices(0, 1, 2, 0, 2, 1);
			scale(0.2f);
			prerender();
		}
	}
}