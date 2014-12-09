package utils;

import java.io.UnsupportedEncodingException;

public class StringEncoder {
	protected String charset;

	public StringEncoder() {
	}

	public StringEncoder(String charset) {
		setCharset(charset);
	}

	public String decode(byte[] data) {
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] encode(String string) {
		try {
			return string.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charsetname) {
		charset = charsetname;
	}
}
