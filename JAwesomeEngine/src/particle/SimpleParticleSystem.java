package particle;

import static org.lwjgl.opengl.GL11.glDepthMask;

import java.nio.FloatBuffer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import math.VecMath;
import objects.Camera3;
import objects.ShapedObject;
import objects.ShapedObject3;
import utils.VectorConstants;
import vector.Vector2f;
import vector.Vector3f;

public class SimpleParticleSystem extends ParticleSystem3 {
	ShapedObject3 particles;
	ArrayList<Particle> particleList;
	ArrayDeque<Integer> freevertices, freeindices;
	Camera3 cam;
	boolean useDepthSorting;

	public SimpleParticleSystem(Vector3f center, Camera3 cam, boolean depthSorting) {
		particles = new ShapedObject3(center);
		particles.setRenderMode(GL11.GL_TRIANGLES);
		particleList = new ArrayList<Particle>();
		freevertices = new ArrayDeque<Integer>();
		freeindices = new ArrayDeque<Integer>();
		useDepthSorting = depthSorting;
		setCamera(cam);
	}

	private final Vector3f normal = new Vector3f(0, 0, 1);
	private final Vector2f topleft = new Vector2f(0, 0), bottomleft = new Vector2f(0, 1),
			bottomright = new Vector2f(1, 1), topright = new Vector2f(1, 0);

	@Override
	public int addParticle(Vector3f position, Vector3f velocity, Vector2f size, int lifetime) {
		return addParticle(new Particle(position, velocity, lifetime, size));
	}

	public int addParticle(Particle particle) {
		Integer pos = freevertices.poll();
		int insertindex, insertvertex;
		Vector3f color = new Vector3f(1, 1, 1);
		if (pos != null) {
			insertindex = freeindices.poll();
			insertvertex = pos;
			pos *= 4;
			particles.setVertex(pos, new Vector3f(particle.position.x - particle.size.x,
					particle.position.y - particle.size.y, particle.position.z), color, topleft, normal);
			particles.setVertex(pos + 1, new Vector3f(particle.position.x - particle.size.x,
					particle.position.y + particle.size.y, particle.position.z), color, topright, normal);
			particles.setVertex(pos + 2, new Vector3f(particle.position.x + particle.size.x,
					particle.position.y + particle.size.y, particle.position.z), color, bottomright, normal);
			particles.setVertex(pos + 3, new Vector3f(particle.position.x + particle.size.x,
					particle.position.y - particle.size.y, particle.position.z), color, bottomleft, normal);
			int indexpos = insertindex * 6;
			particles.setIndex(indexpos, pos);
			particles.setIndex(indexpos + 1, pos + 1);
			particles.setIndex(indexpos + 2, pos + 2);
			particles.setIndex(indexpos + 3, pos);
			particles.setIndex(indexpos + 4, pos + 2);
			particles.setIndex(indexpos + 5, pos + 3);
		} else {
			particles.addVertex(new Vector3f(particle.position.x - particle.size.x,
					particle.position.y - particle.size.y, particle.position.z), color, topleft, normal);
			particles.addVertex(new Vector3f(particle.position.x - particle.size.x,
					particle.position.y + particle.size.y, particle.position.z), color, topright, normal);
			particles.addVertex(new Vector3f(particle.position.x + particle.size.x,
					particle.position.y + particle.size.y, particle.position.z), color, bottomright, normal);
			particles.addVertex(new Vector3f(particle.position.x + particle.size.x,
					particle.position.y - particle.size.y, particle.position.z), color, bottomleft, normal);
			insertindex = particleList.size();
			insertvertex = insertindex;
			pos = insertvertex * 4;
			particles.addIndices(pos, pos + 1, pos + 2, pos, pos + 2, pos + 3);
		}
		particle.indexindex = insertindex;
		particle.vertexindex = insertvertex;
		particleList.add(particle);
		return insertindex;
	}

	public Particle getParticle(int particleID) {
		return particleList.get(particleID);
	}

	public List<Particle> getParticleList() {
		return particleList;
	}

	private final Vector3f nullvec = new Vector3f();

	@Override
	public void removeParticle(int particleID) {
		Particle p = particleList.remove(particleID);
		int i4 = p.vertexindex * 4;
		particles.setVertex(i4, nullvec);
		particles.setVertex(i4 + 1, nullvec);
		particles.setVertex(i4 + 2, nullvec);
		particles.setVertex(i4 + 3, nullvec);
		int i6 = p.indexindex * 6;
		particles.setIndex(i6, 0);
		particles.setIndex(i6 + 1, 0);
		particles.setIndex(i6 + 2, 0);
		particles.setIndex(i6 + 3, 0);
		particles.setIndex(i6 + 4, 0);
		particles.setIndex(i6 + 5, 0);
		freevertices.add(p.vertexindex);
		freeindices.add(p.indexindex);
	}

	private final Vector3f particleright = new Vector3f();
	private final Vector3f particleup = new Vector3f();

