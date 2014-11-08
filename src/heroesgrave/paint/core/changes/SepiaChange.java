package heroesgrave.paint.core.changes;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.IImageChange;
import heroesgrave.paint.image.change.SingleChange;

public class SepiaChange extends SingleChange implements IImageChange
{
	public static final SepiaChange instance = new SepiaChange();
	
	@Override
	public RawImage apply(RawImage image)
	{
		
		int[] buffer = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		
		for(int i = 0; i < buffer.length; i++)
		{
			if(mask == null || mask[i])
			{
				int c = buffer[i];
				int r = (c >> 16) & 0xFF;
				int g = (c >> 8) & 0xFF;
				int b = (c) & 0xFF;
				
				int l = (int) (0.2126f * r + 0.7152f * g + 0.0722f * b) & 0xFF;
				buffer[i] = (c & 0xFF000000) | Math.min((int) (l * 1.2f), 0xFF) << 16 | l << 8 | (int) (l * 0.8f);
			}
		}
		
		return image;
	}
	
	@Override
	public SingleChange getInstance()
	{
		return instance;
	}
	
}
