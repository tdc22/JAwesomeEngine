package physics;

import java.util.ArrayList;
import java.util.List;

import objects.ShapedObject;
import objects.ShapedObject2;
import shapedata.BoxStructure;
import shapedata.CapsuleStructure;
import shapedata.CylinderStructure;
import shapedata2d.EllipseStructure;
import shapedata2d.QuadStructure;
import vector.Vector2f;
import vector.Vector3f;
import collisionshape.BoxShape;
import collisionshape.CapsuleShape;
import collisionshape.ConvexShape;
import collisionshape.CylinderShape;
import collisionshape2d.ConvexShape2;
import collisionshape2d.EllipseShape;
import collisionshape2d.QuadShape;
import convexhull.Quickhull;
import convexhull.Quickhull2;

public class PhysicsShapeCreator {
	public static BoxShape create(BoxStructure box) {
		return new BoxShape(0, 0, 0, box.getHalfSize());
	}

	public static CapsuleShape create(CapsuleStructure capsule) {
		return new CapsuleShape(0, 0, 0, capsule.getRadius(),
				capsule.getHeight());
	}

	public static CylinderShape create(CylinderStructure cylinder) {
		return new CylinderShape(0, 0, 0, cylinder.getRadius(),
				cylinder.getHalfHeight());
	}

	public static EllipseShape create(EllipseStructure ellipse) {
		return new EllipseShape(0, 0, ellipse.getRadius(), ellipse.getHeight());
	}

	public static QuadShape create(QuadStructure quad) {
		return new QuadShape(0, 0, quad.getHalfSize());
	}

	public static ConvexShape createHull(ShapedObject shapedobject) {
		return Quickhull.computeConvexHull(shapedobject.getVertices());
	}

	public static ConvexShape2 createHull(ShapedObject2 shapedobject) {
		List<Vector2f> vertices = new ArrayList<Vector2f>();
		for (Vector3f v : shapedobject.getVertices())
			vertices.add(new Vector2f(v.x, v.y));
		return Quickhull2.computeConvexHull(vertices);
	}
}