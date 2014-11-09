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

package heroesgrave.paint.core.tools;

import heroesgrave.paint.core.changes.MoveChange;
import heroesgrave.paint.editing.Tool;
import heroesgrave.paint.image.Layer;

public class Move extends Tool
{
	private short sx, sy;
	private MoveChange change;
	
	public Move(String name)
	{
		super(name);
	}
	
	public void onPressed(Layer layer, short x, short y, int button)
	{
		sx = x;
		sy = y;
		change = new MoveChange((short) 0, (short) 0);
		preview(change);
	}
	
	public void onReleased(Layer layer, short x, short y, int button)
	{
		if(isCtrlDown())
		{
			change.dx = (short) (x - sx);
			change.dy = (short) (y - sy);
			if(Math.abs(change.dx) > Math.abs(change.dy))
			{
				change.dy = 0;
			}
			else
			{
				change.dx = 0;
			}
		}
		else
		{
			change.dx = (short) (x - sx);
			change.dy = (short) (y - sy);
		}
		applyPreview();
	}
	
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		short dx = (short) (x - sx);
		short dy = (short) (y - sy);
		if(isCtrlDown())
		{
			if(Math.abs(dx) > Math.abs(dy))
			{
				dy = 0;
			}
			else
			{
				dx = 0;
			}
		}
		if(change.moved(dx, dy))
			repaint();
	}
}
