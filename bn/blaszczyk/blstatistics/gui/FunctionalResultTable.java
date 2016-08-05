package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.filters.*;

@SuppressWarnings("serial")
public class FunctionalResultTable extends JPanel implements ItemListener
{
	private static final Integer[] POINTS_FOR_WIN_OPTIONS = {2,3};
	
	
	private ResultTable resultTable = new ResultTable();	
	private JPanel optionsPanel;

	private JCheckBox cboHome = new JCheckBox("Heimspiele",true);
	private JCheckBox cboAway = new JCheckBox("Auswärtsspiele",true);
	private JLabel lblWinPoints = new JLabel("Punkte pro Sieg:",SwingConstants.RIGHT);
	private JComboBox<Integer> boxWinPoints = new JComboBox<>(POINTS_FOR_WIN_OPTIONS);
	
	private JCheckBox cboRelative = new JCheckBox("Nach Spielen gewichtet",false);
	

	private Iterable<Game> games;
	private BiFilter<TeamResult, Game> filter = null;

	public FunctionalResultTable()
	{
		super(new BorderLayout(5, 5));		
		
		cboHome.setBounds(10, 10, 150, 30);
		cboHome.addItemListener(this);
		
		cboAway.setBounds(160, 10, 150, 30);
		cboAway.addItemListener(this);
		
		lblWinPoints.setBounds(370, 10, 150, 30);
		
		boxWinPoints.setBounds(530, 10, 70, 30);
		boxWinPoints.setSelectedIndex(1);
		boxWinPoints.addItemListener(this);
		
		cboRelative.setBounds(650,10,250,30);
		cboRelative.addItemListener(this);
		
		optionsPanel = new JPanel();
		optionsPanel.setLayout(null);
		optionsPanel.setPreferredSize(new Dimension(1000, 50));
		optionsPanel.add(cboHome);
		optionsPanel.add(cboAway);
		optionsPanel.add(lblWinPoints);
		optionsPanel.add(boxWinPoints);
		optionsPanel.add(cboRelative);
		
		add(optionsPanel,BorderLayout.NORTH);
		add(new JScrollPane(resultTable),BorderLayout.CENTER);
	}
	
	public List<String> getSelectedTeams()
	{
		return resultTable.getSelectedTeams();
	}

	public void addListSelectionListener(ListSelectionListener l)
	{
		resultTable.getSelectionModel().addListSelectionListener(l);
	}
	
	public void removeListSelectionListener(ListSelectionListener l)
	{
		resultTable.getSelectionModel().removeListSelectionListener(l);
	}
	
	public void setSource(Iterable<Game> games)
	{
		this.games = games;
		createTable();
	}
	
	
	private void setFilter()
	{
		if(cboHome.isSelected())
			if(cboAway.isSelected())
				filter = LogicalBiFilter.getTRUEBiFilter();
			else
				filter = TeamResultFilter.getHomeGameFilter();
		else
			if(cboAway.isSelected())
				filter = TeamResultFilter.getAwayGameFilter();
			else
				filter = LogicalBiFilter.getFALSEBiFilter();
		createTable();
	}
	
	
	private void createTable()
	{
		Table table = new Table(games, filter,(Integer)boxWinPoints.getSelectedItem());
		table.sort();	
		resultTable.setRelativeTable(cboRelative.isSelected());
		resultTable.setSource(table);
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		setFilter();
	}
	
}
