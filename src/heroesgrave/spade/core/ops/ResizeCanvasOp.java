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

package heroesgrave.spade.core.ops;

import heroesgrave.spade.editing.Effect;
import heroesgrave.spade.gui.dialogs.TabbedEffectDialog;
import heroesgrave.spade.gui.misc.WeblafWrapper;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.change.doc.DocResize;
import heroesgrave.spade.image.change.edit.ResizeCanvasChange;
import heroesgrave.spade.main.Popup;
import heroesgrave.utils.math.MathUtils;
import heroesgrave.utils.misc.NumberFilter;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;

public class ResizeCanvasOp extends Effect
{
	public ResizeCanvasOp(String name)
	{
		super(name);
	}
	
	@Override
	public void perform(final Layer layer)
	{
		final TabbedEffectDialog dialog = new TabbedEffectDialog(2, "Resize Image", getIcon());
		
		final JTextField width = new JTextField("" + layer.getWidth());
		final JTextField height = new JTextField("" + layer.getHeight());
		
		final JTextField widthF = new JTextField("100%");
		final JTextField heightF = new JTextField("100%");
		
		// Dimensional
		{
			JPanel panel = dialog.getPanel(0);
			dialog.setPanelTitle(0, "Dimensions");
			panel.setLayout(new GridLayout(0, 2));
			
			((AbstractDocument) width.getDocument()).setDocumentFilter(new NumberFilter());
			((AbstractDocument) height.getDocument()).setDocumentFilter(new NumberFilter());
			
			width.setColumns(4);
			height.setColumns(4);
			
			JLabel wl = WeblafWrapper.createLabel("Width: ");
			wl.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel hl = WeblafWrapper.createLabel("Height: ");
			hl.setHorizontalAlignment(SwingConstants.CENTER);
			
			panel.add(wl);
			panel.add(width);
			
			panel.add(hl);
			panel.add(height);
		}
		
		// Proportional
		{
			JPanel panel = dialog.getPanel(1);
			dialog.setPanelTitle(1, "Proportions");
			panel.setLayout(new GridLayout(0, 2));
			
			((AbstractDocument) widthF.getDocument()).setDocumentFilter(new NumberFilter());
			((AbstractDocument) heightF.getDocument()).setDocumentFilter(new NumberFilter());
			
			widthF.setColumns(4);
			heightF.setColumns(4);
			
			JLabel wl = WeblafWrapper.createLabel("Width: ");
			wl.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel hl = WeblafWrapper.createLabel("Height: ");
			hl.setHorizontalAlignment(SwingConstants.CENTER);
			
			panel.add(wl);
			panel.add(widthF);
			
			panel.add(hl);
			panel.add(heightF);
		}
		
		JPanel panel = dialog.getBottomPanel();
		panel.setLayout(new GridLayout(0, 2));
		JButton create = WeblafWrapper.createButton("Resize");
		JButton cancel = WeblafWrapper.createButton("Cancel");
		
		panel.add(create);
		panel.add(cancel);
		
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
				if(width.getText().equals("") || height.getText().equals(""))
					return;
				if(dialog.getSelectedPanel() == 0)
				{
					resize(layer, Integer.parseInt(width.getText()), Integer.parseInt(height.getText()));
				}
				else if(dialog.getSelectedPanel() == 1)
				{
					String w = widthF.getText().replace("%", "");
					String h = heightF.getText().replace("%", "");
					resize(layer, MathUtils.floor(Float.parseFloat(w) / 100f * layer.getWidth()),
							MathUtils.floor(Float.parseFloat(h) / 100f * layer.getHeight()));
				}
			}
		});
		
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
			}
		});
		
		dialog.display();
	}
	
	public void resize(Layer layer, int w, int h)
	{
		if(w < 1 || h < 1 || w > Document.MAX_DIMENSION || h > Document.MAX_DIMENSION)
		{
			Popup.show("Invalid Image Dimensions", "Image must have dimensions higher than 0 and no more than " + Document.MAX_DIMENSION);
		}
		else
		{
			layer.getDocument().addChange(new DocResize(new ResizeCanvasChange(w, h)));
		}
	}
}
