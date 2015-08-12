package terrain;

import static org.lwjgl.opengl.GL11.glMultMatrixf;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTranslatef;

import java.awt.Color;
import java.awt.image.BufferedImage;

import math.QuatMath;
import math.VecMath;
import objects.RenderedObject;
import objects.ShapedObject;
import quaternion.Quaternion;
import shader.Shader;
import vector.Vector2f;
import vector.Vector3f;

class Chunk extends ShapedObject {
	int posx, posy;
	int sizex, sizey;
	Vector3f center;

	int lod = 5;
	boolean lodchanged;
	boolean smoothed = false;

	Chunk(int x, int y, int sx, int sy) {
		posx = x;
		posy = y;
		sizex = sx;
		sizey = sy;

		translate(posx * (sizex - 1), 0, posy * (sizey - 1));
	}

	public void chunkrender() {
		if (lod != 0) {
			render();
		}
	}

	public void computeCenter(Quaternion rotation, Vector3f translation) {
		center = new Vector3f(posx * (sizex - 1) + sizex / 2f, 0, posy * (sizey - 1) + sizey / 2f);
		Vector3f transformed = new Vector3f(center.x, center.y, center.z);
		QuatMath.transform(rotation, transformed);
		transformed.translate(translation);
		center = new Vector3f(transformed.x, transformed.y, transformed.z);
	}

	public Vector3f getCenter() {
		return center;
	}

	public int getLOD() {
		return lod;
	}

	public Vector2f getPosition() {
		return new Vector2f(posx, posy);
	}

	public boolean isSmoothed() {
		return smoothed;
	}

	public boolean LODchanged() {
		return lodchanged;
	}

	public void setHeightmap(float[][] heightmap) {
		deleteData();
		int sx = heightmap.length;
		int sy = heightmap[0].length;
		for (int vx = 0; vx < sx; vx++) {
			for (int vz = 0; vz < sy; vz++) {
				addVertex(new Vector3f(vx, heightmap[vx][vz], vz), Color.GRAY, new Vector2f(0, 0),
						new Vector3f(0, 1, 0));
			}
		}
		int pos = 0;
		for (int rx = 0; rx < sx - 1; rx++) {
			for (int rz = 0; rz < sy - 1; rz++) {
				addQuad(pos, 0, pos + 1, 0, pos + sy + 1, 0, pos + sy, 0);
				pos++;
			}
			pos++;
		}
	}

	public void setLOD(int lod) {
		if (this.lod != lod) {
			lodchanged = true;
			this.lod = lod;
		} else {
			lodchanged = false;
		}
	}

	public void setSmoothed(boolean s) {
		smoothed = s;
	}
}

/**
 * @deprecated
 * @see #meineNeueMethode
 */
@Deprecated
public class ChunkedTerrain extends RenderedObject {
	int chunksizex = 64, chunksizey = 64;
	Chunk[][] chunks;
	BufferedImage heightmap;
	Shader shader;

	float lod1 = 160, lod2 = 80, lod3 = 40, lod4 = 20, lod5 = 10;

	public ChunkedTerrain(BufferedImage heightmap) {
		this.heightmap = heightmap;

		int width = heightmap.getWidth();
		int height = heightmap.getHeight();
		int anzx = (int) Math.floor(width / (float) chunksizex);
		int anzy = (int) Math.floor(height / (float) chunksizey);
		// eins fehlt dann noch wegen abrunden!
		chunks = new Chunk[anzx][anzy];
		for (int cx = 0; cx < anzx; cx++) {
			for (int cy = 0; cy < anzy; cy++) {
				chunks[cx][cy] = new Chunk(cx, cy, chunksizex + 1, chunksizey + 1);
			}
		}
	}

	private int computeLOD(Vector3f chunkcenter, Vector3f campos) {
		float distance = VecMath.length(VecMath.subtraction(chunkcenter, campos));
		if (distance <= lod1) {
			if (distance <= lod5) {
				return 5;
			}
			if (distance <= lod4) {
				return 4;
			}
			if (distance <= lod3) {
				return 3;
			}
			if (distance <= lod2) {
				return 2;
			}
			return 1;
		}
		return 0;
	}

