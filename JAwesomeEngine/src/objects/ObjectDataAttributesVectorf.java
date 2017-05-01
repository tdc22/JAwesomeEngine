package objects;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vector.Vector;

public class ObjectDataAttributesVectorf<v extends Vector> extends ObjectDataAttributes<v, FloatBuffer> {
	float[] fillNumbers;

	public ObjectDataAttributesVectorf(int position, int datasize, float[] fillNumbers, boolean active) {
		super(position, datasize, active);
		this.fillNumbers = fillNumbers;
	}

	@Override
	protected void updateBuffer() {
		int dataCount = data.size();
		bufData = BufferUtils.createFloatBuffer(dataCount * datasize);
		if (dataCount > 0) {
			int dataDimensions = data.get(0).getDimensions();
			for (int v = 0; v < dataCount; v++) {
				v vector = data.get(v);
				for (int i = 0; i < dataDimensions; i++) {
					bufData.put(vector.getf(i));
				}
				bufData.put(fillNumbers);
			}
		}
	}

	@Override
	protected void glPushBuffer() {
		glBufferData(GL_ARRAY_BUFFER, bufData, GL_STATIC_DRAW);
		glVertexAttribPointer(position, datasize, GL_FLOAT, false, 0, 0);
	}
}