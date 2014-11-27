// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
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

import heroesgrave.spade.core.changes.FloodPathChange;
import heroesgrave.spade.core.changes.GlobalFloodPathChange;
import heroesgrave.spade.editing.Tool;
import heroesgrave.spade.gui.misc.WeblafWrapper;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.change.edit.PathChange;

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
