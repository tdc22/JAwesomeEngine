package terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import math.VecMath;
import objects.ShapedObject3;
import vector.Vector2f;
import vector.Vector3f;

public class Terrain extends ShapedObject3 {
	int terrainsizex, terrainsizey;
	float maxheight, minheight, halfheightdiff;

	public Terrain(BufferedImage heightmap) {
		super();
		setHeightmap(heightmap);
	}

	public Terrain(float[][] heightmap) {
		super();
		setHeightmap(heightmap);
	}

	public float getHeightDifference() {
		return halfheightdiff;
	}

	public float getMaxHeight() {
		return maxheight;
	}

	public float getMinHeight() {
		return minheight;
	}

	public int getTerrainSizeX() {
		return terrainsizex;
	}

	public int getTerrainSizeY() {
		return terrainsizex;
	}

	private void init(int sizex, int sizey) {
		shapetype = SHAPE_TERRAIN;
		int pos = 0;
		for (int x = 0; x < sizex - 1; x++) {
			for (int z = 0; z < sizey - 1; z++) {
				addQuad(pos, 0, pos + 1, 0, pos + sizey + 1, 0, pos + sizey, 0); // Adjs
																					// stimmen
																					// noch
																					// nicht!
				pos++;
			}
			pos++;
		}

		List<Vector3f> verts = this.getVertices();
		minheight = verts.get(0).y;
		maxheight = verts.get(0).y;
		for (int v = 0; v < verts.size(); v++) {
			float h = verts.get(v).y;
			if (h < minheight)
				minheight = h;
			if (h > maxheight)
				maxheight = h;
		}
		halfheightdiff = (maxheight - minheight) / 2f;
		System.out
				.println(halfheightdiff + "; " + minheight + "; " + maxheight);
		Vector3f size = new Vector3f(terrainsizex - 1, halfheightdiff,
				terrainsizey - 1);
		for (int v = 0; v < verts.size(); v++) {
			verts.set(v, VecMath.subtraction(verts.get(v),
					VecMath.scale(size, 0.5f)));
		}

		this.prerender();
	}

	public void setHeightmap(BufferedImage heightmap) {
		terrainsizex = heightmap.getWidth();
		terrainsizey = heightmap.getHeight();
		for (int x = 0; x < terrainsizex; x++) {
			for (int y = 0; y < terrainsizey; y++) {
				float h = new Color(heightmap.getRGB(x, y)).getRed() / 100f;
				addVertex(new Vector3f(x, h, y), Color.GRAY,
						new Vector2f(0, 0), new Vector3f(0, 1, 0));
			}
		}
		init(terrainsizex, terrainsizey);
	}

	public void setHeightmap(float[][] heightmap) {
		terrainsizex = heightmap.length;
		terrainsizey = heightmap[0].length;
		for (int x = 0; x < terrainsizex; x++) {
			for (int y = 0; y < terrainsizey; y++) {
				float h = heightmap[x][y];
				addVertex(new Vector3f(x, h, y), Color.GRAY,
						new Vector2f(0, 0), new Vector3f(0, 1, 0));
			}
		}
		init(terrainsizex, terrainsizey);
	}
}
