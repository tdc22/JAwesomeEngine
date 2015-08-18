package utils;

import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class SystemProperties {
	private static String OS = System.getProperty("os.name").toLowerCase();

	private static String fileseperator = System.getProperty("file.separator").toLowerCase();

	public static String getFileSeperator() {
		return fileseperator;
	}

	public static String getLWJGLVersion() {
		return Sys.getVersion();
	}

	public static String getOpenGLVersion() {
		return GL11.glGetString(GL11.GL_VERSION);
	}

	public static String getOSName() {
		if (isWindows())
			return "Windows";
		if (isMac())
			return "Mac";
		if (isLinux())
			return "Linux";
		if (isUnix())
			return "Unix";
		if (isSolaris())
			return "Solaris";
		if (isBSD())
			return "BSD";

		return getTrueOSName();
	}

	public static String getOSVersion() {
		return System.getProperty("os.version");
	}

	public static String getTrueOSName() {
		return System.getProperty("os.name");
	}

	public static boolean isBSD() {
		return (OS.indexOf("bsd") >= 0);
	}

	public static boolean isLinux() {
		return (OS.indexOf("linux") >= 0);
	}

	public static boolean isMac() {
		return (OS.indexOf("mac") >= 0);
	}

	public static boolean isSolaris() {
		return (OS.indexOf("sunos") >= 0);
	}

	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
	}

	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}
}