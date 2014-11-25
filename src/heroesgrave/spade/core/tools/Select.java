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

import heroesgrave.spade.core.changes.MaskRectChange;
import heroesgrave.spade.editing.SelectionTool;
import heroesgrave.spade.editing.Tool;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.RawImage.MaskMode;
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.misc.WeblafWrapper;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

public class Select extends Tool implements SelectionTool
{
	private JCheckBox square;
	private JRadioButton add, sub, xor, and, rep;
	
	private MaskRectChange rect;
	
	public Select(String name)
	{
		super(name);
		
		square = WeblafWrapper.createCheckBox();
		square.setText(" Square");
		
		ButtonGroup modes = new ButtonGroup();
		
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
		menu.add(WeblafWrapper.asMenuItem(square));
	}
	
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
		rect = new MaskRectChange(x, y, x, y, mode);
		preview(rect);
	}
	
	public void onReleased(Layer layer, short x, short y, int button)
	{
		if(square.isSelected())
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
		if(square.isSelected())
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
