package particle;

import static org.lwjgl.opengl.GL11.glDepthMask;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;

import math.QuatMath;
import math.VecMath;
import objects.Camera3;
import objects.ShapedObject;
import objects.ShapedObject3;

import org.lwjgl.opengl.GL11;

import vector.Vector2f;
import vector.Vector3f;

public class SimpleParticleSystem extends ParticleSystem3 {
	ShapedObject3 particles;
	HashMap<Integer, Particle> particleList;
	LinkedList<Integer> freeindices;
	int maxParticles;
	Camera3 cam;
	boolean useDepthSorting;

	public SimpleParticleSystem(Vector3f center, Camera3 cam, boolean depthSorting) {
		particles = new ShapedObject3(center);
		particles.setRenderMode(GL11.GL_TRIANGLES);
		particleList = new HashMap<Integer, Particle>();
		freeindices = new LinkedList<Integer>();
		maxParticles = 0;
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
		Integer pos = freeindices.poll();
		int insertpos;
		Vector3f color = new Vector3f(1, 1, 1);
		if (pos != null) {
			insertpos = pos;
			pos *= 4;
			particles.setVertex(pos, new Vector3f(particle.position.x - particle.size.x, particle.position.y - particle.size.y, particle.position.z),
					color, topleft, normal);
			particles.setVertex(pos + 1, new Vector3f(particle.position.x - particle.size.x, particle.position.y + particle.size.y, particle.position.z),
					color, topright, normal);
			particles.setVertex(pos + 2, new Vector3f(particle.position.x + particle.size.x, particle.position.y + particle.size.y, particle.position.z),
					color, bottomright, normal);
			particles.setVertex(pos + 3, new Vector3f(particle.position.x + particle.size.x, particle.position.y - particle.size.y, particle.position.z),
					color, bottomleft, normal);
			int indexpos = insertpos * 6;
			particles.setIndex(indexpos, pos);
			particles.setIndex(indexpos + 1, pos + 1);
			particles.setIndex(indexpos + 2, pos + 2);
			particles.setIndex(indexpos + 3, pos);
			particles.setIndex(indexpos + 4, pos + 2);
			particles.setIndex(indexpos + 5, pos + 3);
		} else {
			particles.addVertex(new Vector3f(particle.position.x - particle.size.x, particle.position.y - particle.size.y, particle.position.z), color,
					topleft, normal);
			particles.addVertex(new Vector3f(particle.position.x - particle.size.x, particle.position.y + particle.size.y, particle.position.z), color,
					topright, normal);
			particles.addVertex(new Vector3f(particle.position.x + particle.size.x, particle.position.y + particle.size.y, particle.position.z), color,
					bottomright, normal);
			particles.addVertex(new Vector3f(particle.position.x + particle.size.x, particle.position.y - particle.size.y, particle.position.z), color,
					bottomleft, normal);
			insertpos = maxParticles;
			pos = maxParticles * 4;
			particles.addIndices(pos, pos + 1, pos + 2, pos, pos + 2, pos + 3);
			maxParticles++;
		}
		particleList.put(insertpos, particle);
		return insertpos;
	}

	private final Vector3f nullvec = new Vector3f();
	
	@Override
	public void removeParticle(int particleID) {
		particleList.remove(particleID);
		int i4 = particleID * 4;
		particles.setVertex(i4 + 3, nullvec);
		particles.setVertex(i4 + 2, nullvec);
		particles.setVertex(i4 + 1, nullvec);
		particles.setVertex(i4, nullvec);
		int i6 = particleID * 6;
		particles.setIndex(i6 + 5, 0);
		particles.setIndex(i6 + 4, 0);
		particles.setIndex(i6 + 3, 0);
		particles.setIndex(i6 + 2, 0);
		particles.setIndex(i6 + 1, 0);
		particles.setIndex(i6, 0);
		freeindices.add(particleID);
	}

