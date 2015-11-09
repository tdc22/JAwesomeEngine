package animationEditor;

import java.util.ArrayList;
import java.util.List;

import math.VecMath;
import objects.ShapedObject2;
import quaternion.Complexf;
import shader.Shader;
import shape2d.Circle;
import shape2d.Quad;
import utils.GLConstants;
import vector.Vector2f;
import curves.BezierCurve2;
import curves.SquadCurve2;

public class AnimationPath {
	List<ShapedObject2> markers;
	List<ShapedObject2> rotationreferences;
	List<RenderedBezierCurve> beziercurves;
	List<SquadCurve2> squadcurves;
	Quad pathmarker;

	Shader markershader, defaultshader;

	int numCurves = 0;
	float oneOverNum = 0;

	boolean skipPress = false;
	boolean dragging = false;
	int temppoint = -1, temppoint2 = -1, tempreference = -1;
	int dragID;
	boolean isMarkerDragged = true;
	final float maxDraggingDistanceSqr = 20f;
	float animationTimer = 0;
	boolean closed = false;
	Vector2f clickpos;

	public AnimationPath(Shader defaultshader, Shader markershader, Shader textureshader, Vector2f markersize) {
		this.defaultshader = defaultshader;
		this.markershader = markershader;
		markers = new ArrayList<ShapedObject2>();
		rotationreferences = new ArrayList<ShapedObject2>();
		beziercurves = new ArrayList<RenderedBezierCurve>();
		squadcurves = new ArrayList<SquadCurve2>();
		pathmarker = new Quad(-1000, -1000, markersize.x, markersize.y);
		pathmarker.setRenderHints(false, true, false);
		textureshader.addObject(pathmarker);
	}

