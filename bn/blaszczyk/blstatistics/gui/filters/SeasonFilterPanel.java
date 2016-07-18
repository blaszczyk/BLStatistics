package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.filters.SeasonFilter;

@SuppressWarnings("serial")
public class SeasonFilterPanel extends AbstractFilterPanel<Season>
{
	
	private JLabel label = new JLabel("Saison");
	private JTextField textField = new JTextField("2016");
	private JCheckBox earlier = new JCheckBox("und frühere",false);
	private JCheckBox later = new JCheckBox("und spätere",false);
	
	public SeasonFilterPanel()
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
			int year = Integer.parseInt(textField.getText());
			if(earlier.isSelected())
				if(later.isSelected())
					setFilter(LogicalFilter.getTRUEFilter());
				else
					setFilter(SeasonFilter.getSeasonMaxFilter(year));
			else
				if(later.isSelected())
					setFilter(SeasonFilter.getSeasonMinFilter(year));
				else
					setFilter(SeasonFilter.getSeasonFilter(year));
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
		return "Saison " + textField.getText() + (earlier.isSelected()?" und davor":"") + (later.isSelected()?" und danach":"");
	}
	

}
