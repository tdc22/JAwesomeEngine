package gui;

public class Color {
	float r, g, b;
	
	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	public Color(int sRGB) {
		r = ((sRGB >> 16) & 0xFF) / 255f;
		g = ((sRGB >> 8) & 0xFF) / 255f;
		b = (sRGB & 0xFF) / 255f;
	}
	
	public float getRed() {
		return r;
	}
	
	public float getGreen() {
		return g;
	}
	
	public float getBlue() {
		return b;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);
		sb.append("Color[");
		sb.append(r);
		sb.append(", ");
		sb.append(g);
		sb.append(", ");
		sb.append(b);
		sb.append(']');
		return sb.toString();
	}
	
	public final static Color WHITE = new Color(1, 1, 1);
	public final static Color GRAY = new Color(0.5f, 0.5f, 0.5f);
	public final static Color BLACK = new Color(0, 0, 0);
	public final static Color RED = new Color(1, 0, 0);
	public final static Color GREEN = new Color(0, 1, 0);
	public final static Color BLUE = new Color(0, 0, 1);
	public final static Color YELLOW = new Color(1, 1, 0);
	public final static Color MAGENTA = new Color(1, 0, 1);
	public final static Color CYAN = new Color(0, 1, 1);
}