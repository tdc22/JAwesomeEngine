package particle;

import vector.Vector1f;
import vector.Vector2f;

public abstract class ParticleSource2 extends ParticleSource<Vector2f, Vector1f> {

	public ParticleSource2(Vector2f center, Vector2f spawnAreaHalfSize, Vector1f minAngle, Vector1f maxAngle,
			float minVelocity, float maxVelocity, float minSize, float maxSize, int minLifeTime, int maxLifeTime,
			float spawnRate) {
		super(center, spawnAreaHalfSize, minAngle, maxAngle, minVelocity, maxVelocity, minSize, maxSize, minLifeTime,
				maxLifeTime, spawnRate);
	}

	public void setParticleAngle(Vector1f minAngle, Vector1f maxAngle) {
		this.minAngle = new Vector1f(Math.toRadians(minAngle.x));
		diffAngle = new Vector1f(Math.toRadians(maxAngle.x) - minAngle.x);
	}

	public void update(int delta) {
		lastparticle += delta;
		for (; lastparticle > 0; lastparticle -= spawnRate) {
			float angle = minAngle.x + (float) Math.random() * diffAngle.x;
			Vector2f velocity = new Vector2f(Math.sin(angle), Math.cos(angle));
			velocity.scale(minVelocity + (float) Math.random() * diffVelocity);
			addParticle(
					new Vector2f(center.x + Math.random() * spawnAreaHalfSize.x,
							center.y + Math.random() * spawnAreaHalfSize.y),
					velocity, minSize + (float) Math.random() * diffSize,
					minLifeTime + (int) (Math.random() * diffLifeTime));
		}
		updateParticles(delta);
	}

}