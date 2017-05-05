package objects;

import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL30.glVertexAttribIPointer;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class ObjectDataAttributesVectori extends ObjectDataAttributes<Integer[], IntBuffer> {
	int[] fillNumbers;

	public ObjectDataAttributesVectori(int position, int datasize, int[] fillNumbers, boolean active) {
		super(position, datasize, active);
		this.fillNumbers = fillNumbers;
	}

	@Override
	protected void updateBuffer() {
		int dataCount = data.size();
		bufData = BufferUtils.createIntBuffer(dataCount * datasize);
		if (dataCount > 0) {
			int dataDimensions = data.get(0).length;
			for (int v = 0; v < dataCount; v++) {
				Integer[] vector = data.get(v);
				for (int i = 0; i < dataDimensions; i++) {
					bufData.put(vector[i]);
				}
				bufData.put(fillNumbers);
			}
		}
	}

	@Override
	protected void glPushBuffer() {
		glBufferData(GL_ARRAY_BUFFER, bufData, GL_STATIC_DRAW);
		glVertexAttribIPointer(position, datasize, GL_INT, 0, 0);
	}

}
