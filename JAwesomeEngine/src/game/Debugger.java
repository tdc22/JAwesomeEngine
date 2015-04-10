package game;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_STACK_UNDERFLOW;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;
import gui.Font;
import gui.Text;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;

import java.awt.Color;

import objects.ShapedObject;
import utils.GLConstants;
import vector.Vector3f;

public class Debugger {
	Font font;
	boolean showdata = false;
	boolean showaxis = false;
	boolean showgrid = false;
	boolean wireframe = false;
	boolean isFirstError = true;
	String firsterror;
	Text text;
	Camera cam;
	Vector3f range;
	private ShapedObject xaxis, yaxis, zaxis;
	InputEvent toggledata, toggleaxis, togglegrid, togglewireframe;

	public Debugger(InputManager i, Font f, Camera cam) {
		font = f;
		this.cam = cam;

		text = new Text("", 10, 10, font);

		xaxis = new ShapedObject();
		yaxis = new ShapedObject();
		zaxis = new ShapedObject();
		xaxis.setRenderMode(GLConstants.LINES);
		yaxis.setRenderMode(GLConstants.LINES);
		zaxis.setRenderMode(GLConstants.LINES);

		setRange(new Vector3f(1000, 1000, 1000));
		setupControls(i);
	}

	public void begin() {
		if (wireframe)
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}

	public void end() {
		if (wireframe)
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}

	public static String getGLErrorName(int glErrorID) {
		String glerror = "no error";
		if (glErrorID != GL_NO_ERROR) {
			switch (glErrorID) {
			case GL_INVALID_ENUM:
				glerror = "invalid enum";
				break;
			case GL_INVALID_VALUE:
				glerror = "invalid value";
				break;
			case GL_INVALID_OPERATION:
				glerror = "invalid operation";
				break;
			case GL_INVALID_FRAMEBUFFER_OPERATION:
				glerror = "invalid framebuffer operation";
				break;
			case GL_OUT_OF_MEMORY:
				glerror = "out of memory";
				break;
			case GL_STACK_UNDERFLOW:
				glerror = "stack underflow";
				break;
			case GL_STACK_OVERFLOW:
				glerror = "stack overflow";
				break;
			default:
				glerror = "unknown error";
			}
		}
		return glerror;
	}

	public boolean isAxisShown() {
		return showaxis;
	}

	public boolean isDataShown() {
		return showdata;
	}

	public boolean isGridShown() {
		return showgrid;
	}

	public boolean isRendering2d() {
		return showdata;
	}

	public boolean isRendering3d() {
		return showaxis || showgrid;
	}

	public boolean isWireframeRendered() {
		return wireframe;
	}

	public void render2d(int fps, int objects, int objects2d) {
		// if(wireframe) wireframeshader.unbind();
		if (showdata) {
			Vector3f campos = cam.getTranslation();

			if (isFirstError) {
				firsterror = getGLErrorName(glGetError());
				isFirstError = false;
			}

			text.setText("FPS: " + fps + " ("
					+ String.format("%.2f", 1000 / (float) fps)
					+ " ms)\nObjects: " + objects + "\n2d Objects: "
					+ objects2d + "\nPolygons:\nCamera: " + campos.x + "; "
					+ campos.y + "; " + campos.z + "\nGL-Error: "
					+ getGLErrorName(glGetError()) + " (" + firsterror + ")");
			text.render();
		}
	}

	public void render3d() {
		if (showaxis) {
			Vector3f campos = cam.getTranslation();
			if (campos.x >= range.x)
				xaxis.translateTo(campos.x, 0, 0);
			else
				xaxis.translateTo(range.x, 0, 0);
			if (campos.y >= range.y)
				yaxis.translateTo(0, campos.y, 0);
			else
				yaxis.translateTo(0, range.y, 0);
			if (campos.z >= range.z)
				zaxis.translateTo(0, 0, campos.z);
			else
				zaxis.translateTo(0, 0, range.z);
			xaxis.render();
			yaxis.render();
			zaxis.render();
		}

		if (showgrid) {
			glColor3f(1f, 1f, 1f);
			glBegin(GLConstants.LINES);
			for (int x = (int) -range.x; x < range.x; x++) {
				if (x != 0) {
					glVertex3f(x, 0, -range.z);
					glVertex3f(x, 0, range.z);
				} else {
					glVertex3f(x, 0, -range.z);
					glVertex3f(x, 0, 0);
				}
			}
			for (int z = (int) -range.z; z < range.x; z++) {
				if (z != 0) {
					glVertex3f(-range.x, 0, z);
					glVertex3f(range.x, 0, z);
				} else {
					glVertex3f(-range.x, 0, z);
					glVertex3f(0, 0, z);
				}
			}
			glEnd();
		}
	}

	public void setRange(Vector3f range) {
		this.range = range;
		xaxis.deleteData();
		yaxis.deleteData();
		zaxis.deleteData();
		xaxis.addVertex(new Vector3f(-range.x, 0, 0), Color.RED);
		xaxis.addVertex(new Vector3f(range.x, 0, 0), Color.RED);
		yaxis.addVertex(new Vector3f(0, -range.y, 0), Color.GREEN);
		yaxis.addVertex(new Vector3f(0, range.y, 0), Color.GREEN);
		zaxis.addVertex(new Vector3f(0, 0, -range.z), Color.BLUE);
		zaxis.addVertex(new Vector3f(0, 0, range.z), Color.BLUE);
		xaxis.addIndices(0, 1);
		yaxis.addIndices(0, 1);
		zaxis.addIndices(0, 1);
		xaxis.prerender();
		yaxis.prerender();
		zaxis.prerender();
	}

	public void setRenderWireframe(boolean w) {
		wireframe = w;
		if (!w)
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}

	public void setShowAxis(boolean a) {
		showaxis = a;
	}

	public void setShowData(boolean d) {
		showdata = d;
	}

	public void setShowGrid(boolean g) {
		showgrid = g;
	}

	private void setupControls(InputManager inputs) {
		toggledata = new InputEvent("debug_showdata", new Input(
				Input.KEYBOARD_EVENT, "F1", KeyInput.KEY_PRESSED));
		toggleaxis = new InputEvent("debug_showaxis", new Input(
				Input.KEYBOARD_EVENT, "F2", KeyInput.KEY_PRESSED));
		togglegrid = new InputEvent("debug_showgrid", new Input(
				Input.KEYBOARD_EVENT, "F3", KeyInput.KEY_PRESSED));
		togglewireframe = new InputEvent("debug_showwireframe", new Input(
				Input.KEYBOARD_EVENT, "F4", KeyInput.KEY_PRESSED));

		inputs.addEvent(toggledata);
		inputs.addEvent(toggleaxis);
		inputs.addEvent(togglegrid);
		inputs.addEvent(togglewireframe);
	}

	public void toggleAxis() {
		setShowAxis(!showaxis);
	}

	public void toggleData() {
		setShowData(!showdata);
	}

	public void toggleGrid() {
		setShowGrid(!showgrid);
	}

	public void toggleWireframe() {
		setRenderWireframe(!wireframe);
	}

	public void update() {
		if (toggledata.isActive())
			toggleData();
		if (toggleaxis.isActive())
			toggleAxis();
		if (togglegrid.isActive())
			toggleGrid();
		if (togglewireframe.isActive())
			toggleWireframe();
	}
}