package sound;

import static org.lwjgl.openal.AL10.AL_BUFFER;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alSourcePause;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceRewind;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourcei;

public class Sound {
	int sourcehandle, bufferhandle;

	public Sound(int bufferhandle) {
		sourcehandle = alGenSources();
		alSourcei(sourcehandle, AL_BUFFER, bufferhandle);
	}

	public void pause() {
		alSourcePause(sourcehandle);
	}

	public void play() {
		alSourcePlay(sourcehandle);
	}

	public void rewind() {
		alSourceRewind(sourcehandle);
	}

	public void stop() {
		alSourceStop(sourcehandle);
	}

	// private int getState() {
	//
	// }
	//
	// public void isPlaying() {
	//
	// }
}