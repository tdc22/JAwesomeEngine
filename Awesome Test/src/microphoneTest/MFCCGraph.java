package microphoneTest;

public class MFCCGraph extends GraphObject {
	MFCC mfcc;

	public MFCCGraph(MFCC mfcc) {
		this.mfcc = mfcc;
	}

	@Override
	public void format(MicrophoneSettings settings) {
		mfcc.format(settings);
		samplesize = settings.getSampleSizeInBits();
	}

	@Override
	public void process(byte[] buffer) {
		mfcc.process(buffer);
		int listlen = mfcc.lastframes.size();
		int sum;
		for (int i = currlength - listlen; i > 0; i--) {
			sum = i + listlen;
			if (sum < vallength)
				values[sum] = values[i];
		}
		for (int f = 0; f < listlen; f++) {

		}
		newvalues = true;
	}
}
