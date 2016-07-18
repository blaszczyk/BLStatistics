package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;

@SuppressWarnings("serial")
public class MatchDayFilterPanel extends AbstractFilterPanel<Game>
{

	private JLabel label = new JLabel("Spieltag");
	private JTextField textField = new JTextField("1");
	private JCheckBox earlier = new JCheckBox("und frühere",false);
	private JCheckBox later = new JCheckBox("und spätere",false);
	
	public MatchDayFilterPanel()
	{
		textField.setMaximumSize(new Dimension(70,30));
		
		ActionListener listener = e -> resetFilter();
		textField.addActionListener(listener);
		earlier.addActionListener(listener);
		later.addActionListener(listener);
		resetFilter();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	private void resetFilter()
	{
		try{
			int matchDay = Integer.parseInt(textField.getText());
			if(earlier.isSelected())
				if(later.isSelected())
					setFilter(LogicalFilter.getTRUEFilter());
				else
					setFilter(GameFilter.getMatchDayMaxFilter(matchDay));
			else
				if(later.isSelected())
					setFilter(GameFilter.getMatchDayMinFilter(matchDay));
				else
					setFilter(GameFilter.getMatchDayFilter(matchDay));
			notifyListeners();
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(textField, "Falschens Zahlenformat", "Error", JOptionPane.ERROR_MESSAGE);
		}				
	}
	
	@Override
	protected void addComponents()
	{
		add(label);
		add(textField);
		add(earlier);
		add(later);
	}
	
	@Override
	public String toString()
	{
		return "Spieltag " + textField.getText() + (earlier.isSelected()?" und davor":"") + (later.isSelected()?" und danach":"");
	}
	
}
