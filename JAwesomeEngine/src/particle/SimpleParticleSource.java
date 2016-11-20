package particle;

import objects.Camera3;
import vector.Vector3f;

public class SimpleParticleSource extends ParticleSource3 {
	public SimpleParticleSource(Vector3f center, Vector3f spawnAreaHalfSize,
			Vector3f minAngle, Vector3f maxAngle, float minVelocity,
			float maxVelocity, float minSize, float maxSize, int minLifeTime,
			int maxLifeTime, float spawnRate, Camera3 cam, boolean depthSorting) {
		super(center, spawnAreaHalfSize, minAngle, maxAngle, minVelocity,
				maxVelocity, minSize, maxSize, minLifeTime, maxLifeTime,
				spawnRate);
		particles = new SimpleParticleSystem(center, cam, depthSorting);
	}
}
