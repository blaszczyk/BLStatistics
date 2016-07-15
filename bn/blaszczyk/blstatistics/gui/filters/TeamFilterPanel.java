package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.filters.Filter;
import bn.blaszczyk.blstatistics.filters.GameFilter;
import bn.blaszczyk.blstatistics.filters.LogicalFilter;

@SuppressWarnings({"serial"})
public abstract class TeamFilterPanel extends AbstractFilterPanel<Game>
{
	public static TeamFilterPanel getTeamFilterPanel(Iterable<String> allTeams)
	{
		TeamFilterPanel panel = new TeamFilterPanel(){
			private JComboBox<String> teamBox;
			private JCheckBox home;
			private JCheckBox away;
			
			@Override
			protected void onload()
			{
				setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
				teamBox = createTeamBox(allTeams);
				home = new JCheckBox("Heim",true);
				away = new JCheckBox("Auswärts",true);
				
				ActionListener listener = e -> {
					String team = teamBox.getSelectedItem().toString();
					Filter<Game> filter = LogicalFilter.getFALSEFilter();
					if(home.isSelected())
						if(away.isSelected())
							filter = GameFilter.getTeamFilter(team);
						else
							filter = GameFilter.getTeamHomeFilter(team);
					else
						if(away.isSelected())
							filter = GameFilter.getTeamAwayFilter(team);
					setFilter(filter);
					notifyListeners();
				};
				teamBox.addActionListener(listener);
				home.addActionListener(listener);
				away.addActionListener(listener);
			}
			
			@Override
			protected void addComponents()
			{
				add(new JLabel("Team"));
				add(teamBox);
				add(home);
				add(away);
			}
		};
		return panel;
	}


	public static  TeamFilterPanel getDuelFilterPanel(Iterable<String> allTeams)
	{
		TeamFilterPanel panel = new TeamFilterPanel(){
			private JComboBox<String> team1Box;
			private JComboBox<String> team2Box;
			@Override
			protected void onload()
			{
				setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
				team1Box = createTeamBox(allTeams);
				team2Box = createTeamBox(allTeams);
				ActionListener listener = e -> {
					String team1 = team1Box.getSelectedItem().toString();
					String team2 = team2Box.getSelectedItem().toString();
					setFilter(GameFilter.getDuelFilter(team1, team2));
					notifyListeners();
				};
				team1Box.addActionListener(listener);
				team2Box.addActionListener(listener);
			}
			
			@Override
			protected void addComponents()
			{
				add(team1Box);
				add(new JLabel(" vs "));
				add(team2Box);
			}
		};
		return panel;
	}
	
	public static  TeamFilterPanel getSubLeagueFilterPanel(Iterable<String> allTeams)
	{
		TeamFilterPanel panel = new TeamFilterPanel(){
			private List<JComboBox<String>> teamBoxes;
			private JButton more;
			private ActionListener listener;
			
			private void addTeamBox()
			{
				JComboBox<String> box = createTeamBox(allTeams);
				box.addActionListener(listener);
				teamBoxes.add(box);
			}
			
			@Override
			protected void onload()
			{
				teamBoxes = new ArrayList<>();
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				
				listener = e -> {
						List<String> teams = new ArrayList<>();
						for(JComboBox<String> box : teamBoxes)
							teams.add(box.getSelectedItem().toString());
						setFilter(GameFilter.getSubLeagueFilter(teams));
						notifyListeners();
					};
				addTeamBox();
				addTeamBox();
				
				more = new JButton("Neues Team");
				more.addActionListener( e -> {
					addTeamBox();
					paint();
				});
			}
			
			@Override
			protected void addComponents()
			{
				add(new JLabel("Direkter Vergleich", SwingConstants.LEFT));
				for(JComboBox<String> box : teamBoxes)
					add(box);
				add(more);			
			}
		};
		return panel;
	}
	
	private static JComboBox<String> createTeamBox(Iterable<String> allTeams)
	{
		JComboBox<String> teamBox = new JComboBox<String>();
		for(String team : allTeams)
			teamBox.addItem(team);
		teamBox.setEditable(true);
		return teamBox;
	}
	
}
