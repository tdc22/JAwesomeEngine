package objects;

import java.awt.Color;

import vector.Vector2f;
import vector.Vector3f;

public class SimpleBoxVoxelObject extends ShapedObject3 {
	int[][][] data;

	public SimpleBoxVoxelObject(int sizex, int sizey, int sizez) {
		data = new int[sizex][sizey][sizez];
	}

	public SimpleBoxVoxelObject(int[][][] data) {
		this.data = data;
	}

	public void resetData() {
		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data[x].length; y++) {
				for (int z = 0; z < data[x][y].length; z++) {
					data[x][y][z] = 0;
				}
			}
		}
	}

	public void set(int x, int y, int z, int value) {
		data[x][y][z] = value;
	}

	public void setData(int[][][] data) {
		this.data = data;
	}

	public void updateShapes() {
		this.deleteData();
		int[][][] indicesFrontBack = new int[data.length + 1][data[0].length + 1][data[0][0].length + 1];
		int[][][] indicesUpDown = new int[data.length + 1][data[0].length + 1][data[0][0].length + 1];
		int[][][] indicesLeftRight = new int[data.length + 1][data[0].length + 1][data[0][0].length + 1];

		Color color = Color.GRAY;
		Vector2f a = new Vector2f(0, 0);
		Vector2f b = new Vector2f(1, 0);
		Vector2f c = new Vector2f(1, 1);
		Vector2f d = new Vector2f(0, 1);

		Vector3f down = new Vector3f(0, -1, 0);
		Vector3f back = new Vector3f(0, 0, -1);
		Vector3f left = new Vector3f(-1, 0, 0);
		Vector3f front = new Vector3f(0, 0, 1);
		Vector3f right = new Vector3f(1, 0, 0);
		Vector3f up = new Vector3f(0, 1, 0);

		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data[x].length; y++) {
				for (int z = 0; z < data[x][y].length; z++) {

					if (data[x][y][z] != 0) {

						// left
						if (x == 0) {
							addVertex(new Vector3f(x, y, z), color, a, left);
							indicesLeftRight[x][y][z] = getVertexCount() - 1;
							addVertex(new Vector3f(x, y, z + 1), color, b, left);
							indicesLeftRight[x][y][z + 1] = getVertexCount() - 1;
							addVertex(new Vector3f(x, y + 1, z + 1), color, c, left);
							indicesLeftRight[x][y + 1][z + 1] = getVertexCount() - 1;
							addVertex(new Vector3f(x, y + 1, z), color, d, left);
							indicesLeftRight[x][y + 1][z] = getVertexCount() - 1;
						} else {
							if (data[x - 1][y][z] == 0) {
								addVertex(new Vector3f(x, y, z), color, a, left);
								indicesLeftRight[x][y][z] = getVertexCount() - 1;
								addVertex(new Vector3f(x, y, z + 1), color, b, left);
								indicesLeftRight[x][y][z + 1] = getVertexCount() - 1;
								addVertex(new Vector3f(x, y + 1, z + 1), color, c, left);
								indicesLeftRight[x][y + 1][z + 1] = getVertexCount() - 1;
								addVertex(new Vector3f(x, y + 1, z), color, d, left);
								indicesLeftRight[x][y + 1][z] = getVertexCount() - 1;
							}
						}
						// right
						if (x == data.length - 1) {
							addVertex(new Vector3f(x + 1, y, z), color, a, right);
							indicesLeftRight[x + 1][y][z] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y + 1, z), color, d, right);
							indicesLeftRight[x + 1][y + 1][z] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y + 1, z + 1), color, c, right);
							indicesLeftRight[x + 1][y + 1][z + 1] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y, z + 1), color, b, right);
							indicesLeftRight[x + 1][y][z + 1] = getVertexCount() - 1;
						} else {
							if (data[x + 1][y][z] == 0) {
								addVertex(new Vector3f(x + 1, y, z), color, a, right);
								indicesLeftRight[x + 1][y][z] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y + 1, z), color, d, right);
								indicesLeftRight[x + 1][y + 1][z] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y + 1, z + 1), color, c, right);
								indicesLeftRight[x + 1][y + 1][z + 1] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y, z + 1), color, b, right);
								indicesLeftRight[x + 1][y][z + 1] = getVertexCount() - 1;
							}
						}

						// down
						if (y == 0) {
							addVertex(new Vector3f(x, y, z), color, a, down);
							indicesUpDown[x][y][z] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y, z), color, b, down);
							indicesUpDown[x + 1][y][z] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y, z + 1), color, c, down);
							indicesUpDown[x + 1][y][z + 1] = getVertexCount() - 1;
							addVertex(new Vector3f(x, y, z + 1), color, d, down);
							indicesUpDown[x][y][z + 1] = getVertexCount() - 1;
						} else {
							if (data[x][y - 1][z] == 0) {
								addVertex(new Vector3f(x, y, z), color, a, down);
								indicesUpDown[x][y][z] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y, z), color, b, down);
								indicesUpDown[x + 1][y][z] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y, z + 1), color, c, down);
								indicesUpDown[x + 1][y][z + 1] = getVertexCount() - 1;
								addVertex(new Vector3f(x, y, z + 1), color, d, down);
								indicesUpDown[x][y][z + 1] = getVertexCount() - 1;
							}
						}
						// up
						if (y == data[0].length - 1) {
							addVertex(new Vector3f(x, y + 1, z), color, a, up);
							indicesUpDown[x][y + 1][z] = getVertexCount() - 1;
							addVertex(new Vector3f(x, y + 1, z + 1), color, d, up);
							indicesUpDown[x][y + 1][z + 1] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y + 1, z + 1), color, c, up);
							indicesUpDown[x + 1][y + 1][z + 1] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y + 1, z), color, b, up);
							indicesUpDown[x + 1][y + 1][z] = getVertexCount() - 1;
						} else {
							if (data[x][y + 1][z] == 0) {
								addVertex(new Vector3f(x, y + 1, z), color, a, up);
								indicesUpDown[x][y + 1][z] = getVertexCount() - 1;
								addVertex(new Vector3f(x, y + 1, z + 1), color, d, up);
								indicesUpDown[x][y + 1][z + 1] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y + 1, z + 1), color, c, up);
								indicesUpDown[x + 1][y + 1][z + 1] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y + 1, z), color, b, up);
								indicesUpDown[x + 1][y + 1][z] = getVertexCount() - 1;
							}
						}

						// back
						if (z == 0) {
							addVertex(new Vector3f(x, y, z), color, a, back);
							indicesFrontBack[x][y][z] = getVertexCount() - 1;
							addVertex(new Vector3f(x, y + 1, z), color, d, back);
							indicesFrontBack[x][y + 1][z] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y + 1, z), color, c, back);
							indicesFrontBack[x + 1][y + 1][z] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y, z), color, b, back);
							indicesFrontBack[x + 1][y][z] = getVertexCount() - 1;
						} else {
							if (data[x][y][z - 1] == 0) {
								addVertex(new Vector3f(x, y, z), color, a, back);
								indicesFrontBack[x][y][z] = getVertexCount() - 1;
								addVertex(new Vector3f(x, y + 1, z), color, d, back);
								indicesFrontBack[x][y + 1][z] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y + 1, z), color, c, back);
								indicesFrontBack[x + 1][y + 1][z] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y, z), color, b, back);
								indicesFrontBack[x + 1][y][z] = getVertexCount() - 1;
							}
						}
						// front
						if (z == data.length - 1) {
							addVertex(new Vector3f(x, y, z + 1), color, a, front);
							indicesFrontBack[x][y][z + 1] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y, z + 1), color, b, front);
							indicesFrontBack[x + 1][y][z + 1] = getVertexCount() - 1;
							addVertex(new Vector3f(x + 1, y + 1, z + 1), color, c, front);
							indicesFrontBack[x + 1][y + 1][z + 1] = getVertexCount() - 1;
							addVertex(new Vector3f(x, y + 1, z + 1), color, d, front);
							indicesFrontBack[x][y + 1][z + 1] = getVertexCount() - 1;
						} else {
							if (data[x][y][z + 1] == 0) {
								addVertex(new Vector3f(x, y, z + 1), color, a, front);
								indicesFrontBack[x][y][z + 1] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y, z + 1), color, b, front);
								indicesFrontBack[x + 1][y][z + 1] = getVertexCount() - 1;
								addVertex(new Vector3f(x + 1, y + 1, z + 1), color, c, front);
								indicesFrontBack[x + 1][y + 1][z + 1] = getVertexCount() - 1;
								addVertex(new Vector3f(x, y + 1, z + 1), color, d, front);
								indicesFrontBack[x][y + 1][z + 1] = getVertexCount() - 1;
							}
						}
					}
				}
			}
		}

		for (int x = 0; x < data.length; x++) {
			for (int y = 0; y < data[x].length; y++) {
				for (int z = 0; z < data[x][y].length; z++) {

					if (data[x][y][z] != 0) {

						// left
						if (x == 0) {
							addQuad(indicesLeftRight[x][y][z], 0, indicesLeftRight[x][y][z + 1], 0,
									indicesLeftRight[x][y + 1][z + 1], 0, indicesLeftRight[x][y + 1][z], 0);
						} else {
							if (data[x - 1][y][z] == 0) {
								addQuad(indicesLeftRight[x][y][z], 0, indicesLeftRight[x][y][z + 1], 0,
										indicesLeftRight[x][y + 1][z + 1], 0, indicesLeftRight[x][y + 1][z], 0);
							}
						}
						// right
						if (x == data.length - 1) {
							addQuad(indicesLeftRight[x + 1][y][z], 0, indicesLeftRight[x + 1][y + 1][z], 0,
									indicesLeftRight[x + 1][y + 1][z + 1], 0, indicesLeftRight[x + 1][y][z + 1], 0);
						} else {
							if (data[x + 1][y][z] == 0) {
								addQuad(indicesLeftRight[x + 1][y][z], 0, indicesLeftRight[x + 1][y + 1][z], 0,
										indicesLeftRight[x + 1][y + 1][z + 1], 0, indicesLeftRight[x + 1][y][z + 1], 0);
							}
						}

						// down
						if (y == 0) {
							addQuad(indicesUpDown[x][y][z], 0, indicesUpDown[x + 1][y][z], 0,
									indicesUpDown[x + 1][y][z + 1], 0, indicesUpDown[x][y][z + 1], 0);
						} else {
							if (data[x][y - 1][z] == 0) {
								addQuad(indicesUpDown[x][y][z], 0, indicesUpDown[x + 1][y][z], 0,
										indicesUpDown[x + 1][y][z + 1], 0, indicesUpDown[x][y][z + 1], 0);
							}
						}
						// up
						if (y == data[0].length - 1) {
							addQuad(indicesUpDown[x][y + 1][z], 0, indicesUpDown[x][y + 1][z + 1], 0,
									indicesUpDown[x + 1][y + 1][z + 1], 0, indicesUpDown[x + 1][y + 1][z], 0);
						} else {
							if (data[x][y + 1][z] == 0) {
								addQuad(indicesUpDown[x][y + 1][z], 0, indicesUpDown[x][y + 1][z + 1], 0,
										indicesUpDown[x + 1][y + 1][z + 1], 0, indicesUpDown[x + 1][y + 1][z], 0);
							}
						}

						// back
						if (z == 0) {
							addQuad(indicesFrontBack[x][y][z], 0, indicesFrontBack[x][y + 1][z], 0,
									indicesFrontBack[x + 1][y + 1][z], 0, indicesFrontBack[x + 1][y][z], 0);
						} else {
							if (data[x][y][z - 1] == 0) {
								addQuad(indicesFrontBack[x][y][z], 0, indicesFrontBack[x][y + 1][z], 0,
										indicesFrontBack[x + 1][y + 1][z], 0, indicesFrontBack[x + 1][y][z], 0);
							}
						}
						// front
						if (z == data.length - 1) {
							addQuad(indicesFrontBack[x][y][z + 1], 0, indicesFrontBack[x + 1][y][z + 1], 0,
									indicesFrontBack[x + 1][y + 1][z + 1], 0, indicesFrontBack[x][y + 1][z + 1], 0);
						} else {
							if (data[x][y][z + 1] == 0) {
								addQuad(indicesFrontBack[x][y][z + 1], 0, indicesFrontBack[x + 1][y][z + 1], 0,
										indicesFrontBack[x + 1][y + 1][z + 1], 0, indicesFrontBack[x][y + 1][z + 1], 0);
							}
						}
					}
				}
			}
		}
		this.prerender();
	}
}
