package objects;

import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class ObjectDataAttributesInteger extends ObjectDataAttributes<Integer, IntBuffer> {
	boolean index;
	int buffertype;

	public ObjectDataAttributesInteger(int position, int datasize, boolean index, boolean active) {
		super(position, datasize, active);
		this.index = index;
		if (index) {
			buffertype = GL_ELEMENT_ARRAY_BUFFER;
		}
	}

	public boolean isIndex() {
		return index;
	}

	@Override
	protected void pushBuffer() {
		handle = glGenBuffers();
		glBindBuffer(buffertype, handle);
		glPushBuffer();
		glBindBuffer(buffertype, 0);
	}

	@Override
	protected void updateBuffer() {
		int dataCount = data.size();
		bufData = BufferUtils.createIntBuffer(dataCount);
		for (int v = 0; v < dataCount; v++) {
			bufData.put(data.get(v));
		}
	}

	@Override
	protected void glPushBuffer() {
		glBufferData(buffertype, bufData, GL_STATIC_DRAW);
		if (!index) {
			glVertexAttribPointer(position, datasize, GL_INT, false, 0, 0);
		}
	}
}