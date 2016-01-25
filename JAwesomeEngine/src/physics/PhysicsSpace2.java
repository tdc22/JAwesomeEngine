package physics;

import integration.IntegrationSolver;

import java.util.ArrayList;
import java.util.List;

import manifold.ManifoldManager;
import matrix.Matrix1f;
import narrowphase.Narrowphase;
import objects.CollisionShape;
import objects.CompoundObject2;
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
	List<GameObject<Vector2f, Complexf>> addedobjects;

	public PhysicsSpace2(IntegrationSolver integrationsolver,
			Broadphase<Vector2f, RigidBody<Vector2f, ?, ?, ?>> broadphase, Narrowphase<Vector2f> narrowphase,
			CollisionResolution collisionresolution, PositionalCorrection positionalcorrection,
			ManifoldManager<Vector2f> manifoldmanager) {
		super(integrationsolver, broadphase, narrowphase, collisionresolution, positionalcorrection, manifoldmanager);
		addedobjects = new ArrayList<GameObject<Vector2f, Complexf>>();
	}

	public void addRigidBody(GameObject<Vector2f, Complexf> obj, float mass) {

	}

	public void addRigidBody(GameObject<Vector2f, Complexf> obj,
			RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> body) {
		body.setRotation(obj.getRotation());
		body.setTranslation(obj.getTranslation());
		addRigidBody(body);
		addedobjects.add(obj);
	}

	public void removeRigidBody(GameObject<Vector2f, Complexf> obj,
			RigidBody<Vector2f, Vector1f, Complexf, Matrix1f> body) {
		addedobjects.remove(obj);
		removeRigidBody(body);
	}

	public void addCompoundObject(CompoundObject2 compoundobject, GameObject<Vector2f, Complexf>[] obj) {
		for (int i = 0; i < obj.length; i++) {
			GameObject<Vector2f, Complexf> dgo = obj[i];
			CollisionShape<Vector2f, Complexf, ?> cs = compoundobject.getCollisionShapes().get(i);
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
		for (GameObject<Vector2f, Complexf> o : addedobjects)
			o.updateBuffer();
	}
}
