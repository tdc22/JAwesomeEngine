package particle;

import vector.Vector1f;
import vector.Vector2f;

public class SimpleParticleSource2 extends ParticleSource2 {
	public SimpleParticleSource2(Vector2f center, Vector2f spawnAreaHalfSize, Vector1f minAngle, Vector1f maxAngle,
			float minVelocity, float maxVelocity, float minSize, float maxSize, int minLifeTime, int maxLifeTime,
			float spawnRate) {
		super(center, spawnAreaHalfSize, minAngle, maxAngle, minVelocity, maxVelocity, minSize, maxSize, minLifeTime,
				maxLifeTime, spawnRate);
		particles = new SimpleParticleSystem2(center);
	}
}
