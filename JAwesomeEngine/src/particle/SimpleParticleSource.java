package particle;

import static org.lwjgl.opengl.GL11.glDepthMask;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import math.QuatMath;
import math.VecMath;
import objects.Camera3;
import objects.ShapedObject;
import vector.Vector2f;
import vector.Vector3f;

public class SimpleParticleSource extends ParticleSource3 {
	ShapedObject particles;
	HashMap<Integer, Particle> particleList;
	LinkedList<Integer> freevertices, freeindices;
	int maxParticles;
	Camera3 cam;
	// List<Particle> distanceList;

	private final Vector3f normal = new Vector3f(0, 0, 1);
	private final Vector2f topleft = new Vector2f(0, 0), bottomleft = new Vector2f(0, 1),
			bottomright = new Vector2f(1, 1), topright = new Vector2f(1, 0);

	public SimpleParticleSource(Vector3f center, Vector3f spawnAreaHalfSize, Vector3f minAngle, Vector3f maxAngle,
			float minVelocity, float maxVelocity, float minSize, float maxSize, int minLifeTime, int maxLifeTime,
			float spawnRate, Camera3 cam) {
		super(center, spawnAreaHalfSize, minAngle, maxAngle, minVelocity, maxVelocity, minSize, maxSize, minLifeTime,
				maxLifeTime, spawnRate);
		particles = new ShapedObject(center);
		particles.setRenderMode(GL11.GL_TRIANGLES);
		particleList = new HashMap<Integer, Particle>();
		freevertices = new LinkedList<Integer>();
		freeindices = new LinkedList<Integer>();
		// distanceList = new ArrayList<Particle>();
		maxParticles = 0;
		setCamera(cam);
	}

	@Override
	public void addParticle(Vector3f position, Vector3f velocity, float size, int lifetime) {
		Integer pos = freevertices.poll();
		int insertpos;
		if (pos != null) {
			insertpos = pos;
			pos *= 4;
			particles.setVertex(pos, new Vector3f(position.x - size, position.y - size, position.z),
					new Vector3f(1, 1, 1), topleft, normal);
			particles.setVertex(pos + 1, new Vector3f(position.x - size, position.y + size, position.z),
					new Vector3f(1, 1, 1), topright, normal);
			particles.setVertex(pos + 2, new Vector3f(position.x + size, position.y + size, position.z),
					new Vector3f(1, 1, 1), bottomright, normal);
			particles.setVertex(pos + 3, new Vector3f(position.x + size, position.y - size, position.z),
					new Vector3f(1, 1, 1), bottomleft, normal);
			int indexpos = freeindices.poll();
			indexpos *= 6;
			particles.setIndex(indexpos, pos);
			particles.setIndex(indexpos + 1, pos + 1);
			particles.setIndex(indexpos + 2, pos + 2);
			particles.setIndex(indexpos + 3, pos);
			particles.setIndex(indexpos + 4, pos + 2);
			particles.setIndex(indexpos + 5, pos + 3);
		} else {
			particles.addVertex(new Vector3f(position.x - size, position.y - size, position.z), new Vector3f(1, 1, 1),
					topleft, normal);
			particles.addVertex(new Vector3f(position.x - size, position.y + size, position.z), new Vector3f(1, 1, 1),
					topright, normal);
			particles.addVertex(new Vector3f(position.x + size, position.y + size, position.z), new Vector3f(1, 1, 1),
					bottomright, normal);
			particles.addVertex(new Vector3f(position.x + size, position.y - size, position.z), new Vector3f(1, 1, 1),
					bottomleft, normal);
			insertpos = maxParticles;
			pos = maxParticles * 4;
			particles.addIndices(pos, pos + 1, pos + 2, pos, pos + 2, pos + 3);
			maxParticles++;
		}
		Particle particle = new Particle(position, velocity, lifetime, size);
		particleList.put(insertpos, particle);
		// distanceList.add(particle);
	}

