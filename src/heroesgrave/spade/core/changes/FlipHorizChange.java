// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.spade.core.changes;

import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.image.change.IImageChange;
import heroesgrave.spade.image.change.IMaskChange;
import heroesgrave.spade.image.change.SingleChange;

public class FlipHorizChange extends SingleChange implements IImageChange, IMaskChange
{
	public static final FlipHorizChange instance = new FlipHorizChange();
	
	@Override
	public RawImage apply(RawImage image)
	{
		int[] buffer = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		
		if(mask == null)
		{
			// Just flip the image.
			int[] tmp = new int[image.width];
			for(int j = 0; j < image.height; j++)
			{
				final int jj = j * image.width;
				for(int i = 0; i < image.width; i++)
				{
					tmp[image.width - i - 1] = buffer[jj + i];
				}
				System.arraycopy(tmp, 0, buffer, jj, image.width);
			}
			return image;
		}
		else
		{
			RawImage newImage = new RawImage(image.width, image.height, image.copyBuffer(), mask);
			newImage.fill(0x00000000);
			boolean[] newMask = newImage.borrowMask();
			int[] newBuffer = newImage.borrowBuffer();
			// Flip mask
			{
				boolean[] tmp = new boolean[image.width];
				for(int j = 0; j < image.height; j++)
				{
					final int jj = j * image.width;
					for(int i = 0; i < image.width; i++)
					{
						tmp[image.width - i - 1] = mask[jj + i];
					}
					System.arraycopy(tmp, 0, newMask, jj, image.width);
				}
			}
			// Flip image
			for(int j = 0; j < image.height; j++)
			{
				final int jj = j * image.width;
				for(int i = 0; i < image.width; i++)
				{
					if(newMask[jj + i])
					{
						newBuffer[jj + i] = buffer[jj + (image.width - i - 1)];
					}
				}
			}
			
			return newImage;
		}
	}
	
	@Override
	public SingleChange getInstance()
	{
		return instance;
	}
}
