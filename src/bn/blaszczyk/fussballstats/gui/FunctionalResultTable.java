package bn.blaszczyk.fussballstats.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import bn.blaszczyk.fussballstats.core.*;
import bn.blaszczyk.fussballstats.filters.*;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public class FunctionalResultTable extends JPanel implements ItemListener
{
	private static final Integer[] POINTS_FOR_WIN_OPTIONS = {2,3};
	
	private ResultTable resultTable = new ResultTable();	

	private JLabel header = new JLabel("Tabelle", SwingConstants.CENTER);
	
	private JCheckBox chbHome = new JCheckBox("Heimspiele",true);
	private JCheckBox chbAway = new JCheckBox("Auswärtsspiele",true);
	private JLabel lblWinPoints = new JLabel("Punkte pro Sieg:",SwingConstants.RIGHT);
	private JComboBox<Integer> boxWinPoints = new MyComboBox<>(POINTS_FOR_WIN_OPTIONS,70,false);
	private JCheckBox chbWeighted = new JCheckBox("Nach Spielen gewichtet",false);

	private Iterable<Game> games;
	private BiFilter<TeamResult, Game> filter = null;

	public FunctionalResultTable()
	{
		super(new BorderLayout(5, 5));		

		header.setBounds(0, 0, 940, 50);
		header.setFont(new Font("Arial", Font.BOLD, 28));
		
		chbHome.setBounds(10, 60, 150, 30);
		chbHome.addItemListener(this);
		
		chbAway.setBounds(160, 60, 150, 30);
		chbAway.addItemListener(this);
		
		lblWinPoints.setBounds(370, 60, 150, 30);
		
		boxWinPoints.setBounds(530, 60, 70, 30);
		boxWinPoints.setSelectedIndex(1);
		boxWinPoints.addItemListener(this);
		
		chbWeighted.setBounds(650,60,250,30);
		chbWeighted.addItemListener(this);
		
		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(null);
		optionsPanel.setPreferredSize(new Dimension(1000, 100));
		optionsPanel.add(header);
		optionsPanel.add(chbHome);
		optionsPanel.add(chbAway);
		optionsPanel.add(lblWinPoints);
		optionsPanel.add(boxWinPoints);
		optionsPanel.add(chbWeighted);
		
		add(optionsPanel,BorderLayout.NORTH);
		add(new JScrollPane(resultTable),BorderLayout.CENTER);
	}
	
	public void setGames(Iterable<Game> games)
	{
		this.games = games;
		setTable();
	}
	
	public List<String> getSelectedTeams()
	{
		return resultTable.getSelectedTeams();
	}

	public void setSelectedTeams(List<String> teams)
	{
		resultTable.setSelectedTeams(teams);
	}
	
	public void addListSelectionListener(ListSelectionListener l)
	{
		resultTable.getSelectionModel().addListSelectionListener(l);
	}
	
	public void removeListSelectionListener(ListSelectionListener l)
	{
		resultTable.getSelectionModel().removeListSelectionListener(l);
	}

	private void setFilter()
	{
		if(chbHome.isSelected())
			if(chbAway.isSelected())
				filter = LogicalBiFilter.getTRUEBiFilter();
			else
				filter = TeamResultFilter.getHomeGameFilter();
		else
			if(chbAway.isSelected())
				filter = TeamResultFilter.getAwayGameFilter();
			else
				filter = LogicalBiFilter.getFALSEBiFilter();
		setTable();
	}
	
	private void setTable()
	{
		Table table = new Table(games, filter,(Integer)boxWinPoints.getSelectedItem());
		table.sort();	
		resultTable.setRelativeTable(chbWeighted.isSelected());
		resultTable.setSource(table);
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		setFilter();
	}
	
}
