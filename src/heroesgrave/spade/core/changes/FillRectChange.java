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
import heroesgrave.spade.image.change.IEditChange;
import heroesgrave.spade.io.Serialised;

public class FillRectChange extends RectChange implements IEditChange, Serialised
{
	public FillRectChange()
	{
		
	}
	
	public FillRectChange(short x1, short y1, short x2, short y2, int colour)
	{
		super(x1, y1, x2, y2, colour);
	}
	
	@Override
	public void apply(RawImage image)
	{
		image.fillRect(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2), colour);
	}
}
