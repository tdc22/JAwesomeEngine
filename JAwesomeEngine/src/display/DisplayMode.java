package display;

import utils.DefaultValues;

public class DisplayMode {
	int width, height;
	boolean resizeable, vsync;
	String title;

	public DisplayMode() {
		init(DefaultValues.DEFAULT_DISPLAY_RESOLUTION_X, DefaultValues.DEFAULT_DISPLAY_RESOLUTION_Y,
				DefaultValues.DEFAULT_DISPLAY_TITLE, DefaultValues.DEFAULT_DISPLAY_RESIZEABLE,
				DefaultValues.DEFAULT_DISPLAY_VSYNC);
	}

	public DisplayMode(int width, int height) {
		init(width, height, DefaultValues.DEFAULT_DISPLAY_TITLE, DefaultValues.DEFAULT_DISPLAY_RESIZEABLE,
				DefaultValues.DEFAULT_DISPLAY_VSYNC);
	}

	public DisplayMode(int width, int height, String title) {
		init(width, height, title, DefaultValues.DEFAULT_DISPLAY_RESIZEABLE, DefaultValues.DEFAULT_DISPLAY_VSYNC);
	}

	public DisplayMode(int width, int height, String title, boolean vsync) {
		init(width, height, title, DefaultValues.DEFAULT_DISPLAY_RESIZEABLE, vsync);
	}

	public DisplayMode(int width, int height, String title, boolean resizeable, boolean vsync) {
		init(width, height, title, resizeable, vsync);
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	private void init(int width, int height, String title, boolean resizeable, boolean vsync) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.resizeable = resizeable;
		this.vsync = vsync;
	}

	public boolean isResizeable() {
		return resizeable;
	}

	public boolean isVSync() {
		return vsync;
	}
}