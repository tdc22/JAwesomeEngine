package demo_portal;

import java.util.Set;

import broadphase.DynamicAABBTree3;
import broadphase.DynamicAABBTree3Generic;
import collisionshape.CylinderShape;
import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Color;
import input.Input;
import input.InputEvent;
import input.KeyInput;
import input.MouseInput;
import integration.VerletIntegration;
import loader.FontLoader;
import loader.ModelLoader;
import loader.ShaderLoader;
import loader.TextureLoader;
import manifold.MultiPointManifoldManager;
import manifold.RaycastResult;
import math.QuatMath;
import math.VecMath;
import narrowphase.EPA;
import narrowphase.GJK;
import narrowphase.SupportRaycast;
import objects.Camera3;
import objects.CollisionShape;
import objects.CompoundObject3;
import objects.GhostObject3;
import objects.Ray3;
import objects.RigidBody;
import objects.RigidBody3;
import objects.ShapedObject2;
import objects.ShapedObject3;
import objects.ViewFrustum;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.ProjectionCorrection;
import quaternion.Quaternionf;
import resolution.SimpleLinearImpulseResolution;
import shader.Shader;
import shape.Box;
import shape.Cylinder;
import shape.Plane;
import shape.Sphere;
import sound.NullSoundEnvironment;
import texture.FramebufferObject;
import texture.Texture;
import utils.Debugger;
import utils.GLConstants;
import utils.Pair;
import utils.VectorConstants;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class PortalDemo extends StandardGame {
	InputEvent forward, backward, left, right, jump, shootleft, shootright;
	Debugger debugger;
	PhysicsSpace space;

	RigidBody3 playerbody, groundchecker;
	Cylinder playerbounds, playerclonebounds;
	float playerspeed = 6; // turn up the speed to ~10 to have fun ;)
	float mousesensitivity = 0.2f;
	boolean onground = false;
	ShapedObject3 playerclone;

	final String PLAYER_MODELNAME = "res/models/tmModel.dae";
	final float PLAYER_RADIUS = 0.55f;
	final float PLAYER_RADIUS_CAMERA = 0.65f;
	final float PLAYER_RADIUS_COLLISION = 0.7f;
	final float PLAYER_HEIGHT = 1.7f;
	final Vector3f PLAYER_START_POSITION = new Vector3f(0, 5, 0);
	final float GROUNDCHECKER_RADIUS = PLAYER_RADIUS_COLLISION - 0.1f;
	final float GROUNDCHECKER_HEIGHT = 0.1f;
	final float TINY_SPACE = 0.001f;
	final float RESPAWN_HEIGHT = -20;
	final float CAMERA_HEIGHT_OFFSET = PLAYER_HEIGHT * 0.375f;

	final float PORTAL_BOARDER_WIDTH = 0.02f;
	final int PORTAL_RESOLUTION_X = 800;
	final int PORTAL_RESOLUTION_Y = 800;
	final float PORTAL_VIEW_ASPECT = PORTAL_RESOLUTION_X / PORTAL_RESOLUTION_Y;

	final float DOT_FLOOR_CEILING_THRESHOLD = 0.01f;

	final Vector3f HIDDEN_SPAWN = new Vector3f(0, -10, 0);

	PortalShape portal1, portal2;
	Vector3f portal1Normal, portal2Normal;
	Camera3 portal1cam, portal2cam;
	ViewFrustum portal1frustum, portal2frustum;
	FramebufferObject portal1FB, portal2FB;
	CompoundObject3 portal1body, portal2body;
	GhostObject3 portal1checker, portal2checker;
	RigidBody<Vector3f, ?, ?, ?> portal1Hit, portal2Hit;
	float portal1RotH, portal2RotH;

	Sphere portalTestCamSphere1, portalTestCamSphere2;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(), new VideoSettings(),
				new NullSoundEnvironment());
		display.bindMouse();
		cam.setFlyCam(true);
		cam.translateTo(0.5f, 0f, 5);
		cam.rotateTo(0, 0);

		int defaultshaderID = ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert",
				"res/shaders/defaultshader.frag");
		int colorshaderID = ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert",
				"res/shaders/colorshader.frag");
		int textureshaderID = ShaderLoader.loadShaderFromFile("res/shaders/textureshader.vert",
				"res/shaders/textureshader.frag");
		int portalshaderID = ShaderLoader.loadShaderFromFile("res/shaders/portalshader.vert",
				"res/shaders/portalshader.frag");

		Shader defaultshader = new Shader(defaultshaderID);
		addShader(defaultshader);
		Shader defaultshaderInterface = new Shader(defaultshaderID);
		addShaderInterface(defaultshaderInterface);

		debugger = new Debugger(inputs, defaultshader, defaultshaderInterface,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);

		space = new PhysicsSpace(new VerletIntegration(), new DynamicAABBTree3(), new GJK(new EPA()),
				new SupportRaycast(), new SimpleLinearImpulseResolution(), new ProjectionCorrection(0.01f),
				new MultiPointManifoldManager());
		space.setGlobalGravitation(new Vector3f(0, -8f, 0));

		Texture texture = new Texture(TextureLoader.loadTexture("res/textures/grid.png"));
		Shader textureshader = new Shader(textureshaderID);
		textureshader.addArgumentName("u_texture");
		textureshader.addArgument(texture);
		addShader(textureshader);

		initLevel(textureshader);

		ShapedObject3 player = ModelLoader.load(PLAYER_MODELNAME);
		player.translate(0, 5, 0);
		player.rotate(90, 0, 0);
		player.scale(0.6f);
		playerclone = new ShapedObject3(player);
		player.setRenderHints(false, false, false);
		playerclone.setRenderHints(false, false, false);

		playerbounds = new Cylinder(PLAYER_START_POSITION, PLAYER_RADIUS, PLAYER_HEIGHT / 2f, 50);
		playerclonebounds = new Cylinder(HIDDEN_SPAWN, PLAYER_RADIUS, PLAYER_HEIGHT / 2f, 50);
		playerbounds.setRenderHints(false, false, false);
		playerclonebounds.setRenderHints(false, false, false);

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

		portal1 = new PortalShape(HIDDEN_SPAWN, 1, 2, 64);
		portal2 = new PortalShape(HIDDEN_SPAWN, 1, 2, 64);
		portal1.setRenderHints(false, true, false);
		portal2.setRenderHints(false, true, false);
		portalTestCamSphere1 = new Sphere(HIDDEN_SPAWN, 0.2f, 36, 36);
		portalTestCamSphere2 = new Sphere(HIDDEN_SPAWN, 0.2f, 36, 36);

		portal1cam = new Camera3();
		portal2cam = new Camera3();
		portal1frustum = new ViewFrustum(PORTAL_VIEW_ASPECT, settings.getZNear(), settings.getZFar(),
				settings.getFOVy());
		portal2frustum = new ViewFrustum(PORTAL_VIEW_ASPECT, settings.getZNear(), settings.getZFar(),
				settings.getFOVy());
		portal1FB = new FramebufferObject(layer3d, PORTAL_RESOLUTION_X, PORTAL_RESOLUTION_Y, 0, portal1cam,
				portal1frustum);
		portal2FB = new FramebufferObject(layer3d, PORTAL_RESOLUTION_X, PORTAL_RESOLUTION_Y, 0, portal2cam,
				portal2frustum);

		portal1Normal = new Vector3f();
		portal2Normal = new Vector3f();

		Shader portalShader1 = new Shader(portalshaderID);
		portalShader1.addArgument("u_color", new Vector3f(1f, 0f, 0f));
		portalShader1.addArgument("u_texture", portal2FB.getColorTexture());
		addShader(portalShader1);
		portalShader1.addObject(portal1);
		portalShader1.addObject(portalTestCamSphere1);

		Shader portalShader2 = new Shader(portalshaderID);
		portalShader2.addArgument("u_color", new Vector3f(0f, 1f, 0f));
		portalShader2.addArgument("u_texture", portal1FB.getColorTexture());
		addShader(portalShader2);
		portalShader2.addObject(portal2);
		portalShader2.addObject(portalTestCamSphere2);

		playerbody = new RigidBody3(
				new CylinderShape(player.getTranslation(), PLAYER_RADIUS_COLLISION, playerbounds.getHalfHeight()));
		playerbody.setMass(1f);
		playerbody.setLinearFactor(new Vector3f(1, 1, 1));
		playerbody.setAngularFactor(new Vector3f(0, 0, 0));
		playerbody.setRestitution(0);
		space.addRigidBody(player, playerbody);

		portalHitFilter1.setFirst(playerbody);
		portalHitFilter2.setFirst(playerbody);

		groundchecker = new RigidBody3(new CylinderShape(0, -999, 0, GROUNDCHECKER_RADIUS, GROUNDCHECKER_HEIGHT / 2f));
		groundchecker.setMass(1f);
		groundchecker.setLinearFactor(new Vector3f(0, 0, 0));
		groundchecker.setAngularFactor(new Vector3f(0, 0, 0));
		groundchecker.setRestitution(0);
		space.addRigidBody(groundchecker);
		space.addCollisionFilter(
				new Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>(playerbody, groundchecker));

		Shader playershader = new Shader(colorshaderID);
		playershader.addArgumentName("u_color");
		playershader.addArgument(new Vector4f(0f, 0f, 1f, 0.4f));
		addShader(playershader);
		playershader.addObject(player);
		playershader.addObject(playerclone);
		playershader.addObject(playerbounds);
		playershader.addObject(playerclonebounds);

		Shader crosshairshader = new Shader(colorshaderID);
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

		portal1body = new CompoundObject3(new DynamicAABBTree3Generic<CollisionShape<Vector3f, Quaternionf, ?>>());
		portal2body = new CompoundObject3(new DynamicAABBTree3Generic<CollisionShape<Vector3f, Quaternionf, ?>>());
		createCollisionForPortal(portal1, portal1body);
		createCollisionForPortal(portal2, portal2body);

		portal1checker = new GhostObject3(PhysicsShapeCreator.create(portal1));
		portal2checker = new GhostObject3(PhysicsShapeCreator.create(portal2));
		space.addGhostObject(portal1, portal1checker);
		space.addGhostObject(portal2, portal2checker);

		/*
		 * Shader portal1texturetest = new Shader(textureshaderID); Shader
		 * portal2texturetest = new Shader(textureshaderID);
		 * portal1texturetest.addArgument("u_texture", new
		 * Texture(portal1FB.getColorTextureID()));
		 * portal2texturetest.addArgument("u_texture", new
		 * Texture(portal2FB.getColorTextureID()));
		 * addShaderInterface(portal1texturetest);
		 * addShaderInterface(portal2texturetest);
		 * 
		 * Quad q1 = new Quad(100, 200, 100, 100); Quad q2 = new Quad(100, 400, 100,
		 * 100); q1.setRenderHints(false, true, false); q2.setRenderHints(false, true,
		 * false); portal1texturetest.addObject(q1); portal2texturetest.addObject(q2);
		 */
	}

	private void initLevel(Shader levelshader) {
		addPlane(levelshader, 0, 0, 0, 8, 8); // ground
		addPlane(levelshader, 0, 12, 0, 8, 8).rotate(180, 0, 0); // roof
		addPlane(levelshader, 8, 6, 0, 6, 8).rotate(0, 0, 90); // wall1
		addPlane(levelshader, -8, 6, 0, 6, 8).rotate(0, 0, -90); // wall2
		addPlane(levelshader, 0, 6, 8, 8, 6).rotate(-90, 0, 0); // wall3
		addPlane(levelshader, 0, 6, -8, 8, 6).rotate(90, 0, 0); // wall4
		addBox(levelshader, -6, 2, 6, 2, 2, 2);
		addBox(levelshader, -7, -1, -9, 4, 4, 4).rotate(45, 0, 0);
		addBox(levelshader, 6, 2.5f, -6.5f, 2, 2.5f, 1.5f);
		addBox(levelshader, 6.5f, -0.7f, 0, 1.5f, 3, 7).rotate(25, 0, 0);

		Sphere testsphere = new Sphere(0, 3, 0, 2, 36, 36);
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

	private Plane addPlane(Shader shader, float x, float y, float z, float width, float depth) {
		Plane plane = new Plane(x, y, z, width, depth, false);
		plane.setRenderHints(false, true, false);
		shader.addObject(plane);
		RigidBody3 b = new RigidBody3(PhysicsShapeCreator.create(plane));
		space.addRigidBody(plane, b);
		return plane;
	}

	private void createCollisionForPortal(PortalShape portal, CompoundObject3 portalbody) {
		Plane p1 = new Plane(VecMath.addition(portal.getTranslation(), new Vector3f(0, 0, portal1.getRadius() * 2)),
				portal1.getRadius(), PORTAL_BOARDER_WIDTH, true);
		Plane p2 = new Plane(VecMath.addition(portal.getTranslation(), new Vector3f(0, 0, -portal1.getRadius() * 2)),
				portal1.getRadius(), PORTAL_BOARDER_WIDTH, true);
		Plane p3 = new Plane(VecMath.addition(portal1.getTranslation(), new Vector3f(portal1.getRadius(), 0, 0)),
				PORTAL_BOARDER_WIDTH, 2 * portal1.getRadius(), true);
		Plane p4 = new Plane(VecMath.addition(portal1.getTranslation(), new Vector3f(-portal1.getRadius(), 0, 0)),
				PORTAL_BOARDER_WIDTH, 2 * portal1.getRadius(), true);
		p1.setColor(Color.RED);
		p2.setColor(Color.RED);
		p3.setColor(Color.RED);
		p4.setColor(Color.RED);
		portalbody.addCollisionShape(PhysicsShapeCreator.create(p1));
		portalbody.addCollisionShape(PhysicsShapeCreator.create(p2));
		portalbody.addCollisionShape(PhysicsShapeCreator.create(p3));
		portalbody.addCollisionShape(PhysicsShapeCreator.create(p4));
		space.addCompoundObject(portalbody, new ShapedObject3[] { p1, p2, p3, p4 });
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

	boolean wasCollidingPortal1 = false;
	boolean wasCollidingPortal2 = false;
	final Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>> portalHitFilter1 = new Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>(
			null, null);
	final Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>> portalHitFilter2 = new Pair<RigidBody<Vector3f, ?, ?, ?>, RigidBody<Vector3f, ?, ?, ?>>(
			null, null);

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
		groundchecker.setTranslation(
				VecMath.subtraction(playerbody.getTranslation(), new Vector3f(0, PLAYER_HEIGHT / 2f, 0)));

		debugger.update(fps, 0, 0);
		space.update(delta);
		// cam.update(delta);
		if (playerbody.getTranslation().y < RESPAWN_HEIGHT)
			playerbody.translateTo(PLAYER_START_POSITION);
		onground = space.hasCollision(groundchecker);

		Vector3f offset = QuatMath.transform(playerbody.getRotation(), new Vector3f(0, 0, -PLAYER_RADIUS_CAMERA));
		offset.setY(CAMERA_HEIGHT_OFFSET);
		cam.translateTo(VecMath.addition(playerbody.getTranslation(), offset));

		if (shootleft.isActive()) {
			determinePortalPosition(portal1, portal1body, true);
		}
		if (shootright.isActive()) {
			determinePortalPosition(portal2, portal2body, false);
		}

		adjustPortalCamera(portal1cam, portal1frustum, portal1.getTranslation(), portal2.getTranslation(),
				playerbody.getTranslation(), portal1Normal, portal1RotH);
		adjustPortalCamera(portal2cam, portal2frustum, portal2.getTranslation(), portal1.getTranslation(),
				playerbody.getTranslation(), portal2Normal, portal2RotH);
		portal1FB.updateTexture();
		portal2FB.updateTexture();
		portalTestCamSphere1.translateTo(portal1cam.getTranslation());
		portalTestCamSphere2.translateTo(portal2cam.getTranslation());

		boolean isCollidingPortal1 = space.hasCollision(portal1checker, playerbody);
		boolean isCollidingPortal2 = space.hasCollision(portal2checker, playerbody);
		// System.out.println(isCollidingPortal1 + "; " + isCollidingPortal2 +
		// "; " + wasCollidingPortal1 + "; " + wasCollidingPortal2);

		if (wasCollidingPortal1) {
			if (!isCollidingPortal1) {
				space.removeCollisionFilter(portalHitFilter1);
				playerclone.translateTo(HIDDEN_SPAWN);
				wasCollidingPortal1 = false;
			} else {
				adjustPlayerClonePosition(portal1, portal2);
				checkCameraPassPortal1();
			}
		} else {
			if (isCollidingPortal1) {
				space.addCollisionFilter(portalHitFilter1);
				adjustPlayerClonePosition(portal1, portal2);
				wasCollidingPortal1 = true;
				checkCameraPassPortal1();
			}
		}
		if (wasCollidingPortal2) {
			if (!isCollidingPortal2) {
				space.removeCollisionFilter(portalHitFilter2);
				playerclone.translateTo(HIDDEN_SPAWN);
				wasCollidingPortal2 = false;
			} else {
				adjustPlayerClonePosition(portal2, portal1);
				checkCameraPassPortal2();
			}
		} else {
			if (isCollidingPortal2) {
				space.addCollisionFilter(portalHitFilter2);
				adjustPlayerClonePosition(portal2, portal1);
				wasCollidingPortal2 = true;
				checkCameraPassPortal2();
			}
		}
		playerbounds.translateTo(playerbody.getTranslation());
		playerclonebounds.translateTo(playerclone.getTranslation());
	}

	private void checkCameraPassPortal1() {
		if (VecMath.dotproduct(portal1.getTranslation().x - cam.getTranslation().x,
				portal1.getTranslation().y - cam.getTranslation().y,
				portal1.getTranslation().z - cam.getTranslation().z, portal1Normal.x, portal1Normal.y,
				portal1Normal.z) > 0) {
			playerbody.translate(portal2.getTranslation().x - portal1.getTranslation().x,
					portal2.getTranslation().y - portal1.getTranslation().y,
					portal2.getTranslation().z - portal1.getTranslation().z);
			System.out.println("Portal 1");
			rotatePlayerAfterGoingThroughPortal(portal1Normal, portal2Normal);
			wasCollidingPortal2 = true;
			space.addCollisionFilter(portalHitFilter2);
		}
	}

	private void checkCameraPassPortal2() {
		if (VecMath.dotproduct(portal2.getTranslation().x - cam.getTranslation().x,
				portal2.getTranslation().y - cam.getTranslation().y,
				portal2.getTranslation().z - cam.getTranslation().z, portal2Normal.x, portal2Normal.y,
				portal2Normal.z) > 0) {
			playerbody.translate(portal1.getTranslation().x - portal2.getTranslation().x,
					portal1.getTranslation().y - portal2.getTranslation().y,
					portal1.getTranslation().z - portal2.getTranslation().z);
			System.out.println("Portal 2");
			rotatePlayerAfterGoingThroughPortal(portal2Normal, portal1Normal);
			wasCollidingPortal1 = true;
			space.addCollisionFilter(portalHitFilter1);
		}
	}

	private void adjustPlayerClonePosition(PortalShape p1, PortalShape p2) {
		playerclone.translateTo(p2.getTranslation().x + (p1.getTranslation().x - playerbody.getTranslation().x),
				p2.getTranslation().y + (p1.getTranslation().y - playerbody.getTranslation().y),
				p2.getTranslation().z + (p1.getTranslation().z - playerbody.getTranslation().z));
	}

	final Vector3f portalToPlayer = new Vector3f();
	final Vector2f portalTPH = new Vector2f();

	private void adjustPortalCamera(Camera3 camToMove, ViewFrustum portalFrustum, Vector3f portalPosition,
			Vector3f otherPortalPosition, Vector3f playerPosition, Vector3f portalNormal, float portalRotH) {
		portalToPlayer.set(playerPosition.x - otherPortalPosition.x, playerPosition.y - otherPortalPosition.y,
				playerPosition.z - otherPortalPosition.z);
		portalFrustum.update(PORTAL_VIEW_ASPECT, (float) portalToPlayer.length(), settings.getZFar(),
				settings.getFOVy());
		camToMove.translateTo(portalPosition.x + portalToPlayer.x, portalPosition.y + portalToPlayer.y,
				portalPosition.z + portalToPlayer.z);

		float rotH = 0;
		float rotV = 0;

		portalTPH.set(portalToPlayer.x, portalToPlayer.z);
		if (portalTPH.lengthSquared() > 0) {
			portalTPH.normalize();
			float dot = VecMath.dotproduct(portalReferenceUp, portalTPH);
			rotH = (float) Math.toDegrees(Math.acos(dot));

			Vector3f ref = VecMath.crossproduct(portalNormal, up);
			if (VecMath.dotproduct(portalTPH.x, portalTPH.y, ref.x, ref.y) < 0)
				rotH = -rotH;
		}

		// System.out.println(portalToPlayer + "; " + rotH + "; " + portalRotH);

		camToMove.rotateTo(portalRotH + rotH, rotV);
	}

	final Ray3 shootray = new Ray3(new Vector3f(), new Vector3f());
	final Vector3f portalside = new Vector3f(1, 0, 0);
	final Vector3f portalfront = new Vector3f(1, 0, 1);

	private void determinePortalPosition(ShapedObject3 portal, RigidBody3 portalbody, boolean portalOne) {
		shootray.setPosition(cam.getTranslation());
		shootray.setDirection(cam.getDirection());
		Set<RaycastResult<Vector3f>> hits = space.raycastAll(shootray);
		float currentclosest = Float.MAX_VALUE;
		RaycastResult<Vector3f> closest = null;
		for (RaycastResult<Vector3f> hit : hits) {
			if (!hit.getHitObject().equals(playerbody) && !hit.getHitObject().equals(portal1body)
					&& !hit.getHitObject().equals(portal1checker) && !hit.getHitObject().equals(portal2body)
					&& !hit.getHitObject().equals(portal2checker) && hit.getHitDistance() > 0
					&& hit.getHitDistance() < currentclosest) {
				currentclosest = hit.getHitDistance();
				closest = hit;
			}
		}

		if (closest != null) {
			portal.translateTo(closest.getHitPosition());
			// portal.rotateTo(playerbody.getRotation());
			Vector3f hitnormal = closest.getHitNormal();

			if (portalOne) {
				portal1Hit = closest.getHitObject();
				portalHitFilter1.setSecond(portal1Hit);
				portal1Normal.set(hitnormal);
			} else {
				portal2Hit = closest.getHitObject();
				portalHitFilter2.setSecond(portal2Hit);
				portal2Normal.set(hitnormal);
			}

			Vector3f a = VecMath.crossproduct(up, hitnormal);
			if (a.lengthSquared() > DOT_FLOOR_CEILING_THRESHOLD) {
				Vector2f rot = calculatePortalRotation(hitnormal);
				if (portalOne) {
					portal1RotH = rot.x;
				} else {
					portal2RotH = rot.x;
				}
				Quaternionf q = new Quaternionf();
				q.rotate(rot.x, up);
				q.rotate(rot.y, VectorConstants.AXIS_X);
				portal.rotateTo(q);
			} else {
				portal.rotateTo(playerbody.getRotation());
				if (hitnormal.y < 0) {
					portal.rotate(0, 0, 180);
				}
			}

			portalbody.translateTo(portal.getTranslation());
			portalbody.rotateTo(portal.getRotation());
		}
	}

	final Vector2f projXZ = new Vector2f();
	final Vector2f portalReferenceUp = new Vector2f(0, 1);
	final Vector2f up2 = new Vector2f(0, 1);
	final Vector2f projY = new Vector2f();

	private Vector2f calculatePortalRotation(Vector3f to) {

		// for H project "to" onto x/z
		// reference is front (0, 0, 1)
		projXZ.set(to.x, to.z);
		projXZ.normalize();
		float dot = VecMath.dotproduct(portalReferenceUp, projXZ);
		float angleH = (float) Math.toDegrees(Math.acos(dot));
		if (to.x < 0)
			angleH = -angleH;

		// for V project "to" onto y
		// reference is up (0, 1, 0)
		projY.y = to.y;
		dot = VecMath.dotproduct(up2, projY);
		float angleV = (float) Math.toDegrees(Math.acos(dot));

		return new Vector2f(angleH, angleV);
	}

	final Vector2f projectFromXZ = new Vector2f();
	final Vector2f projectToXZ = new Vector2f();

	private void rotatePlayerAfterGoingThroughPortal(Vector3f fromPortalNormal, Vector3f toPortalNormal) {
		projectFromXZ.set(fromPortalNormal.x, fromPortalNormal.z);
		if (projectFromXZ.lengthSquared() > 0) {
			projectFromXZ.normalize();
			projectToXZ.set(toPortalNormal.x, toPortalNormal.z);
			projectToXZ.normalize();
			float dot = VecMath.dotproduct(projectFromXZ.x, projectFromXZ.y, projectToXZ.x, projectToXZ.y);
			float angleV = 180 - (float) Math.toDegrees(Math.acos(dot));

			Vector3f a = VecMath.crossproduct(projectFromXZ.x, 0, projectFromXZ.y, 0, 1, 0);
			if (VecMath.dotproduct(a.x, a.z, toPortalNormal.x, toPortalNormal.z) < 0)
				angleV = -angleV;

			cam.rotate(angleV, 0);
			playerbody.rotate(0, angleV, 0);
		}
	}
}
