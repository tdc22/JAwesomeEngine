package physics;

import shapedata.BoxStructure;
import shapedata.CapsuleStructure;
import shapedata.CylinderStructure;
import shapedata2d.EllipseStructure;
import shapedata2d.QuadStructure;
import collisionshape.BoxShape;
import collisionshape.CapsuleShape;
import collisionshape.CylinderShape;
import collisionshape2d.EllipseShape;
import collisionshape2d.QuadShape;

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
}