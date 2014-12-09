package outputInputs;

import game.StandardGame;
import input.Input;

public class OutputInputIds extends StandardGame {

	@Override
	public void init() {
		initDisplay(false, 800, 600, true);
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
