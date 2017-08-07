package HCS_HW;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import display.DisplayMode;
import display.GLDisplay;
import display.PixelFormat;
import display.VideoSettings;
import game.StandardGame;
import gui.Font;
import gui.Text;
import loader.FontLoader;
import loader.ShaderLoader;
import shader.Shader;
import shape2d.Circle;
import shape2d.Quad;
import sound.NullSoundEnvironment;
import vector.Vector4f;

public class Scatterplot extends StandardGame {
	Quad rotquad;
	Text coloredjumptext;
	float jumppos;
	final int sizeX = 1600;
	final int sizeY = 800;

	public static void main(String[] args) {
		Scatterplot test = new Scatterplot();
		test.start();
	}

	@Override
	public void init() {
		initDisplay(new GLDisplay(), new DisplayMode(sizeX, sizeY, "HCS Scatterplot", true),
				new PixelFormat().withSamples(0), new VideoSettings(sizeX, sizeY), new NullSoundEnvironment());

		List<PlotData> plotdata = new ArrayList<PlotData>();
		plotdata.add(new PlotData("VW Golf", 220, 110, 5));
		plotdata.add(new PlotData("BMW Z4", 235, 184, 6));
		plotdata.add(new PlotData("Audi TT", 228, 180, 6));
		plotdata.add(new PlotData("Trabant", 108, 26, 4));
		plotdata.add(new PlotData("Ferrari F138", 350, 750, 8));
		plotdata.add(new PlotData("Fiat Punto", 182, 105, 5));
		plotdata.add(new PlotData("Mitsubishi Lancer", 250, 280, 6));
		plotdata.add(new PlotData("Maserati Ghibli", 263, 330, 7));
		plotdata.add(new PlotData("Maybach S600", 250, 530, 7));
		plotdata.add(new PlotData("Mini Cooper", 175, 75, 5));

		Font font = FontLoader.loadFont("res/fonts/DejaVuSans.ttf");
		// add2dObject(new Quad(sizeX/2f, sizeY/2f, sizeX/2f, sizeY/2f));

		for (int i = 0; i < plotdata.size(); i++) {
			Shader colorshader = new Shader(
					ShaderLoader.loadShaderFromFile("res/shaders/colorshader.vert", "res/shaders/colorshader.frag"));
			colorshader.addArgumentName("u_color");
			Color c = null;
			switch (i) {
			case 0:
				c = Color.RED;
				break;
			case 1:
				c = Color.BLUE;
				break;
			case 2:
				c = Color.GREEN;
				break;
			case 3:
				c = Color.CYAN;
				break;
			case 4:
				c = Color.ORANGE;
				break;
			case 5:
				c = Color.MAGENTA;
				break;
			case 6:
				c = Color.YELLOW;
				break;
			case 7:
				c = Color.GRAY;
				break;
			case 8:
				c = Color.LIGHT_GRAY;
				break;
			case 9:
				c = Color.PINK;
				break;
			}
			System.out.println(c.getRed() / 255f + "; " + c.getGreen() / 255f + "; " + c.getBlue() / 255f);
			colorshader.addArgument(new Vector4f(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, 1f));
			addShader2d(colorshader);

			PlotData pd = plotdata.get(i);
			Circle circle = new Circle(pd.ps * 2, sizeY - pd.speed * 2, pd.gaenge * 2, 100);
			colorshader.addObject(circle);

			Text t = new Text(pd.marke, 10, 10 + i * 20, font);
			colorshader.addObject(t);
		}
	}

	@Override
	public void render() {
		render3dLayer();
	}

	@Override
	public void render2d() {
		render2dLayer();
	}

	@Override
	public void update(int delta) {
		cam.update(delta);
	}

	private class PlotData {
		public String marke;
		public int speed, ps, gaenge;

		public PlotData(String m, int s, int p, int g) {
			marke = m;
			speed = s;
			ps = p;
			gaenge = g;
		}
	}

	@Override
	public void renderInterface() {

	}
}
