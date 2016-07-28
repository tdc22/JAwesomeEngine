package utils;

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
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;

import java.awt.Color;

import gui.Font;
import gui.Text;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;
import objects.Camera3;
import objects.ShapedObject3;
import shader.Shader;
import vector.Vector3f;

public class Debugger {
	public static String getGLErrorName(int glErrorID) {
		String glerror = StringConstants.GLERROR_NO_ERROR;
		if (glErrorID != GL_NO_ERROR) {
			switch (glErrorID) {
			case GL_INVALID_ENUM:
				glerror = StringConstants.GLERROR_INVALID_ENUM;
				break;
			case GL_INVALID_VALUE:
				glerror = StringConstants.GLERROR_INVALID_VALUE;
				break;
			case GL_INVALID_OPERATION:
				glerror = StringConstants.GLERROR_INVALID_OPERATION;
				break;
			case GL_INVALID_FRAMEBUFFER_OPERATION:
				glerror = StringConstants.GLERROR_INVALID_FBO;
				break;
			case GL_OUT_OF_MEMORY:
				glerror = StringConstants.GLERROR_OUT_OF_MEMORY;
				break;
			case GL_STACK_UNDERFLOW:
				glerror = StringConstants.GLERROR_STACK_UNDERFLOW;
				break;
			case GL_STACK_OVERFLOW:
				glerror = StringConstants.GLERROR_STACK_OVERFLOW;
				break;
			default:
				glerror = StringConstants.GLERROR_UNKNOWN_ERROR;
			}
		}
		return glerror;
	}

	boolean showdata = false;
	boolean showaxis = false;
	boolean showgrid = false;
	boolean wireframe = false;
	boolean isFirstError = true;
	String firsterror;
	Text text;
	Camera3 cam;
	Vector3f range;
	private ShapedObject3 xaxis, yaxis, zaxis, grid, gridXZAxis;

	InputEvent toggledata, toggleaxis, togglegrid, togglewireframe;

	public Debugger(InputManager input, Shader shader, Shader shader2d, Font font, Camera3 cam) {
		this.cam = cam;

		text = new Text("", 10, 20, font);
		shader2d.addObject(text);

		xaxis = new ShapedObject3();
		yaxis = new ShapedObject3();
		zaxis = new ShapedObject3();
		grid = new ShapedObject3();
		gridXZAxis = new ShapedObject3();
		xaxis.setRenderMode(GLConstants.LINES);
		yaxis.setRenderMode(GLConstants.LINES);
		zaxis.setRenderMode(GLConstants.LINES);
		grid.setRenderMode(GLConstants.LINES);
		gridXZAxis.setRenderMode(GLConstants.LINES);
		xaxis.setRendered(false);
		yaxis.setRendered(false);
		zaxis.setRendered(false);
		grid.setRendered(false);
		gridXZAxis.setRendered(false);
		shader.addObjects(xaxis, yaxis, zaxis, grid, gridXZAxis);

		setRange(new Vector3f(1000, 1000, 1000));
		setupEvents(input);
	}

	public void begin() {
		if (wireframe)
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	}

