package loader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import display.PixelFormat;

public class VideoSettingsLoader {
	protected static String getCleanString(String line) {
		return line.split(":")[1].replace(" ", "");
	}

	public static VideoSettingsOld load(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			try {
				save(new VideoSettingsOld(), file);
				System.out.println("Settings file created.");
				reader = new BufferedReader(new FileReader(file));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		VideoSettingsOld settings = new VideoSettingsOld();
		PixelFormat pixelformat = new PixelFormat();
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("Fullscreen"))
					settings.setFullscreen(Boolean.parseBoolean(getCleanString(line)));
				if (line.startsWith("Resolution X"))
					settings.setResolutionX(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("Resolution Y"))
					settings.setResolutionY(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("FOV Y"))
					settings.setFOVy(Float.parseFloat(getCleanString(line)));
				if (line.startsWith("ZNear"))
					settings.setZNear(Float.parseFloat(getCleanString(line)));
				if (line.startsWith("ZFar"))
					settings.setZFar(Float.parseFloat(getCleanString(line)));
				if (line.startsWith("VSync"))
					settings.setVSync(Boolean.parseBoolean(getCleanString(line)));

				if (line.startsWith("Bits per pixel"))
					pixelformat.withBitsPerPixel(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("Alpha"))
					pixelformat.withAlphaBits(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("Depth"))
					pixelformat.withDepthBits(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("Stencil"))
					pixelformat.withSamples(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("Samples"))
					pixelformat.withSamples(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("Auxiliary"))
					pixelformat.withAuxBuffers(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("Accumulation bits per pixel"))
					pixelformat.withAccumulationBitsPerPixel(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("Accumulation alpha"))
					pixelformat.withAccumulationAlpha(Integer.parseInt(getCleanString(line)));
				if (line.startsWith("Stereo"))
					pixelformat.withStereo(Boolean.parseBoolean(getCleanString(line)));
				if (line.startsWith("Floating point"))
					pixelformat.withFloatingPoint(Boolean.parseBoolean(getCleanString(line)));
			}
			reader.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		settings.setPixelFormat(pixelformat);

		return settings;
	}

	public static VideoSettingsOld load(String path) {
		return load(new File(path));
	}

	public static void save(VideoSettingsOld settings, File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		writer.write("Video Settings\n");
		writer.write("\n");
		writer.write("Resolution Settings:\n");
		writer.write("Fullscreen: " + settings.isFullscreen() + "\n");
		writer.write("Resolution X: " + settings.getResolutionX() + "\n");
		writer.write("Resolution Y: " + settings.getResolutionY() + "\n");
		writer.write("FOV Y: " + settings.getFOVy() + "\n");
		writer.write("ZNear: " + settings.getZNear() + "\n");
		writer.write("ZFar: " + settings.getZFar() + "\n");
		writer.write("VSync: " + settings.isVSync() + "\n");
		writer.write("\n");
		writer.write("Bit Settings:\n");
		writer.write("Bits per pixel: " + settings.getPixelFormat().getBitsPerPixel() + "\n");
		writer.write("Alpha: " + settings.getPixelFormat().getAlphaBits() + "\n");
		writer.write("Depth: " + settings.getPixelFormat().getDepthBits() + "\n");
		writer.write("Stencil: " + settings.getPixelFormat().getStencilBits() + "\n");
		writer.write("Samples: " + settings.getPixelFormat().getSamples() + "\n");
		writer.write("Auxiliary: " + settings.getPixelFormat().getAuxBuffers() + "\n");
		writer.write("Accumulation bits per pixel: " + settings.getPixelFormat().getAccumulationBitsPerPixel() + "\n");
		writer.write("Accumulation alpha: " + settings.getPixelFormat().getAccumulationAlpha() + "\n");
		writer.write("Stereo: " + settings.getPixelFormat().isStereo() + "\n");
		writer.write("Floating point: " + settings.getPixelFormat().isFloatingPoint() + "\n");

		writer.close();
	}

	public static void save(VideoSettingsOld settings, String path) {
		try {
			save(settings, new File(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
