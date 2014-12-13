package utils;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_FAN;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL32.GL_LINES_ADJACENCY;
import static org.lwjgl.opengl.GL32.GL_LINE_STRIP_ADJACENCY;
import static org.lwjgl.opengl.GL32.GL_TRIANGLES_ADJACENCY;
import static org.lwjgl.opengl.GL32.GL_TRIANGLE_STRIP_ADJACENCY;

public class GLConstants {
	public static int POINTS = GL_POINTS;
	public static int LINES = GL_LINES;
	public static int LINE_LOOP = GL_LINE_LOOP;
	public static int LINE_STRIP = GL_LINE_STRIP;
	public static int TRIANGLES = GL_TRIANGLES;
	public static int TRIANGLE_STRIP = GL_TRIANGLE_STRIP;
	public static int TRIANGLE_FAN = GL_TRIANGLE_FAN;
	public static int LINES_ADJACENCY = GL_LINES_ADJACENCY;
	public static int LINE_STRIP_ADJACENCY = GL_LINE_STRIP_ADJACENCY;
	public static int TRIANGLE_ADJACENCY = GL_TRIANGLES_ADJACENCY;
	public static int TRIANGLE_STRIP_ADJACENCY = GL_TRIANGLE_STRIP_ADJACENCY;
}