	public void end() {
		if (wireframe)
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
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

	public void setRange(Vector3f range) {
		this.range = range;
		xaxis.deleteData();
		yaxis.deleteData();
		zaxis.deleteData();
		grid.deleteData();
		gridXZAxis.deleteData();
		xaxis.addVertex(new Vector3f(-range.x, 0, 0), Color.RED);
		xaxis.addVertex(new Vector3f(range.x, 0, 0), Color.RED);
		yaxis.addVertex(new Vector3f(0, -range.y, 0), Color.GREEN);
		yaxis.addVertex(new Vector3f(0, range.y, 0), Color.GREEN);
		zaxis.addVertex(new Vector3f(0, 0, -range.z), Color.BLUE);
		zaxis.addVertex(new Vector3f(0, 0, range.z), Color.BLUE);
		for (int x = (int) -range.x; x < range.x; x++) {
			if (x != 0) {
				grid.addVertex(new Vector3f(x, 0, -range.z), Color.WHITE);
				grid.addVertex(new Vector3f(x, 0, range.z), Color.WHITE);
			} else {
				grid.addVertex(new Vector3f(x, 0, -range.z), Color.WHITE);
				grid.addVertex(new Vector3f(x, 0, 0), Color.WHITE);
			}
		}
		for (int z = (int) -range.z; z < range.z; z++) {
			if (z != 0) {
				grid.addVertex(new Vector3f(-range.x, 0, z), Color.WHITE);
				grid.addVertex(new Vector3f(range.x, 0, z), Color.WHITE);
			} else {
				grid.addVertex(new Vector3f(-range.x, 0, z), Color.WHITE);
				grid.addVertex(new Vector3f(0, 0, z), Color.WHITE);
			}
		}
		gridXZAxis.addVertex(new Vector3f(-range.x, 0, 0), Color.WHITE);
		gridXZAxis.addVertex(new Vector3f(range.x, 0, 0), Color.WHITE);
		gridXZAxis.addVertex(new Vector3f(0, 0, -range.z), Color.WHITE);
		gridXZAxis.addVertex(new Vector3f(0, 0, range.z), Color.WHITE);

		xaxis.addIndices(0, 1);
		yaxis.addIndices(0, 1);
		zaxis.addIndices(0, 1);
		for (int i = 0; i < (2 * (int) range.x + 2 * (int) range.z) * 2; i++)
			grid.addIndex(i);
		gridXZAxis.addIndices(0, 1, 2, 3);

		xaxis.prerender();
		yaxis.prerender();
		zaxis.prerender();
		grid.prerender();
		gridXZAxis.prerender();
	}

	public void setRenderWireframe(boolean w) {
		wireframe = w;
		if (!w)
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
	}

	public void setShowAxis(boolean a) {
		xaxis.setRendered(a);
		yaxis.setRendered(a);
		zaxis.setRendered(a);
		if (showgrid) {
			gridXZAxis.setRendered(!a);
		}
		showaxis = a;
	}

	public void setShowData(boolean d) {
		text.setRendered(d);
		showdata = d;
	}

	public void setShowGrid(boolean g) {
		grid.setRendered(g);
		if (!showaxis) {
			gridXZAxis.setRendered(g);
		}
		showgrid = g;
	}

	private void setupEvents(InputManager inputs) {
		toggledata = new InputEvent("debug_showdata", new Input(Input.KEYBOARD_EVENT, "F1", KeyInput.KEY_PRESSED));
		toggleaxis = new InputEvent("debug_showaxis", new Input(Input.KEYBOARD_EVENT, "F2", KeyInput.KEY_PRESSED));
		togglegrid = new InputEvent("debug_showgrid", new Input(Input.KEYBOARD_EVENT, "F3", KeyInput.KEY_PRESSED));
		togglewireframe = new InputEvent("debug_showwireframe",
				new Input(Input.KEYBOARD_EVENT, "F4", KeyInput.KEY_PRESSED));

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

	private final StringBuilder stringbuilder = new StringBuilder();

	public void update(int fps, int objects, int objects2d) {
		if (toggledata.isActive())
			toggleData();
		if (toggleaxis.isActive())
			toggleAxis();
		if (togglegrid.isActive())
			toggleGrid();
		if (togglewireframe.isActive())
			toggleWireframe();

		if (showdata) {
			Vector3f campos = cam.getTranslation();

			if (isFirstError) {
				firsterror = getGLErrorName(glGetError());
				isFirstError = false;
			}

			stringbuilder.setLength(0);
			stringbuilder.append("FPS: ");
			stringbuilder.append(fps);
			stringbuilder.append(" (");
			stringbuilder.append(String.format("%.2f", 1000 / (float) fps));
			stringbuilder.append(" ms)\nObjects: ");
			stringbuilder.append(objects);
			stringbuilder.append("\n2d Objects: ");
			stringbuilder.append(objects2d);
			stringbuilder.append("\nPolygons:\nCamera: ");
			stringbuilder.append(campos.x);
			stringbuilder.append("; ");
			stringbuilder.append(campos.y);
			stringbuilder.append("; ");
			stringbuilder.append("\nGL-Error: ");
			stringbuilder.append(getGLErrorName(glGetError()));
			stringbuilder.append(" (");
			stringbuilder.append(firsterror);
			stringbuilder.append(")");
			text.setText(stringbuilder.toString());
		}
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
		}
	}
}