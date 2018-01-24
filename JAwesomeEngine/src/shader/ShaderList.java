package shader;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.glActiveTexture;
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
import java.util.List;

import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

/**
 * @deprecated
 * @see #Shader
 */
@Deprecated
public class ShaderList {
	List<Shader> list;

	public ShaderList() {
		list = new ArrayList<Shader>();
	}

	public void addShader(int shaderProgram, List<Object> uniformarguments, List<String> argumentnames) {
		Shader element = new Shader(shaderProgram);
		element.addArguments(uniformarguments, argumentnames);
		list.add(element);
	}

	public void render() {
		for (int l = 0; l < list.size(); l++) {
			Shader element = list.get(l);
			int texturenumber = 0;
			glUseProgram(element.getShaderProgram());

			List<Object> arguments = element.getArguments();
			List<Integer> argumenttypes = element.getArgumentTypes();

			for (int e = 0; e < arguments.size(); e++) {
				Object argument = arguments.get(e);
				int argumenttype = argumenttypes.get(e);
				int uniformlocation = element.getUniformPositions().get(e);

				// glUniform1f(uniformlocation, 0.5f);
				switch (argumenttype) {
				case 1:
					glUniform1i(uniformlocation, (Integer) argument);
					break;
				case 2:
					glUniform1f(uniformlocation, (Float) argument);
					break;
				case 3:
					glUniform2f(uniformlocation, ((Vector2f) argument).x, ((Vector2f) argument).y);
					break;
				case 4:
					glUniform3f(uniformlocation, ((Vector3f) argument).x, ((Vector3f) argument).y,
							((Vector3f) argument).z);
					break;
				case 5:
					glUniform4f(uniformlocation, ((Vector4f) argument).x, ((Vector4f) argument).y,
							((Vector4f) argument).z, ((Vector4f) argument).w);
					break;
				case 6:
					glUniformMatrix2fv(uniformlocation, false, (FloatBuffer) argument);
					break;
				case 7:
					glUniformMatrix3fv(uniformlocation, false, (FloatBuffer) argument);
					break;
				case 8:
					glUniformMatrix4fv(uniformlocation, false, (FloatBuffer) argument);
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

				/*
				 * if(argumenttype == 1) glUniform1i(uniformlocation, (Integer)argument);
				 * if(argumenttype == 2) glUniform1f(uniformlocation, (Float)argument);
				 * if(argumenttype == 3) glUniform2f(uniformlocation, ((Vector2f)argument).x,
				 * ((Vector2f)argument).y); if(argumenttype == 4) glUniform3f(uniformlocation,
				 * ((Vector3f)argument).x, ((Vector3f)argument).y, ((Vector3f)argument).z);
				 * if(argumenttype == 5) glUniform4f(uniformlocation, ((Vector4f)argument).x,
				 * ((Vector4f)argument).y, ((Vector4f)argument).z, ((Vector4f)argument).w);
				 * if(argumenttype == 6) glUniformMatrix2(uniformlocation, false,
				 * (FloatBuffer)argument); if(argumenttype == 7)
				 * glUniformMatrix3(uniformlocation, false, (FloatBuffer)argument);
				 * if(argumenttype == 8) glUniformMatrix4(uniformlocation, false,
				 * (FloatBuffer)argument); if(argumenttype == 9) { glActiveTexture(GL_TEXTURE0);
				 * glBindTexture(GL_TEXTURE_2D, (Integer)argument); }
				 */
			}
		}
	}
}