package loader;

import java.awt.FontFormatException;
import java.awt.Point;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.imageio.ImageIO;

import gui.BitmapFont;
import gui.Color;
import gui.Font;
import gui.FontCharacter;
import gui.OutlineFont;
import texture.Texture;
import vector.Vector2f;

public class FontLoader {
	private static double _B(int n, int m, double t) {
		return _bernstein(n, m, t);
	}

	private static double _bernstein(int n, int m, double t) {
		assert n > 0 && m >= 0 : "_bernstein : invalid arg value for n,m : must be n > 0 and m >= 0";
		double b = Math.pow(t, m) * Math.pow(1 - t, n - m);
		if (m != 0) {
			return _C(n, m) * b;
		} else {
			return b;
		}
	}

	private static double _C(int n, int m) {
		return _combine(n, m);
	}

	private static double _combine(int n, int m) {
		assert m != 0 : "zero is not allowed for arg m";
		return _factorialN(n) / (_factorialN(m) * _factorialN(n - m));
	}

	@SuppressWarnings("unchecked")
	private static <P extends Point2D> P[] _computeBezierCurve(P[] p, int amount) {
		assert amount > 2 : "not a valid value, amount must be greater than 2";
		Vector<Point2D> curve = new Vector<Point2D>();
		for (double t = 0.; t <= 1.; t += 1. / (amount - 1)) {
			double x = 0, y = 0;
			for (int i = 0; i < p.length; i++) {
				x += _B(p.length - 1, i, t) * p[i].getX();
				y += _B(p.length - 1, i, t) * p[i].getY();
			}
			/* System.err.println("bezier cubic : " + x + " ; " + y); */
			if (p instanceof Point.Float[]) {
				curve.add(new Point.Float((float) x, (float) y));
			} else if (p instanceof Point.Double[]) {
				curve.add(new Point.Double(x, y));
			} else {
				curve.add(new Point((int) Math.round(x), (int) Math.round(y)));
			}
		}
		if (p instanceof Point.Float[]) {
			return (P[]) curve.toArray(new Point2D.Float[curve.size()]);
		} else if (p instanceof Point.Double[]) {
			return (P[]) curve.toArray(new Point2D.Double[curve.size()]);
		} else {
			return (P[]) curve.toArray(new Point2D[curve.size()]);
		}
	}

	private static double _factorialN(int n) {
		if (n > 99) {
			return _stirlingFactor(n);
		} else {
			int ret = 1;
			for (int i = 1; i <= n; i++) {
				ret *= i;
			}
			return ret;
		}
	}

	private static double _stirlingFactor(int n) {
		return Math.pow(n / Math.E, n) * Math.sqrt(2.0 * Math.PI * n);
	}

	private static char[] checkCharacters(char[] characters) {
		if (characters.length == 0) {
			characters = new char[96];
			for (int i = 0; i < 94; i++) {
				characters[i] = (char) (i + 33);
			}
			characters[94] = (char) (10); // Line Break
			characters[95] = (char) (32); // Space
		}
		return characters;
	}

