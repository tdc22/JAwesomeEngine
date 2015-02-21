package shader;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix2;
import static org.lwjgl.opengl.GL20.glUniformMatrix3;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import matrix.Matrix2f;
import matrix.Matrix3f;
import matrix.Matrix4f;

import org.lwjgl.BufferUtils;

import texture.Texture;
import utils.Pair;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class Shader {
	int shaderProgram;
	private List<Integer> uniformpositions;
	private List<Integer> uniformtypes;
	private List<Object> uniformarguments;

	public Shader(int shaderProgram) {
		this.shaderProgram = shaderProgram;
		uniformpositions = new ArrayList<Integer>();
		uniformtypes = new ArrayList<Integer>();
		uniformarguments = new ArrayList<Object>();
	}

	public Shader(int shaderProgram, List<String> argumentnames,
			List<Object> arguments) {
		this.shaderProgram = shaderProgram;
		uniformpositions = new ArrayList<Integer>();
		uniformtypes = new ArrayList<Integer>();
		uniformarguments = new ArrayList<Object>();
		addArguments(argumentnames, arguments);
	}

	@SafeVarargs
	public Shader(int shaderProgram, Pair<String, Object>... arguments) {
		this.shaderProgram = shaderProgram;
		uniformpositions = new ArrayList<Integer>();
		uniformtypes = new ArrayList<Integer>();
		uniformarguments = new ArrayList<Object>();
		for (Pair<String, Object> a : arguments)
			addArgument(a.getFirst(), a.getSecond());
	}

	public Shader(int shaderProgram, String argumentname, Object argument) {
		this.shaderProgram = shaderProgram;
		uniformpositions = new ArrayList<Integer>();
		uniformtypes = new ArrayList<Integer>();
		uniformarguments = new ArrayList<Object>();
		addArgument(argumentname, argument);
	}

	public Shader(Shader shader) {
		this.shaderProgram = shader.getShaderProgram();
		uniformpositions = new ArrayList<Integer>(shader.getUniformPositions());
		uniformtypes = new ArrayList<Integer>(shader.getArgumentTypes());
		uniformarguments = new ArrayList<Object>(shader.getArguments());
	}

	public void addArgument(Object argument) {
		if (argument instanceof Integer) {
			uniformtypes.add(1);
			uniformarguments.add(argument);
			System.out.println("Argument type is Integer");
		}
		if (argument instanceof Float) {
			uniformtypes.add(2);
			uniformarguments.add(argument);
			System.out.println("Argument type is Float");
		}
		if (argument instanceof Vector2f) {
			uniformtypes.add(3);
			uniformarguments.add(argument);
			System.out.println("Argument type is Vector2f");
		}
		if (argument instanceof Vector3f) {
			uniformtypes.add(4);
			uniformarguments.add(argument);
			System.out.println("Argument type is Vector3f");
		}
		if (argument instanceof Vector4f) {
			uniformtypes.add(5);
			uniformarguments.add(argument);
			System.out.println("Argument type is Vector4f");
		}
		if (argument instanceof Matrix2f) {
			uniformtypes.add(6);
			FloatBuffer buf = BufferUtils.createFloatBuffer(16 * 2);
			((Matrix2f) argument).store(buf);
			buf.rewind();
			uniformarguments.add(buf);
			System.out.println("Argument type is Matrix2f");
		}
		if (argument instanceof Matrix3f) {
			uniformtypes.add(7);
			FloatBuffer buf = BufferUtils.createFloatBuffer(16 * 3);
			((Matrix3f) argument).store(buf);
			buf.rewind();
			uniformarguments.add(buf);
			System.out.println("Argument type is Matrix3f");
		}
		if (argument instanceof Matrix4f) {
			uniformtypes.add(8);
			FloatBuffer buf = BufferUtils.createFloatBuffer(16 * 4);
			((Matrix4f) argument).store(buf);
			buf.rewind();
			uniformarguments.add(buf);
			System.out.println("Argument type is Matrix4f");
		}
		if (argument instanceof Texture) {
			uniformtypes.add(9);
			uniformarguments.add(((Texture) argument).getTextureID());
			System.out.println("Argument type is Texture");
		}
	}

	public void addArgument(String argumentname, Object argument) {
		addArgumentName(argumentname);
		addArgument(argument);
	}

	public void addArgumentName(String argumentname) {
		uniformpositions.add(glGetUniformLocation(shaderProgram, argumentname));
	}

	public void addArgumentNames(String... argumentnames) {
		for (String argumentname : argumentnames) {
			addArgumentName(argumentname);
		}
	}

	public void addArguments(List<String> argumentnames, List<Object> arguments) {
		for (int e = 0; e < arguments.size(); e++) {
			Object argument = arguments.get(e);
			String argumentname = argumentnames.get(e);
			addArgument(argumentname, argument);
		}
	}

	public void addArguments(Object... arguments) {
		for (Object argument : arguments) {
			addArgument(argument);
		}
	}

	public void addTextureID(String argumentname, int argument) {
		uniformtypes.add(9);
		uniformarguments.add(argument);
		uniformpositions.add(glGetUniformLocation(shaderProgram, argumentname));
		System.out.println("Argument type is Texture");
	}

	public void bind() {
		int texturenumber = 0;
		glUseProgram(shaderProgram);
		for (int e = 0; e < uniformarguments.size(); e++) {
			Object argument = uniformarguments.get(e);
			int argumenttype = uniformtypes.get(e);
			int uniformlocation = uniformpositions.get(e);

			// glUniform1f(uniformlocation, 0.5f);
			switch (argumenttype) {
			case 1:
				glUniform1i(uniformlocation, (Integer) argument);
				break;
			case 2:
				glUniform1f(uniformlocation, (Float) argument);
				break;
			case 3:
				glUniform2f(uniformlocation, ((Vector2f) argument).x,
						((Vector2f) argument).y);
				break;
			case 4:
				glUniform3f(uniformlocation, ((Vector3f) argument).x,
						((Vector3f) argument).y, ((Vector3f) argument).z);
				break;
			case 5:
				glUniform4f(uniformlocation, ((Vector4f) argument).x,
						((Vector4f) argument).y, ((Vector4f) argument).z,
						((Vector4f) argument).w);
				break;
			case 6:
				glUniformMatrix2(uniformlocation, false, (FloatBuffer) argument);
				break;
			case 7:
				glUniformMatrix3(uniformlocation, false, (FloatBuffer) argument);
				break;
			case 8:
				glUniformMatrix4(uniformlocation, false, (FloatBuffer) argument);
				break;
			case 9:
				switch (texturenumber) {
				case 0:
					glActiveTexture(GL_TEXTURE0);
					break;
				case 1:
					glActiveTexture(GL_TEXTURE1);
					break;
				case 2:
					glActiveTexture(GL_TEXTURE2);
					break;
				case 3:
					glActiveTexture(GL_TEXTURE3);
					break;
				}
				glBindTexture(GL_TEXTURE_2D, (Integer) argument);
				glUniform1i(uniformlocation, texturenumber);
				texturenumber++;
				break;
			}
		}
	}

	public void delete() {
		glDeleteProgram(shaderProgram);
	}

	public List<Object> getArguments() {
		return uniformarguments;
	}

	public List<Integer> getArgumentTypes() {
		return uniformtypes;
	}

	public int getShaderProgram() {
		return shaderProgram;
	}

	public List<Integer> getUniformPositions() {
		return uniformpositions;
	}

	public void setArgument(int id, Object argument) {
		uniformarguments.set(id, argument);
	}

	public void unbind() {
		int texturenumber = 0;
		for (int e = 0; e < uniformarguments.size(); e++) {
			int argumenttype = uniformtypes.get(e);
			if (argumenttype == 9) {
				switch (texturenumber) {
				case 0:
					glActiveTexture(GL_TEXTURE0);
					break;
				case 1:
					glActiveTexture(GL_TEXTURE1);
					break;
				case 2:
					glActiveTexture(GL_TEXTURE2);
					break;
				case 3:
					glActiveTexture(GL_TEXTURE3);
					break;
				}
				glBindTexture(GL_TEXTURE_2D, 0);
				texturenumber++;
			}
		}
		glUseProgram(0);
	}
}