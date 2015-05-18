package tutorialpX;

import game.StandardGame;
import gui.Font;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import integration.VerletIntegration;

import java.util.ArrayList;
import java.util.List;

import loader.FontLoader;
import loader.ShaderLoader;
import manifold.MultiPointManifoldManager;
import math.QuatMath;
import math.VecMath;
import narrowphase.EPA;
import narrowphase.GJK;
import objects.RigidBody3;
import physics.PhysicsDebug;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.ProjectionCorrection;
import resolution.LinearImpulseResolution;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import utils.Debugger;
import utils.GLConstants;
import vector.Vector3f;
import vector.Vector4f;
import broadphase.SAP;
import collisionshape.CylinderShape;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;

public class Tutorial extends StandardGame {
	InputEvent forward, backward, left, right, jump;
	PhysicsSpace space;
	RigidBody3 playerbody, groundchecker, spacerbody, goal;
	float playerspeed = 10;
	float mousesensitivity = 0.2f;
	Shader edgeshader;
	boolean onground = false;

	final float PLAYER_RADIUS = 0.7f;
	final float PLAYER_HEIGHT = 1.7f;
	final Vector3f PLAYER_START_POSITION = new Vector3f(0, 5, 0);
	final float GROUNDCHECKER_RADIUS = PLAYER_RADIUS - 0.1f;
	final float GROUNDCHECKER_HEIGHT = 0.01f;
	final float TINY_SPACE = 0.001f;

	final float STARTBOX_SIZE_X = 8f;
	final float STARTBOX_SIZE_Z = 8f;
	final float VOID_OUT_DEPTH = -100f;

	final int NUM_BLOCKS = 80;
	final float BLOCK_SIZE_MIN = 1f;
	final float BLOCK_SIZE_MAX = 5f;
	final float MIN_Y = -4f;
	final float MAX_Y = 3f;
	final float LEVEL_SIZE_X = 100f;
	final float LEVEL_SIZE_Z = 100f;
	List<Vector3f> colors;

	// TODO: REMOVE!!!
	Debugger debugger;
	PhysicsDebug physicsdebug;