	public static Font loadFont(File file, char... characters) {
		if (file.getName().toLowerCase().endsWith(".ttf")) {
			InputStream is = null;
			try {
				is = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			java.awt.Font font = null;
			try {
				font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, is);
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return loadFont(font, characters);
		}
		try {
			return loadFont(ImageIO.read(file), characters);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// from http://lwjgl.org/forum/index.php?topic=3028.0
	public static Font loadFont(java.awt.Font f, char... characters) {
		// Font -> createGlyphVector -> getGlyphVisualBounds -> getPathIterator
		Font font = new OutlineFont();
		characters = checkCharacters(characters);
		int l = characters.length;
		Color color = Color.WHITE;
		GlyphVector glyphvector = f.createGlyphVector(new FontRenderContext(null, true, true), characters);
		for (int i = 0; i < l; i++) {
			Character c = characters[i];
			FontCharacter character = new FontCharacter();
			Shape shape = glyphvector.getGlyphOutline(i, -(float) glyphvector.getGlyphOutline(i).getBounds2D().getX(),
					0);
			Rectangle2D bounds = shape.getBounds2D();
			PathIterator iterator = shape.getPathIterator(f.getTransform());

			character.setMargin((float) bounds.getWidth(), (float) bounds.getHeight());
			Point2D.Float current = new Point2D.Float();
			Vector2f texcoords = new Vector2f(0, 0);
			int index = 0;

			while (!iterator.isDone()) {
				float[] coords = new float[6];
				int path = iterator.currentSegment(coords);
				switch (path) {
				case PathIterator.SEG_MOVETO:
					character.addVertex(new Vector2f(coords[0], coords[1]), color, texcoords);
					index++;
					current.x = coords[0];
					current.y = coords[1];
					break;
				case PathIterator.SEG_LINETO:
					character.addVertex(new Vector2f(coords[0], coords[1]), color, texcoords);
					character.addIndex(index - 1);
					character.addIndex(index);
					index++;
					current.x = coords[0];
					current.y = coords[1];
					break;
				case PathIterator.SEG_CUBICTO:
					for (Point2D.Float p : _computeBezierCurve(
							new Point2D.Float[] { current, new Point2D.Float(coords[0], coords[1]),
									new Point.Float(coords[2], coords[3]), new Point.Float(coords[4], coords[5]) },
							f.getSize())) {
						character.addVertex(new Vector2f(p.x, p.y), color, texcoords);
						character.addIndex(index - 1);
						character.addIndex(index);
						index++;
					}
					character.addVertex(new Vector2f(coords[4], coords[5]), color, texcoords);
					character.addIndex(index - 1);
					character.addIndex(index);
					index++;
					current.x = coords[4];
					current.y = coords[5];
					break;
				case PathIterator.SEG_QUADTO:
					for (Point2D.Float p : _computeBezierCurve(new Point2D.Float[] { current,
							new Point2D.Float(coords[0], coords[1]), new Point.Float(coords[2], coords[3]) },
							f.getSize())) {
						character.addVertex(new Vector2f(p.x, p.y), color, texcoords);
						character.addIndex(index - 1);
						character.addIndex(index);
						index++;
					}
					character.addVertex(new Vector2f(coords[2], coords[3]), color, texcoords);
					character.addIndex(index - 1);
					character.addIndex(index);
					index++;
					current.x = coords[2];
					current.y = coords[3];
					break;
				default:
					break;
				}

				iterator.next();
			}
			character.prerender();
			font.addCharacter(c, character);
		}

		return font;
	}

	public static BitmapFont loadFont(BufferedImage bitmap, char... characters) {
		BitmapFont font = new BitmapFont(new Texture(TextureLoader.loadTexture(bitmap, true)));
		characters = checkCharacters(characters);
		int l = characters.length;
		Color color = Color.WHITE;

		Vector2f tl = new Vector2f(0, 0);
		Vector2f bl = new Vector2f(0, 1); // TODO: size
		Vector2f br = new Vector2f(1, 1);
		Vector2f tr = new Vector2f(1, 0);

		for (int i = 0; i < l; i++) {
			Character c = characters[i];
			FontCharacter character = new FontCharacter();

			int gridSize = 16;
			int asciiCode = (int) characters[i];
			final float cellSize = 1.0f / gridSize;
			float cellX = ((int) asciiCode % gridSize) * cellSize;
			float cellY = 1 - ((int) asciiCode / gridSize) * cellSize;

			character.addVertex(tl, color, new Vector2f(cellX, cellY));
			character.addVertex(bl, color, new Vector2f(cellX, cellY - cellSize));
			character.addVertex(br, color, new Vector2f(cellX + cellSize, cellY - cellSize));
			character.addVertex(tr, color, new Vector2f(cellX + cellSize, cellY));

			character.addQuad(0, 1, 2, 3);
			character.setMargin(0.2f, 0);

			character.prerender();
			font.addCharacter(c, character);
		}
		return font;
	}

	public static Font loadFont(String path, char... characters) {
		return loadFont(new File(path), characters);
	}
}
