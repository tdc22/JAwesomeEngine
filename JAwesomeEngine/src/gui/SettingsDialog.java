package gui;

import game.StandardGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import display.Display;
import display.DisplayMode;
import display.PixelFormat;

public class SettingsDialog {
	JFrame frame;
	boolean open = true;

	public SettingsDialog(StandardGame game) {
		frame = new JFrame();
		frame.setSize(500, 300);
		frame.setTitle("Settings");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Dimension dimension = frame.getToolkit().getScreenSize();
		frame.setLocation(
				(int) ((dimension.getWidth() - frame.getWidth()) / 2),
				(int) ((dimension.getHeight() - frame.getHeight()) / 2));
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		frame.setLayout(new BorderLayout());

		JPanel options = new JPanel();
		options.setLayout(new FlowLayout());

		// Resolution
		JComboBox<String> resolution = new JComboBox<String>();
		try {
			for (int d = 0; d < Display.getAvailableDisplayModes().length; d++) {
				DisplayMode current = Display.getAvailableDisplayModes()[d];
				resolution.addItem((current.getWidth() + " x "
						+ current.getHeight() + " x " + current
						.getBitsPerPixel()));
			}
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		options.add(resolution);

		// Fullscreen
		JCheckBox fullscreen = new JCheckBox("Fullscreen");
		options.add(fullscreen);

		// VSync
		JCheckBox vsync = new JCheckBox("VSync", true);
		options.add(vsync);

		JButton furtheroptions = new JButton("...");
		options.add(furtheroptions);

		JButton start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.dispose();
				open = false;
			}
		});
		options.add(start);

		frame.add(options, BorderLayout.SOUTH);
		frame.setVisible(true);

		while (open) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String[] res = resolution.getSelectedItem().toString().split(" x ");
		int width = Integer.parseInt(res[0]);
		int height = Integer.parseInt(res[1]);
		// int bpp = Integer.parseInt(res[2]);

		VideoSettingsOld settings = new VideoSettingsOld(new PixelFormat(0, 24,
				4, 4), fullscreen.isSelected(), width, height,
				vsync.isSelected());
		game.initDisplay(settings);
	}
}
