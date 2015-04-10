package texture;

import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;

public class CubeMap extends Texture {
	public CubeMap() {
		super();
		textureType = GL_TEXTURE_CUBE_MAP;
	}

	public CubeMap(int textureid) {
		super(textureid);
		textureType = GL_TEXTURE_CUBE_MAP;
	}
}