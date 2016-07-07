package sound;

import vector.Vector2f;
import vector.Vector3f;

public abstract class SoundEnvironment {
	public abstract void setListenerPosition(Vector3f pos);

	public abstract void setListenerPosition(Vector2f pos);

	public abstract void setListenerPosition(float x, float y, float z);

	public abstract void setListenerPosition(float x, float y);

	public abstract void setListenerOrientation(Vector3f up, Vector3f front);

	public abstract void setListenerOrientation(Vector2f up, Vector2f front);

	public abstract void delete();
}
