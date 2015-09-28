package particle;

import math.VecMath;
import vector.Vector2f;

public abstract class ParticleSource2 extends ParticleSource<Vector2f> {

	public ParticleSource2(Vector2f center, Vector2f spawnAreaHalfSize, Vector2f minVelocity, Vector2f maxVelocity,
			float minSize, float maxSize, int minLifeTime, int maxLifeTime, int minSpawnedParticles,
			int maxSpawnedParticles) {
		super(center, spawnAreaHalfSize, minVelocity, maxVelocity, minSize, maxSize, minLifeTime, maxLifeTime,
				minSpawnedParticles, maxSpawnedParticles);
	}

	public void setParticleVelocity(Vector2f minVelocity, Vector2f maxVelocity) {
		this.minVelocity = minVelocity;
		diffVelocity = VecMath.subtraction(maxVelocity, minVelocity);
	}

	public void update(int delta) {
		int spawnedParticles = minSpawnedParticles + (int) (Math.random() * diffSpawnedParticles);
		for (int i = 0; i < spawnedParticles; i++) {
			addParticle(
					new Vector2f(center.x + Math.random() * spawnAreaHalfSize.x,
							center.y + Math.random() * spawnAreaHalfSize.y),
					new Vector2f(minVelocity.x + Math.random() * diffVelocity.x,
							minVelocity.y + Math.random() * diffVelocity.y),
					minSize + (float) Math.random() * diffSize, minLifeTime + (int) (Math.random() * diffLifeTime));
		}
		updateParticles(delta);
	}

}