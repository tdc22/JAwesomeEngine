package game;

import objects.Updateable;

public abstract class AbstractGame implements Updateable {
	protected long frameTime, lastFrame, lastFPS;
	protected int fps, cfps;

	protected boolean running = true;

	protected abstract void deleteAllObjects();

	protected void destroy() {
	} // Acts as destructor

	protected void destroyEngine() {
		setRunning(false);
		deleteAllObjects();
		destroy();
	}

	protected void exit() {
		destroyEngine();
	}

	/**
	 * Calculate how many milliseconds have passed since last frame.
	 * 
	 * @return milliseconds passed since last frame
	 */
	protected int getDelta() {
		frameTime = getTime();
		int delta = (int) (frameTime - lastFrame);
		lastFrame += delta;

		return delta;
	}

	protected void resetTimers() {
		lastFPS = getTime();
		lastFrame = getTime();
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
		return System.nanoTime() / 1000000;
	}

	public abstract void init();

	protected abstract void initEngine();

	public abstract boolean isRunning();

	public void setRunning(boolean running) {
		this.running = running;
	}

	public abstract void start();

	/**
	 * Calculate the FPS
	 */
	protected void updateFPS() {
		if (frameTime - lastFPS > 1000) {
			fps = cfps;
			cfps = 0;
			lastFPS += 1000;
		}
		cfps++;
	}
}
