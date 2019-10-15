package physics;

import java.util.ArrayList;
import java.util.List;

import broadphase.Broadphase;
import integration.IntegrationSolver;
import manifold.ManifoldManager;
import narrowphase.Narrowphase;
import narrowphase.RaycastNarrowphase;
import objects.CollisionShape;
import objects.CompoundObject3;
import objects.GameObject;
import objects.GhostObject;
import objects.RigidBody;
import positionalcorrection.PositionalCorrection;
import quaternion.Quaternionf;
import resolution.CollisionResolution;
import space.Space3;
import vector.Vector3f;

public class PhysicsSpace extends Space3 {
	List<GameObject<Vector3f, Quaternionf>> addedobjects;

	public PhysicsSpace(IntegrationSolver integrationsolver,
			Broadphase<Vector3f, RigidBody<Vector3f, ?, Quaternionf, ?>> broadphase, Narrowphase<Vector3f> narrowphase,
			RaycastNarrowphase<Vector3f> raycastnarrowphase, CollisionResolution collisionresolution,
			PositionalCorrection positionalcorrection, ManifoldManager<Vector3f, Quaternionf> manifoldmanager) {
		super(integrationsolver, broadphase, narrowphase, raycastnarrowphase, collisionresolution, positionalcorrection,
				manifoldmanager);
		addedobjects = new ArrayList<GameObject<Vector3f, Quaternionf>>();
	}

	public void addRigidBody(GameObject<Vector3f, Quaternionf> obj, float mass) {

	}

	public void addRigidBody(GameObject<Vector3f, Quaternionf> obj,
			RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> body) {
		body.setRotation(obj.getRotation());
		body.setTranslation(obj.getTranslation());
		body.setRotationCenter(obj.getRotationCenter());
		addRigidBody(body);
		addedobjects.add(obj);
	}

	public void removeRigidBody(GameObject<Vector3f, Quaternionf> obj,
			RigidBody<Vector3f, Vector3f, Quaternionf, Quaternionf> body) {
		addedobjects.remove(obj);
		removeRigidBody(body);
	}

	public void addGhostObject(GameObject<Vector3f, Quaternionf> obj,
			GhostObject<Vector3f, Vector3f, Quaternionf, Quaternionf> body) {
		body.setRotation(obj.getRotation());
		body.setTranslation(obj.getTranslation());
		body.setRotationCenter(obj.getRotationCenter());
		addGhostObject(body);
	}

	public void removeGhostObject(GameObject<Vector3f, Quaternionf> obj,
			GhostObject<Vector3f, Vector3f, Quaternionf, Quaternionf> body) {
		removeGhostObject(body);
	}

	public void addCompoundObject(CompoundObject3 compoundobject, GameObject<Vector3f, Quaternionf>[] obj) {
		for (int i = 0; i < obj.length; i++) {
			GameObject<Vector3f, Quaternionf> dgo = obj[i];
			CollisionShape<Vector3f, Quaternionf, ?> cs = compoundobject.getCollisionShapes().get(i);
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
		for (GameObject<Vector3f, Quaternionf> o : addedobjects)
			o.updateBuffer();
	}
}
