package texture;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;

public class Texture {
	int textureID, textureType;

	public Texture() {
		this.textureID = glGenTextures();
		this.textureType = GL_TEXTURE_2D;
	}

	public Texture(int textureid) {
		this.textureID = textureid;
		this.textureType = GL_TEXTURE_2D;
	}

	public Texture(int textureid, int texturetype) {
		this.textureID = textureid;
		this.textureType = texturetype;
	}

	public void setTextureType(int texturetype) {
		this.textureType = texturetype;
	}

	public void bind() {
		glBindTexture(textureType, textureID);
	}

	public void delete() {
		glDeleteTextures(textureID);
	}

	public int getTextureID() {
		return textureID;
	}

	public int getTextureType() {
		return textureType;
	}

	public void unbind() {
		glBindTexture(textureType, 0);
	}
}
