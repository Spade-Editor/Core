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

import heroesgrave.paint.core.changes.FloodPathChange;
import heroesgrave.paint.core.changes.GlobalFloodPathChange;
import heroesgrave.paint.editing.Tool;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.change.edit.PathChange;
import heroesgrave.utils.misc.WeblafWrapper;

import java.awt.Point;

import javax.swing.JCheckBox;

public class FloodFill extends Tool
{
	private PathChange path;
	
	private JCheckBox isGlobal;
	
	public FloodFill(String name)
	{
		super(name);
		
		// XXX: WebCheckBoxMenuItem is broken.
		
		this.isGlobal = WeblafWrapper.createCheckBox();
		isGlobal.setText(" Global");
		
		menu.add(WeblafWrapper.asMenuItem(isGlobal));
	}
	
	@Override
	public void onPressed(Layer layer, short x, short y, int button)
	{
		if(isGlobal.isSelected())
			path = new PathChange(new Point(x, y), getColour(button), GlobalFloodPathChange.instance);
		else
			path = new PathChange(new Point(x, y), getColour(button), FloodPathChange.instance);
		preview(path);
	}
	
	@Override
	public void onReleased(Layer layer, short x, short y, int button)
	{
		applyPreview();
		path = null;
	}
	
	@Override
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		if(path.moveTo(x, y))
			repaint();
	}
}
