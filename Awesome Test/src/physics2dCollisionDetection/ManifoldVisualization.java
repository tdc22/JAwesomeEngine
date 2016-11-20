package physics2dCollisionDetection;

import java.awt.Color;

import manifold.CollisionManifold;
import math.VecMath;
import objects.ShapedObject2;
import utils.GLConstants;
import vector.Vector2f;

public class ManifoldVisualization extends ShapedObject2 {
	ManifoldVisualization(CollisionManifold<Vector2f> cm) {
		setRenderMode(GLConstants.LINES);
		Vector2f ca = VecMath.subtraction(cm.getContactPointA(), cm
				.getObjects().getFirst().getTranslation());
		Vector2f cb = VecMath.subtraction(cm.getContactPointB(), cm
				.getObjects().getSecond().getTranslation());
		Vector2f center1 = VecMath.addition(cm.getObjects().getFirst()
				.getTranslation(), ca);
		Vector2f center2 = VecMath.addition(cm.getObjects().getSecond()
				.getTranslation(), cb);
		Vector2f normal1 = VecMath.subtraction(center1, VecMath.scale(
				cm.getCollisionNormal(), cm.getPenetrationDepth()));
		Vector2f normal2 = VecMath.addition(center2, VecMath.scale(
				cm.getCollisionNormal(), cm.getPenetrationDepth()));
		addVertex(center1, Color.GREEN, new Vector2f());
		addVertex(normal1, Color.GREEN, new Vector2f());
		addVertex(center2, Color.GREEN, new Vector2f());
		addVertex(normal2, Color.GREEN, new Vector2f());

		Vector2f tangent1 = VecMath.addition(center1, VecMath.scale(
				cm.getContactTangentA(), cm.getPenetrationDepth()));
		Vector2f tangent2 = VecMath.addition(center2, VecMath.scale(
				cm.getContactTangentB(), cm.getPenetrationDepth()));
		addVertex(center1, Color.YELLOW, new Vector2f());
		addVertex(tangent1, Color.YELLOW, new Vector2f());
		addVertex(center2, Color.YELLOW, new Vector2f());
		addVertex(tangent2, Color.YELLOW, new Vector2f());

		addIndices(0, 1, 2, 3);
		addIndices(4, 5, 6, 7);
		prerender();
	}
}