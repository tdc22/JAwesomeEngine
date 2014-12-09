package physics;

import integration.IntegrationSolver;

import java.util.ArrayList;
import java.util.List;

import manifold.ManifoldManager;
import narrowphase.Narrowphase;
import objects.DataObject;
import objects.GameObject;
import objects.RigidBody;
import positionalcorrection.PositionalCorrection;
import quaternion.Quaternionf;
import resolution.CollisionResolution;
import space.Space3;
import vector.Vector3f;
import broadphase.Broadphase;

public class PhysicsSpace extends Space3 {
	List<GameObject> addedobjects;

	public PhysicsSpace(IntegrationSolver integrationsolver,
			Broadphase<Vector3f> broadphase, Narrowphase<Vector3f> narrowphase,
			CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection,
			ManifoldManager<Vector3f> manifoldmanager) {
		super(integrationsolver, broadphase, narrowphase, collisionresolution,
				positionalcorrection, manifoldmanager);
		addedobjects = new ArrayList<GameObject>();
	}

	public void addRigidBody(DataObject obj, float mass) {

	}

	public void addRigidBody(DataObject obj,
			RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> body) {
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
