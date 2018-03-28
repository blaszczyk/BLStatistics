package bn.blaszczyk.fussballstats.tools;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import bn.blaszczyk.fussballstats.model.*;
import bn.blaszczyk.fussballstats.gui.corefilters.*;
import bn.blaszczyk.fussballstats.gui.filters.*;

public class FilterMenuFactory  
{
	
	/*
	 * Gives BiFilterPanel a PopupMenu according to its Type
	 */
	public static void createPopupMenu(BiFilterPanel<Season, Game> panel)
	{
		JPopupMenu popupMenu = new JPopupMenu();
		
		/*
		 * Header First
		 */
		JLabel header = new JLabel( " " + panel.toString());
		popupMenu.add(header);
		popupMenu.addSeparator();
		panel.addFilterListener(e -> header.setText(" " + panel.toString()));
		panel.getPanel().setComponentPopupMenu(popupMenu);
		
		/*
		 * NoFilterPanel Menu
		 */
		if(panel instanceof NoFilterPanel)
		{
			NoFilterPanel<Season, Game> nfPanel = (NoFilterPanel<Season, Game>) panel;
			JMenu setPanel = new JMenu("Filter Setzten");
			addMenuItems(setPanel, nfPanel::replaceMe);
			popupMenu.add(setPanel);
			return;
		}
		
		/*
		 * MultiOperatorFilterPanel Menu
		 */
		if(panel instanceof MultiOperatorFilterPanel)
		{
			MultiOperatorFilterPanel<Season, Game> moPanel = (MultiOperatorFilterPanel<Season, Game>) panel;
			JMenu miAddFilter = new JMenu("Filter Hinzufügen");
			addMenuItems(miAddFilter, moPanel::addPanel);
			popupMenu.add(miAddFilter);
			popupMenu.add(moPanel.getMenuRemoveFilter());
			popupMenu.addSeparator();
		}
		/*
		 * IfThenElseFilterPanel Menu
		 */
		else if( panel instanceof IfThenElseFilterPanel)
		{
			IfThenElseFilterPanel<Season, Game> itePanel = (IfThenElseFilterPanel<Season, Game>) panel;
			
			JMenu miSetIf = new JMenu("IF Filter Setzen");
			addMenuItems(miSetIf, itePanel::replaceIfPanel);
			popupMenu.add(miSetIf);
			
			JMenu miSetThen = new JMenu("THEN Filter Setzen");
			addMenuItems(miSetThen, itePanel::replaceThenPanel);
			popupMenu.add(miSetThen);
			
			JMenu miSetElse = new JMenu("ELSE Filter Setzen");
			addMenuItems(miSetElse, itePanel::replaceElsePanel);
			popupMenu.add(miSetElse);
			
			popupMenu.addSeparator();
		}
		/*
		 * UnaryOperatorFilterPanel Menu
		 */
		else if( panel instanceof UnaryOperatorFilterPanel)
		{
			UnaryOperatorFilterPanel<Season, Game> uPanel = (UnaryOperatorFilterPanel<Season, Game>) panel;
			JMenu miSetPanel = new JMenu("Inneren Filter Setzen");
			addMenuItems(miSetPanel, uPanel::replaceInnerPanel);
			popupMenu.add(miSetPanel);
			popupMenu.addSeparator();
		}
		/*
		 * SubLeagueFilterPanel Menu
		 */
		else if(panel instanceof FilterPanelAdapter && 
				((FilterPanelAdapter<Season,Game>)panel).getInnerPanel() instanceof SubLeagueFilterPanel)
		{
			SubLeagueFilterPanel slPanel = (SubLeagueFilterPanel) ((FilterPanelAdapter<Season,Game>)panel).getInnerPanel();
			popupMenu.add( slPanel.getMenuRemoveTeam() );
			popupMenu.addSeparator();
		}
		
		/*
		 * General Menu
		 */
		JMenuItem miInvert = new JMenuItem("Invertieren");
		miInvert.addActionListener(e -> {
			if(panel instanceof UnaryOperatorFilterPanel)
				panel.replaceMe( ((UnaryOperatorFilterPanel<Season, Game>)panel).getInnerPanel() );
			else if(panel instanceof AbsoluteOperatorFilterPanel)
			{
				BiFilterPanel<Season, Game> invertPanel = new AbsoluteOperatorFilterPanel<>( !((AbsoluteOperatorFilterPanel<Season, Game>) panel).getValue() );
				createPopupMenu(invertPanel);
				panel.replaceMe(invertPanel);
			}
			else if(panel instanceof FilterPanelAdapter && 
					((FilterPanelAdapter<Season, Game>)panel).getInnerPanel() instanceof CompareToFilterPanel)
			{
				CompareToFilterPanel<?> ctPanel = (CompareToFilterPanel<?>) ((FilterPanelAdapter<Season, Game>)panel).getInnerPanel();
				ctPanel.invertOperator();
			}
			else
			{
				BiFilterPanel<Season, Game> newPanel = new UnaryOperatorFilterPanel<Season, Game>(panel);
				createPopupMenu(newPanel);
				panel.replaceMe(newPanel);	
			}
		});
		popupMenu.add(miInvert);

		JMenuItem miSetActive = new JMenuItem(panel.isActive() ? "Deaktivieren" : "Aktivieren");
		miSetActive.addActionListener(e -> {
			if(panel.isActive())
			{
				panel.setActive(false);
				miSetActive.setText("Aktivieren");
			}
			else
			{
				panel.setActive(true);
				miSetActive.setText("Deaktivieren");
			}
		});
		popupMenu.add(miSetActive);
		
		JMenu miReplace = new JMenu("Ersetzten");
		addMenuItems(miReplace, panel::replaceMe);
		popupMenu.add(miReplace);
		
		JMenuItem miRemove = new JMenuItem("Entfernen");
		miRemove.addActionListener(e -> panel.replaceMe(FilterFactory.createNoFilterPanel()) );
		popupMenu.add(miRemove);
		
		popupMenu.addSeparator();
		
		JMenuItem miSave = new JMenuItem("Speichern");
		miSave.addActionListener(e -> FilterIO.saveFilter(panel));
		popupMenu.add(miSave);
		
		panel.getPanel().setComponentPopupMenu(popupMenu);
	}
	
	
	/*
	 * Adds Create-Buttons for known Filters
	 */
	private static void addMenuItems(JMenu menu, final Consumer<BiFilterPanel<Season, Game>> panelConsumer)
	{

		/*
		 * Logical Filters
		 */
		JMenu logicalFilters = new JMenu("Logische Filter");
		
		addMenuItem(logicalFilters,"AND, OR, XOR", FilterFactory::createMultiFilterPanel, panelConsumer);
		addMenuItem(logicalFilters,"NOT", FilterFactory::createUnaryFilterPanel, panelConsumer);
		addMenuItem(logicalFilters,"IF_THEN_ELSE", FilterFactory::createIfThenElseFilterPanel, panelConsumer);
		addMenuItem(logicalFilters,"TRUE, FALSE", AbsoluteOperatorFilterPanel::new , panelConsumer);
		
		/*
		 * Season Filters
		 */
		JMenu seasonFilters = new JMenu("Saison Filter");

		addMenuItemSeason(seasonFilters,"Liga", LeagueFilterPanel::new, panelConsumer);
		addMenuItemSeason(seasonFilters,"Saison", SeasonFilterPanel::new, panelConsumer);
		addMenuItem(seasonFilters,"Hin-/Rückrunde", RoundFilterPanel::new, panelConsumer);

		/*
		 * Team Filters
		 */
		JMenu teamFilters = new JMenu("Verein Filter");

		addMenuItemGame(teamFilters,"Verein", TeamFilterPanel::new, panelConsumer );
		addMenuItemGame(teamFilters,"Direkter Vergleich", SubLeagueFilterPanel::new, panelConsumer );
		
		/*
		 * Date Filters
		 */
		JMenu dateFilters = new JMenu("Spieltag Filter");

		addMenuItemGame(dateFilters,"Spieltag", MatchDayFilterPanel::new, panelConsumer);
		addMenuItemGame(dateFilters, "Datum", DateFilterPanel::new, panelConsumer);
		addMenuItemGame(dateFilters, "Wochentag", DayOfWeekFilterPanel::new, panelConsumer);

		/*
		 * Goal Filters
		 */
		JMenu goalFilters = new JMenu("Tor Filter");

		addMenuItemGame(goalFilters,"Tore", GoalFilterPanelFactory::createGoalFilterPanel, panelConsumer);
		addMenuItemGame(goalFilters,"Heimtore", GoalFilterPanelFactory::createHomeGoalFilterPanel, panelConsumer);
		addMenuItemGame(goalFilters,"Auswärtstore", GoalFilterPanelFactory::createAwayGoalFilterPanel, panelConsumer);
		addMenuItemGame(goalFilters,"Tordifferenz", GoalFilterPanelFactory::createGoalDiffFilterPanel, panelConsumer );
		
		menu.add(logicalFilters);
		menu.addSeparator();
		menu.add(seasonFilters);
		menu.add(teamFilters);
		menu.add(dateFilters);
		menu.add(goalFilters);
		menu.addSeparator();
		
		/*
		 * Load Filter
		 */
		addMenuItem(menu,"Lade Filter", FilterIO::loadFilter, panelConsumer );
	}
	