	@Override
	public void updateParticles(int delta) {
		// TODO: parallel
		float maxLifeTime = minLifeTime + diffLifeTime;
		Vector3f right = QuatMath.transform(cam.getRotation(), new Vector3f(1, 0, 0));
		right.normalize();
		Vector3f up = VecMath.crossproduct(right, cam.getDirection());
		Vector2f urA = new Vector2f(right.x + up.x, right.z + up.z);
		Vector2f urB = new Vector2f(right.x - up.x, right.z - up.z);
		for (int i = 0; i < maxParticles; i++) {
			Particle p = particleList.get(i);
			if (p != null) {
				if (p.lifetime > delta) {
					p.lifetime -= delta;
					p.position.x += p.velocity.x * delta;
					p.position.y += p.velocity.y * delta;
					p.position.z += p.velocity.z * delta;
					// TODO: optimize
					p.distance = (float) VecMath.subtraction(p.position, cam.getTranslation())
							.length();/*
										 * Math.abs(p.position.x -
										 * cam.getTranslation().x) +
										 * Math.abs(p.position.y -
										 * cam.getTranslation().y) +
										 * Math.abs(p.position.z -
										 * cam.getTranslation().z);
										 */
					int i4 = i * 4;
					float uy = up.y * p.size;
					float ax = urA.x * p.size;
					float ay = urA.y * p.size;
					float bx = urB.x * p.size;
					float by = urB.y * p.size;
					particles.getVertex(i4).x = p.position.x - ax;
					particles.getVertex(i4).y = p.position.y - uy;
					particles.getVertex(i4).z = p.position.z - ay;
					particles.getVertex(i4 + 1).x = p.position.x + bx;
					particles.getVertex(i4 + 1).y = p.position.y - uy;
					particles.getVertex(i4 + 1).z = p.position.z + by;
					particles.getVertex(i4 + 2).x = p.position.x + ax;
					particles.getVertex(i4 + 2).y = p.position.y + uy;
					particles.getVertex(i4 + 2).z = p.position.z + ay;
					particles.getVertex(i4 + 3).x = p.position.x - bx;
					particles.getVertex(i4 + 3).y = p.position.y + uy;
					particles.getVertex(i4 + 3).z = p.position.z - by;
					float particleAlpha = p.lifetime / (float) maxLifeTime;
					particles.getColor(i4).x = particleAlpha;
					particles.getColor(i4 + 1).x = particleAlpha;
					particles.getColor(i4 + 2).x = particleAlpha;
					particles.getColor(i4 + 3).x = particleAlpha;
				} else {
					particleList.remove(i);
					// distanceList.remove(p);
					int i4 = i * 4;
					Vector3f nullvec = new Vector3f();
					particles.setVertex(i4 + 3, nullvec);
					particles.setVertex(i4 + 2, nullvec);
					particles.setVertex(i4 + 1, nullvec);
					particles.setVertex(i4, nullvec);
					freevertices.add(i);
					int i6 = i * 6;
					particles.setIndex(i6 + 5, 0);
					particles.setIndex(i6 + 4, 0);
					particles.setIndex(i6 + 3, 0);
					particles.setIndex(i6 + 2, 0);
					particles.setIndex(i6 + 1, 0);
					particles.setIndex(i6, 0);
					freeindices.add(i);
				}
			}
		}
		// TODO: depth-sorting by insertion sort
		// TODO: insert at right position to speed up sorting for new particles
		/*
		 * for (int i = 0; i < distanceList.size(); i++) { Particle p =
		 * distanceList.get(i); int i6 = i * 6; int index0 =
		 * particles.getIndex(i6); int index1 = particles.getIndex(i6+1); int
		 * index2 = particles.getIndex(i6+2); int index3 =
		 * particles.getIndex(i6+3); int index4 = particles.getIndex(i6+4); int
		 * index5 = particles.getIndex(i6+5); int j = i; while(j > 0 &&
		 * distanceList.get(j-1).distance > p.distance) { distanceList.set(j,
		 * distanceList.get(j-1)); int j6 = j * 6; particles.setIndex(j6,
		 * particles.getIndex(j6-6)); particles.setIndex(j6+1,
		 * particles.getIndex(j6-5)); particles.setIndex(j6+2,
		 * particles.getIndex(j6-4)); particles.setIndex(j6+3,
		 * particles.getIndex(j6-3)); particles.setIndex(j6+4,
		 * particles.getIndex(j6-2)); particles.setIndex(j6+5,
		 * particles.getIndex(j6-1)); j--; } distanceList.set(j, p); int j6 = j
		 * * 6; particles.setIndex(j6, index0); particles.setIndex(j6+1,
		 * index1); particles.setIndex(j6+2, index2); particles.setIndex(j6+3,
		 * index3); particles.setIndex(j6+4, index4); particles.setIndex(j6+5,
		 * index5); } /*System.out.println("--------------------------------");
		 * for(Particle p : distanceList) System.out.println(p.distance);
		 */
		particles.prerender();
	}

	public void setCamera(Camera3 cam) {
		this.cam = cam;
	}

	protected class Particle {
		Vector3f position, velocity;
		int lifetime;
		float size, distance;

		protected Particle(Vector3f position, Vector3f velocity, int lifetime, float size) {
			this.position = position;
			this.velocity = velocity;
			this.lifetime = lifetime;
			this.size = size;
		}
	}

	@Override
	public void render() {
		glDepthMask(false);
		particles.render();
		glDepthMask(true);
	}

	@Override
	public void delete() {
		particles.delete();
	}

	@Override
	public FloatBuffer getMatrixBuffer() {
		return particles.getMatrixBuffer();
	}

	public ShapedObject getObject() {
		return particles;
	}
}
