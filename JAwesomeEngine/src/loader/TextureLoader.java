package loader;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGB8;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_BASE_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

public class TextureLoader {
	public static int loadCubeMap(BufferedImage top, BufferedImage bottom, BufferedImage front, BufferedImage back,
			BufferedImage left, BufferedImage right, boolean hasalpha) {
		int width = top.getWidth();
		int height = top.getHeight();
		int bpp = 3; // 4 for RGBA, 3 for RGB
		if (hasalpha)
			bpp = 4;

		ByteBuffer bufferTop = loadTextureIntoBuffer(top, width, height, bpp, hasalpha);
		ByteBuffer bufferBottom = loadTextureIntoBuffer(bottom, width, height, bpp, hasalpha);
		ByteBuffer bufferFront = loadTextureIntoBuffer(front, width, height, bpp, hasalpha);
		ByteBuffer bufferBack = loadTextureIntoBuffer(back, width, height, bpp, hasalpha);
		ByteBuffer bufferLeft = loadTextureIntoBuffer(left, width, height, bpp, hasalpha);
		ByteBuffer bufferRight = loadTextureIntoBuffer(right, width, height, bpp, hasalpha);

		int cubemapID = glGenTextures();
		glBindTexture(GL_TEXTURE_CUBE_MAP, cubemapID);

		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_BASE_LEVEL, 0);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAX_LEVEL, 0);

		int format = GL_RGB;
		int internalformat = GL_RGB;
		if (hasalpha) {
			format = GL_RGBA;
			internalformat = GL_RGBA;
		}

		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, internalformat, width, height, 0, format, GL_UNSIGNED_BYTE,
				bufferTop);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, internalformat, width, height, 0, format, GL_UNSIGNED_BYTE,
				bufferBottom);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, internalformat, width, height, 0, format, GL_UNSIGNED_BYTE,
				bufferFront);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, internalformat, width, height, 0, format, GL_UNSIGNED_BYTE,
				bufferBack);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, internalformat, width, height, 0, format, GL_UNSIGNED_BYTE,
				bufferLeft);
		glTexImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, internalformat, width, height, 0, format, GL_UNSIGNED_BYTE,
				bufferRight);

		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

		return cubemapID;
	}

	public static int loadCubeMap(File fileTop, File fileBottom, File fileFront, File fileBack, File fileLeft,
			File fileRight, boolean hasalpha) {
		int cubemap = 0;
		try {
			cubemap = loadCubeMap(ImageIO.read(fileTop), ImageIO.read(fileBottom), ImageIO.read(fileFront),
					ImageIO.read(fileBack), ImageIO.read(fileLeft), ImageIO.read(fileRight), hasalpha);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return cubemap;
	}

	public static int loadCubeMap(String pathTop, String pathBottom, String pathFront, String pathBack, String pathLeft,
			String pathRight) {
		boolean hasalpha = true;
		String p = pathTop.toLowerCase();
		if (p.endsWith("jpg") || p.endsWith("jpeg"))
			hasalpha = false;
		return loadCubeMap(new File(pathTop), new File(pathBottom), new File(pathFront), new File(pathBack),
				new File(pathLeft), new File(pathRight), hasalpha);
	}

	public static int loadCubeMap(String pathTop, String pathBottom, String pathFront, String pathBack, String pathLeft,
			String pathRight, boolean hasalpha) {
		return loadCubeMap(new File(pathTop), new File(pathBottom), new File(pathFront), new File(pathBack),
				new File(pathLeft), new File(pathRight), hasalpha);
	}

	// from http://www.java-gaming.org/index.php?topic=25516.0
	public static int loadTexture(BufferedImage image, boolean hasalpha) {
		int width = image.getWidth();
		int height = image.getHeight();
		int bpp = 3; // 4 for RGBA, 3 for RGB
		if (hasalpha)
			bpp = 4;

		ByteBuffer buffer = loadTextureIntoBuffer(image, width, height, bpp, hasalpha);

		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);

		// Setup wrap mode
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		// Setup texture scaling filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		// Send texel data to OpenGL
		if (hasalpha)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		else
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, buffer);

		glBindTexture(GL_TEXTURE_2D, 0);

		return textureID;
	}

	public static int loadTexture(File file, boolean hasalpha) {
		int texture = 0;
		try {
			texture = loadTexture(ImageIO.read(file), hasalpha);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return texture;
	}

	public static int loadTexture(String path) {
		boolean hasalpha = true;
		String p = path.toLowerCase();
		if (p.endsWith("jpg") || p.endsWith("jpeg"))
			hasalpha = false;
		return loadTexture(new File(path), hasalpha);
	}

	public static int loadTexture(String path, boolean hasalpha) {
		return loadTexture(new File(path), hasalpha);
	}

	private static ByteBuffer loadTextureIntoBuffer(BufferedImage image, int width, int height, int bpp,
			boolean hasalpha) {
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);

		if (hasalpha) {
			for (int y = (height - 1); y >= 0; y--) {
				for (int x = 0; x < width; x++) {
					int pixel = pixels[y * width + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
					buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
					buffer.put((byte) (pixel & 0xFF)); // Blue component
					buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
																// component
				}
			}
		} else {
			for (int y = (height - 1); y >= 0; y--) {
				for (int x = 0; x < width; x++) {
					int pixel = pixels[y * width + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
					buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
					buffer.put((byte) (pixel & 0xFF)); // Blue component
				}
			}
		}

		buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS
		return buffer;
	}
}
