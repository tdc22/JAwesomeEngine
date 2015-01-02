package physicsImpulse;

import game.Debugger;
import game.StandardGame;
import gui.DisplayMode;
import gui.GLDisplay;
import gui.PixelFormat;
import gui.VideoSettings;
import integration.EulerIntegration;
import loader.FontLoader;
import manifold.SimpleManifoldManager;
import narrowphase.EPA;
import narrowphase.GJK;
import objects.RigidBody3;
import physics.PhysicsShapeCreator;
import physics.PhysicsSpace;
import positionalcorrection.NullCorrection;
import quaternion.Quaternionf;
import resolution.NullResolution;
import shape.Box;
import vector.Vector3f;
import broadphase.SAP;

public class ImpulseTest extends StandardGame {
	PhysicsSpace space;
	Box b;
	RigidBody3 rb;
	Debugger debugmanager;
	int tempdelta = 0;
	boolean impulseapplied = false;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(), new PixelFormat(),
				new VideoSettings());
		cam.setFlyCam(true);
		cam.translateTo(0f, 0f, 5);
		cam.rotateTo(0, 0);

		debugmanager = new Debugger(inputs,
				FontLoader.loadFont("res/fonts/DejaVuSans.ttf"), cam);
		this.setRendering2d(true);
		// mouse.setGrabbed(false);

		space = new PhysicsSpace(new EulerIntegration(), new SAP(), new GJK(
				new EPA()), new NullResolution(), new NullCorrection(),
				new SimpleManifoldManager<Vector3f>());

		b = new Box(0, 0, 0, 1, 1, 1);
		rb = PhysicsShapeCreator.create(b);
		rb.setMass(1);
		rb.setInertia(new Quaternionf());
		space.addRigidBody(b, rb);
		addObject(b);
	}

	@Override
	public void render() {
		debugmanager.render3d();
		renderScene();
	}

	@Override
	public void render2d() {
		debugmanager.render2d(fps, objects.size(), objects2d.size());
		render2dScene();
	}

	@Override
	public void update(int delta) {
		if (tempdelta <= 2000)
			tempdelta += delta;
		else if (!impulseapplied) {
			System.out.println("Impulse!");
			rb.applyImpulse(new Vector3f(10f, 0, 0), new Vector3f(0, 1, 0));
			// rb.applyCentralImpulse(new Vector3f(0.01f, 0, 0));
			impulseapplied = true;
		}

		debugmanager.update();
		space.update(delta);
		cam.update(delta);
	}
}