	// 2er-Potenz fuer Skalierung, -4 <= exp <= +4
	private float exp2(int exp) {
		int val = 1;
		switch (exp) {
		case 1:
		case -1:
			val = 2;
			break;
		case 2:
		case -2:
			val = 4;
			break;
		case 3:
		case -3:
			val = 8;
			break;
		case 4:
		case -4:
			val = 16;
			break;
		case 5:
		case -5:
			val = 32;
			break;
		}
		if (exp >= 0)
			return val;
		else
			return (1 / (float) val);
	}

	public ShapedObject[][] getChunks() {
		return chunks;
	}

	private float getHeight(int x, int y, int chunkx, int chunky, int stepsize) {
		int imgx = (x * stepsize) + (chunkx * chunksizex);
		int imgy = (y * stepsize) + (chunky * chunksizey);
		return new Color(heightmap.getRGB(imgx, imgy)).getRed() / 10f;
	}

	private float[][] getHeights(int chunklod, int chunkx, int chunky, int stepsize) {
		int w = (int) (chunksizex / (float) stepsize) + 1;
		int h = (int) (chunksizey / (float) stepsize) + 1;
		float[][] heights = new float[w][h];
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				heights[x][y] = getHeight(x, y, chunkx, chunky, stepsize);
			}
		}

		return heights;
	}

	private int getStepsize(int chunklod) {
		switch (chunklod) {
		case 5:
			return 1;
		case 4:
			return 2;
		case 3:
			return 4;
		case 2:
			return 8;
		case 1:
			return 16;
		}
		return 0;
	}

	private float mid(float val1, float val2) {
		return (val1 + val2) / 2f;
	}

	@Override
	public void render() {
		glPushMatrix();

		if (shader != null)
			shader.bind();

		glTranslatef(rotcenter.x, rotcenter.y, rotcenter.z);
		glMultMatrixf(buf);
		glTranslatef(-rotcenter.x, -rotcenter.y, -rotcenter.z);

		for (int chx = 0; chx < chunks.length; chx++) {
			for (int chy = 0; chy < chunks[0].length; chy++) {
				chunks[chx][chy].chunkrender();
			}
		}

		if (shader != null)
			shader.unbind();

		glPopMatrix();
	}

	public void setLODs(float lod1, float lod2, float lod3, float lod4, float lod5) {
		this.lod1 = lod1;
		this.lod2 = lod2;
		this.lod3 = lod3;
		this.lod4 = lod4;
		this.lod5 = lod5;
	}

	@Override
	public void setShader(Shader s) {
		shader = s;
	}

	private void smootheEdges(Chunk chunk, int chunklod, int chunkx, int chunky, int stepsize) {
		int w = (int) (chunksizex / (float) stepsize) + 1;
		int h = (int) (chunksizey / (float) stepsize) + 1;
		boolean s = false;
		if (chunky > 0) {
			if (chunks[chunkx][chunky - 1].getLOD() < chunklod) {
				s = true;
				for (int x = 1; x < w; x += 2) {
					chunk.getVertex(x * w).y = 100;
					// //heights[x+1][y1] = getHeight(x+1, y1, chunkx, chunky,
					// stepx, stepy);
					// heights[x][y1] = mid(heights[x-1][y1], heights[x+1][y1]);
				}
			}
		}
		if (chunkx > 0) {
			if (chunks[chunkx - 1][chunky].getLOD() < chunklod) {
				s = true;
				int x1 = 0;
				for (int y = 1; y < h; y += 2) {
					chunk.getVertex(y).y = 100;
					// //heights[x1][y+1] = getHeight(x1, y+1, chunkx, chunky,
					// stepx, stepy);
					// heights[x1][y] = mid(heights[x1][y-1], heights[x1][y+1]);
				}
			}
		}
		if (chunky < chunks[0].length - 1) {
			if (chunks[chunkx][chunky + 1].getLOD() < chunklod) {
				s = true;
				for (int x = 1; x < w; x += 2) {
					chunk.getVertex(h + x * w).y = 100;
					// //heights[x+1][y1] = getHeight(x+1, y1, chunkx, chunky,
					// stepx, stepy);
					// heights[x][y1] = mid(heights[x-1][y1], heights[x+1][y1]);
				}
			}
		}
		if (chunkx < chunks.length - 1) {
			if (chunks[chunkx + 1][chunky].getLOD() < chunklod) {
				s = true;
				int x1 = w - 1;
				for (int y = 1; y < h; y += 2) {
					chunk.getVertex(w * (h - 1) + y).y = 100;
					// //heights[x1][y+1] = getHeight(x1, y+1, chunkx, chunky,
					// stepx, stepy);
					// heights[x1][y] = mid(heights[x1][y-1], heights[x1][y+1]);
				}
			}
		}
		if (chunk.isSmoothed() && !s)
			unsmootheEdges(chunk, chunkx, chunky, stepsize);
		chunk.setSmoothed(s);
	}

	private void unsmootheEdges(Chunk chunk, int chunkx, int chunky, int stepsize) {
		int w = (int) (chunksizex / (float) stepsize) + 1;
		int h = (int) (chunksizey / (float) stepsize) + 1;
		for (int x = 1; x < w; x += 2) {
			chunk.getVertex(x * w).y = getHeight(x, 0, chunkx, chunky, stepsize);
			chunk.getVertex(h + x * w).y = getHeight(x, h, chunkx, chunky, stepsize);
		}
		for (int y = 1; y < h; y += 2) {
			chunk.getVertex(y).y = getHeight(0, y, chunkx, chunky, stepsize);
			chunk.getVertex(w * (h - 1) + y).y = getHeight(w, y, chunkx, chunky, stepsize);
		}
	}

	public void updateChunks(Vector3f campos) {
		for (int chx = 0; chx < chunks.length; chx++) {
			for (int chy = 0; chy < chunks[0].length; chy++) {
				Chunk chunk = chunks[chx][chy];
				chunk.computeCenter(this.getRotation(), this.getTranslation());
			}
		}
		updateLODstart(campos);
	}

	public void updateLOD(Vector3f campos) {
		for (int chx = 0; chx < chunks.length; chx++) {
			for (int chy = 0; chy < chunks[0].length; chy++) {
				Chunk chunk = chunks[chx][chy];
				int prevlod = chunk.getLOD();
				int chunklod = computeLOD(chunk.getCenter(), campos);
				chunk.setLOD(chunklod);
				if (chunk.LODchanged()) {
					if (prevlod != chunklod) {
						if (chunklod != 0)
							chunk.setHeightmap(getHeights(chunklod, (int) chunk.getPosition().x,
									(int) chunk.getPosition().y, getStepsize(chunklod)));
						else
							chunk.deleteData();
						int difference = prevlod - chunklod;
						float value = exp2(difference);
						chunk.scale(new Vector3f(value, 1, value));
					}
					if (chunklod == 0) {
						chunk.deleteData();
					}
				}
			}
		}
		for (int chx = 0; chx < chunks.length; chx++) {
			for (int chy = 0; chy < chunks[0].length; chy++) {
				if (chunks[chx][chy].LODchanged()) {
					Chunk chunk = chunks[chx][chy];
					int chunklod = chunk.getLOD();
					smootheEdges(chunk, chunklod, chx, chy, getStepsize(chunklod));
					chunk.prerender();
				}
			}
		}
	}

	private void updateLODstart(Vector3f campos) {
		for (int chx = 0; chx < chunks.length; chx++) {
			for (int chy = 0; chy < chunks[0].length; chy++) {
				Chunk chunk = chunks[chx][chy];
				int prevlod = chunk.getLOD();
				int chunklod = computeLOD(chunk.getCenter(), campos);
				chunk.setLOD(chunklod);
				if (chunklod != 0)
					chunk.setHeightmap(getHeights(chunklod, (int) chunk.getPosition().x, (int) chunk.getPosition().y,
							getStepsize(chunklod)));
				else
					chunk.deleteData();
				int difference = prevlod - chunklod;
				float value = exp2(difference);
				chunk.scale(new Vector3f(value, 1, value));
			}
		}
		for (int chx = 0; chx < chunks.length; chx++) {
			for (int chy = 0; chy < chunks[0].length; chy++) {
				Chunk chunk = chunks[chx][chy];
				int chunklod = chunk.getLOD();
				smootheEdges(chunk, chunklod, chx, chy, getStepsize(chunklod));
				chunk.prerender();
			}
		}
	}
}