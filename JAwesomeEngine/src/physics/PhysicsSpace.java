package physics;

import integration.IntegrationSolver;

import java.util.ArrayList;
import java.util.List;

import manifold.ManifoldManager;
import narrowphase.Narrowphase;
import objects.CollisionShape;
import objects.CompoundObject3;
import objects.DataGameObject;
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
			Broadphase<Vector3f, RigidBody<Vector3f, ?, ?, ?>> broadphase,
			Narrowphase<Vector3f> narrowphase,
			CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection,
			ManifoldManager<Vector3f> manifoldmanager) {
		super(integrationsolver, broadphase, narrowphase, collisionresolution,
				positionalcorrection, manifoldmanager);
		addedobjects = new ArrayList<GameObject>();
	}

	public void addRigidBody(DataGameObject obj, float mass) {

	}

	public void addRigidBody(DataGameObject obj,
			RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> body) {
		obj.setRotation(body.getRotation());
		obj.setTranslation(body.getTranslation());
		addRigidBody(body);
		addedobjects.add(obj);
	}

	public void addCompoundObject(CompoundObject3 compoundobject,
			DataGameObject... obj) {
		for (int i = 0; i < obj.length; i++) {
			DataGameObject dgo = obj[i];
			CollisionShape<Vector3f, Quaternionf, ?> cs = compoundobject
					.getCollisionShapes().get(i);
			dgo.setRotation(cs.getRotation());
			dgo.setTranslation(cs.getTranslation());
			dgo.setRotationCenter(cs.getRotationCenter());
			addedobjects.add(dgo);
		}
		addCompoundObject(compoundobject);
	}

	@Override
	public void update(int delta) {
		super.update(delta);
		for (GameObject o : addedobjects)
			o.updateBuffer();
	}
}