	/*
	 * Internal Methods
	 */
	private static void addMenuItem(JMenu menu, String label, 
			final Supplier<BiFilterPanel<Season, Game>> panelProducer, final Consumer<BiFilterPanel<Season, Game>> panelConsumer)
	{
		JMenuItem item = new JMenuItem(label);
		item.addActionListener( e ->{
			final BiFilterPanel<Season, Game> panel = panelProducer.get();
			createPopupMenu(panel);
			panelConsumer.accept(panel);
		});
		menu.add(item);
	}

	private static void addMenuItemSeason(JMenu menu, String label, 
			final Supplier<FilterPanel<Season>> panelProducer, final Consumer<BiFilterPanel<Season, Game>> panelConsumer)
	{
		final Supplier<BiFilterPanel<Season, Game>> biPanelProducer = () -> FilterPanelAdapter.createFirstArgAdapter(panelProducer.get());
		addMenuItem(menu, label, biPanelProducer, panelConsumer);
	}

	private static void addMenuItemGame(JMenu menu, String label, 
			final Supplier<FilterPanel<Game>> panelProducer, final Consumer<BiFilterPanel<Season, Game>> panelConsumer)
	{
		final Supplier<BiFilterPanel<Season, Game>> biPanelProducer = () -> FilterPanelAdapter.createSecondArgAdapter(panelProducer.get());
		addMenuItem(menu, label, biPanelProducer, panelConsumer);
	}

}
