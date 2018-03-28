package bn.blaszczyk.fussballstats.tools;

import java.util.Arrays;

import bn.blaszczyk.fussballstats.gui.filters.BiFilterPanel;
import bn.blaszczyk.fussballstats.gui.filters.IfThenElseFilterPanel;
import bn.blaszczyk.fussballstats.gui.filters.MultiOperatorFilterPanel;
import bn.blaszczyk.fussballstats.gui.filters.NoFilterPanel;
import bn.blaszczyk.fussballstats.gui.filters.UnaryOperatorFilterPanel;
import bn.blaszczyk.fussballstats.model.Game;
import bn.blaszczyk.fussballstats.model.Season;

public class FilterFactory
{
	
	public static BiFilterPanel<Season,Game> createNoFilterPanel()
	{
		BiFilterPanel<Season, Game> noPanel = new NoFilterPanel<>();
		FilterMenuFactory.createPopupMenu(noPanel);
		return noPanel;
	}
	
	public static BiFilterPanel<Season,Game> createUnaryFilterPanel()
	{
		BiFilterPanel<Season, Game> noPanel = new UnaryOperatorFilterPanel<>(createNoFilterPanel());
		FilterMenuFactory.createPopupMenu(noPanel);
		return noPanel;
	}
	
	public static BiFilterPanel<Season,Game> createMultiFilterPanel()
	{
		BiFilterPanel<Season, Game> noPanel = new MultiOperatorFilterPanel<>(Arrays.asList(createNoFilterPanel(),createNoFilterPanel()), 
				MultiOperatorFilterPanel.AND);
		FilterMenuFactory.createPopupMenu(noPanel);
		return noPanel;
	}
	
	public static BiFilterPanel<Season,Game> createIfThenElseFilterPanel()
	{
		BiFilterPanel<Season, Game> noPanel = new IfThenElseFilterPanel<>(createNoFilterPanel(), createNoFilterPanel(), createNoFilterPanel());
		FilterMenuFactory.createPopupMenu(noPanel);
		return noPanel;
	}
	
}
