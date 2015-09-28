package particle;

import objects.RenderableObject;
import objects.Updateable;
import vector.Vector;

public abstract class ParticleSource<L extends Vector> implements Updateable, RenderableObject {
	L center, spawnAreaHalfSize, minVelocity, diffVelocity;
	int minLifeTime, diffLifeTime, minSpawnedParticles, diffSpawnedParticles;
	float minSize, diffSize;

	public ParticleSource(L center, L spawnAreaHalfSize, L minVelocity, L maxVelocity, float minSize, float maxSize,
			int minLifeTime, int maxLifeTime, int minSpawnedParticles, int maxSpawnedParticles) {
		this.center = center;
		this.spawnAreaHalfSize = spawnAreaHalfSize;
		setParticleVelocity(minVelocity, maxVelocity);
		setParticleSize(minSize, maxSize);
		setParticleLifeTime(minLifeTime, maxLifeTime);
		setParticleSpawnRate(minSpawnedParticles, maxSpawnedParticles);
	}

	public abstract void setParticleVelocity(L minVelocity, L maxVelocity);

	public void setParticleSize(float minSize, float maxSize) {
		this.minSize = minSize;
		diffSize = maxSize - minSize;
	}

	public void setParticleSpawnRate(int minSpawnedParticles, int maxSpawnedParticles) {
		this.minSpawnedParticles = minSpawnedParticles;
		diffSpawnedParticles = maxSpawnedParticles - minSpawnedParticles;
	}

	public void setParticleLifeTime(int minLifeTime, int maxLifeTime) {
		this.minLifeTime = minLifeTime;
		diffLifeTime = maxLifeTime - minLifeTime;
	}

	public abstract void addParticle(L position, L velocity, float size, int lifetime);

	public abstract void updateParticles(int delta);
}