	public void generateLevel() {
		float posx, posy, posz, sizex, sizey, sizez;
		float blocksizerange = BLOCK_SIZE_MAX - BLOCK_SIZE_MIN;
		float yrange = MAX_Y - MIN_Y;
		int colornum = colors.size();
		int blocknum = 0, color = 0;

		List<Shader> colorshaders = new ArrayList<Shader>();
		for (Vector3f c : colors) {
			colorshaders.add(new Shader(ShaderLoader.loadShaderFromFile(
					"res/shaders/colorshader.vert",
					"res/shaders/colorshader.frag"), "color", new Vector4f(c.x,
					c.y, c.z, 1)));
		}

		while (blocknum < NUM_BLOCKS) {
			posx = (float) (Math.random() * LEVEL_SIZE_X);
			posz = (float) (Math.random() * LEVEL_SIZE_Z);
			sizex = BLOCK_SIZE_MIN + (float) (Math.random() * blocksizerange);
			sizez = BLOCK_SIZE_MIN + (float) (Math.random() * blocksizerange);
			if (!((posx - sizex) <= STARTBOX_SIZE_X && (posz - sizez) <= STARTBOX_SIZE_Z)) {
				posy = MIN_Y + (float) (Math.random() * yrange);
				sizey = BLOCK_SIZE_MIN
						+ (float) (Math.random() * blocksizerange);
				color = (int) (Math.random() * colornum);

				Box box = new Box(posx, posy, posz, sizex, sizey, sizez);
				box.setRenderHints(false, false, true);
				box.setShader(colorshaders.get(color));
				addObject(box);
				RigidBody3 boxbody = new RigidBody3(
						PhysicsShapeCreator.create(box));
				boxbody.translateTo(box.getTranslation());
				space.addRigidBody(box, boxbody);

				blocknum++;
			}
		}
	}

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Tutorial",
				false), new PixelFormat(), new VideoSettings());
		display.bindMouse();
		cam.rotateTo(225, 0);

		Cylinder player = new Cylinder(PLAYER_START_POSITION, PLAYER_RADIUS,
				PLAYER_HEIGHT / 2f, 50);
		player.setRenderHints(false, false, true);
		addObject(player);

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
				new MultiPointManifoldManager());
		space.setGlobalGravitation(new Vector3f(0, -8f, 0));

		playerbody = new RigidBody3(PhysicsShapeCreator.create(player));
		playerbody.setMass(1f);
		playerbody.setLinearFactor(new Vector3f(1, 1, 1));
		playerbody.setAngularFactor(new Vector3f(0, 0, 0));
		playerbody.setRestitution(0);
		space.addRigidBody(player, playerbody);

		groundchecker = new RigidBody3(new CylinderShape(0, -999, 0,
				GROUNDCHECKER_RADIUS, GROUNDCHECKER_HEIGHT / 2f));
		groundchecker.setMass(1f);
		groundchecker.setLinearFactor(new Vector3f(0, 0, 0));
		groundchecker.setAngularFactor(new Vector3f(0, 0, 0));
		groundchecker.setRestitution(0);
		space.addRigidBody(groundchecker);

		spacerbody = new RigidBody3(new CylinderShape(PLAYER_START_POSITION,
				PLAYER_RADIUS + TINY_SPACE, PLAYER_HEIGHT / 2f));
		spacerbody.setMass(1f);
		spacerbody.setLinearFactor(new Vector3f(1, 0, 1));
		spacerbody.setAngularFactor(new Vector3f(0, 0, 0));
		spacerbody.setRestitution(0);
		// TODO: ignore collision between playerbody, groundchecker and
		// spacerbody, remove TINY_SPACE
		// space.addRigidBody(spacerbody);

		Box ground = new Box(0, 0, 0, STARTBOX_SIZE_X, 1, STARTBOX_SIZE_Z);
		ground.setRenderHints(false, false, true);
		RigidBody3 rb = new RigidBody3(PhysicsShapeCreator.create(ground));
		space.addRigidBody(ground, rb);
		addObject(ground);

		Box goalBox = new Box(LEVEL_SIZE_X - 5, MAX_Y + BLOCK_SIZE_MAX,
				LEVEL_SIZE_Z - 5, 0.2f, 0.2f, 0.2f);
		goalBox.setRenderHints(false, false, true);
		goal = new RigidBody3(PhysicsShapeCreator.create(goalBox));
		space.addRigidBody(goalBox, goal);
		addObject(goalBox);

		Shader playershader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		playershader.addArgumentName("color");
		playershader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		player.setShader(playershader);

		Shader goalshader = new Shader(playershader);
		goalshader.setArgument(0, new Vector4f(1f, 0f, 0f, 0.8f));

		goalBox.setShader(goalshader);

		edgeshader = new Shader(ShaderLoader.loadShaderFromFile(
				"res/shaders/edgeshader.vert", "res/shaders/edgeshader.geo",
				GLConstants.TRIANGLE_ADJACENCY, GLConstants.LINE_STRIP, 6));

		colors = new ArrayList<Vector3f>();
		colors.add(new Vector3f(0.92f, 0.92f, 0.92f));
		colors.add(new Vector3f(0.3f, 0.3f, 0.92f));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugger = new Debugger(inputs, font, cam);
		physicsdebug = new PhysicsDebug(inputs, font, space);

		generateLevel();
	}

	@Override
	public void render() {
		debugger.render3d();
		debugger.begin();
		renderScene();
		setShadersActive(false);
		if (!debugger.isWireframeRendered()) {
			edgeshader.bind();
			renderScene();
			edgeshader.unbind();
		}
		setShadersActive(true);
		physicsdebug.render3d();
	}

	@Override
	public void render2d() {
		debugger.end();
		debugger.render2d(fps, objects.size(), objects2d.size());
	}

	public void reset() {
		playerbody.translateTo(PLAYER_START_POSITION);
		playerbody.setLinearVelocity(new Vector3f(0, 0, 0));
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
		if (jump.isActive() && onground) {
			move = VecMath.addition(move, new Vector3f(0, 8, 0));
		}
		playerbody.setLinearVelocity(new Vector3f(move.x, playerbody
				.getLinearVelocity().y + move.y, move.z));
		if (playerbody.getTranslation().getY() <= VOID_OUT_DEPTH)
			reset();
		groundchecker.setTranslation(VecMath.subtraction(
				playerbody.getTranslation(), new Vector3f(0, PLAYER_HEIGHT / 2f
						+ GROUNDCHECKER_HEIGHT / 2f + TINY_SPACE, 0)));
		spacerbody.setTranslation(playerbody.getTranslation());

		goal.rotate(0.05f * delta, 0.05f * delta, 0.05f * delta);

		debugger.update();
		space.update(delta);
		physicsdebug.update();

		onground = space.hasCollision(groundchecker);

		if (space.hasCollision(playerbody, goal))
			System.out.println("Goal!");

		Vector3f offset = QuatMath.transform(playerbody.getRotation(),
				new Vector3f(0.70710677, 0, 0.70710677));
		offset.setY((PLAYER_HEIGHT * (6 / 8f)) / 2f);
		cam.translateTo(VecMath.addition(playerbody.getTranslation(), offset));
	}
}
