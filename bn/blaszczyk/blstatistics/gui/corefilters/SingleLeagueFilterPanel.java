package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;
import bn.blaszczyk.blstatistics.filters.SeasonFilter;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;
import bn.blaszczyk.blstatistics.gui.tools.ComboBoxFactory;

@SuppressWarnings("serial")
public class SingleLeagueFilterPanel extends AbstractFilterPanel<Season>
{
	private JComboBox<String> cboLeagues;
	private JCheckBox boxRecursive = new JCheckBox("Alle",true);

	public SingleLeagueFilterPanel(List<String> allLeagues)
	{
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		ComboBoxFactory<String> cbf = new ComboBoxFactory<>(allLeagues);
		cboLeagues = cbf.createComboBox();
		cboLeagues.addActionListener(e -> setFilter());

		boxRecursive.setMinimumSize(new Dimension(60, 30));
		boxRecursive.setMinimumSize(new Dimension(60, 30));
		boxRecursive.setInheritsPopupMenu(true);
		boxRecursive.addActionListener(e -> setFilter());
		
		setFilter();
	}

	public SingleLeagueFilterPanel(List<String> allLeagues, String league, boolean isRecursive)
	{
		this(allLeagues);
		cboLeagues.setSelectedItem(league);
		boxRecursive.setSelected(isRecursive);
	}
	
	public String getSelectedLeague()
	{
		return (String) cboLeagues.getSelectedItem();
	}

	public boolean isRecursive()
	{
		return boxRecursive.isSelected();
	}
	
	private void setFilter()
	{
		String league = (String) cboLeagues.getSelectedItem();
		Filter<Season> filter;
		if(league == null)
			filter = LogicalFilter.getTRUEFilter();
		else if( boxRecursive.isSelected())
			filter = SeasonFilter.getLeagueRecursiveFilter(league);
		else
			filter = SeasonFilter.getLeagueFilter(league);
		setFilter(filter);
	}

	@Override
	protected void addComponents()
	{
		add(cboLeagues);
		add(boxRecursive);
	}
	
	@Override
	public String toString()
	{
		return getSelectedLeague().toString();
	}

}
