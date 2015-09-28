package objects;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import vector.Vector2f;
import vector.Vector3f;

public class ShapedObject2 extends ShapedObject {

	public ShapedObject2() {
		super();
		setRenderMode(GL11.GL_TRIANGLES);
	}

	public ShapedObject2(float x, float y) {
		super();
		translateTo(x, y);
		setRenderMode(GL11.GL_TRIANGLES);
	}

	public ShapedObject2(Vector2f pos) {
		super();
		translateTo(pos);
		setRenderMode(GL11.GL_TRIANGLES);
	}

	public void addQuad(int index1, int index2, int index3, int index4) {
		addTriangle(index1, index2, index3);
		addTriangle(index1, index3, index4);
	}

	public void addTriangle(int index1, int index2, int index3) {
		addIndex(index1);
		addIndex(index2);
		addIndex(index3);
	}

	public void addVertex(Vector2f vertex) {
		addVertex(new Vector3f(vertex.x, vertex.y, 0f));
	}

	public void addVertex(Vector2f vertex, Color c) {
		addVertex(new Vector3f(vertex.x, vertex.y, 0f), c);
	}

	public void addVertex(Vector2f vertex, Color c, Vector2f texturecoord) {
		addVertex(new Vector3f(vertex.x, vertex.y, 0f), c, texturecoord, new Vector3f(0, 0, 1));
	}

	public void addVertex(Vector2f vertex, Vector3f c, Vector2f texturecoord) {
		addVertex(new Vector3f(vertex.x, vertex.y, 0f), c, texturecoord, new Vector3f(0, 0, 1));
	}

	public void setVertex(int id, Vector2f vertex) {
		setVertex(id, new Vector3f(vertex.x, vertex.y, 0f));
	}

	public void setVertex(int id, Vector2f vertex, Color c) {
		setVertex(id, new Vector3f(vertex.x, vertex.y, 0f), c);
	}

	public void setVertex(int id, Vector2f vertex, Color c, Vector2f texturecoord) {
		setVertex(id, new Vector3f(vertex.x, vertex.y, 0f), c, texturecoord, new Vector3f(0, 0, 1));
	}

	public void setVertex(int id, Vector2f vertex, Vector3f c, Vector2f texturecoord) {
		setVertex(id, new Vector3f(vertex.x, vertex.y, 0f), c, texturecoord, new Vector3f(0, 0, 1));
	}

	// @Override
	// public void translate(float x, float y) {
	// translate(x, y, 0);
	// }
	//
	// public void translate(Vector2f trans) {
	// translate(new Vector3f(trans.x, trans.y, 0f));
	// }
	//
	// @Override
	// public void translateTo(float x, float y) {
	// translateTo(x, y, 0);
	// }
	//
	// public void translateTo(Vector2f trans) {
	// translateTo(new Vector3f(trans.x, trans.y, 0f));
	// }
	//
	// public void rotate(float angle) {
	// rotate(0, 0, angle);
	// }

	// public void rotate(float angle) {
	// rotate(angle, 0, 0);
	// }
	//
	// public void rotateTo(float angle) {
	// rotateTo(angle, 0, 0);
	// }
}
