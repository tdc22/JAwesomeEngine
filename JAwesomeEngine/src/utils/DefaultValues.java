package utils;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;

import vector.Vector3f;

public class DefaultValues {
	// Display values
	public static final int DEFAULT_DISPLAY_POSITION_X = 300;
	public static final int DEFAULT_DISPLAY_POSITION_Y = 200;
	public static final int DEFAULT_DISPLAY_RESOLUTION_X = 800;
	public static final int DEFAULT_DISPLAY_RESOLUTION_Y = 600;
	public static final String DEFAULT_DISPLAY_TITLE = "JAwesomeEngine";
	public static final boolean DEFAULT_DISPLAY_RESIZEABLE = true;
	public static final boolean DEFAULT_DISPLAY_VSYNC = true;
	public static final boolean DEFAULT_DISPLAY_FULLSCREEN = false;
	// PixelFormat values
	public static final int DEFAULT_PIXELFORMAT_BPP = 8;
	public static final int DEFAULT_PIXELFORMAT_ALPHA = 8;
	public static final int DEFAULT_PIXELFORMAT_DEPTH = 24;
	public static final int DEFAULT_PIXELFORMAT_STENCIL = 8;
	public static final int DEFAULT_PIXELFORMAT_SAMPLES = 4;
	public static final int DEFAULT_PIXELFORMAT_NUM_AUX_BUFFERS = 0;
	public static final int DEFAULT_PIXELFORMAT_ACCUM_BPP = 0;
	public static final int DEFAULT_PIXELFORMAT_ACCUM_ALPHA = 0;
	public static final boolean DEFAULT_PIXELFORMAT_STEREO = false;
	public static final boolean DEFAULT_PIXELFORMAT_SRGB = false;
	// VideoSettings values
	public static final int DEFAULT_VIDEO_RESOLUTION_X = 800;
	public static final int DEFAULT_VIDEO_RESOLUTION_Y = 600;
	public static final float DEFAULT_VIDEO_FOVY = 90;
	public static final float DEFAULT_VIDEO_ZNEAR = 0.1f;
	public static final float DEFAULT_VIDEO_ZFAR = 200;
	public static final boolean DEFAULT_VIDEO_BACKFACECULLING = true;
	// Text values
	public static final float DEFAULT_FONT_SIZE = 12;
	public static final float DEFAULT_FONT_CHARACTER_MARGIN = 0.14f;
	public static final float DEFAULT_FONT_SPACE_SIZE = 0.2f;
	// UDPServer values
	public static final long DEFAULT_UDP_SERVER_TIMEOUT = 10000;
	// Camera values
	public static final Vector3f DEFAULT_CAMERA_POSITION = new Vector3f();
	public static final float DEFAULT_CAMERA_HORIZONTAL_ROTATION = 0;
	public static final float DEFAULT_CAMERA_VERTICAL_ROTATION = 0;
	// GameCamera values
	public static final float DEFAULT_GAMECAMERA_MIN_V_ANGLE = -85;
	public static final float DEFAULT_GAMECAMERA_MAX_V_ANGLE = 85;
	public static final float DEFAULT_GAMECAMERA_SPEED = 0.012f;
	// CubeEnvironmentMap values
	public static final int DEFAULT_ENVIRONMENT_CUBEMAP_RESOLUTION_X = 1024;
	public static final int DEFAULT_ENVIRONMENT_CUBEMAP_RESOLUTION_Y = 1024;
	public static final float DEFAULT_ENVIRONMENT_CUBEMAP_ZNEAR = 0.1f;
	public static final float DEFAULT_ENVIRONMENT_CUBEMAP_ZFAR = 200;
	// FramebufferObject values
	public static final int DEFAULT_FRAMEBUFFER_RESOLUTION_X = 1024;
	public static final int DEFAULT_FRAMEBUFFER_RESOLUTION_Y = 1024;
	public static final int DEFAULT_FRAMEBUFFER_SAMPLES = 0;
	public static final boolean DEFAULT_FRAMEBUFFER_RENDER_COLOR = true;
	public static final boolean DEFAULT_FRAMEBUFFER_RENDER_DEPTH = true;
	public static final boolean DEFAULT_FRAMEBUFFER_COLOR_TEXTURE = true;
	public static final boolean DEFAULT_FRAMEBUFFER_DEPTH_TEXTURE = true;
	// Texture values
	public static final int DEFAULT_TEXTURE_TYPE = GL_TEXTURE_2D;
	// PostProcessingShader values
	public static final int DEFAULT_POST_PROCESSING_ITERATIONS = 20;
}