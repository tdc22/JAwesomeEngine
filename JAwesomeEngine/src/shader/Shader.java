package shader;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.GL_TEXTURE6;
import static org.lwjgl.opengl.GL13.GL_TEXTURE7;
import static org.lwjgl.opengl.GL13.GL_TEXTURE8;
import static org.lwjgl.opengl.GL13.GL_TEXTURE9;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix2fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import matrix.Matrix2f;
import matrix.Matrix3f;
import matrix.Matrix4f;
import objects.RenderableObject;
import objects.ViewProjection;

import org.lwjgl.BufferUtils;

import texture.Texture;
import utils.StringConstants;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class Shader implements ViewProjection {
	int shaderProgram, uniformBuffer;
	private List<Integer> uniformpositions;
	private List<Integer> uniformtypes;
	private List<Object> uniformarguments;
	private HashMap<String, Integer> uniformnames;
	private List<RenderableObject> objects;
	private boolean rendered = true;

	public Shader(int shaderProgram) {
		this.shaderProgram = shaderProgram;
		uniformpositions = new ArrayList<Integer>();
		uniformtypes = new ArrayList<Integer>();
		uniformarguments = new ArrayList<Object>();
		uniformnames = new HashMap<String, Integer>();
		objects = new ArrayList<RenderableObject>();
	}

	public Shader(int shaderProgram, List<String> argumentnames,
			List<Object> arguments) {
		this.shaderProgram = shaderProgram;
		uniformpositions = new ArrayList<Integer>();
		uniformtypes = new ArrayList<Integer>();
		uniformarguments = new ArrayList<Object>();
		uniformnames = new HashMap<String, Integer>();
		addArguments(argumentnames, arguments);
		objects = new ArrayList<RenderableObject>();
	}

	public Shader(int shaderProgram, String argumentname, Object argument) {
		this.shaderProgram = shaderProgram;
		uniformpositions = new ArrayList<Integer>();
		uniformtypes = new ArrayList<Integer>();
		uniformarguments = new ArrayList<Object>();
		uniformnames = new HashMap<String, Integer>();
		addArgument(argumentname, argument);
		objects = new ArrayList<RenderableObject>();
	}

	public Shader(Shader shader) {
		this.shaderProgram = shader.getShaderProgram();
		uniformpositions = new ArrayList<Integer>(shader.getUniformPositions());
		uniformtypes = new ArrayList<Integer>(shader.getArgumentTypes());
		uniformarguments = new ArrayList<Object>(shader.getArguments());
		uniformnames = new HashMap<String, Integer>(shader.getUniformNames());
		objects = new ArrayList<RenderableObject>();
	}

	public void addObject(RenderableObject obj) {
		objects.add(obj);
	}

	public void removeObject(RenderableObject obj) {
		objects.remove(obj);
	}

	public void addObjects(RenderableObject... objs) {
		for (RenderableObject obj : objs)
			objects.add(obj);
	}

	public List<RenderableObject> getObjects() {
		return objects;
	}

	public void addArgument(Object argument) {
		if (argument instanceof Integer) {
			uniformtypes.add(1);
			uniformarguments.add(argument);
			System.out.println("Argument type is Integer");
		} else if (argument instanceof Float) {
			uniformtypes.add(2);
			uniformarguments.add(argument);
			System.out.println("Argument type is Float");
		} else if (argument instanceof Vector2f) {
			uniformtypes.add(3);
			uniformarguments.add(argument);
			System.out.println("Argument type is Vector2f");
		} else if (argument instanceof Vector3f) {
			uniformtypes.add(4);
			uniformarguments.add(argument);
			System.out.println("Argument type is Vector3f");
		} else if (argument instanceof Vector4f) {
			uniformtypes.add(5);
			uniformarguments.add(argument);
			System.out.println("Argument type is Vector4f");
		} else if (argument instanceof Matrix2f) {
			uniformtypes.add(6);
			FloatBuffer buf = BufferUtils.createFloatBuffer(4);
			((Matrix2f) argument).store(buf);
			buf.flip();
			uniformarguments.add(buf);
			System.out.println("Argument type is Matrix2f");
		} else if (argument instanceof Matrix3f) {
			uniformtypes.add(7);
			FloatBuffer buf = BufferUtils.createFloatBuffer(9);
			((Matrix3f) argument).store(buf);
			buf.flip();
			uniformarguments.add(buf);
			System.out.println("Argument type is Matrix3f");
		} else if (argument instanceof Matrix4f) {
			uniformtypes.add(8);
			FloatBuffer buf = BufferUtils.createFloatBuffer(16);
			((Matrix4f) argument).store(buf);
			buf.flip();
			uniformarguments.add(buf);
			System.out.println("Argument type is Matrix4f");
		} else if (argument instanceof FloatBuffer) {
			// same as before "direct" route
			// has to be already flipped!
			uniformtypes.add(8);
			uniformarguments.add(argument);
			System.out.println("Argument type is Matrix4f (FloatBuffer)");
		} else if (argument instanceof Texture) {
			uniformtypes.add(9);
			uniformarguments.add(argument);
			System.out.println("Argument type is Texture");
		}
	}

	public void addArgument(String argumentname, Object argument) {
		addArgumentName(argumentname);
		addArgument(argument);
	}

	public void addArgumentName(String argumentname) {
		uniformnames.put(argumentname, uniformpositions.size());
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
		addArgumentName(argumentname);
		System.out.println("Argument type is Texture");
	}

	public void bind() {
		int texturenumber = 0;
		glUseProgram(shaderProgram);
		for (int e = 0; e < uniformarguments.size(); e++) {
			Object argument = uniformarguments.get(e);
			int argumenttype = uniformtypes.get(e);
			int uniformlocation = uniformpositions.get(e);

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
				glUniformMatrix2fv(uniformlocation, false,
						(FloatBuffer) argument);
				break;
			case 7:
				glUniformMatrix3fv(uniformlocation, false,
						(FloatBuffer) argument);
				break;
			case 8:
				glUniformMatrix4fv(uniformlocation, false,
						(FloatBuffer) argument);
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
				case 4:
					glActiveTexture(GL_TEXTURE4);
					break;
				case 5:
					glActiveTexture(GL_TEXTURE5);
					break;
				case 6:
					glActiveTexture(GL_TEXTURE6);
					break;
				case 7:
					glActiveTexture(GL_TEXTURE7);
					break;
				case 8:
					glActiveTexture(GL_TEXTURE8);
					break;
				case 9:
					glActiveTexture(GL_TEXTURE9);
					break;
				}
				glBindTexture(((Texture) argument).getTextureType(),
						((Texture) argument).getTextureID());
				glUniform1i(uniformlocation, texturenumber);
				texturenumber++;
				break;
			}
		}
		if (texturenumber > 0)
			glActiveTexture(GL_TEXTURE0);
	}

	public void delete() {
		for (RenderableObject obj : objects) {
			obj.delete();
		}

		glDeleteProgram(shaderProgram);
		uniformpositions.clear();
		uniformtypes.clear();
		uniformarguments.clear();
		uniformnames.clear();
	}

	public Object getArgument(String argumentname) {
		return uniformarguments.get(getArgumentID(argumentname));
	}

	public int getArgumentID(String argumentname) {
		return uniformnames.get(argumentname);
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

	public HashMap<String, Integer> getUniformNames() {
		return uniformnames;
	}

	public List<Integer> getUniformPositions() {
		return uniformpositions;
	}

	public void setArgument(int argumentID, Object argument) {
		int argumentType = uniformtypes.get(argumentID);
		if (argumentType == 8) {
			FloatBuffer buf = (FloatBuffer) uniformarguments.get(argumentID);
			((Matrix4f) argument).store(buf);
			buf.flip();
			uniformarguments.set(argumentID, buf);
		} else if (argumentType == 7) {
			FloatBuffer buf = (FloatBuffer) uniformarguments.get(argumentID);
			((Matrix3f) argument).store(buf);
			buf.flip();
			uniformarguments.set(argumentID, buf);
		} else if (argumentType == 6) {
			FloatBuffer buf = (FloatBuffer) uniformarguments.get(argumentID);
			((Matrix2f) argument).store(buf);
			buf.flip();
			uniformarguments.set(argumentID, buf);
		} else {
			uniformarguments.set(argumentID, argument);
		}
	}

	public void setArgument(String argumentname, Object argument) {
		setArgument(getArgumentID(argumentname), argument);
	}

	public void setArgumentDirect(int argumentID, Object argument) {
		uniformarguments.set(argumentID, argument);
	}

	public void setArgumentDirect(String argumentname, Object argument) {
		setArgumentDirect(getArgumentID(argumentname), argument);
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
				case 4:
					glActiveTexture(GL_TEXTURE4);
					break;
				case 5:
					glActiveTexture(GL_TEXTURE5);
					break;
				case 6:
					glActiveTexture(GL_TEXTURE6);
					break;
				case 7:
					glActiveTexture(GL_TEXTURE7);
					break;
				case 8:
					glActiveTexture(GL_TEXTURE8);
					break;
				case 9:
					glActiveTexture(GL_TEXTURE9);
					break;
				}
				glBindTexture(
						((Texture) uniformarguments.get(e)).getTextureType(), 0);
				texturenumber++;
			}
		}
		if (texturenumber > 0)
			glActiveTexture(GL_TEXTURE0);
		glUseProgram(0);
	}

	public void setRendered(boolean rendered) {
		this.rendered = rendered;
	}

	public boolean isRendered() {
		return rendered;
	}

	@Override
	public void render() {
		if (rendered) {
			bind();
			int modelLocation = uniformpositions
					.get(getArgumentID(StringConstants.MVP_MODEL));
			for (RenderableObject obj : objects) {
				glUniformMatrix4fv(modelLocation, false, obj.getMatrixBuffer());
				obj.render();
			}
			unbind();
		}
	}

	public void renderNoMatrix() {
		if (rendered) {
			bind();
			for (RenderableObject obj : objects) {
				obj.render();
			}
			unbind();
		}
	}

	@Override
	public void setViewMatrix(FloatBuffer buffer) {
		setArgumentDirect(StringConstants.MVP_VIEW, buffer);
	}

	@Override
	public void setProjectionMatrix(FloatBuffer buffer) {
		setArgumentDirect(StringConstants.MVP_PROJECTION, buffer);
	}

	@Override
	public void setViewProjectionMatrix(FloatBuffer viewBuffer,
			FloatBuffer projectionBuffer) {
		setArgumentDirect(StringConstants.MVP_VIEW, viewBuffer);
		setArgumentDirect(StringConstants.MVP_PROJECTION, projectionBuffer);
	}

	@Override
	public void setViewMatrix(Matrix4f matrix) {
		setArgument(StringConstants.MVP_VIEW, matrix);
	}

	@Override
	public void setProjectionMatrix(Matrix4f matrix) {
		setArgument(StringConstants.MVP_PROJECTION, matrix);
	}

	@Override
	public void setViewProjectionMatrix(Matrix4f viewMatrix,
			Matrix4f projectionMatrix) {
		setArgument(StringConstants.MVP_VIEW, viewMatrix);
		setArgument(StringConstants.MVP_PROJECTION, projectionMatrix);
	}

	@Override
	public FloatBuffer getViewMatrixBuffer() {
		return (FloatBuffer) getArgument(StringConstants.MVP_VIEW);
	}

	@Override
	public FloatBuffer getProjectionMatrixBuffer() {
		return (FloatBuffer) getArgument(StringConstants.MVP_PROJECTION);
	}
}