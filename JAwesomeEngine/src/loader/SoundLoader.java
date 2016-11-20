package loader;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_close;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_info;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_get_samples_short_interleaved;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_open_memory;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_stream_length_in_samples;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.stb.STBVorbisInfo;

public class SoundLoader {
	public static int loadSound(File file) {
		STBVorbisInfo info = STBVorbisInfo.malloc();
		ByteBuffer vorbis;
		try {
			vorbis = loadSoundToByteBuffer(file, (int) file.length());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		IntBuffer error = BufferUtils.createIntBuffer(1);
		long decoder = stb_vorbis_open_memory(vorbis, error, null);
		if (decoder == NULL)
			throw new RuntimeException(
					"Failed to open Ogg Vorbis file. Error: " + error.get(0));

		stb_vorbis_get_info(decoder, info);

		int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);

		ShortBuffer pcm = BufferUtils.createShortBuffer(info.channels()
				* lengthSamples);

		stb_vorbis_get_samples_short_interleaved(decoder, info.channels(), pcm);
		stb_vorbis_close(decoder);
		int buffer = alGenBuffers();
		alBufferData(buffer, info.channels() == 2 ? AL10.AL_FORMAT_STEREO16
				: AL10.AL_FORMAT_MONO16, pcm, info.sample_rate());

		return buffer;
	}

	public static int loadSound(String path) {
		return loadSound(new File(path));
	}

	private static ByteBuffer loadSoundToByteBuffer(File file, int bufferSize)
			throws IOException {
		ByteBuffer buffer;

		Path path = file.toPath();
		if (Files.isReadable(path)) {
			try (SeekableByteChannel fc = Files.newByteChannel(path)) {
				buffer = BufferUtils.createByteBuffer((int) fc.size());
				while (buffer.capacity() > buffer.position())
					fc.read(buffer);
			}
		} else {
			try (InputStream source = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(file.getPath());
					ReadableByteChannel rbc = Channels.newChannel(source)) {
				buffer = createByteBuffer(bufferSize);

				while (true) {
					int bytes = rbc.read(buffer);
					if (bytes == -1)
						break;
					if (buffer.remaining() == 0)
						return null;
				}
			}
		}

		buffer.flip();
		return buffer;
	}
}
