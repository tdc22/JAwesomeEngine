package demo_portal;

import java.util.Set;

import broadphase.DynamicAABBTree3;
import collisionshape.CylinderShape;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import input.MouseInput;
import integration.VerletIntegration;
import loader.FontLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import manifold.MultiPointManifoldManager;
import manifold.RaycastResult;
import math.QuatMath;
import math.VecMath;
import narrowphase.EPA;
import narrowphase.GJK;
import narrowphase.SupportRaycast;
import objects.Ray3;
import objects.RigidBody3;
import objects.ShapedObject2;
import objects.ShapedObject3;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.ProjectionCorrection;
import quaternion.Quaternionf;
import resolution.SimpleLinearImpulseResolution;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import shape.Sphere;
import sound.NullSoundEnvironment;
import texture.Texture;
import utils.Debugger;
import utils.GLConstants;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class PortalDemo extends StandardGame {
	InputEvent forward, backward, left, right, jump, shootleft, shootright;
	Debugger debugger;
	PhysicsSpace space;

	RigidBody3 playerbody, groundchecker;
	float playerspeed = 6; // turn up the speed to ~10 to have fun ;)
	float mousesensitivity = 0.2f;
	boolean onground = false;

	final float PLAYER_RADIUS = 0.7f;
	final float PLAYER_HEIGHT = 1.7f;
	final Vector3f PLAYER_START_POSITION = new Vector3f(0, 5, 0);
	final float GROUNDCHECKER_RADIUS = PLAYER_RADIUS - 0.1f;
	final float GROUNDCHECKER_HEIGHT = 0.05f;
	final float TINY_SPACE = 0.001f;

	final float PORTAL_HEIGHT = 0.001f;

	Cylinder portal1, portal2;

	@Override
	public void init() {
		useFBO = false;
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		space = new PhysicsSpace(new VerletIntegration(), new DynamicAABBTree3(), new GJK(new EPA()),
				new SupportRaycast(), new SimpleLinearImpulseResolution(), new ProjectionCorrection(0.01f),
				new MultiPointManifoldManager());
		space.setGlobalGravitation(new Vector3f(0, -8f, 0));

		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/grid.png"));
		Shader textureshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert", "res/shaders/textureshader.frag"));
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(texture);
		addShader(textureshader);

		// TODO: introduce plane shape and physics classes (reduces number of
		// triangles in GRAM!)
		initLevel(textureshader);

		Cylinder player = new Cylinder(PLAYER_START_POSITION, PLAYER_RADIUS, PLAYER_HEIGHT / 2f, 50);
		player.setRenderHints(false, false, true);

		forward = new InputEvent("Forward", new Input(Input.KEYBOARD_EVENT, "W", KeyInput.KEY_DOWN),
				new Input(Input.KEYBOARD_EVENT, "Up", KeyInput.KEY_DOWN));
		backward = new InputEvent("Backward", new Input(Input.KEYBOARD_EVENT, "S", KeyInput.KEY_DOWN),
				new Input(Input.KEYBOARD_EVENT, "Down", KeyInput.KEY_DOWN));
		left = new InputEvent("Left", new Input(Input.KEYBOARD_EVENT, "A", KeyInput.KEY_DOWN),
				new Input(Input.KEYBOARD_EVENT, "Left", KeyInput.KEY_DOWN));
		right = new InputEvent("Right", new Input(Input.KEYBOARD_EVENT, "D", KeyInput.KEY_DOWN),
				new Input(Input.KEYBOARD_EVENT, "Right", KeyInput.KEY_DOWN));
		jump = new InputEvent("Jump", new Input(Input.KEYBOARD_EVENT, " ", KeyInput.KEY_PRESSED));
		inputs.addEvent(forward);
		inputs.addEvent(backward);
		inputs.addEvent(left);
		inputs.addEvent(right);
		inputs.addEvent(jump);

		shootleft = new InputEvent("ShootLeft", new Input(Input.MOUSE_EVENT, "Left", MouseInput.MOUSE_BUTTON_PRESSED));
		shootright = new InputEvent("ShootRight",
				new Input(Input.MOUSE_EVENT, "Right", MouseInput.MOUSE_BUTTON_PRESSED));
		inputs.addEvent(shootleft);
		inputs.addEvent(shootright);

		portal1 = new Cylinder(0, 9, 0, 1, PORTAL_HEIGHT, 32);
		portal2 = new Cylinder(0, 9, 0, 1, PORTAL_HEIGHT, 32);
		portal1.scale(1, 1, 2);
		portal2.scale(1, 1, 2);

		Shader portalShader1 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		portalShader1.addArgumentName("u_color");
		portalShader1.addArgument(new Vector4f(1f, 0f, 0f, 1f));
		addShader(portalShader1);
		portalShader1.addObject(portal1);

		Shader portalShader2 = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		portalShader2.addArgumentName("u_color");
		portalShader2.addArgument(new Vector4f(0f, 1f, 0f, 1f));
		addShader(portalShader2);
		portalShader2.addObject(portal2);

		playerbody = new RigidBody3(PhysicsShapeCreator.create(player));
		playerbody.setMass(1f);
		playerbody.setLinearFactor(new Vector3f(1, 1, 1));
		playerbody.setAngularFactor(new Vector3f(0, 0, 0));
		playerbody.setRestitution(0);
		space.addRigidBody(player, playerbody);

		groundchecker = new RigidBody3(new CylinderShape(0, -999, 0, GROUNDCHECKER_RADIUS, GROUNDCHECKER_HEIGHT / 2f));
		groundchecker.setMass(1f);
		groundchecker.setLinearFactor(new Vector3f(0, 0, 0));
		groundchecker.setAngularFactor(new Vector3f(0, 0, 0));
		groundchecker.setRestitution(0);
		space.addRigidBody(groundchecker);

		Shader playershader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		playershader.addArgumentName("u_color");
		playershader.addArgument(new Vector4f(0f, 0f, 1f, 1f));
		addShader(playershader);
		playershader.addObject(player);

		Shader crosshairshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
		crosshairshader.addArgumentName("u_color");
		crosshairshader.addArgument(new Vector4f(0f, 0f, 0f, 1f));
		addShaderInterface(crosshairshader);

		ShapedObject2 crosshair = new ShapedObject2();
		crosshair.setRenderMode(GLConstants.LINES);
		float halfwidth = settings.getResolutionX() / 2f;
		float halfheight = settings.getResolutionY() / 2f;
		int size = 10;
		crosshair.addVertex(new Vector2f(halfwidth - size, halfheight));
		crosshair.addVertex(new Vector2f(halfwidth + size, halfheight));
		crosshair.addVertex(new Vector2f(halfwidth, halfheight - size));
		crosshair.addVertex(new Vector2f(halfwidth, halfheight + size));
		crosshair.addIndices(0, 1, 2, 3);
		crosshair.prerender();
		crosshairshader.addObject(crosshair);

		int shaderprogram = ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert",
				"res/shaders/colorshader.frag");
		Shader hitmarkershader = new Shader(shaderprogram);
		Shader hitnormalshader = new Shader(shaderprogram);
		hitmarkershader.addArgument("u_color", new Vector4f(0f, 1f, 0f, 1f));
		hitnormalshader.addArgument("u_color", new Vector4f(0f, 0f, 1f, 1f));
		addShader(hitmarkershader);
		addShader(hitnormalshader);
	}

	private void initLevel(Shader levelshader) {
		addBox(levelshader, 0, -1, 0, 8, 1, 8); // ground
		addBox(levelshader, 0, 12, 0, 8, 0, 8); // roof
		addBox(levelshader, 8, 6, 0, 0, 6, 8); // wall1
		addBox(levelshader, -8, 6, 0, 0, 6, 8); // wall2
		addBox(levelshader, 0, 6, 8, 8, 6, 0); // wall3
		addBox(levelshader, 0, 6, -8, 8, 6, 0); // wall4
		addBox(levelshader, -6, 2, 6, 2, 2, 2);
		addBox(levelshader, -7, -1, -9, 4, 4, 4).rotate(45, 0, 0);
		addBox(levelshader, 6, 2.5f, -6.5f, 2, 2.5f, 1.5f);
		addBox(levelshader, 6.5f, -0.7f, 0, 1.5f, 3, 7).rotate(25, 0, 0);

		Sphere testsphere = new Sphere(0, 3, 0, 3, 36, 36);
		testsphere.setRenderHints(false, true, false);
		levelshader.addObject(testsphere);
		RigidBody3 s = new RigidBody3(PhysicsShapeCreator.create(testsphere));
		space.addRigidBody(testsphere, s);
	}

	private Box addBox(Shader shader, float x, float y, float z, float width, float height, float depth) {
		Box box = new Box(x, y, z, width, height, depth);
		box.setRenderHints(false, true, false);
		shader.addObject(box);
		RigidBody3 b = new RigidBody3(PhysicsShapeCreator.create(box));
		space.addRigidBody(box, b);
		return box;
	}

	@Override
	public void render() {
		debugger.begin();
		render3dLayer();
	}

	@Override
	public void render2d() {

	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
		debugger.end();
	}

	final Vector3f move = new Vector3f();
	final Vector3f down = new Vector3f(0, -1, 0);
	final Vector3f up = new Vector3f(0, 1, 0);
	final Vector3f jumpimpulse = new Vector3f(0, 5, 0);

	Vector3f testvec = new Vector3f(0, 1, 0);
	Quaternionf rotV = new Quaternionf();
	Quaternionf rotH = new Quaternionf();

	@Override
	public void update(int delta) {
		float mousedx = 0;
		if (inputs.isMouseMoved()) {
			mousedx = -inputs.getMouseX() * mousesensitivity;
			float mousedy = -inputs.getMouseY() * mousesensitivity;
			cam.rotate(mousedx, mousedy);
			playerbody.rotate(0, mousedx, 0);
		}

		move.set(0, 0, 0);
		if (forward.isActive()) {
			move.translate(cam.getDirection());
		}
		if (backward.isActive()) {
			move.translate(-cam.getDirection().x, -cam.getDirection().y, -cam.getDirection().z);
		}
		if (left.isActive()) {
			move.translate(VecMath.crossproduct(cam.getDirection(), down));
		}
		if (right.isActive()) {
			move.translate(VecMath.crossproduct(cam.getDirection(), up));
		}
		if (move.lengthSquared() > 0) {
			move.setY(0);
			move.normalize();
			move.scale(playerspeed);
		}
		if (jump.isActive() && onground) {
			move.translate(jumpimpulse);
		}

		playerbody.setLinearVelocity(new Vector3f(move.x, playerbody.getLinearVelocity().y + move.y, move.z));
		groundchecker.setTranslation(VecMath.subtraction(playerbody.getTranslation(),
				new Vector3f(0, PLAYER_HEIGHT / 2f + GROUNDCHECKER_HEIGHT / 2f + TINY_SPACE, 0)));

		debugger.update(fps, 0, 0);
		space.update(delta);
		// cam.update(delta);
		onground = space.hasCollision(groundchecker);

		Vector3f offset = QuatMath.transform(playerbody.getRotation(), new Vector3f(0, 0, -1));
		offset.setY(PLAYER_HEIGHT * 0.375f);
		cam.translateTo(VecMath.addition(playerbody.getTranslation(), offset));

		if (shootleft.isActive()) {
			determinePortalPosition(portal1);
		}
		if (shootright.isActive()) {
			determinePortalPosition(portal2);
		}
	}

	final Ray3 shootray = new Ray3(new Vector3f(), new Vector3f());
	final Vector3f portalside = new Vector3f(1, 0, 0);
	final Vector3f portalfront = new Vector3f(1, 0, 1);

	private void determinePortalPosition(ShapedObject3 portal) {
		shootray.setPosition(cam.getTranslation());
		shootray.setDirection(cam.getDirection());
		Set<RaycastResult<Vector3f>> hits = space.raycastAll(shootray);
		float currentclosest = Float.MAX_VALUE;
		RaycastResult<Vector3f> closest = null;
		for (RaycastResult<Vector3f> hit : hits) {
			if (!hit.getHitObject().equals(playerbody) && hit.getHitDistance() > 0
					&& hit.getHitDistance() < currentclosest) {
				currentclosest = hit.getHitDistance();
				closest = hit;
			}
		}

		if (closest != null) {
			portal.translateTo(closest.getHitPosition());
			// portal.rotateTo(playerbody.getRotation());
			Vector3f hitnormal = closest.getHitNormal();

			Quaternionf q = new Quaternionf();
			portal.rotateTo(q);
			Vector3f a = VecMath.crossproduct(up, hitnormal);
			if (a.lengthSquared() > 0) {
				Quaternionf rot = calculatePortalRotation(hitnormal);
				portal.rotate(rot);

			} else {
				portal.rotateTo(playerbody.getRotation());
			}
		}
	}

	private Quaternionf calculatePortalRotation(Vector3f to) {
		Quaternionf q = new Quaternionf();

		// for H project "to" onto x/z
		// reference is front (0, 0, 1)
		Vector3f a = new Vector3f(to.x, 0, to.z);
		a.normalize();
		float dot = VecMath.dotproduct(new Vector3f(0, 0, 1), a);
		float angleH = (float) Math.toDegrees(Math.acos(dot));
		if (to.x < 0)
			angleH = -angleH;
		q.rotate(angleH, new Vector3f(0, 1, 0));

		// for V project "to" onto y
		// reference is up (0, 1, 0)
		dot = VecMath.dotproduct(new Vector2f(0, 1), new Vector2f(0, to.y));
		float angleV = (float) Math.toDegrees(Math.acos(dot));
		q.rotate(angleV, new Vector3f(1, 0, 0));

		return q;
	}
}
