package utils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import loader.FileLoader;
import net.TCPByteClient;

public class Updater extends TCPByteClient implements ActionListener {
	JFrame frame;
	File downloadfile, gamefile, tmpfile, versionfile;
	CharsetEncoder encoder;
	JButton start;
	String newversion, jarname;
	FileOutputStream output;
	int filesize;
	boolean download;

	public Updater(String title, String pathgame, String pathversion, String jarname, String hostname, int port) {
		super();
		init(title, pathgame, pathversion, jarname, hostname, port);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(start)) {
			String libpath = gamefile.getAbsolutePath().replace(jarname,
					"native" + SystemProperties.getFileSeperator() + SystemProperties.getOSName().toLowerCase());
			ProcessBuilder pb = new ProcessBuilder("java", "-Djava.library.path=" + libpath, "-jar",
					gamefile.getAbsolutePath());
			System.out.print("command: ");
			for (String s : pb.command()) {
				System.out.print(s + " ");
			}
			System.out.println("");
			// Map<String, String> env = pb.environment();
			// env.put("LD_LIBRARY_PATH",
			// gamefile.getAbsolutePath().replace(jarname, "native" +
			// OSProperties.getFileSeperator() +
			// OSProperties.getOSName().toLowerCase()));
			System.out.println(SystemProperties.getOSName().toLowerCase());
			System.out.println(libpath);
			System.out.println(gamefile.getAbsolutePath().replace(jarname,
					"native" + SystemProperties.getFileSeperator() + SystemProperties.getOSName().toLowerCase()));
			pb.redirectErrorStream(true);
			pb.directory(new File(gamefile.getAbsolutePath().replace(jarname, "")));
			try {
				Process process = pb.start();
				frame.dispose();

				String output;
				BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
				while ((output = stdout.readLine()) != null) {
					System.out.println(output);
				}
				System.out.println("Exit value: " + process.waitFor());

				process.getInputStream().close();
				process.getOutputStream().close();
				process.getErrorStream().close();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void init(String title, String pathgame, String pathversion, String jarname, String hostname, int port) {
		ImageIcon icon = new ImageIcon("");

		this.jarname = jarname;

		frame = new JFrame();
		frame.setTitle(title);
		if (icon.getIconHeight() > 0 && icon.getIconWidth() > 0) {
			frame.setSize(icon.getIconHeight() + 20, icon.getIconWidth());
		} else {
			frame.setSize(300, 80);
		}
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		encoder = Charset.forName("US-ASCII").newEncoder();

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);

		initDownload(pathgame, pathversion, hostname, port);
		initContent(new JLabel(icon));

		frame.setVisible(true);
	}

	private void initContent(JLabel midcontent) {
		frame.add(midcontent, BorderLayout.NORTH);

		JProgressBar progress = new JProgressBar();
		frame.add(progress, BorderLayout.CENTER);
		progress.setValue(10);

		start = new JButton("Start Game");
		start.setEnabled(false);
		start.addActionListener(this);
		frame.add(start, BorderLayout.EAST);
	}

	/*
	 * @Override protected void received(String message) { //
	 * System.out.println("received: " + buffer); if (!download) { //
	 * System.out.println(message); if (message.startsWith("start download")) {
	 * tmpfile = new File(downloadfile + ".tmp"); try { output = new
	 * FileOutputStream(tmpfile).getChannel(); } catch (IOException e) {
	 * e.printStackTrace(); } newversion = message.split(" ")[3]; download = true; }
	 * if (message.equals("no download needed")) { start.setEnabled(true); } } else
	 * { try { output.write(encoder.encode(CharBuffer.wrap(message.toCharArray())));
	 * // System.out.println(output.size()); } catch (IOException e) {
	 * e.printStackTrace(); } } }
	 */

	private void initDownload(String pathgame, String pathversion, String hostname, int port) {
		downloadfile = new File(pathgame);
		versionfile = new File(pathversion);
		download = false;
		String version = "";
		try {
			version = FileLoader.readFile(versionfile);
		} catch (FileNotFoundException e) {
		}

		if (!downloadfile.exists())
			version = null;

		connect(hostname, port);
		sendMessage("d init " + version);
	}

	@Override
	protected void onDisconnect() {
		if (tmpfile != null) {
			if (tmpfile.exists()) {
				try {
					output.close();
				} catch (IOException e1) {
				}
				downloadfile.delete();
				if (!tmpfile.renameTo(downloadfile)) {
					try {
						Files.move(tmpfile.toPath(), downloadfile.toPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				System.out.println("Download file: " + downloadfile.getAbsolutePath());
				System.out.println("File renamed: " + tmpfile.getAbsolutePath());
			}
		}
		try {
			gamefile = new File(ZipExtractor.extract(downloadfile) + "/" + downloadfile.getName().replace(".zip", "")
					+ "/" + jarname);
			System.out.println(gamefile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		start.setEnabled(true);
	}

	@Override
	protected void received(byte[] received) {
		if (!download) {
			String message = decode(received);
			if (message.startsWith("start download")) {
				tmpfile = new File(downloadfile + ".tmp");
				try {
					output = new FileOutputStream(tmpfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				newversion = message.split(" ")[3];
				download = true;
			}
			if (message.equals("no download needed")) {
				start.setEnabled(true);
			}
		} else {
			try {
				output.write(received);
				// System.out.println(output.size());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
