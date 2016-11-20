package particle;

import quaternion.Quaternionf;
import utils.VectorConstants;
import vector.Vector2f;
import vector.Vector3f;

public abstract class ParticleSource3 extends
		ParticleSource<Vector3f, Vector3f> {

	public ParticleSource3(Vector3f center, Vector3f spawnAreaHalfSize,
			Vector3f minAngle, Vector3f maxAngle, float minVelocity,
			float maxVelocity, float minSize, float maxSize, int minLifeTime,
			int maxLifeTime, float spawnRate) {
		super(center, spawnAreaHalfSize, minAngle, maxAngle, minVelocity,
				maxVelocity, minSize, maxSize, minLifeTime, maxLifeTime,
				spawnRate);
	}

	public void setParticleAngle(Vector3f minAngle, Vector3f maxAngle) {
		this.minAngle = minAngle;
		diffAngle = maxAngle;
		diffAngle.x -= minAngle.x;
		diffAngle.y -= minAngle.y;
		diffAngle.z -= minAngle.z;
	}

	private Quaternionf helper = new Quaternionf();

	public void update(int delta) {
		lastparticle += delta;
		for (; lastparticle > 0; lastparticle -= spawnRate) {
			float angleX = minAngle.x + (float) Math.random() * diffAngle.x;
			float angleY = minAngle.y + (float) Math.random() * diffAngle.y;
			float angleZ = minAngle.z + (float) Math.random() * diffAngle.z;
			helper.setIdentity();
			helper.rotate(angleZ, VectorConstants.AXIS_Z);
			helper.rotate(angleY, VectorConstants.AXIS_Y);
			helper.rotate(angleX, VectorConstants.AXIS_X);
			Vector3f velocity = new Vector3f(0, 1, 0);
			velocity.transform(helper);
			velocity.scale(minVelocity + (float) Math.random() * diffVelocity);
			float size = minSize + (float) Math.random() * diffSize;
			particles.addParticle(new Vector3f(center.x + Math.random()
					* spawnAreaHalfSize.x, center.y + Math.random()
					* spawnAreaHalfSize.y, center.z + Math.random()
					* spawnAreaHalfSize.z), velocity, new Vector2f(size, size),
					minLifeTime + (int) (Math.random() * diffLifeTime));
		}
		particles.updateParticles(delta, minLifeTime + diffLifeTime);
	}

}