package heroesgrave.spade.core.effects;

import heroesgrave.spade.core.changes.GreyscaleChange;
import heroesgrave.spade.core.changes.TrueGreyscaleChange;
import heroesgrave.spade.editing.Effect;
import heroesgrave.spade.gui.dialogs.GridEffectDialog;
import heroesgrave.spade.gui.misc.WeblafWrapper;
import heroesgrave.spade.image.Layer;

import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class GreyscaleEffect extends Effect
{
	public GreyscaleEffect(String name)
	{
		super(name);
	}
	
	@Override
	public void perform(final Layer layer)
	{
		final GridEffectDialog dialog = new GridEffectDialog(1, 1, "Greyscale", getIcon());
		
		final ButtonGroup buttons = new ButtonGroup();
		final JRadioButton isLinear = WeblafWrapper.createRadioButton();
		final JRadioButton isTrue = WeblafWrapper.createRadioButton();
		
		isLinear.setSelected(true);
		
		isLinear.setText("Linear");
		isTrue.setText("Perceived");
		
		isLinear.setMargin(new Insets(5, 5, 5, 5));
		isTrue.setMargin(new Insets(5, 5, 5, 5));
		
		buttons.add(isLinear);
		buttons.add(isTrue);
		
		{
			JPanel panel = dialog.getPanel(0);
			panel.setLayout(new GridLayout(0, 1));
			
			panel.add(isLinear);
			panel.add(isTrue);
		}
		
		JPanel panel = dialog.getBottomPanel();
		panel.setLayout(new GridLayout(0, 2));
		JButton create = WeblafWrapper.createButton("Apply");
		JButton cancel = WeblafWrapper.createButton("Cancel");
		
		panel.add(create);
		panel.add(cancel);
		
		create.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dialog.close();
				if(buttons.isSelected(isLinear.getModel()))
					layer.addChange(GreyscaleChange.instance);
				else if(buttons.isSelected(isTrue.getModel()))
					layer.addChange(TrueGreyscaleChange.instance);
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
}
