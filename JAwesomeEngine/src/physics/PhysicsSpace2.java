package physics;

import integration.IntegrationSolver;

import java.util.ArrayList;
import java.util.List;

import manifold.ManifoldManager;
import matrix.Matrix1f;
import narrowphase.Narrowphase;
import objects.DataObject;
import objects.GameObject;
import objects.RigidBody;
import positionalcorrection.PositionalCorrection;
import quaternion.Complexf;
import resolution.CollisionResolution;
import space.Space2;
import vector.Vector1f;
import vector.Vector2f;
import broadphase.Broadphase;

public class PhysicsSpace2 extends Space2 {
	List<GameObject> addedobjects;

	public PhysicsSpace2(IntegrationSolver integrationsolver,
			Broadphase<Vector2f> broadphase, Narrowphase<Vector2f> narrowphase,
			CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection,
			ManifoldManager<Vector2f> manifoldmanager) {
		super(integrationsolver, broadphase, narrowphase, collisionresolution,
				positionalcorrection, manifoldmanager);
		addedobjects = new ArrayList<GameObject>();
	}

	public void addRigidBody(DataObject obj, float mass) {

	}

	public void addRigidBody(DataObject obj,
			RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> body) {
		body.setRotation(obj.getRotation());
		body.setTranslation(obj.getTranslation());
		addRigidBody(body);
		addedobjects.add(obj);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		for (GameObject o : addedobjects)
			o.updateBuffer();
	}
}
