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
import heroesgrave.paint.image.change.IImageChange;
import heroesgrave.paint.image.change.SingleChange;

public class InvertChange extends SingleChange implements IImageChange
{
	public static final InvertChange instance = new InvertChange();
	
	@Override
	public RawImage apply(RawImage image)
	{
		System.out.println("a");
		int[] buffer = image.borrowBuffer();
		boolean[] mask = image.borrowMask();
		
		for(int i = 0; i < buffer.length; i++)
		{
			if(mask == null || mask[i])
				buffer[i] ^= 0x00FFFFFF; // I hope this is correct.
		}
		
		return image;
	}
	
	@Override
	public SingleChange getInstance()
	{
		return instance;
	}
}
