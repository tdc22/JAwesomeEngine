package objects;

import java.nio.FloatBuffer;

public interface Camera {
	public void delete();

	public FloatBuffer getMatrixBuffer();
}
