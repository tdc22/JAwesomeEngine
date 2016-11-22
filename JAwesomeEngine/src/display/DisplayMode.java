package display;

import utils.DefaultValues;

public class DisplayMode {
	int positionX, positionY, width, height;
	boolean resizeable, vsync, fullscreen;
	String title;

	public DisplayMode() {
		init(DefaultValues.DEFAULT_DISPLAY_POSITION_X, DefaultValues.DEFAULT_DISPLAY_POSITION_Y,
				DefaultValues.DEFAULT_DISPLAY_RESOLUTION_X, DefaultValues.DEFAULT_DISPLAY_RESOLUTION_Y,
				DefaultValues.DEFAULT_DISPLAY_TITLE, DefaultValues.DEFAULT_DISPLAY_VSYNC,
				DefaultValues.DEFAULT_DISPLAY_RESIZEABLE, DefaultValues.DEFAULT_DISPLAY_FULLSCREEN);
	}

	public DisplayMode(int width, int height) {
		init(DefaultValues.DEFAULT_DISPLAY_POSITION_X, DefaultValues.DEFAULT_DISPLAY_POSITION_Y, width, height,
				DefaultValues.DEFAULT_DISPLAY_TITLE, DefaultValues.DEFAULT_DISPLAY_VSYNC,
				DefaultValues.DEFAULT_DISPLAY_RESIZEABLE, DefaultValues.DEFAULT_DISPLAY_FULLSCREEN);
	}

	public DisplayMode(int width, int height, String title) {
		init(DefaultValues.DEFAULT_DISPLAY_POSITION_X, DefaultValues.DEFAULT_DISPLAY_POSITION_Y, width, height, title,
				DefaultValues.DEFAULT_DISPLAY_VSYNC, DefaultValues.DEFAULT_DISPLAY_RESIZEABLE,
				DefaultValues.DEFAULT_DISPLAY_FULLSCREEN);
	}

	public DisplayMode(int width, int height, String title, boolean vsync) {
		init(DefaultValues.DEFAULT_DISPLAY_POSITION_X, DefaultValues.DEFAULT_DISPLAY_POSITION_Y, width, height, title,
				vsync, DefaultValues.DEFAULT_DISPLAY_RESIZEABLE, DefaultValues.DEFAULT_DISPLAY_FULLSCREEN);
	}

	public DisplayMode(int width, int height, String title, boolean vsync, boolean resizeable) {
		init(DefaultValues.DEFAULT_DISPLAY_POSITION_X, DefaultValues.DEFAULT_DISPLAY_POSITION_Y, width, height, title,
				vsync, resizeable, DefaultValues.DEFAULT_DISPLAY_FULLSCREEN);
	}

	public DisplayMode(int width, int height, String title, boolean vsync, boolean resizeable, boolean fullscreen) {
		init(DefaultValues.DEFAULT_DISPLAY_POSITION_X, DefaultValues.DEFAULT_DISPLAY_POSITION_Y, width, height, title,
				vsync, resizeable, fullscreen);
	}

	public DisplayMode(int positionX, int positionY, int width, int height, String title, boolean vsync,
			boolean resizeable, boolean fullscreen) {
		init(positionX, positionY, width, height, title, vsync, resizeable, fullscreen);
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

	public int getPositionX() {
		return positionX;
	}

	public int getPositionY() {
		return positionY;
	}

	private void init(int positionX, int positionY, int width, int height, String title, boolean vsync,
			boolean resizeable, boolean fullscreen) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.width = width;
		this.height = height;
		this.title = title;
		this.vsync = vsync;
		this.resizeable = resizeable;
		this.fullscreen = fullscreen;
	}

	public boolean isResizeable() {
		return resizeable;
	}

	public boolean isFullscreen() {
		return fullscreen;
	}

	public boolean isVSync() {
		return vsync;
	}
}