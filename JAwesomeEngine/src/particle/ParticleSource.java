package particle;

import objects.GameObject;
import objects.Updateable;
import vector.Vector3f;

public class ParticleSource extends GameObject implements Updateable {
	protected Vector3f spawndistance, initialVelocity, gravity;
	protected float spawningRate, lifetime;
	private boolean randomposition = false, randomrate = false, radomlife = false;

	public ParticleSource(float x, float y, float z, float spawnrate, float lifetime) {
		super();
		translate(x, y, z);

	}

	public ParticleSource(Vector3f position, float spawnrate, float lifetime) {
		super();
		translate(position);

	}

	private void init(Vector3f spawnrate, Vector3f initialVelocity, Vector3f gravity, float spawnratetime,
			float lifetime) {
		this.spawningRate = spawnratetime;
		this.lifetime = lifetime;
	}

	@Override
	public void update(int delta) {

	}
}