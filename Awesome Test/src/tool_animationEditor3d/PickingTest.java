package tool_animationEditor3d;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import loader.ShaderLoader;
import matrix.Matrix4f;
import shader.Shader;
import shape.Box;
import shape.Sphere;
import sound.NullSoundEnvironment;
import utils.ProjectionHelper;
import vector.Vector2f;
import vector.Vector3f;
import vector.Vector4f;

public class PickingTest extends StandardGame {
	Matrix4f inverseprojectionmatrix;
	Sphere mousesphere1, mousesphere2;

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(800, 600, "Animation Editor", true, false),
				new PixelFormat().withSamples(0), new VideoSettings(), new NullSoundEnvironment());
		cam.setFlyCam(false);
		cam.translateTo(0, 0, 5);
		cam.rotate(40, 0);
		
		inverseprojectionmatrix = ProjectionHelper.perspective(settings.getFOVy(),
				settings.getResolutionX() / (float) settings.getResolutionY(), settings.getZNear(),
				settings.getZFar());
		inverseprojectionmatrix.invert();
		
		Shader defaultshader = new Shader(
				ShaderLoader.loadShaderFromFile("res/shaders/defaultshader.vert", "res/shaders/defaultshader.frag"));
		addShader(defaultshader);
		
		defaultshader.addObject(new Box(-1, 0, 0, 1, 1, 1));
		mousesphere1 = new Sphere(0, 0, 0, 0.02f, 32, 32);
		defaultshader.addObject(mousesphere1);
		mousesphere2 = new Sphere(0, 0, 0, 0.02f, 32, 32);
		defaultshader.addObject(mousesphere2);
	}

	@Override
	public void update(int delta) {
		Vector3f mousepos = screenPositionToRayDirection(inputs.getMouseX(), inputs.getMouseY());
		System.out.println("Res1 " + mousepos);
		mousepos.scale(3);
		mousesphere2.translateTo(cam.getTranslation());
		mousesphere2.translate(mousepos);
		
		Vector3f realresult = calculateMouseRay();
		System.out.println("Res2 " + realresult);
		realresult.scale(3);
		mousesphere1.translateTo(cam.getTranslation());
		mousesphere1.translate(realresult);
		
		
		cam.update(delta);
	}

	@Override
	public void render() {
		render3dLayer();
	}

	@Override
	public void render2d() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void renderInterface() {
		renderInterfaceLayer();
	}
	
	private Vector3f screenPositionToRayDirection(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / settings.getResolutionX() - 1f;
		float y = (2.0f * mouseY) / settings.getResolutionY() - 1f;
		Vector4f clipCoords = new Vector4f(x, -y, -1.0f, 1.0f);
		clipCoords.transform(inverseprojectionmatrix);
		clipCoords.z = -1.0f;
		clipCoords.w = 0;
		System.out.println("D1 " + clipCoords);
		
		Matrix4f invertedView = new Matrix4f(cam.getMatrix());
		invertedView.invert();
		clipCoords.transform(invertedView);
		Vector3f mouseRay = new Vector3f(clipCoords.x, clipCoords.y, clipCoords.z);
		mouseRay.normalize();
		
		return mouseRay;
	}
	
	private Vector3f calculateMouseRay() {
		float mouseX = inputs.getMouseX();
		float mouseY = inputs.getMouseY();
		Vector2f normalizedCoords = getNormalisedDeviceCoordinates(mouseX, mouseY);
		Vector4f clipCoords = new Vector4f(normalizedCoords.x, normalizedCoords.y, -1.0f, 1.0f);
		Vector4f eyeCoords = toEyeCoords(clipCoords);
		System.out.println("D2 " + eyeCoords);
		Vector3f worldRay = toWorldCoords(eyeCoords);
		return worldRay;
	}

	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedView = new Matrix4f(cam.getMatrix());
		invertedView.invert();
		eyeCoords.transform(invertedView);
		Vector3f mouseRay = new Vector3f(eyeCoords.x, eyeCoords.y, eyeCoords.z);
		mouseRay.normalize();
		return mouseRay;
	}

	private Vector4f toEyeCoords(Vector4f clipCoords) {
		clipCoords.transform(inverseprojectionmatrix);
		return new Vector4f(clipCoords.x, clipCoords.y, -1f, 0f);
	}

	private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
		float x = (2.0f * mouseX) / settings.getResolutionX() - 1f;
		float y = (2.0f * mouseY) / settings.getResolutionY() - 1f;
		return new Vector2f(x, -y);
	}
	
	public static void main(String[] args) {
		PickingTest pt = new PickingTest();
		pt.start();
	}

}