	@Override
	public void updateParticles(int delta, float maxLifeTime) {
		// TODO: parallel
		particleright.set(VectorConstants.AXIS_X);
		particleright.transform(cam.getRotation());
		particleright.normalize();
		VecMath.crossproduct(particleright, cam.getDirection(), particleup);

		for (int i = 0; i < particleList.size(); i++) {
			Particle p = particleList.get(i);
			if (p != null) {
				if (p.lifetime > delta) {
					p.lifetime -= delta;
					p.position.x += p.velocity.x * delta;
					p.position.y += p.velocity.y * delta;
					p.position.z += p.velocity.z * delta;
					float xd = p.position.x - cam.getTranslation().x;
					float yd = p.position.y - cam.getTranslation().y;
					float zd = p.position.z - cam.getTranslation().z;
					p.distance = xd * xd + yd * yd + zd * zd;

					int i4 = p.vertexindex * 4;
					float rx = particleright.x * p.size.x;
					float ux = particleup.x * p.size.y;
					float ax = rx + ux;
					float bx = rx - ux;
					float rz = particleright.z * p.size.x;
					float uz = particleup.z * p.size.y;
					float ay = rz + uz;
					float by = rz - uz;
					float uy = particleup.y * p.size.y;
					particles.getVertex(i4).set(p.position.x - ax, p.position.y - uy, p.position.z - ay);
					particles.getVertex(i4 + 1).set(p.position.x + bx, p.position.y - uy, p.position.z + by);
					particles.getVertex(i4 + 2).set(p.position.x + ax, p.position.y + uy, p.position.z + ay);
					particles.getVertex(i4 + 3).set(p.position.x - bx, p.position.y + uy, p.position.z - by);
					float particleAlpha = p.lifetime / (float) maxLifeTime;
					particles.getColor(i4).x = particleAlpha;
					// particles.getColor(i4 + 1).x = particleAlpha;
					// particles.getColor(i4 + 2).x = particleAlpha;
					// particles.getColor(i4 + 3).x = particleAlpha;
				} else {
					removeParticle(i);
				}
			}
		}
		// System.out.print("Before " + particleList.size() + " ");
		// for(Particle p : particleList.values())
		// System.out.print(p.distance + " ");
		if (useDepthSorting) {
			for (int i = 1; i < particleList.size(); i++) {
				Particle p = particleList.get(i);
				if (p != null) {
					int i6 = p.indexindex * 6;
					int ind0 = particles.getIndex(i6);
					int ind1 = particles.getIndex(i6 + 1);
					int ind2 = particles.getIndex(i6 + 2);
					int ind3 = particles.getIndex(i6 + 3);
					int ind4 = particles.getIndex(i6 + 4);
					int ind5 = particles.getIndex(i6 + 5);

					int k = i;
					int j = i - 1;
					Particle pj;
					while (j >= 0 && ((pj = particleList.get(j)) == null || pj.distance < p.distance)) {
						if (pj != null) {
							particleList.set(k, pj);
							int from6 = pj.indexindex * 6;
							int to6 = k * 6;
							particles.setIndex(to6, particles.getIndex(from6));
							particles.setIndex(to6 + 1, particles.getIndex(from6 + 1));
							particles.setIndex(to6 + 2, particles.getIndex(from6 + 2));
							particles.setIndex(to6 + 3, particles.getIndex(from6 + 3));
							particles.setIndex(to6 + 4, particles.getIndex(from6 + 4));
							particles.setIndex(to6 + 5, particles.getIndex(from6 + 5));
							pj.indexindex = k;
							k = j;
						}
						j--;
					}
					/*
					 * k = j+1; // TODO: what if j+1 is null? while(particleList.get(k) == null && k
					 * < i) k++;
					 */
					// We don't need to reassign k because k is the origin (j) of the last swap
					// here!
					// if(i != k) {
					particleList.set(k, p);
					int k6 = k * 6;
					particles.setIndex(k6, ind0);
					particles.setIndex(k6 + 1, ind1);
					particles.setIndex(k6 + 2, ind2);
					particles.setIndex(k6 + 3, ind3);
					particles.setIndex(k6 + 4, ind4);
					particles.setIndex(k6 + 5, ind5);
					p.indexindex = k;
					// }
				}
			}
		}
		// System.out.print("After ");
		// for(Particle p : particleList)
		// System.out.print(p.distance + " ");
		// System.out.println();
		particles.prerender();
	}

	public void setCamera(Camera3 cam) {
		this.cam = cam;
	}

	public class Particle {
		Vector3f position, velocity;
		int lifetime, vertexindex, indexindex;
		Vector2f size;
		float distance;

		protected Particle(Vector3f position, Vector3f velocity, int lifetime, Vector2f size) {
			this.position = position;
			this.velocity = velocity;
			this.lifetime = lifetime;
			this.size = size;
		}

		public Vector3f getPosition() {
			return position;
		}

		public Vector3f getVelocity() {
			return velocity;
		}

		public int getLifetime() {
			return lifetime;
		}

		public void setLifetime(int lifetime) {
			this.lifetime = lifetime;
		}

		public Vector2f getSize() {
			return size;
		}
	}

	@Override
	public void render() {
		if (useDepthSorting)
			glDepthMask(false);
		particles.render();
		if (useDepthSorting)
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

	@Override
	public ShapedObject<Vector3f, ?> getParticleObject() {
		return particles;
	}
}
