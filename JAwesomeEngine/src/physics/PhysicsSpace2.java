package physics;

import integration.IntegrationSolver;

import java.util.ArrayList;
import java.util.List;

import manifold.ManifoldManager;
import matrix.Matrix1f;
import narrowphase.Narrowphase;
import objects.CollisionShape;
import objects.CompoundObject2;
import objects.DataGameObject;
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
			Broadphase<Vector2f, RigidBody<Vector2f, ?, ?, ?>> broadphase,
			Narrowphase<Vector2f> narrowphase,
			CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection,
			ManifoldManager<Vector2f> manifoldmanager) {
		super(integrationsolver, broadphase, narrowphase, collisionresolution,
				positionalcorrection, manifoldmanager);
		addedobjects = new ArrayList<GameObject>();
	}

	public void addRigidBody(DataGameObject obj, float mass) {

	}

	public void addRigidBody(DataGameObject obj,
			RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> body) {
		obj.setRotation(body.getRotation());
		obj.setTranslation(body.getTranslation());
		addRigidBody(body);
		addedobjects.add(obj);
	}

	public void removeRigidBody(DataGameObject obj,
			RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> body) {
		addedobjects.remove(obj);
		removeRigidBody(body);
	}

	public void addCompoundObject(CompoundObject2 compoundobject,
			DataGameObject... obj) {
		for (int i = 0; i < obj.length; i++) {
			DataGameObject dgo = obj[i];
			CollisionShape<Vector2f, Complexf, ?> cs = compoundobject
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
