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

package heroesgrave.spade.core.tools;

import heroesgrave.spade.core.changes.LineChange;
import heroesgrave.spade.editing.Tool;
import heroesgrave.spade.image.Layer;
import heroesgrave.utils.math.MathUtils;

public class Line extends Tool
{
	private LineChange line;
	
	public Line(String name)
	{
		super(name);
	}
	
	public void onPressed(Layer layer, short x, short y, int button)
	{
		line = new LineChange(x, y, getColour(button));
		preview(line);
	}
	
	public void onReleased(Layer layer, short x, short y, int button)
	{
		applyPreview();
		line = null;
	}
	
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		if(isCtrlDown())
		{
			int dx = x - line.sx;
			int dy = y - line.sy;
			int adx = Math.abs(dx);
			int ady = Math.abs(dy);
			if(adx > ady * 2)
			{
				// X axis
				dy = 0;
			}
			else if(ady > adx * 2)
			{
				// Y axis
				dx = 0;
			}
			else
			{
				// Diagonal
				int mag = Math.max(adx, ady);
				dy = MathUtils.sign(dy) * mag;
				dx = MathUtils.sign(dx) * mag;
			}
			x = (short) (line.sx + dx);
			y = (short) (line.sy + dy);
		}
		if(line.end(x, y))
			repaint();
	}
}
