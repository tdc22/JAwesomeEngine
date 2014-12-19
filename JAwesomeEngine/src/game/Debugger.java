package game;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex3f;
import gui.Font;
import gui.Text;
import input.Input;
import input.InputEvent;
import input.InputManager;
import input.KeyInput;

import java.awt.Color;

import objects.ShapedObject;
import vector.Vector3f;

public class Debugger {
	Font font;
	boolean showdata = false;
	boolean showaxis = false;
	boolean showgrid = false;
	boolean wireframe = false;
	Text text;
	Camera cam;
	Vector3f range;
	private ShapedObject xaxis, yaxis, zaxis;
	InputEvent toggledata, toggleaxis, togglegrid, togglewireframe;

	public Debugger(InputManager i, Font f, Camera cam) {
		font = f;
		this.cam = cam;

		text = new Text("FPS:\nObjects:\nPolygons:\nCamera:", 10, 10, font);

		xaxis = new ShapedObject();
		yaxis = new ShapedObject();
		zaxis = new ShapedObject();
		xaxis.setRenderMode(GL_LINES);
		yaxis.setRenderMode(GL_LINES);
		zaxis.setRenderMode(GL_LINES);

		setRange(new Vector3f(1000, 1000, 1000));
		setupControls(i);
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

	public void render2d(int fps, int objects) {
		if (showdata) {
			Vector3f campos = cam.getPosition();
			text.setText("FPS: " + fps + "\nObjects: " + objects
					+ "\nPolygons:\nCamera: " + campos.x + "; " + campos.y
					+ "; " + campos.z);
			text.render();
		}
	}

	public void render3d() {
		if (showaxis) {
			Vector3f campos = cam.getPosition();
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
			glBegin(GL_LINES);
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
		if (w)
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		else
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		wireframe = w;
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
