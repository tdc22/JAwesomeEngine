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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL12;

public class TextureLoader {
	// from http://www.java-gaming.org/index.php?topic=25516.0
	public static int loadTexture(BufferedImage image, boolean hasalpha) {
		int width = image.getWidth();
		int height = image.getWidth();
		int bpp = 3; // 4 for RGBA, 3 for RGB
		if (hasalpha)
			bpp = 4;
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);

		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);

		if (hasalpha) {
			for (int y = 0; y < height; y++) {
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
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = pixels[y * width + x];
					buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
					buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
					buffer.put((byte) (pixel & 0xFF)); // Blue component
				}
			}
		}

		buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS

		int textureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, textureID);

		// Setup wrap mode
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		// Setup texture scaling filtering
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		// Send texel data to OpenGL
		if (hasalpha)
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA,
					GL_UNSIGNED_BYTE, buffer);
		else
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB8, width, height, 0, GL_RGB,
					GL_UNSIGNED_BYTE, buffer);

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

	/*
	 * private int checkifpow(int num) { float n = num; while(n > 1 && n % 1 ==
	 * 0) { n = n/2f; } if(n != 1) { n = 0; for(int i = 0; n < num; n++) {
	 * 
	 * } return num; } return num; }
	 */
}
