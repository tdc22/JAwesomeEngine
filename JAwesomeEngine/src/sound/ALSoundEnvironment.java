package sound;

import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alListener3f;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

import java.nio.ByteBuffer;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import vector.Vector2f;
import vector.Vector3f;

public class ALSoundEnvironment extends SoundEnvironment {
	public long device, context;

	public ALSoundEnvironment() {
		device = alcOpenDevice((ByteBuffer) null);
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		context = alcCreateContext(device, null);

		if (!alcMakeContextCurrent(context)) {
			System.err.println("Failed to make context current.");
		}
		AL.createCapabilities(deviceCaps);
	}

	@Override
	public void delete() {
		alcDestroyContext(context);
		alcCloseDevice(device);
	}

	@Override
	public void setListenerPosition(Vector3f pos) {
		alListener3f(AL_POSITION, pos.x, pos.y, pos.z);
	}

	@Override
	public void setListenerPosition(Vector2f pos) {
		alListener3f(AL_POSITION, pos.x, pos.y, 0);
	}

	@Override
	public void setListenerPosition(float x, float y, float z) {
		alListener3f(AL_POSITION, x, y, z);
	}

	@Override
	public void setListenerPosition(float x, float y) {
		alListener3f(AL_POSITION, x, y, 0);
	}
}