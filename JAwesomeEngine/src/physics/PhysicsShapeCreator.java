package physics;

import java.util.ArrayList;
import java.util.List;

import collisionshape.BoxShape;
import collisionshape.CapsuleShape;
import collisionshape.ConvexShape;
import collisionshape.CylinderShape;
import collisionshape2d.ConvexShape2;
import collisionshape2d.EllipseShape;
import collisionshape2d.QuadShape;
import convexhull.Quickhull;
import convexhull.Quickhull2;
import objects.ShapedObject2;
import objects.ShapedObject3;
import shapedata.BoxStructure;
import shapedata.CapsuleStructure;
import shapedata.CylinderStructure;
import shapedata2d.EllipseStructure;
import shapedata2d.QuadStructure;
import vector.Vector2f;

public class PhysicsShapeCreator {
	public static BoxShape create(BoxStructure box) {
		return new BoxShape(box.getTranslation(), box.getHalfSize());
	}

	public static CapsuleShape create(CapsuleStructure capsule) {
		return new CapsuleShape(capsule.getTranslation(), capsule.getRadius(), capsule.getHalfHeight());
	}

	public static CylinderShape create(CylinderStructure cylinder) {
		return new CylinderShape(cylinder.getTranslation(), cylinder.getRadius(), cylinder.getHalfHeight());
	}

	public static EllipseShape create(EllipseStructure ellipse) {
		return new EllipseShape(ellipse.getTranslation(), ellipse.getRadius(), ellipse.getHalfHeight());
	}

	public static QuadShape create(QuadStructure quad) {
		return new QuadShape(quad.getTranslation(), quad.getHalfSize());
	}

	public static ConvexShape createHull(ShapedObject3 shapedobject) {
		ConvexShape hull = Quickhull.computeConvexHull(shapedobject.getVertices());
		hull.translateTo(shapedobject.getTranslation());
		return hull;
	}

	public static ConvexShape2 createHull(ShapedObject2 shapedobject) {
		List<Vector2f> vertices = new ArrayList<Vector2f>();
		for (Vector2f v : shapedobject.getVertices())
			vertices.add(new Vector2f(v.x, v.y));
		ConvexShape2 hull = Quickhull2.computeConvexHull(vertices);
		hull.translateTo(shapedobject.getTranslation());
		return hull;
	}
}