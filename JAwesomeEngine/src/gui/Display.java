package gui;

public abstract class Display {
	public abstract void open(DisplayMode displaymode, PixelFormat pixelformat);

	public abstract void close();

	public abstract void clear();

	public abstract void swap();

	public abstract boolean isCloseRequested();

	public abstract void bindMouse();
	
	public abstract void unbindMouse();
	
	public abstract boolean isMouseBound();
}