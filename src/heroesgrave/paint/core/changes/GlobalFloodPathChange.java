// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
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

package heroesgrave.paint.core.changes;

import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.image.change.SingleChange;
import heroesgrave.paint.image.change.edit.PathChange.IPathChange;

public class GlobalFloodPathChange extends IPathChange
{
	public static final GlobalFloodPathChange instance = new GlobalFloodPathChange();
	
	// TODO: move to RawImage instance method
	// scan line flood fill as described in Wikipedia
	public void point(RawImage image, int x, int y, int color)
	{
		final int iw = image.width;
		
		if(x < 0 | y < 0 || x >= iw || y >= image.height)
			return;
		
		int[] buffer = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		int targetColor = buffer[x + y * iw];
		
		if(targetColor == color || mask != null && !mask[x + y * iw])
			return;
		
		for(int i = 0; i < buffer.length; i++)
		{
			if(mask == null || mask[i])
			{
				if(buffer[i] == targetColor)
				{
					buffer[i] = color;
				}
			}
		}
	}
	
	@Override
	public void line(RawImage image, int x1, int y1, int x2, int y2, int colour)
	{
		lineToPoints(this, image, x1, y1, x2, y2, colour);
	}
	
	@Override
	public SingleChange getInstance()
	{
		return instance;
	}
}
