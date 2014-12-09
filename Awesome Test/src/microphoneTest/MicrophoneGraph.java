package microphoneTest;

public class MicrophoneGraph extends GraphObject {
	@Override
	public void process(byte[] buffer) {
		int buflen = buffer.length;
		int numbytes = samplesize / 8;
		int arraylen = buflen / numbytes;
		int sum;
		for (int i = currlength - arraylen; i > 0; i--) {
			sum = i + arraylen;
			if (sum < vallength)
				values[sum] = values[i];
		}
		int[] newvals = MicrophoneByteToInt.convert(buffer, samplesize);
		for (int i = 0; i < arraylen; i++) {
			// int newval = 0;
			// for(int e = 0; e < numbytes; e++) {
			// newval += (buffer[(i*numbytes) + e] >> (e*8));
			// }
			values[i] = newvals[i];
		}
		if (currlength < vallength) {
			currlength += arraylen;
			if (currlength > vallength)
				currlength = vallength;
		}
		newvalues = true;
	}
}
