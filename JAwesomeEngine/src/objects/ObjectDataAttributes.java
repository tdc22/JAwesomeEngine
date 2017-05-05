package objects;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

public abstract class ObjectDataAttributes<datatype extends Object, buf extends Buffer> {
	int position, handle;
	boolean active;
	int datasize;
	public List<datatype> data;
	buf bufData;

	public ObjectDataAttributes(int position, int datasize, boolean active) {
		super();
		this.position = position;
		this.datasize = datasize;
		this.active = active;
		data = new ArrayList<datatype>();
	}

	public void updateData() {
		updateBuffer();
		bufData.flip();
	}

	protected abstract void updateBuffer();

	protected void pushBuffer() {
		handle = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, handle);
		glPushBuffer();
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	protected abstract void glPushBuffer();

	public void setActive(boolean active) {
		this.active = active;
		updateActive();
	}

	public void updateActive() {
		if (active) {
			glEnableVertexAttribArray(position);
		} else {
			glDisableVertexAttribArray(position);
		}
	}

	public int getPosition() {
		return position;
	}

	public int getHandle() {
		return handle;
	}

	public int getDatasize() {
		return datasize;
	}

	public boolean isActive() {
		return active;
	}

	public void delete() {
		deleteData();
		deleteGPUData();
	}

	public void deleteData() {
		data.clear();
	}

	public void deleteGPUData() {
		if (handle != 0) {
			glDeleteBuffers(handle);
		}
	}
}