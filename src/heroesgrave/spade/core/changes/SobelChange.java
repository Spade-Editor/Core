package heroesgrave.spade.core.changes;

import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.image.change.IImageChange;
import heroesgrave.spade.image.change.SingleChange;
import heroesgrave.utils.math.MathUtils;

public class SobelChange extends SingleChange implements IImageChange {
	
	public static final SobelChange instance = new SobelChange();
	
	@Override
	public RawImage apply(RawImage image) {
		int[] data = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		int[] luma = new int[data.length];
		
		for (int i = 0; i < data.length; i++)
			if (mask == null || mask[i])
				luma[i] = luma(data[i]);
		
		for (int y = 1; y < image.height - 1; y++) {
			for (int x = 1; x < image.width - 1; x++) {
				int i = x + y * image.width;
				if (mask == null || mask[i]) {
					int c = sobel(i, luma, image.width);
					data[i] = (data[i] & 0xFF000000) | (c << 16) | (c << 8) | c;
				}
			}
		}
		
		return image;
	}
	
	@Override
	public SingleChange getInstance() {
		return instance;
	}
	
	private static final int sobel(int i, int[] luma, int width) {
		int nw = luma[i - width - 1];
		int n = luma[i - width] << 1;
		int ne = luma[i - width + 1];
		
		int w = luma[i - 1] << 1;
		int e = luma[i + 1] << 1;
		
		int sw = luma[i + width - 1];
		int s = luma[i + width] << 1;
		int se = luma[i + width + 1];
		
		int sumX = ne + e + se - nw - w - sw;
		int sumY = nw + n + ne - sw - s - se;
		
		return MathUtils.clamp(Math.abs(sumX) + Math.abs(sumY), 0, 255);
	}
	
	private static final int luma(int rgb) {
		int r = (rgb >> 16) & 0xFF;
		int g = (rgb >> 8) & 0xFF;
		int b = (rgb) & 0xFF;
		// approximate
		return ((r << 1) + r + b + (g << 2)) >> 3;
	}
}
