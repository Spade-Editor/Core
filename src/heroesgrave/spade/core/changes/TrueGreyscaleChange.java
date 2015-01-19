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
import heroesgrave.spade.image.change.SingleChange;

public class TrueGreyscaleChange extends SingleChange implements IImageChange
{
	public static final TrueGreyscaleChange instance = new TrueGreyscaleChange();
	
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
				buffer[i] = (c & 0xFF000000) | l << 16 | l << 8 | l;
			}
		}
		
		return image;
	}
	
	public SingleChange getInstance()
	{
		return instance;
	}
}
