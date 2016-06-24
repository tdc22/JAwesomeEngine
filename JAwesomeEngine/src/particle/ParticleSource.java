package particle;

import objects.Updateable;
import vector.Vector;

public abstract class ParticleSource<L extends Vector, A extends Vector> implements Updateable {
	ParticleSystem<L, A> particles;
	L center, spawnAreaHalfSize;
	A minAngle, diffAngle;
	int minLifeTime, diffLifeTime;
	float minSize, diffSize, minVelocity, diffVelocity, spawnRate, lastparticle;

	public ParticleSource(L center, L spawnAreaHalfSize, A minAngle, A maxAngle, float minVelocity, float maxVelocity,
			float minSize, float maxSize, int minLifeTime, int maxLifeTime, float spawnRate) {
		this.center = center;
		this.spawnAreaHalfSize = spawnAreaHalfSize;
		setParticleAngle(minAngle, maxAngle);
		setParticleVelocity(minVelocity, maxVelocity);
		setParticleSize(minSize, maxSize);
		setParticleLifeTime(minLifeTime, maxLifeTime);
		this.spawnRate = spawnRate;
		lastparticle = 0;
	}

	public abstract void setParticleAngle(A minAngle, A maxAngle);

	public void setParticleVelocity(float minVelocity, float maxVelocity) {
		this.minVelocity = minVelocity;
		diffVelocity = maxVelocity - minVelocity;
	}

	public void setParticleSize(float minSize, float maxSize) {
		this.minSize = minSize;
		diffSize = maxSize - minSize;
	}

	public void setParticleLifeTime(int minLifeTime, int maxLifeTime) {
		this.minLifeTime = minLifeTime;
		diffLifeTime = maxLifeTime - minLifeTime;
	}

	public ParticleSystem<L, A> getParticleSystem() {
		return particles;
	}
}