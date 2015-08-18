package game;

import java.util.ArrayList;
import java.util.List;

import objects.GameObject;

public abstract class ServerGame extends AbstractGame {
	List<GameObject> objects;

	public void addObject(GameObject obj) {
		objects.add(obj);
	}

	@Override
	protected void deleteAllObjects() {
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).delete();
		}
	}

	public List<GameObject> getObjects() {
		return objects;
	}

	@Override
	protected void initEngine() {
		objects = new ArrayList<GameObject>();
		resetTimers();
	}

	@Override
	public synchronized boolean isRunning() {
		return running;
	}

	@Override
	public void start() {
		initEngine();
		init();
		getDelta();

		while (isRunning()) {
			int delta = getDelta();
			updateFPS();
			update(delta);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		destroyEngine();
	}
}
