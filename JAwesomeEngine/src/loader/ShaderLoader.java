package loader;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_INPUT_TYPE;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_OUTPUT_TYPE;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_VERTICES_OUT;
import static org.lwjgl.opengl.GL41.glProgramParameteri;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ShaderLoader {
	private static void compileShader(int shader, String source, int type) {
		glShaderSource(shader, source);
		glCompileShader(shader);
		if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
			if (type == 1)
				System.err.println("Vertex shader wasn't able to be compiled correctly. Error log:");
			if (type == 2)
				System.err.println("Fragment shader wasn't able to be compiled correctly. Error log:");
			if (type == 3)
				System.err.println("Geometry shader wasn't able to be compiled correctly. Error log:");
			System.err.println(glGetShaderInfoLog(shader, 1024));
		}
	}

	public static int loadShader(String vertexShaderSource, String fragmentShaderSource) {
		return loadShader(vertexShaderSource, fragmentShaderSource, null, 0, 0, 0);
	}

	public static int loadShader(String vertexShaderSource, String geometryShaderSource, int inputtype, int outputtype,
			int verticesout) {
		return loadShader(vertexShaderSource, null, geometryShaderSource, inputtype, outputtype, verticesout);
	}

	public static int loadShader(String vertexShaderSource, String fragmentShaderSource, String geometryShaderSource,
			int inputtype, int outputtype, int verticesout) {
		boolean includeVertexShader = vertexShaderSource != null;
		boolean includeFragmentShader = fragmentShaderSource != null;
		boolean includeGeometryShader = geometryShaderSource != null;

		int shaderProgram = glCreateProgram();
		int vertexShader = 0;
		if (includeVertexShader)
			vertexShader = glCreateShader(GL_VERTEX_SHADER);
		int fragmentShader = 0;
		if (includeFragmentShader)
			fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		int geometryShader = 0;
		if (includeGeometryShader)
			geometryShader = glCreateShader(GL_GEOMETRY_SHADER);

		if (includeVertexShader)
			compileShader(vertexShader, vertexShaderSource, 1);
		if (includeFragmentShader)
			compileShader(fragmentShader, fragmentShaderSource, 2);
		if (includeGeometryShader)
			compileShader(geometryShader, geometryShaderSource, 3);

		if (includeVertexShader)
			glAttachShader(shaderProgram, vertexShader);
		if (includeFragmentShader)
			glAttachShader(shaderProgram, fragmentShader);
		if (includeGeometryShader) {
			glAttachShader(shaderProgram, geometryShader);

			glProgramParameteri(shaderProgram, GL_GEOMETRY_INPUT_TYPE, inputtype);
			glProgramParameteri(shaderProgram, GL_GEOMETRY_OUTPUT_TYPE, outputtype);
			glProgramParameteri(shaderProgram, GL_GEOMETRY_VERTICES_OUT, verticesout);
		}

		glBindAttribLocation(shaderProgram, 0, "in_Position");
		glBindAttribLocation(shaderProgram, 1, "in_Color");

		glLinkProgram(shaderProgram);
		glValidateProgram(shaderProgram);
		if (includeVertexShader)
			glDeleteShader(vertexShader);
		if (includeFragmentShader)
			glDeleteShader(fragmentShader);
		if (includeGeometryShader)
			glDeleteShader(geometryShader);
		return shaderProgram;
	}

	public static int loadShaderFromFile(String vertexShaderLocation, String fragmentShaderLocation) {
		return loadShaderFromFile(vertexShaderLocation, fragmentShaderLocation, null, 0, 0, 0);
	}

	public static int loadShaderFromFile(String vertexShaderLocation, String geometryShaderLocation, int inputtype,
			int outputtype, int verticesout) {
		return loadShaderFromFile(vertexShaderLocation, null, geometryShaderLocation, inputtype, outputtype,
				verticesout);
	}

	public static int loadShaderFromFile(String vertexShaderLocation, String fragmentShaderLocation,
			String geometryShaderLocation, int inputtype, int outputtype, int verticesout) {
		String vertexShaderSource = null;
		if (vertexShaderLocation != null)
			vertexShaderSource = readSourceFile(vertexShaderLocation, 1);
		String fragmentShaderSource = null;
		if (fragmentShaderLocation != null)
			fragmentShaderSource = readSourceFile(fragmentShaderLocation, 2);
		String geometryShaderSource = null;
		if (geometryShaderLocation != null)
			geometryShaderSource = readSourceFile(geometryShaderLocation, 3);

		return loadShader(vertexShaderSource, fragmentShaderSource, geometryShaderSource, inputtype, outputtype,
				verticesout);
	}

	private static String readSourceFile(String location, int type) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(location));
			String line;
			while ((line = reader.readLine()) != null) {
				result.append(line).append('\n');
			}
			reader.close();
		} catch (IOException e) {
			System.err.println(e);
			if (type == 1)
				System.err.println("Vertex shader wasn't loaded properly.");
			if (type == 2)
				System.err.println("Fragment shader wasn't loaded properly.");
			if (type == 3)
				System.err.println("Geometry shader wasn't loaded properly.");
		}
		return result.toString();
	}
}