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

import heroesgrave.paint.core.changes.MaskRectChange;
import heroesgrave.paint.editing.SelectionTool;
import heroesgrave.paint.editing.Tool;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.RawImage.MaskMode;
import heroesgrave.utils.math.MathUtils;

public class Select extends Tool implements SelectionTool
{
	private MaskRectChange rect;
	
	public Select(String name)
	{
		super(name);
	}
	
	public void onPressed(Layer layer, short x, short y, int button)
	{
		MaskMode mode;
		if(isCtrlDown())
		{
			if(isAltDown())
				mode = MaskMode.XOR;
			else
				mode = MaskMode.ADD;
		}
		else if(isAltDown())
		{
			mode = MaskMode.SUB;
		}
		else if(isShiftDown())
		{
			mode = MaskMode.AND;
		}
		else
		{
			mode = MaskMode.REP;
		}
		rect = new MaskRectChange(x, y, x, y, mode);
		preview(rect);
	}
	
	public void onReleased(Layer layer, short x, short y, int button)
	{
		if(isCtrlDown())
		{
			int dx = x - rect.x1;
			int dy = y - rect.y1;
			int mag = Math.max(Math.abs(dx), Math.abs(dy));
			dx = MathUtils.sign(dx) * mag;
			dy = MathUtils.sign(dy) * mag;
			x = (short) (rect.x1 + dx);
			y = (short) (rect.y1 + dy);
		}
		rect.moveTo(x, y);
		applyPreview();
		rect = null;
	}
	
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		if(isCtrlDown())
		{
			int dx = x - rect.x1;
			int dy = y - rect.y1;
			int mag = Math.max(Math.abs(dx), Math.abs(dy));
			dx = MathUtils.sign(dx) * mag;
			dy = MathUtils.sign(dy) * mag;
			x = (short) (rect.x1 + dx);
			y = (short) (rect.y1 + dy);
		}
		if(rect.moveTo(x, y))
			repaint();
	}
}