	public Vector2f getPoint(float t) {
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

	public Complexf getRotation(float t) {
		int num = (int) (numCurves * t);
		t %= oneOverNum;
		t *= numCurves;
		if (num == numCurves) {
			num--;
			t = 1;
		}
		return squadcurves.get(num).getRotation(t);
	}

	public void clickLeft(Vector2f pos) {
		clickpos = pos;
		float mindistance = Float.MAX_VALUE;
		double dist;
		for (int i = 0; i < markers.size(); i++) {
			if ((dist = VecMath.subtraction(pos, markers.get(i).getTranslation()).lengthSquared()) < mindistance) {
				dragID = i;
				mindistance = (float) dist;
				isMarkerDragged = true;
			}
		}
		for (int i = 0; i < rotationreferences.size(); i++) {
			if ((dist = VecMath.subtraction(pos, rotationreferences.get(i).getTranslation())
					.lengthSquared()) < mindistance) {
				dragID = i;
				mindistance = (float) dist;
				isMarkerDragged = false;
			}
		}
		if (mindistance <= maxDraggingDistanceSqr) {
			dragging = true;
		} else {
			if (!closed) {
				if (!skipPress) {
					if (temppoint == -1) {
						temppoint = markers.size();
					} else {
						temppoint2 = markers.size();
					}
					addQuadMarker(pos);

					pos.x += 10;
					addRotationMarker(pos);
				} else {
					markers.add(markers.get(markers.size() - 2));
					skipPress = false;
				}
			}
		}
	}

	public void addQuadMarker(Vector2f pos) {
		Quad m = new Quad(pos, 1, 1);
		markers.add(m);
		markershader.addObject(m);
	}

	public void addCircleMarker(Vector2f pos) {
		Circle m = new Circle(pos, 1, 10);
		markers.add(m);
		markershader.addObject(m);
	}

	public void addRotationMarker(Vector2f pos) {
		RotationMarker rm = new RotationMarker(pos);
		rotationreferences.add(rm);
		markershader.addObject(rm);
	}

	public void downLeft(Vector2f pos) {
		if (dragging) {
			if (isMarkerDragged) {
				markers.get(dragID).translateTo(pos);
			} else {
				rotationreferences.get(dragID).translateTo(pos);
			}
		}
	}

	public void releaseLeft(Vector2f pos) {
		if (dragging) {
			if (isMarkerDragged) {
				markers.get(dragID).translateTo(pos);

				int bezierid = dragID / 4;
				int pointid = dragID % 4;
				if (bezierid < beziercurves.size()) {
					RenderedBezierCurve rb = beziercurves.get(bezierid);
					BezierCurve2 b = rb.bezier;
					rb.delete();
					if (pointid == 0) {
						rb = new RenderedBezierCurve(new BezierCurve2(pos, b.getP1(), b.getP2(), b.getP3()));
						rotationreferences.get(bezierid).translate(VecMath.subtraction(pos, clickpos));
						if (closed) {
							RenderedBezierCurve rbLast = beziercurves.get(beziercurves.size() - 1);
							BezierCurve2 b1 = rbLast.bezier;
							rbLast.delete();
							rbLast = new RenderedBezierCurve(new BezierCurve2(b1.getP0(), b1.getP1(), b1.getP2(), pos));
							beziercurves.set(beziercurves.size() - 1, rbLast);
						}
					} else if (pointid == 1) {
						rb = new RenderedBezierCurve(new BezierCurve2(b.getP0(), pos, b.getP2(), b.getP3()));
					} else if (pointid == 3) {
						rb = new RenderedBezierCurve(new BezierCurve2(b.getP0(), b.getP1(), pos, b.getP3()));
					} else if (pointid == 2) {
						rb = new RenderedBezierCurve(new BezierCurve2(b.getP0(), b.getP1(), b.getP2(), pos));
						rotationreferences.get(bezierid + 1).translate(VecMath.subtraction(pos, clickpos));
						if (beziercurves.size() > bezierid + 1) {
							RenderedBezierCurve rbNext = beziercurves.get(bezierid + 1);
							BezierCurve2 b1 = rbNext.bezier;
							rbNext.delete();
							rbNext = new RenderedBezierCurve(new BezierCurve2(pos, b1.getP1(), b1.getP2(), b1.getP3()));
							beziercurves.set(bezierid + 1, rbNext);
						}
					}
					beziercurves.set(bezierid, rb);
					updatePathMarker();
				} else {
					markers.get(dragID).translateTo(pos);
				}
			} else {
				rotationreferences.get(dragID).translateTo(pos);
				if (closed) {
					updateSquad();
					updatePathMarker();
				}
				System.out.println(rotationreferences.size());
			}

			dragging = false;
		} else {
			if (!closed) {
				if (tempreference == -1) {
					tempreference = markers.size();
					addCircleMarker(pos);
				} else {
					int p = markers.size();
					addCircleMarker(pos);
					addBezierCurve(new BezierCurve2(markers.get(temppoint).getTranslation(),
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

	public void addBezierCurve(RenderedBezierCurve rbc) {
		beziercurves.add(rbc);
		numCurves++;
		oneOverNum = 1 / (float) numCurves;
		defaultshader.addObject(rbc);
	}

	public void addBezierCurve(BezierCurve2 bez) {
		addBezierCurve(new RenderedBezierCurve(bez));
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

		RenderedBezierCurve bezier = null;
		Vector2f closevector = VecMath.subtraction(beziercurves.get(0).bezier.getP0(),
				markers.get(temppoint).getTranslation());
		closevector.scale(0.3f);
		Vector2f pos1 = null;
		Vector2f ref1 = null;
		Vector2f ref2 = VecMath.subtraction(markers.get(0).getTranslation(), closevector);
		Vector2f pos2 = markers.get(0).getTranslation();
		if (tempreference != -1) {
			pos1 = markers.get(temppoint).getTranslation();
			ref1 = markers.get(tempreference).getTranslation();

			markers.add(markers.get(temppoint));
			markers.add(markers.get(tempreference));

			bezier = new RenderedBezierCurve(new BezierCurve2(pos1, ref1, ref2, pos2));
		} else if (temppoint != -1) {
			pos1 = markers.get(temppoint).getTranslation();
			ref1 = VecMath.addition(markers.get(temppoint).getTranslation(), closevector);

			markers.add(markers.get(temppoint));
			addCircleMarker(ref1);

			bezier = new RenderedBezierCurve(new BezierCurve2(pos1, ref1, ref2, pos2));
		} else {
			pos1 = markers.get(markers.size() - 1).getTranslation();
			ref1 = VecMath.addition(markers.get(temppoint).getTranslation(), closevector);

			markers.add(markers.get(markers.size() - 1));
			addCircleMarker(ref1);

			bezier = new RenderedBezierCurve(new BezierCurve2(pos1, ref1, ref2, pos2));
		}

		markers.add(markers.get(0));
		addCircleMarker(ref2);

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
		Vector2f pos = getPoint(animationTimer);
		if (pos != null) {
			pathmarker.translateTo(pos);
		}
		if (closed) {
			Complexf rot = getRotation(animationTimer);
			if (rot != null) {
				pathmarker.rotateTo(rot);
			}
		}
	}

	public void updateSquad() {
		List<Complexf> rotations = new ArrayList<Complexf>();
		squadcurves.clear();
		System.out.println("AAA " + rotationreferences.size() + "; " + markers.size());
		for (int i = 0; i < rotationreferences.size(); i++) {
			rotations.add(getRotation(rotationreferences.get(i).getTranslation(), markers.get(i * 4).getTranslation()));
		}
		int rs = rotations.size();
		for (int i = 0; i < rs; i++) {
			// Complexf r0 = null;
			Complexf r1 = rotations.get(i);
			Complexf r2 = rotations.get((i + 1) % rs);
			// Complexf r3 = null;
			// if(i == 0) {
			// r0 = rotations.get(rs-1);
			// r3 = rotations.get(2%rs);
			// }
			// else {
			// r0 = rotations.get(i-1);
			// r3 = rotations.get((i+2)%rs);
			// }
			squadcurves.add(new SquadCurve2(r1, r1, r2, r2));
		}
	}

	private Complexf getRotation(Vector2f rotationmarker, Vector2f pathmarker) {
		Vector2f direction = VecMath.subtraction(rotationmarker, pathmarker);
		direction.normalize();
		Complexf result = new Complexf();
		result.rotate(Math.toDegrees(Math.atan2(direction.y, direction.x)));
		return result;
	}

	private class RotationMarker extends ShapedObject2 {
		public RotationMarker(Vector2f pos) {
			super(pos);
			setRenderMode(GLConstants.TRIANGLES);
			addVertex(new Vector2f(0, -1));
			addVertex(new Vector2f(-1, 1));
			addVertex(new Vector2f(1, 1));
			addIndices(0, 1, 2);
			prerender();
		}
	}
}