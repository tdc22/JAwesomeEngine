package tutorialp4;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.VerletIntegration;
import loader.ShaderLoader;
import manifold.PersistentManifoldManager;
import math.QuatMath;
import math.VecMath;
import narrowphase.EPA;
import narrowphase.GJK;
import objects.RigidBody3;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.ProjectionCorrection;
import resolution.LinearImpulseResolution;
import shape.Box;
import shape.Cylinder;
import utils.GLConstants;
import utils.Shader;
import vector.Vector3f;
import vector.Vector4f;
import broadphase.SAP;

public class Tutorial extends StandardGame {
	InputEvent forward, backward, left, right, jump;
	PhysicsSpace space;
	RigidBody3 playerbody;
	float playerradius = 0.7f;
	float playerheight = 1.7f;
	float playerspeed = 10;
	float mousesensitivity = 0.2f;
	Shader edgeshader;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.rotateTo(0, 0);

		Cylinder player = new Cylinder(0, 0, 0, playerradius,
				playerheight / 2f, 50);
		player.setRenderHints(false, false, true);
		this.addObject(player);

		forward = new InputEvent("Forward", new Input(Input.KEYBOARD_EVENT,
				"W", KeyInput.KEY_DOWN), new Input(Input.KEYBOARD_EVENT, "Up",
				KeyInput.KEY_DOWN));
		backward = new InputEvent("Backward", new Input(Input.KEYBOARD_EVENT,
				"S", KeyInput.KEY_DOWN), new Input(Input.KEYBOARD_EVENT,
				"Down", KeyInput.KEY_DOWN));
		left = new InputEvent("Left", new Input(Input.KEYBOARD_EVENT, "A",
				KeyInput.KEY_DOWN), new Input(Input.KEYBOARD_EVENT, "Left",
				KeyInput.KEY_DOWN));
		right = new InputEvent("Right", new Input(Input.KEYBOARD_EVENT, "D",
				KeyInput.KEY_DOWN), new Input(Input.KEYBOARD_EVENT, "Right",
				KeyInput.KEY_DOWN));
		jump = new InputEvent("Jump", new Input(Input.KEYBOARD_EVENT, " ",
				KeyInput.KEY_PRESSED));
		inputs.addEvent(forward);
		inputs.addEvent(backward);
		inputs.addEvent(left);
		inputs.addEvent(right);
		inputs.addEvent(jump);

		space = new PhysicsSpace(new VerletIntegration(), new SAP(), new GJK(
				new EPA()), new LinearImpulseResolution(),
				new ProjectionCorrection(0.02f, 0.0f),
				new PersistentManifoldManager());
		space.setGlobalForce(new Vector3f(0, -8, 0));

		playerbody = PhysicsShapeCreator.create(player);
		playerbody.setMass(1f);
		playerbody.setLinearFactor(new Vector3f(1, 1, 1));
		playerbody.setAngularFactor(new Vector3f());
		playerbody.setRestitution(0);
		space.addRigidBody(player, playerbody);

		Box ground = new Box(0, -5, 0, 10, 1, 10);
		ground.setRenderHints(false, false, true);
		RigidBody3 rb = PhysicsShapeCreator.create(ground);
		space.addRigidBody(ground, rb);
		addObject(ground);

		Shader colorshader = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		// player.setShader(colorshader);

		edgeshader = new Shader(ShaderLoader.loadShaderPair(
				"res/shaders/edgeshader.vert", "res/shaders/edgeshader.geo",
				GLConstants.TRIANGLE_ADJACENCY, GLConstants.LINE_STRIP, 6));
	}

	@Override
	public void render() {
		renderScene();
		edgeshader.bind();
		renderScene();
		edgeshader.unbind();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void update(int delta) {
		float mousedx = 0;
		if (inputs.isMouseMoved()) {
			mousedx = -inputs.getMouseDX() * mousesensitivity;
			float mousedy = -inputs.getMouseDY() * mousesensitivity;
			cam.rotate(mousedx, mousedy);
			playerbody.rotate(new Vector3f(0, mousedx, 0));
		}

		Vector3f move = new Vector3f();
		if (forward.isActive()) {
			move = VecMath.addition(move, cam.getDirection());
		}
		if (backward.isActive()) {
			move = VecMath.subtraction(move, cam.getDirection());
		}
		if (left.isActive()) {
			move = VecMath.subtraction(move, VecMath.crossproduct(
					cam.getDirection(), new Vector3f(0, 1, 0)));
		}
		if (right.isActive()) {
			move = VecMath.addition(move, VecMath.crossproduct(
					cam.getDirection(), new Vector3f(0, 1, 0)));
		}
		if (move.length() > 0) {
			move.setY(0);
			move.normalize();
			move.scale(playerspeed);
		}
		if (jump.isActive()) {
			move = VecMath.addition(move, new Vector3f(0, 8, 0));
		}
		playerbody.setLinearVelocity(new Vector3f(move.x, playerbody
				.getLinearVelocity().y + move.y, move.z));

		space.update(delta);

		Vector3f offset = QuatMath.transform(playerbody.getRotation(),
				new Vector3f(0, 0, -1));
		offset.setY(playerheight * 2 / 3f + 2);
		cam.translateTo(VecMath.addition(playerbody.getTranslation(), offset));
	}

}
