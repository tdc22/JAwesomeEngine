package outputInputs;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.Input;

public class OutputInputIds extends StandardGame {

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(int delta) {
		for (Input i : inputs.getInputs())
			System.out.println(i.getInputType() + ";" + i.getComponentId()
					+ ";" + i.getValue());
	}
}
