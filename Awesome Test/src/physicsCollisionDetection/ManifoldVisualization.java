package physicsCollisionDetection;

import java.awt.Color;

import manifold.CollisionManifold;
import math.VecMath;
import objects.ShapedObject3;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;

public class ManifoldVisualization extends ShapedObject3 {
	ManifoldVisualization(CollisionManifold<Vector3f> cm) {
		setRenderMode(GLConstants.LINES);
		Vector3f ca = VecMath.subtraction(cm.getContactPointA(), cm.getObjects().getFirst().getTranslation());
		Vector3f cb = VecMath.subtraction(cm.getContactPointB(), cm.getObjects().getSecond().getTranslation());
		Vector3f center1 = VecMath.addition(cm.getObjects().getFirst().getTranslation(), ca);
		Vector3f center2 = VecMath.addition(cm.getObjects().getSecond().getTranslation(), cb);
		Vector3f normal1 = VecMath.subtraction(center1,
				VecMath.scale(cm.getCollisionNormal(), cm.getPenetrationDepth()));
		Vector3f normal2 = VecMath.addition(center2, VecMath.scale(cm.getCollisionNormal(), cm.getPenetrationDepth()));
		addVertex(center1, Color.GREEN, new Vector2f(), new Vector3f());
		addVertex(normal1, Color.GREEN, new Vector2f(), new Vector3f());
		addVertex(center2, Color.GREEN, new Vector2f(), new Vector3f());
		addVertex(normal2, Color.GREEN, new Vector2f(), new Vector3f());

		Vector3f tangent1 = VecMath.addition(center1, VecMath.scale(cm.getContactTangentA(), cm.getPenetrationDepth()));
		Vector3f tangent2 = VecMath.addition(center2, VecMath.scale(cm.getContactTangentB(), cm.getPenetrationDepth()));
		addVertex(center1, Color.YELLOW, new Vector2f(), new Vector3f());
		addVertex(tangent1, Color.YELLOW, new Vector2f(), new Vector3f());
		addVertex(center2, Color.YELLOW, new Vector2f(), new Vector3f());
		addVertex(tangent2, Color.YELLOW, new Vector2f(), new Vector3f());

		addIndices(0, 1, 2, 3);
		addIndices(4, 5, 6, 7);
		prerender();
	}
}