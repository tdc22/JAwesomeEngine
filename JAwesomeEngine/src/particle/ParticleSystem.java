package particle;

import objects.RenderableObject;
import objects.ShapedObject;
import vector.Vector;
import vector.Vector2f;

public abstract class ParticleSystem<L extends Vector, A extends Vector> implements RenderableObject {
	public abstract int addParticle(L position, L velocity, Vector2f size, int lifetime);
	
	public abstract void removeParticle(int particleID);

	public abstract void updateParticles(int delta, float maxLifeTime);

	public abstract ShapedObject<L, ?> getParticleObject();
}
