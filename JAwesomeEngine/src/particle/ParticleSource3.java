package particle;

import math.VecMath;
import matrix.Matrix4f;
import vector.Vector3f;

public abstract class ParticleSource3 extends ParticleSource<Vector3f, Vector3f> {

	public ParticleSource3(Vector3f center, Vector3f spawnAreaHalfSize, Vector3f minAngle, Vector3f maxAngle,
			float minVelocity, float maxVelocity, float minSize, float maxSize, int minLifeTime, int maxLifeTime,
			float spawnRate) {
		super(center, spawnAreaHalfSize, minAngle, maxAngle, minVelocity, maxVelocity, minSize, maxSize, minLifeTime,
				maxLifeTime, spawnRate);
	}

	public void setParticleAngle(Vector3f minAngle, Vector3f maxAngle) {
		this.minAngle = minAngle;
		diffAngle = VecMath.subtraction(maxAngle, minAngle);
	}

	public void update(int delta) {
		lastparticle += delta;
		for (; lastparticle > 0; lastparticle -= spawnRate) {
			float angleX = minAngle.x + (float) Math.random() * diffAngle.x;
			float angleY = minAngle.y + (float) Math.random() * diffAngle.y;
			float angleZ = minAngle.z + (float) Math.random() * diffAngle.z;
			Matrix4f mat = new Matrix4f();
			// TODO: Optimize by creating Matrix3 directly!
			mat.rotate(angleZ, new Vector3f(0.0f, 0.0f, 1.0f));
			mat.rotate(angleY, new Vector3f(0.0f, 1.0f, 0.0f));
			mat.rotate(angleX, new Vector3f(1.0f, 0.0f, 0.0f));
			Vector3f velocity = new Vector3f(0, 1, 0);
			velocity.transform(mat.getSubMatrix());
			velocity.scale(minVelocity + (float) Math.random() * diffVelocity);
			addParticle(
					new Vector3f(center.x + Math.random() * spawnAreaHalfSize.x,
							center.y + Math.random() * spawnAreaHalfSize.y,
							center.z + Math.random() * spawnAreaHalfSize.z),
					velocity, minSize + (float) Math.random() * diffSize,
					minLifeTime + (int) (Math.random() * diffLifeTime));
		}
		updateParticles(delta);
	}

}