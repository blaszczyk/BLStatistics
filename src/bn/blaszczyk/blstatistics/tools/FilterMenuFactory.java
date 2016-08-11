package bn.blaszczyk.blstatistics.tools;

import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.corefilters.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

public class FilterMenuFactory  
{
	private interface MyAction
	{
		public void doAction();
	}
	
	private static MyAction panelAction;
	private static BiFilterPanel<Season,Game> panel;
	
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
		
		/*
		 * NoFilterPanel Menu
		 */
		if(panel instanceof NoFilterPanel)
		{
			NoFilterPanel<Season, Game> nfPanel = (NoFilterPanel<Season, Game>) panel;
			JMenu setPanel = new JMenu("Filter Setzten");
			setPanelAction(() -> nfPanel.replaceMe( getPanel() ) );
			addMenuItems(setPanel);
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
			setPanelAction( () -> moPanel.addPanel(getPanel()));
			addMenuItems(miAddFilter);
			popupMenu.add(miAddFilter);
			popupMenu.add(moPanel.getMiRemoveFilter());
			popupMenu.addSeparator();
		}
		/*
		 * IfThenElseFilterPanel Menu
		 */
		else if( panel instanceof IfThenElseFilterPanel)
		{
			IfThenElseFilterPanel<Season, Game> itePanel = (IfThenElseFilterPanel<Season, Game>) panel;
			
			JMenu miSetIf = new JMenu("IF Filter Setzen");
			setPanelAction(() -> itePanel.getIfFilter().replaceMe(getPanel()) );
			addMenuItems(miSetIf);
			popupMenu.add(miSetIf);
			
			JMenu miSetThen = new JMenu("THEN Filter Setzen");
			setPanelAction(() -> itePanel.getThenFilter().replaceMe(getPanel()) );
			addMenuItems(miSetThen);
			popupMenu.add(miSetThen);
			
			JMenu miSetElse = new JMenu("ELSE Filter Setzen");
			setPanelAction(() -> itePanel.getElseFilter().replaceMe(getPanel()) );
			addMenuItems(miSetElse);
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
			setPanelAction(() -> uPanel.getInnerPanel().replaceMe( getPanel() ));
			addMenuItems(miSetPanel);
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
			popupMenu.add( slPanel.getMiRemoveTeam() );
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
					((FilterPanelAdapter<Season, Game>)panel).getInnerPanel() instanceof IntegerValueFilterPanel)
			{
				IntegerValueFilterPanel<?> ivPanel = (IntegerValueFilterPanel<?>) ((FilterPanelAdapter<Season, Game>)panel).getInnerPanel();
				ivPanel.invertOperator();
			}
			else
			{
				BiFilterPanel<Season, Game> newPanel = new UnaryOperatorFilterPanel<Season, Game>(panel);
				createPopupMenu(newPanel);
				panel.replaceMe(newPanel);	
			}
		});
		popupMenu.add(miInvert);

		JMenuItem miSetActive = new JMenuItem("Deaktivieren");
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
		setPanelAction( () -> panel.replaceMe(getPanel()));
		addMenuItems(miReplace);
		popupMenu.add(miReplace);
		
		JMenuItem miRemove = new JMenuItem("Entfernen");
		miRemove.addActionListener(e -> panel.replaceMe(createNoFilterPanel()) );
		popupMenu.add(miRemove);
		
		panel.getPanel().setComponentPopupMenu(popupMenu);
	}
	
	
	private static void addMenuItems(JMenu menu)
	{

		/*
		 * Logical Filters
		 */
		JMenu logicalFilters = new JMenu("Logische Filter");
		
		addMenuItem(logicalFilters,"AND, OR, XOR", () -> setPanel( 
				new MultiOperatorFilterPanel<Season,Game>(Arrays.asList(createNoFilterPanel(),createNoFilterPanel()),MultiOperatorFilterPanel.AND)
				));
		addMenuItem(logicalFilters,"NOT", 
				() -> setPanel( new UnaryOperatorFilterPanel<>(createNoFilterPanel()) ));
		addMenuItem(logicalFilters,"IF_THEN_ELSE", 
			() -> setPanel( new IfThenElseFilterPanel<>(createNoFilterPanel(),createNoFilterPanel(),createNoFilterPanel())));
		addMenuItem(logicalFilters,"TRUE, FALSE", 
			() -> setPanel( new AbsoluteOperatorFilterPanel<>(true) ));
		
		/*
		 * Season Filters
		 */
		JMenu seasonFilters = new JMenu("Saison Filter");

		addMenuItem(seasonFilters,"Liga", 
			() -> setPanel( FilterPanelAdapter.getFirstArgAdapter( new SingleLeagueFilterPanel()) ));
		addMenuItem(seasonFilters,"Saison", 
			() -> setPanel( FilterPanelAdapter.getFirstArgAdapter( new SeasonFilterPanel()) ));
		addMenuItem(seasonFilters,"Hin-/Rückrunde", 
			() -> setPanel( new RoundFilterPanel() ));

		/*
		 * Team Filters
		 */
		JMenu teamFilters = new JMenu("Team Filter");

		addMenuItem(teamFilters,"Team", 
			() -> setPanel( FilterPanelAdapter.getSecondArgAdapter( new TeamFilterPanel() ) ) );
		addMenuItem(teamFilters,"Team Suche", 
				() -> setPanel( FilterPanelAdapter.getSecondArgAdapter( new TeamSearchFilterPanel() ) ) );
		addMenuItem(teamFilters,"Direkter Vergleich", 
			() -> setPanel( FilterPanelAdapter.getSecondArgAdapter( new SubLeagueFilterPanel() ) ) );
		
		/*
		 * Date Filters
		 */
		JMenu dateFilters = new JMenu("Spieltag Filter");

		addMenuItem(dateFilters,"Spieltag", 
			() -> setPanel(FilterPanelAdapter.getSecondArgAdapter( new MatchDayFilterPanel())));
		addMenuItem(dateFilters, "Datum", 
			() -> setPanel(FilterPanelAdapter.getSecondArgAdapter(new DateFilterPanel())));
		addMenuItem(dateFilters, "Wochentag", 
			() -> setPanel(FilterPanelAdapter.getSecondArgAdapter(new DayOfWeekFilterPanel())));

		/*
		 * Goal Filters
		 */
		JMenu goalFilters = new JMenu("Tor Filter");

		addMenuItem(goalFilters,"Tore",
			()-> setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getGoalFilterPanel("=",0) ) ) );
		addMenuItem(goalFilters,"Heimtore", 
			()-> setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getHomeGoalFilterPanel("=",0) ) ));
		addMenuItem(goalFilters,"Auswärtstore", 
			() -> setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getAwayGoalFilterPanel("=",0) ) ));
		addMenuItem(goalFilters,"Tordifferenz", 
			() -> setPanel( FilterPanelAdapter.getSecondArgAdapter( GoalFilterPanel.getGoalDiffFilterPanel("=",0) ) ) );
		
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
		addMenuItem(menu,"Lade Filter", () -> setPanel( FilterIO.loadFilter() ));
	}

	public static BiFilterPanel<Season,Game> createNoFilterPanel()
	{
		BiFilterPanel<Season, Game> noPanel = new NoFilterPanel<>();
		createPopupMenu(noPanel);
		return noPanel;
	}
	
	private static BiFilterPanel<Season,Game> getPanel()
	{
		return panel;
	}
	
	private static JMenuItem addMenuItem(JMenu menu, String label, MyAction createAction )
	{
		JMenuItem item = new JMenuItem(label);
		MyAction panelActionCopy = panelAction;
		item.addActionListener( e ->{
			createAction.doAction();
			panelActionCopy.doAction();
		});
		menu.add(item);
		return item;
	}

	private static void setPanel(BiFilterPanel<Season,Game> panel)
	{
		createPopupMenu(panel);
		FilterMenuFactory.panel = panel;
	}
	
	private static void setPanelAction(MyAction panelAction)
	{
		FilterMenuFactory.panelAction = panelAction;
	}

}
