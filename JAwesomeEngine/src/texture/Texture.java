package texture;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import utils.DefaultValues;

public class Texture {
	int textureID, textureType;

	public Texture() {
		this.textureID = glGenTextures();
		this.textureType = DefaultValues.DEFAULT_TEXTURE_TYPE;
	}

	public Texture(int textureid) {
		this.textureID = textureid;
		this.textureType = DefaultValues.DEFAULT_TEXTURE_TYPE;
	}

	public Texture(int textureid, int texturetype) {
		this.textureID = textureid;
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

	public void setTextureID(int textureid) {
		this.textureID = textureid;
	}

	public void setTextureType(int texturetype) {
		this.textureType = texturetype;
	}

	public void unbind() {
		glBindTexture(textureType, 0);
	}
}
