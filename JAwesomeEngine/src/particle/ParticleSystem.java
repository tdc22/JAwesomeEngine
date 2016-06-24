package particle;

import objects.RenderableObject;
import objects.ShapedObject;
import vector.Vector;

public abstract class ParticleSystem<L extends Vector, A extends Vector> implements RenderableObject {
	public abstract void addParticle(L position, L velocity, float size, int lifetime);

	public abstract void updateParticles(int delta, float maxLifeTime);

	public abstract ShapedObject<L, ?> getParticleObject();
}
