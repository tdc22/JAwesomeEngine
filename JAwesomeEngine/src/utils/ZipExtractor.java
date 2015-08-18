package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipExtractor {
	public static File extract(File file) throws IOException {
		File folder = new File(file.getAbsolutePath().replace(file.getName(), ""));
		// folder.mkdir();

		ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
		ReadableByteChannel in = Channels.newChannel(zis);
		ZipEntry ze = zis.getNextEntry();

		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
		while (ze != null) {
			String fileName = ze.getName();
			File newFile = new File(folder + File.separator + fileName);

			if (!ze.isDirectory()) {
				WritableByteChannel out = Channels.newChannel(new FileOutputStream(newFile));
				while (in.read(buffer) != -1) {
					buffer.flip();
					out.write(buffer);
					buffer.clear();
				}
				out.close();
			} else {
				newFile.mkdirs();
			}

			zis.closeEntry();
			ze = zis.getNextEntry();
		}

		zis.closeEntry();
		zis.close();
		if (in.isOpen())
			in.close();

		return folder;
	}

	public static File extract(String path) throws IOException {
		return extract(new File(path));
	}
}