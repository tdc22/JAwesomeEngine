package display;

import utils.DefaultValues;

public class PixelFormat {
	private int bpp, alpha, depth, stencil, samples, num_aux_buffers,
			accum_bpp, accum_alpha;
	private boolean stereo, sRGB;

	public PixelFormat() {
		init(DefaultValues.DEFAULT_PIXELFORMAT_BPP,
				DefaultValues.DEFAULT_PIXELFORMAT_ALPHA,
				DefaultValues.DEFAULT_PIXELFORMAT_DEPTH,
				DefaultValues.DEFAULT_PIXELFORMAT_STENCIL,
				DefaultValues.DEFAULT_PIXELFORMAT_SAMPLES,
				DefaultValues.DEFAULT_PIXELFORMAT_NUM_AUX_BUFFERS,
				DefaultValues.DEFAULT_PIXELFORMAT_ACCUM_BPP,
				DefaultValues.DEFAULT_PIXELFORMAT_ACCUM_ALPHA,
				DefaultValues.DEFAULT_PIXELFORMAT_STEREO,
				DefaultValues.DEFAULT_PIXELFORMAT_SRGB);
	}

	public PixelFormat(int bpp, int alpha, int depth, int stencil, int samples) {
		init(bpp, alpha, depth, stencil, samples,
				DefaultValues.DEFAULT_PIXELFORMAT_NUM_AUX_BUFFERS,
				DefaultValues.DEFAULT_PIXELFORMAT_ACCUM_BPP,
				DefaultValues.DEFAULT_PIXELFORMAT_ACCUM_ALPHA,
				DefaultValues.DEFAULT_PIXELFORMAT_STEREO,
				DefaultValues.DEFAULT_PIXELFORMAT_SRGB);
	}

	public PixelFormat(int bpp, int alpha, int depth, int stencil, int samples,
			boolean stereo, boolean sRGB) {
		init(bpp, alpha, depth, stencil, samples,
				DefaultValues.DEFAULT_PIXELFORMAT_NUM_AUX_BUFFERS,
				DefaultValues.DEFAULT_PIXELFORMAT_ACCUM_BPP,
				DefaultValues.DEFAULT_PIXELFORMAT_ACCUM_ALPHA, stereo, sRGB);
	}

	public PixelFormat(int bpp, int alpha, int depth, int stencil, int samples,
			int num_aux_buffers, int accum_bpp, int accum_alpha,
			boolean stereo, boolean sRGB) {
		init(bpp, alpha, depth, stencil, samples, num_aux_buffers, accum_bpp,
				accum_alpha, stereo, sRGB);
	}

	public PixelFormat(PixelFormat pf) {
		init(pf.getBitsPerPixel(), pf.getAlpha(), pf.getDepth(),
				pf.getStencil(), pf.getSamples(), pf.getAuxBuffers(),
				pf.getAccumulationBitsPerPixel(), pf.getAccumulationAlpha(),
				pf.isStereo(), pf.isSRGB());
	}

	public int getAccumulationAlpha() {
		return accum_alpha;
	}

	public int getAccumulationBitsPerPixel() {
		return accum_bpp;
	}

	public int getAlpha() {
		return alpha;
	}

	public int getAuxBuffers() {
		return num_aux_buffers;
	}

	public int getBitsPerPixel() {
		return bpp;
	}

	public int getDepth() {
		return depth;
	}

	public int getSamples() {
		return samples;
	}

	public int getStencil() {
		return stencil;
	}

	private void init(int bpp, int alpha, int depth, int stencil, int samples,
			int num_aux_buffers, int accum_bpp, int accum_alpha,
			boolean stereo, boolean sRGB) {
		this.bpp = bpp;
		this.alpha = alpha;
		this.depth = depth;
		this.stencil = stencil;
		this.samples = samples;
		this.num_aux_buffers = num_aux_buffers;
		this.accum_bpp = accum_bpp;
		this.accum_alpha = accum_alpha;
		this.stereo = stereo;
		this.sRGB = sRGB;
	}

	public boolean isSRGB() {
		return sRGB;
	}

	public boolean isStereo() {
		return stereo;
	}

	public PixelFormat withAccumulationAlpha(final int accum_alpha) {
		if (accum_alpha < 0)
			throw new IllegalArgumentException(
					"Invalid number of alpha bits in the accumulation buffer specified: "
							+ accum_alpha);

		final PixelFormat pf = new PixelFormat(this);
		pf.accum_alpha = accum_alpha;
		return pf;
	}

	public PixelFormat withAccumulationBitsPerPixel(final int accum_bpp) {
		if (accum_bpp < 0)
			throw new IllegalArgumentException(
					"Invalid number of bits per pixel in the accumulation buffer specified: "
							+ accum_bpp);

		final PixelFormat pf = new PixelFormat(this);
		pf.accum_bpp = accum_bpp;
		return pf;
	}

	public PixelFormat withAlphaBits(final int alpha) {
		if (alpha < 0)
			throw new IllegalArgumentException(
					"Invalid number of alpha bits specified: " + alpha);

		final PixelFormat pf = new PixelFormat(this);
		pf.alpha = alpha;
		return pf;
	}

	public PixelFormat withAuxBuffers(final int num_aux_buffers) {
		if (num_aux_buffers < 0)
			throw new IllegalArgumentException(
					"Invalid number of auxiliary buffers specified: "
							+ num_aux_buffers);

		final PixelFormat pf = new PixelFormat(this);
		pf.num_aux_buffers = num_aux_buffers;
		return pf;
	}

	public PixelFormat withBitsPerPixel(final int bpp) {
		if (bpp < 0)
			throw new IllegalArgumentException(
					"Invalid number of bits per pixel specified: " + bpp);

		final PixelFormat pf = new PixelFormat(this);
		pf.bpp = bpp;
		return pf;
	}

	public PixelFormat withDepthBits(final int depth) {
		if (depth < 0)
			throw new IllegalArgumentException(
					"Invalid number of depth bits specified: " + depth);

		final PixelFormat pf = new PixelFormat(this);
		pf.depth = depth;
		return pf;
	}

	public PixelFormat withSamples(final int samples) {
		if (samples < 0)
			throw new IllegalArgumentException(
					"Invalid number of samples specified: " + samples);

		final PixelFormat pf = new PixelFormat(this);
		pf.samples = samples;
		return pf;
	}

	public PixelFormat withSRGB(final boolean sRGB) {
		final PixelFormat pf = new PixelFormat(this);
		pf.sRGB = sRGB;
		return pf;
	}

	public PixelFormat withStencilBits(final int stencil) {
		if (stencil < 0)
			throw new IllegalArgumentException(
					"Invalid number of stencil bits specified: " + stencil);

		final PixelFormat pf = new PixelFormat(this);
		pf.stencil = stencil;
		return pf;
	}

	public PixelFormat withStereo(final boolean stereo) {
		final PixelFormat pf = new PixelFormat(this);
		pf.stereo = stereo;
		return pf;
	}
}