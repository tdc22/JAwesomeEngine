package tutorialp5;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.Font;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
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
import objects.RenderedObject;
import objects.RigidBody3;
import physics.PhysicsDebug;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.ProjectionCorrection;
import resolution.LinearImpulseResolution;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import utils.GLConstants;
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

	final float STARTBOX_SIZE_X = 8f;
	final float STARTBOX_SIZE_Z = 8f;

	final int NUM_BLOCKS = 80;
	final float BLOCK_SIZE_MIN = 1f;
	final float BLOCK_SIZE_MAX = 5f;
	final float MIN_Y = -4f;
	final float MAX_Y = 3f;
	final float LEVEL_SIZE = 100f;
	List<Vector3f> colors;
	
	//TODO: REMOVE!!!
	Debugger debugmanager;
	PhysicsDebug physicsdebug;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		display.bindMouse();
		cam.rotateTo(225, 0);

		Cylinder player = new Cylinder(0, 5, 0, playerradius,
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
				new MultiPointManifoldManager());
		space.setGlobalForce(new Vector3f(0, -8, 0));

		playerbody = PhysicsShapeCreator.create(player);
		playerbody.setMass(1f);
		playerbody.setLinearFactor(new Vector3f(1, 1, 1));
		playerbody.setAngularFactor(new Vector3f(0, 0, 0));
		playerbody.setRestitution(0);
		space.addRigidBody(player, playerbody);

		Box ground = new Box(0, 0, 0, STARTBOX_SIZE_X, 1, STARTBOX_SIZE_Z);
		ground.setRenderHints(false, false, true);
		RigidBody3 rb = PhysicsShapeCreator.create(ground);
		space.addRigidBody(ground, rb);
		addObject(ground);

		Shader colorshader = new Shader(ShaderLoader.loadShader(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		colorshader.addArgumentName("color");
		colorshader.addArgument(new Vector4f(1f, 0f, 0f, 1f));

		player.setShader(colorshader);

		edgeshader = new Shader(ShaderLoader.loadShader(
				"res/shaders/edgeshader.vert", "res/shaders/edgeshader.geo",
				GLConstants.TRIANGLE_ADJACENCY, GLConstants.LINE_STRIP, 6));

		colors = new ArrayList<Vector3f>();
		colors.add(new Vector3f(0.92f, 0.92f, 0.92f));
		colors.add(new Vector3f(0.35f, 0.35f, 0.92f));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		debugmanager = new Debugger(inputs, font, cam);
		setRendering2d(true);
		physicsdebug = new PhysicsDebug(inputs, font, space);
		
		generateLevel();
	}

	public void generateLevel() {
		float posx, posy, posz, sizex, sizey, sizez;
		float blocksizerange = BLOCK_SIZE_MAX - BLOCK_SIZE_MIN;
		float yrange = MAX_Y - MIN_Y;
		int colornum = colors.size();
		int blocknum = 0, color = 0;
		
		List<Shader> colorshaders = new ArrayList<Shader>();
		for(Vector3f c : colors) {
			colorshaders.add(new Shader(ShaderLoader.loadShader(
				"res/shaders/colorshader.vert", "res/shaders/colorshader.frag"), "color", new Vector4f(c.x, c.y, c.z, 1)));
		}
		
		while (blocknum < NUM_BLOCKS) {
			posx = (float) (Math.random() * LEVEL_SIZE);
			posz = (float) (Math.random() * LEVEL_SIZE);
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
				RigidBody3 boxbody = PhysicsShapeCreator.create(box);
				boxbody.translateTo(box.getTranslation());
				space.addRigidBody(box, boxbody);
				System.out.println(box.getTranslation() + "; " + boxbody.getTranslation());

				blocknum++;
			}
		}
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
		setShadersActive(false);
		edgeshader.bind();
		renderScene();
		edgeshader.unbind();
		setShadersActive(true);
		physicsdebug.render3d();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size(), objects2d.size());
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

		debugmanager.update();
		space.update(delta);
		physicsdebug.update();

		Vector3f offset = QuatMath.transform(playerbody.getRotation(),
				new Vector3f(0.70710677, 0, 0.70710677));
		offset.setY((playerheight * (6 / 8f))/2f);
		cam.translateTo(VecMath.addition(playerbody.getTranslation(), offset));
//		System.out.println(cam.getTranslation() + "; " + space.getObjects().size());
//		for(RenderedObject obj : this.getObjects())
//			System.out.println(obj.getTranslation());
//		System.out.println("-------------------------------------------------------------");
	}

}
