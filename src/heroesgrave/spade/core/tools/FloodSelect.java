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

import heroesgrave.spade.core.changes.FloodSelectChange;
import heroesgrave.spade.editing.SelectionTool;
import heroesgrave.spade.editing.Tool;
import heroesgrave.spade.gui.misc.WeblafWrapper;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.RawImage.MaskMode;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

public class FloodSelect extends Tool implements SelectionTool
{
	private FloodSelectChange change;
	private JRadioButton add, sub, xor, and, rep;
	
	private JCheckBox isGlobal;
	
	public FloodSelect(String name)
	{
		super(name);
		
		ButtonGroup modes = new ButtonGroup();
		
		this.isGlobal = WeblafWrapper.createCheckBox();
		isGlobal.setText(" Global");
		
		rep = WeblafWrapper.createRadioButton();
		rep.setText(" Replace");
		rep.setSelected(true);
		
		add = WeblafWrapper.createRadioButton();
		add.setText(" Add");
		
		sub = WeblafWrapper.createRadioButton();
		sub.setText(" Subtract");
		
		xor = WeblafWrapper.createRadioButton();
		xor.setText(" Invert");
		
		and = WeblafWrapper.createRadioButton();
		and.setText(" Intersect");
		
		modes.add(rep);
		modes.add(add);
		modes.add(sub);
		modes.add(xor);
		modes.add(and);
		
		menu.add(WeblafWrapper.asMenuItem(rep));
		menu.add(WeblafWrapper.asMenuItem(add));
		menu.add(WeblafWrapper.asMenuItem(sub));
		menu.add(WeblafWrapper.asMenuItem(xor));
		menu.add(WeblafWrapper.asMenuItem(and));
		
		menu.add(WeblafWrapper.createSeparator());
		menu.add(WeblafWrapper.asMenuItem(isGlobal));
	}
	
	@Override
	public void onPressed(Layer layer, short x, short y, int button)
	{
		MaskMode mode;
		boolean add = this.add.isSelected() || isCtrlDown() || xor.isSelected();
		boolean sub = this.sub.isSelected() || isAltDown() || xor.isSelected();
		boolean and = this.and.isSelected() || isShiftDown();
		
		if(add)
		{
			if(sub)
				mode = MaskMode.XOR;
			else
				mode = MaskMode.ADD;
		}
		else if(sub)
		{
			mode = MaskMode.SUB;
		}
		else if(and)
		{
			mode = MaskMode.AND;
		}
		else
		{
			mode = MaskMode.REP;
		}
		change = new FloodSelectChange(x, y, mode, isGlobal.isSelected());
		preview(change);
	}
	
	@Override
	public void onReleased(Layer layer, short x, short y, int button)
	{
		applyPreview();
		change = null;
	}
	
	@Override
	public void whilePressed(Layer layer, short x, short y, int button)
	{
		if(change.moveTo(x, y))
			repaint();
	}
}
