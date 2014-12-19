package gui;

public class DisplayMode {
	int width, height;
	boolean resizeable, vsync;
	String title;
	private final String DEFAULT_TITLE = "JAwesomeEngine Display";

	public DisplayMode() {
		init(800, 600, DEFAULT_TITLE, true, true);
	}

	public DisplayMode(int width, int height) {
		init(width, height, DEFAULT_TITLE, false, true);
	}

	public DisplayMode(int width, int height, String title) {
		init(width, height, title, false, true);
	}

	public DisplayMode(int width, int height, String title, boolean vsync) {
		init(width, height, title, false, vsync);
	}

	public DisplayMode(int width, int height, String title, boolean resizeable,
			boolean vsync) {
		init(width, height, title, resizeable, vsync);
	}

	private void init(int width, int height, String title, boolean resizeable,
			boolean vsync) {
		this.width = width;
		this.height = height;
		this.title = title;
		this.resizeable = resizeable;
		this.vsync = vsync;
	}

	public String getTitle() {
		return title;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isResizeable() {
		return resizeable;
	}

	public boolean isVSync() {
		return vsync;
	}
}