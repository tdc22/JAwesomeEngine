package gui;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.glfw.GLFW.GLFW_ACCUM_ALPHA_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_ACCUM_BLUE_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_ACCUM_GREEN_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_ACCUM_RED_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_ALPHA_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_AUX_BUFFERS;
import static org.lwjgl.system.glfw.GLFW.GLFW_BLUE_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.system.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.system.glfw.GLFW.GLFW_DEPTH_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_RED_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.system.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.system.glfw.GLFW.GLFW_SRGB_CAPABLE;
import static org.lwjgl.system.glfw.GLFW.GLFW_STENCIL_BITS;
import static org.lwjgl.system.glfw.GLFW.GLFW_STEREO;
import static org.lwjgl.system.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.system.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.system.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.system.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.system.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.system.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.system.glfw.GLFW.glfwInit;
import static org.lwjgl.system.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.system.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.system.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.system.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.system.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.system.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.system.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.system.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.system.glfw.GLFW.glfwTerminate;
import static org.lwjgl.system.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.system.glfw.GLFW.glfwWindowShouldClose;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.glfw.ErrorCallback;
import org.lwjgl.system.glfw.GLFWvidmode;

public class GLDisplay extends Display {
	private long windowid;

	@Override
	public void open(DisplayMode displaymode, PixelFormat pixelformat) {
		glfwSetErrorCallback(ErrorCallback.Util.getDefault());

		if (glfwInit() != GL11.GL_TRUE)
			throw new IllegalStateException("Unable to initialize GLFW");

		// See: http://www.glfw.org/docs/latest/window.html#window_hints_values
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, displaymode.isResizeable() ? GL_TRUE
				: GL_FALSE);
		glfwWindowHint(GLFW_RED_BITS, pixelformat.getBitsPerPixel());
		glfwWindowHint(GLFW_GREEN_BITS, pixelformat.getBitsPerPixel());
		glfwWindowHint(GLFW_BLUE_BITS, pixelformat.getBitsPerPixel());
		glfwWindowHint(GLFW_ALPHA_BITS, pixelformat.getAlpha());
		glfwWindowHint(GLFW_DEPTH_BITS, pixelformat.getDepth());
		glfwWindowHint(GLFW_STENCIL_BITS, pixelformat.getStencil());
		glfwWindowHint(GLFW_ACCUM_RED_BITS,
				pixelformat.getAccumulationBitsPerPixel());
		glfwWindowHint(GLFW_ACCUM_GREEN_BITS,
				pixelformat.getAccumulationBitsPerPixel());
		glfwWindowHint(GLFW_ACCUM_BLUE_BITS,
				pixelformat.getAccumulationBitsPerPixel());
		glfwWindowHint(GLFW_ACCUM_ALPHA_BITS,
				pixelformat.getAccumulationAlpha());
		glfwWindowHint(GLFW_AUX_BUFFERS, pixelformat.getAuxBuffers());
		glfwWindowHint(GLFW_SAMPLES, pixelformat.getSamples());
		// glfwWindowHint(GLFW_REFRESH_RATE, ???);
		glfwWindowHint(GLFW_STEREO, pixelformat.isStereo() ? GL_TRUE : GL_FALSE);
		glfwWindowHint(GLFW_SRGB_CAPABLE, pixelformat.isSRGB() ? GL_TRUE
				: GL_FALSE);

		int width = displaymode.getWidth();
		int height = displaymode.getHeight();

		windowid = glfwCreateWindow(width, height, displaymode.getTitle(),
				NULL, NULL);
		if (windowid == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(windowid, (GLFWvidmode.width(vidmode) - width) / 2,
				(GLFWvidmode.height(vidmode) - height) / 2);

		glfwMakeContextCurrent(windowid);
		glfwSwapInterval(displaymode.isVSync() ? 1 : 0);

		glfwShowWindow(windowid);

		GLContext.createFromCurrent();
		// glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	}

	@Override
	public void close() {
		glfwDestroyWindow(windowid);
		glfwTerminate();
	}

	@Override
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT
				| GL_STENCIL_BUFFER_BIT);
	}

	@Override
	public void swap() {
		glfwSwapBuffers(windowid);
		glfwPollEvents();
	}

	@Override
	public boolean isCloseRequested() {
		return glfwWindowShouldClose(windowid) == 1;
	}

	public long getWindowID() {
		return windowid;
	}

	@Override
	public void bindMouse() {
		glfwSetInputMode(windowid, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
	}
}