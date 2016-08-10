package bn.blaszczyk.blstatistics.tools;

import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.gui.corefilters.*;
import bn.blaszczyk.blstatistics.gui.filters.*;

public class NewFilterMenu  
{
	private interface MyAction
	{
		public void doAction();
	}
	
	private static MyAction panelAction;
	private static BiFilterPanel<Season,Game> panel;
	
	public static void populatePopupMenu(BiFilterPanel<Season, Game> panel)
	{
		
		/*
		 * Header First
		 */
		JLabel header = new JLabel("Filter");
		panel.addPopupMenuLabel(header);
		panel.addPopupMenuSeparator();
		panel.addFilterListener(e -> header.setText(panel.toString()));
		
		/*
		 * NoFilterPanel Menu
		 */
		if(panel instanceof NoFilterPanel)
		{
			NoFilterPanel<Season, Game> nfPanel = (NoFilterPanel<Season, Game>) panel;
			JMenu setPanel = new JMenu("Setzte Filter");
			setPanelAction(() -> nfPanel.replaceMe( getPanel() ) );
			addMenuItems(setPanel);
			nfPanel.addPopupMenuItem(setPanel);
			return;
		}
		/*
		 * MultiOperatorFilterPanel Menu
		 */
		if(panel instanceof MultiOperatorFilterPanel)
		{
			MultiOperatorFilterPanel<Season, Game> moPanel = (MultiOperatorFilterPanel<Season, Game>) panel;
			JMenu miAddFilter = new JMenu("Neuer Filter");
			setPanelAction( () -> moPanel.addPanel(getPanel()));
			addMenuItems(miAddFilter);
			moPanel.addPopupMenuItem(miAddFilter);
			moPanel.addPopupMenuItem( moPanel.getMiRemoveFilter() );
		}
		/*
		 * IfThenElseFilterPanel Menu
		 */
		else if( panel instanceof IfThenElseFilterPanel)
		{
			IfThenElseFilterPanel<Season, Game> itePanel = (IfThenElseFilterPanel<Season, Game>) panel;

			JMenu miSetIf = new JMenu("Setze IF Filter");
			setPanelAction(() -> itePanel.getIfFilter().replaceMe(getPanel()) );
			addMenuItems(miSetIf);
			itePanel.addPopupMenuItem(miSetIf);
			
			JMenu miSetThen = new JMenu("Setze THEN Filter");
			setPanelAction(() -> itePanel.getThenFilter().replaceMe(getPanel()) );
			addMenuItems(miSetThen);
			itePanel.addPopupMenuItem(miSetThen);
			
			JMenu miSetElse = new JMenu("Setze IF Filter");
			setPanelAction(() -> itePanel.getElseFilter().replaceMe(getPanel()) );
			addMenuItems(miSetElse);
			itePanel.addPopupMenuItem(miSetElse);
		}
		/*
		 * UnaryOperatorFilterPanel Menu
		 */
		else if( panel instanceof UnaryOperatorFilterPanel)
		{
			UnaryOperatorFilterPanel<Season, Game> uPanel = (UnaryOperatorFilterPanel<Season, Game>) panel;
			JMenu miSetPanel = new JMenu("Setze Inneren Filter");
			setPanelAction(() -> uPanel.getInnerPanel().replaceMe( getPanel() ));
			addMenuItems(miSetPanel);
			uPanel.addPopupMenuItem(miSetPanel);
		}
		/*
		 * SubLeagueFilterPanel Menu
		 */
		else if(panel instanceof SubLeagueFilterPanel)
		{
			SubLeagueFilterPanel slPanel = (SubLeagueFilterPanel) panel;
			slPanel.addPopupMenuItem( slPanel.getMiRemoveTeam() );
		}
		/*
		 * General Menu
		 */
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
		panel.addPopupMenuItem(miSetActive);
		
		JMenuItem miInvert = new JMenuItem("Invertieren");
		miInvert.addActionListener(e -> {
			if(panel instanceof UnaryOperatorFilterPanel)
				panel.replaceMe( ((UnaryOperatorFilterPanel<Season, Game>)panel).getInnerPanel() );
			else if(panel instanceof AbsoluteOperatorFilterPanel)
			{
				BiFilterPanel<Season, Game> invertPanel = new AbsoluteOperatorFilterPanel<>( !((AbsoluteOperatorFilterPanel<Season, Game>) panel).getValue() );
				populatePopupMenu(invertPanel);
				panel.replaceMe(invertPanel);
			}
			else
			{
				BiFilterPanel<Season, Game> newPanel = new UnaryOperatorFilterPanel<Season, Game>(panel);
				populatePopupMenu(newPanel);
				panel.replaceMe(newPanel);	
			}
		});
		panel.addPopupMenuItem(miInvert);
		
		JMenuItem miRemove = new JMenuItem("Entfernen");
		miRemove.addActionListener(e -> panel.replaceMe(createNoFilterPanel()) );
		panel.addPopupMenuItem(miRemove);
		
		JMenu miReplace = new JMenu("Ersetzten");
		setPanelAction( () -> panel.replaceMe(getPanel()));
		addMenuItems(miReplace);
		panel.addPopupMenuItem(miReplace);
	}
	
	
	private static void addMenuItems(JMenu menu)
	{
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
		
		/*
		 * Team Filters
		 */
		JMenu teamFilters = new JMenu("Team Filter");
		
		addMenuItem(teamFilters,"Team", 
			() -> setPanel( FilterPanelAdapter.getSecondArgAdapter( new TeamFilterPanel() ) ) );
		addMenuItem(teamFilters,"Direkter Vergleich", 
			() -> setPanel( FilterPanelAdapter.getSecondArgAdapter( new SubLeagueFilterPanel() ) ) );
		
		/*
		 * Date Filters
		 */
		JMenu dateFilters = new JMenu("Spieltag Filter");

		addMenuItem(dateFilters, "Datum", 
			() -> setPanel(FilterPanelAdapter.getSecondArgAdapter(new DateFilterPanel())));
		addMenuItem(dateFilters, "Wochentag", 
			() -> setPanel(FilterPanelAdapter.getSecondArgAdapter(new DayOfWeekFilterPanel())));
		addMenuItem(dateFilters,"Spieltag", 
			() -> setPanel(FilterPanelAdapter.getSecondArgAdapter( new MatchDayFilterPanel())));
		
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
		 * Logical Filters
		 */

		JMenu logicalFilters = new JMenu("Logische Filter");
		
		addMenuItem(logicalFilters,"TRUE, FALSE", 
			() -> setPanel( new AbsoluteOperatorFilterPanel<>(true) ));
		addMenuItem(logicalFilters,"NOT", 
			() -> setPanel( new UnaryOperatorFilterPanel<>(createNoFilterPanel()) ));
		addMenuItem(logicalFilters,"AND, OR, XOR", () -> setPanel( 
				new MultiOperatorFilterPanel<Season,Game>(Arrays.asList(createNoFilterPanel(),createNoFilterPanel()),MultiOperatorFilterPanel.AND)
				));
		addMenuItem(logicalFilters,"IF_THEN_ELSE", 
			() -> setPanel( new IfThenElseFilterPanel<>(createNoFilterPanel(),createNoFilterPanel(),createNoFilterPanel())));
		
		menu.add(goalFilters);
		menu.add(teamFilters);
		menu.add(dateFilters);
		menu.add(seasonFilters);
		menu.add(logicalFilters);
		
		addMenuItem(menu,"Lade Filter", () -> setPanel( FilterIO.loadFilter() ));
	}

	public static BiFilterPanel<Season,Game> createNoFilterPanel()
	{
		BiFilterPanel<Season, Game> noPanel = new NoFilterPanel<>();
		populatePopupMenu(noPanel);
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
		populatePopupMenu(panel);
		NewFilterMenu.panel = panel;
	}
	
	private static void setPanelAction(MyAction panelAction)
	{
		NewFilterMenu.panelAction = panelAction;
	}

}
