// {LICENSE}
/*
 * Copyright 2013-2015 HeroesGrave and other Spade developers.
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

public class FlipVertChange extends SingleChange implements IImageChange, IMaskChange
{
	public static final FlipVertChange instance = new FlipVertChange();
	
	@Override
	public RawImage apply(RawImage image)
	{
		int[] buffer = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		
		if(mask == null)
		{
			// Just flip the image.
			int[] tmp = new int[image.width];
			for(int j = 0; j < image.height / 2; j++)
			{
				final int j1 = image.height - j - 1;
				System.arraycopy(buffer, j * image.width, tmp, 0, image.width);
				System.arraycopy(buffer, j1 * image.width, buffer, j * image.width, image.width);
				System.arraycopy(tmp, 0, buffer, j1 * image.width, image.width);
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
				for(int j = 0; j < image.height / 2; j++)
				{
					final int j1 = image.height - j - 1;
					System.arraycopy(newMask, j * image.width, tmp, 0, image.width);
					System.arraycopy(newMask, j1 * image.width, newMask, j * image.width, image.width);
					System.arraycopy(tmp, 0, newMask, j1 * image.width, image.width);
				}
			}
			// Flip image
			for(int j = 0; j < image.height; j++)
			{
				final int j1 = image.height - j - 1;
				for(int i = 0; i < image.width; i++)
				{
					if(newMask[j * image.width + i])
					{
						newBuffer[j * image.width + i] = buffer[j1 * image.width + i];
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
