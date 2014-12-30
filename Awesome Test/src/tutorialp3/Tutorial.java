package tutorialp3;

import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.VerletIntegration;
import manifold.PersistentManifoldManager;
import math.VecMath;
import narrowphase.EPA;
import narrowphase.GJK;
import objects.RigidBody3;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.ProjectionCorrection;
import resolution.LinearImpulseResolution;
import shape.Box;
import vector.Vector3f;
import broadphase.SAP;

public class Tutorial extends StandardGame {
	InputEvent forward, backward, left, right, jump;
	PhysicsSpace space;
	RigidBody3 playerbody;
	float playerspeed = 10;
	Vector3f playersize = new Vector3f(1, 1.7f, 1);
	float mousesensitivity = 0.2f;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);
		cam.setRotationCenter(new Vector3f(0, 0, -playersize.z * 0.5f));

		Box player = new Box(new Vector3f(), playersize);
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
		playerbody.setLinearDamping(0);
		playerbody.setAngularDamping(0);
		playerbody.setRestitution(0);
		space.addRigidBody(player, playerbody);

		Box ground = new Box(0, -5, 0, 10, 1, 10);
		RigidBody3 rb = PhysicsShapeCreator.create(ground);
		space.addRigidBody(ground, rb);
		addObject(ground);
	}

	@Override
	public void update(int delta) {
		float mousedx = 0;
		if (inputs.isMouseMoved()) {
			mousedx = -inputs.getMouseDX() * mousesensitivity;
			float mousedy = -inputs.getMouseDY() * mousesensitivity;
			cam.rotate((float) Math.toRadians(mousedx),
					(float) Math.toRadians(mousedy));
			System.out.println(mousedx);
		}
		playerbody.setAngularVelocity(new Vector3f(0, mousedx, 0));

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
			move.normalize();
			move.scale(playerspeed);
		}
		if (jump.isActive()) {
			move = VecMath.addition(move, new Vector3f(0, 6, 0));
		}
		playerbody.setLinearVelocity(new Vector3f(move.x, playerbody
				.getLinearVelocity().y + move.y, move.z));

		space.update(delta);

		cam.translateTo(VecMath.addition(playerbody.getTranslation(),
				new Vector3f(playersize.x * 0.5f, playersize.y * 0.8f,
						playersize.z)));
	}

	@Override
	public void render() {
		renderScene();
	}

	@Override
	public void render2d() {

	}

}
