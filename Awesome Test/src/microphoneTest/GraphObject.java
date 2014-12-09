package microphoneTest;

import java.awt.Color;

import objects.ShapedObject2;
import vector.Vector2f;

public abstract class GraphObject extends ShapedObject2 implements
		MicrophoneDataProcessor {
	int[] values;
	final int vallength = 1100000;
	int currlength = 0;
	int samplesize;
	boolean newvalues = false;

	public GraphObject() {
		values = new int[vallength];
		this.setRenderMode(GL11.GL_LINE_STRIP);
		this.prerender();
	}

	@Override
	public void format(MicrophoneSettings settings) {
		samplesize = settings.getSampleSizeInBits();
	}

	public void updateGraph() {
		if (newvalues) {
			newvalues = false;
			this.deleteData();
			for (int i = 0; i < currlength; i++) {
				this.addVertex(new Vector2f(i / 600f, values[i] * 2f),
						Color.GRAY, new Vector2f());
				this.addIndex(i);
			}
			this.prerender();
		}
	}
}
