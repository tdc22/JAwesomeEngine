package utils;

public class DefaultShader {
	public final static String DEFAULT_SHADER_VERTEX = "#version 330\n"
			+ "layout(location = 0)in vec4 in_Position;\n"
			+ "layout(location = 1)in vec4 in_Color;\n"
			+ "uniform mat4 projection;\n" + "uniform mat4 view;\n"
			+ "uniform mat4 model;\n" + "out vec4 pass_Color;\n"
			+ "void main(void) {\n"
			+ "	gl_Position = projection * view * model * in_Position;\n"
			+ "	pass_Color = in_Color;\n" + "}";
	public final static String DEFAULT_SHADER_FRAGMENT = "#version 330\n"
			+ "in vec4 pass_Color;\n" + "out vec4 out_Color;\n"
			+ "void main(void) {\n" + "	out_Color = pass_Color;\n" + "}";

	public final static String COLOR_SHADER_VERTEX = "#version 330\n"
			+ "layout(location = 0)in vec4 in_Position;\n"
			+ "uniform mat4 projection;\n" + "uniform mat4 view;\n"
			+ "uniform mat4 model;\n" + "uniform vec4 u_color;\n"
			+ "out vec4 pass_Color;\n" + "void main(void) {\n"
			+ "	gl_Position = projection * view * model * in_Position;\n"
			+ "	pass_Color = u_color;\n" + "}";
	public final static String COLOR_SHADER_FRAGMENT = "#version 330\n"
			+ "in vec4 pass_Color;\n" + "out vec4 out_Color;\n"
			+ "void main(void) {\n" + "	out_Color = pass_Color;\n" + "}";

	public final static String SCREEN_SHADER_VERTEX = "#version 330\n"
			+ "layout(location = 0)in vec4 in_Position;\n"
			+ "layout(location = 2)in vec2 in_TextureCoord;\n"
			+ "uniform sampler2D u_texture;\n" + "out vec2 tex_coord;\n"
			+ "void main(void) {\n" + "	gl_Position = in_Position;\n"
			+ "	tex_coord = in_TextureCoord;\n" + "}";
	public final static String SCREEN_SHADER_FRAGMENT = "#version 330\n"
			+ "in vec2 tex_coord;\n" + "uniform sampler2D u_texture;\n"
			+ "out vec4 out_Color;\n" + "void main(void) {\n"
			+ "	out_Color = vec4(texture(u_texture, tex_coord).rgba);\n" + "}";
}