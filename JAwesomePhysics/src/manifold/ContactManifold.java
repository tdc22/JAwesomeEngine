package manifold;

import vector.Vector;

public class ContactManifold<L extends Vector> {
	float penetrationdepth;
	L collisionnormal;
	L contactA, contactB;
	L relativecontactA, relativecontactB;
	L localcontactA, localcontactB;
	L tangentA, tangentB;

	public ContactManifold() {
	}

	public ContactManifold(ContactManifold<L> manifold) {
		this.penetrationdepth = manifold.getPenetrationDepth();
		this.collisionnormal = manifold.getCollisionNormal();
		this.contactA = manifold.getContactPointA();
		this.contactB = manifold.getContactPointB();
		this.relativecontactA = manifold.getRelativeContactPointA();
		this.relativecontactB = manifold.getRelativeContactPointB();
		this.localcontactA = manifold.getLocalContactPointA();
		this.localcontactB = manifold.getLocalContactPointB();
		this.tangentA = manifold.getContactTangentA();
		this.tangentB = manifold.getContactTangentB();
	}

	public ContactManifold(float penetrationdepth, L collisionnormal, L contactA, L contactB, L relativecontactA,
			L relativecontactB, L localcontactA, L localcontactB, L tangentA, L tangentB) {
		this.penetrationdepth = penetrationdepth;
		this.collisionnormal = collisionnormal;
		this.contactA = contactA;
		this.contactB = contactB;
		this.relativecontactA = relativecontactA;
		this.relativecontactB = relativecontactB;
		this.localcontactA = localcontactA;
		this.localcontactB = localcontactB;
		this.tangentA = tangentA;
		this.tangentB = tangentB;
	}

	public L getCollisionNormal() {
		return collisionnormal;
	}

	public L getContactPointA() {
		return contactA;
	}

	public L getContactPointB() {
		return contactB;
	}

	public L getContactTangentA() {
		return tangentA;
	}

	public L getContactTangentB() {
		return tangentB;
	}

	public L getLocalContactPointA() {
		return localcontactA;
	}

	public L getLocalContactPointB() {
		return localcontactB;
	}

	public float getPenetrationDepth() {
		return penetrationdepth;
	}

	public L getRelativeContactPointA() {
		return relativecontactA;
	}

	public L getRelativeContactPointB() {
		return relativecontactB;
	}
}