package sound;

import vector.Vector2f;
import vector.Vector3f;

public abstract class Sound {
	public abstract void pause();

	public abstract void play();

	public abstract void rewind();

	public abstract void stop();

	public abstract void setLooping(boolean loop);

	public abstract void setPitch(float pitch);

	public abstract void setGain(float gain);

	public abstract void delete();

	public abstract void setSourcePositionRelative(boolean relative);

	public abstract void setSourcePosition(Vector3f position);

	public abstract void setSourcePosition(Vector2f position);

	public abstract void setSourcePosition(float x, float y, float z);

	public abstract void setSourcePosition(float x, float y);

	public abstract void setSourceVelocity(Vector3f velocity);

	public abstract void setSourceVelocity(Vector2f velocity);

	public abstract void setSourceVelocity(float x, float y, float z);

	public abstract void setSourceVelocity(float x, float y);

	public abstract void setRolloffFactor(float rolloff);

	public abstract void setReferenceDistance(float refdistance);

	public abstract void setMaxDistance(float maxdistance);
}