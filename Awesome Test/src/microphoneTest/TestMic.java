package microphoneTest;

import game.StandardGame;

public class TestMic extends StandardGame {
	GraphObject graph;
	MFCCGraph mfccgraph;
	Microphone mic;

	@Override
	protected void destroy() {
		mic.stop();
	}

	@Override
	public void init() {
		initDisplay(false, 1920, 1200, true);
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		mouse.setGrabbed(false);

		graph = new MicrophoneGraph();
		graph.translate(20, 300);

		mfccgraph = new MFCCGraph(new MFCC());
		mfccgraph.translate(20, 900);

		mic = new SimpleMicrophone();
		mic.start();
		mic.addProcessor(graph);
		mic.addProcessor(mfccgraph);

		add2dObject(graph);
		add2dObject(mfccgraph);
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {
		render2dScene();
	}

	@Override
	public void update(int delta) {
		cam.update(delta);
		graph.updateGraph();
	}
}