	@Override
	public void updateParticles(int delta, float maxLifeTime) {
		// TODO: parallel
		Vector3f right = QuatMath.transform(cam.getRotation(), new Vector3f(1, 0, 0));
		right.normalize();
		Vector3f up = VecMath.crossproduct(right, cam.getDirection());
		
		for (int i = 0; i < maxParticles; i++) {
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
					p.distance = (float) Math.sqrt(xd * xd + yd * yd + zd * zd);
					/*
					 * Math.abs(p.position.x - cam.getTranslation().x) +
					 * Math.abs(p.position.y - cam.getTranslation().y) +
					 * Math.abs(p.position.z - cam.getTranslation().z);
					 */
					
					int i4 = i * 4;
					float rx = right.x * p.size.x;
					float ux = up.x * p.size.y;
					float ax = rx + ux;
					float bx = rx - ux;
					float rz = right.z * p.size.x;
					float uz = up.z * p.size.y;
					float ay = rz + uz;
					float by = rz - uz;
					float uy = up.y * p.size.y;
					particles.getVertex(i4).set(p.position.x - ax, p.position.y - uy, p.position.z - ay);
					particles.getVertex(i4 + 1).set(p.position.x + bx, p.position.y - uy, p.position.z + by);
					particles.getVertex(i4 + 2).set(p.position.x + ax, p.position.y + uy, p.position.z + ay);
					particles.getVertex(i4 + 3).set(p.position.x - bx, p.position.y + uy, p.position.z - by);
					float particleAlpha = p.lifetime / (float) maxLifeTime;
					particles.getColor(i4).x = particleAlpha;
//					particles.getColor(i4 + 1).x = particleAlpha;
//					particles.getColor(i4 + 2).x = particleAlpha;
//					particles.getColor(i4 + 3).x = particleAlpha;
				} else {
					removeParticle(i);
				}
			}
		}
		// TODO: depth-sorting by insertion sort
		// TODO: insert at right position to speed up sorting for new particles
		if (useDepthSorting) {
			for (int i = 0; i < maxParticles - 1; i++) {
				Particle p = particleList.get(i);
				if (p != null) {
					int a = i;
					Particle next = null;

					while (next == null && a < maxParticles) {
						a++;
						next = particleList.get(a);
					}

					if (next != null && p.distance > next.distance) {
						particleList.put(i, next);
						particleList.put(a, p);
						int i6 = i * 6;
						int a6 = a * 6;
						int index1 = particles.getIndex(i6);
						int index2 = particles.getIndex(i6 + 1);
						int index3 = particles.getIndex(i6 + 2);
						int index4 = particles.getIndex(i6 + 3);
						int index5 = particles.getIndex(i6 + 4);
						int index6 = particles.getIndex(i6 + 5);
						particles.setIndex(i6, particles.getIndex(a6));
						particles.setIndex(i6 + 1, particles.getIndex(a6 + 1));
						particles.setIndex(i6 + 2, particles.getIndex(a6 + 2));
						particles.setIndex(i6 + 3, particles.getIndex(a6 + 3));
						particles.setIndex(i6 + 4, particles.getIndex(a6 + 4));
						particles.setIndex(i6 + 5, particles.getIndex(a6 + 5));
						particles.setIndex(a6, index1);
						particles.setIndex(a6 + 1, index2);
						particles.setIndex(a6 + 2, index3);
						particles.setIndex(a6 + 3, index4);
						particles.setIndex(a6 + 4, index5);
						particles.setIndex(a6 + 5, index6);

						if (a < maxParticles - 1)
							i = a;
					}
				}
			}
		}
		particles.prerender();
	}

	public void setCamera(Camera3 cam) {
		this.cam = cam;
	}

	protected class Particle {
		Vector3f position, velocity;
		int lifetime;
		Vector2f size;
		float distance;

		protected Particle(Vector3f position, Vector3f velocity, int lifetime, Vector2f size) {
			this.position = position;
			this.velocity = velocity;
			this.lifetime = lifetime;
			this.size = size;
		}
	}

	@Override
	public void render() {
		if(useDepthSorting)
			glDepthMask(false);
		particles.render();
		if(useDepthSorting)
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
