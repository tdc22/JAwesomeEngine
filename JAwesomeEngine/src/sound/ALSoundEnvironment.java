package sound;

import static org.lwjgl.openal.AL10.AL_INVERSE_DISTANCE;
import static org.lwjgl.openal.AL10.AL_INVERSE_DISTANCE_CLAMPED;
import static org.lwjgl.openal.AL10.AL_ORIENTATION;
import static org.lwjgl.openal.AL10.AL_POSITION;
import static org.lwjgl.openal.AL10.alDistanceModel;
import static org.lwjgl.openal.AL10.alListener3f;
import static org.lwjgl.openal.AL10.alListenerfv;
import static org.lwjgl.openal.AL11.AL_EXPONENT_DISTANCE;
import static org.lwjgl.openal.AL11.AL_EXPONENT_DISTANCE_CLAMPED;
import static org.lwjgl.openal.AL11.AL_LINEAR_DISTANCE;
import static org.lwjgl.openal.AL11.AL_LINEAR_DISTANCE_CLAMPED;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
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

		setDistanceModel(DistanceModel.InverseDistance);
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

	private FloatBuffer orientationHelperBuffer = (FloatBuffer) BufferUtils.createFloatBuffer(6);

	@Override
	public void setListenerOrientation(Vector3f up, Vector3f front) {
		orientationHelperBuffer.clear();
		orientationHelperBuffer.put(new float[] { front.x, front.y, front.z, up.x, up.y, up.z });
		orientationHelperBuffer.rewind();
		alListenerfv(AL_ORIENTATION, orientationHelperBuffer);
	}

	@Override
	public void setListenerOrientation(Vector2f up, Vector2f front) {
		orientationHelperBuffer.clear();
		orientationHelperBuffer.put(new float[] { front.x, front.y, 0, up.x, up.y, 0 });
		orientationHelperBuffer.rewind();
		alListenerfv(AL_ORIENTATION, orientationHelperBuffer);
	}

	@Override
	public void setDistanceModel(DistanceModel model) {
		int alModel = 0;
		switch (model) {
		case InverseDistance:
			alModel = AL_INVERSE_DISTANCE;
			break;
		case InverseDistanceClamped:
			alModel = AL_INVERSE_DISTANCE_CLAMPED;
			break;
		case LinearDistance:
			alModel = AL_LINEAR_DISTANCE;
			break;
		case LinearDistanceClamped:
			alModel = AL_LINEAR_DISTANCE_CLAMPED;
			break;
		case ExponentDistance:
			alModel = AL_EXPONENT_DISTANCE;
			break;
		case ExponentDistanceClamped:
			alModel = AL_EXPONENT_DISTANCE_CLAMPED;
			break;
		}
		alDistanceModel(alModel);
	}
}