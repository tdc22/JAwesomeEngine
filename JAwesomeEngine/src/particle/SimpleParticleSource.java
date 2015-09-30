package particle;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import objects.Camera;
import objects.ShapedObject;
import vector.Vector2f;
import vector.Vector3f;

public class SimpleParticleSource extends ParticleSource3 {
	ShapedObject particles;
	HashMap<Integer, Particle> particleList;
	LinkedList<Integer> freevertices, freeindices;
	int maxParticles;
	Camera cam;

	private final Vector3f normal = new Vector3f(0, 0, 1);
	private final Vector2f topleft = new Vector2f(0, 0), bottomleft = new Vector2f(0, 1),
			bottomright = new Vector2f(1, 1), topright = new Vector2f(1, 0);

	public SimpleParticleSource(Vector3f center, Vector3f spawnAreaHalfSize, Vector3f minAngle, Vector3f maxAngle,
			float minVelocity, float maxVelocity, float minSize, float maxSize, int minLifeTime, int maxLifeTime,
			float spawnRate, Camera cam) {
		super(center, spawnAreaHalfSize, minAngle, maxAngle, minVelocity, maxVelocity, minSize, maxSize, minLifeTime,
				maxLifeTime, spawnRate);
		particles = new ShapedObject(center);
		particles.setRenderMode(GL11.GL_TRIANGLES);
		particleList = new HashMap<Integer, Particle>();
		freevertices = new LinkedList<Integer>();
		freeindices = new LinkedList<Integer>();
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
		particleList.put(insertpos, new Particle(position, velocity, lifetime));
	}

	@Override
	public void updateParticles(int delta) {
		// TODO: parallel
		float maxLifeTime = minLifeTime + diffLifeTime;
		for (int i = 0; i < maxParticles; i++) {
			Particle p = particleList.get(i);
			if (p != null) {
				if (p.lifetime > delta) {
					p.lifetime -= delta;
					float stepX = p.velocity.x * delta;
					float stepY = p.velocity.y * delta;
					float stepZ = p.velocity.z * delta;
					p.position.x += stepX;
					p.position.y += stepY;
					p.position.z += stepZ;
					p.distance = Math.abs(p.position.x - cam.getTranslation().x)
							+ Math.abs(p.position.y - cam.getTranslation().y)
							+ Math.abs(p.position.z - cam.getTranslation().z);
					int i4 = i * 4;
					particles.getVertex(i4).x += stepX;
					particles.getVertex(i4).y += stepY;
					particles.getVertex(i4).z += stepZ;
					particles.getVertex(i4 + 1).x += stepX;
					particles.getVertex(i4 + 1).y += stepY;
					particles.getVertex(i4 + 1).z += stepZ;
					particles.getVertex(i4 + 2).x += stepX;
					particles.getVertex(i4 + 2).y += stepY;
					particles.getVertex(i4 + 2).z += stepZ;
					particles.getVertex(i4 + 3).x += stepX;
					particles.getVertex(i4 + 3).y += stepY;
					particles.getVertex(i4 + 3).z += stepZ;
					float particleAlpha = p.lifetime / (float) maxLifeTime;
					particles.getColor(i4).x = particleAlpha;
					particles.getColor(i4 + 1).x = particleAlpha;
					particles.getColor(i4 + 2).x = particleAlpha;
					particles.getColor(i4 + 3).x = particleAlpha;
				} else {
					particleList.remove(i);
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
		for (int i = 0; i < maxParticles; i++) {
			Particle p = particleList.get(i);
			if (p != null) {

			}
		}
		particles.prerender();
	}

	public void setCamera(Camera cam) {
		this.cam = cam;
	}

	protected class Particle {
		Vector3f position, velocity;
		int lifetime;
		float distance;

		protected Particle(Vector3f position, Vector3f velocity, int lifetime) {
			this.position = position;
			this.velocity = velocity;
			this.lifetime = lifetime;
		}
	}

	@Override
	public void render() {
		particles.render();
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
