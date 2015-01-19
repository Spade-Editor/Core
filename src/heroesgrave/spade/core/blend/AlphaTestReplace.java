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

package heroesgrave.spade.core.blend;

import heroesgrave.spade.image.blend.BlendMode;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class AlphaTestReplace extends BlendMode
{
	public AlphaTestReplace()
	{
		super("Alpha-Replace");
	}
	
	public void compose(Raster src, Raster dst, WritableRaster out)
	{
		int w = out.getWidth();
		int h = out.getHeight();
		
		int n = src.getNumBands();
		
		int[] ip = new int[n], op = new int[n];
		
		for(int i = 0; i < w; i++)
		{
			for(int j = 0; j < h; j++)
			{
				if(src.getSample(i, j, 3) == 0)
					dst.getPixel(i, j, ip);
				else
					src.getPixel(i, j, ip);
				System.arraycopy(ip, 0, op, 0, n);
				out.setPixel(i, j, op);
			}
		}
	}
}
