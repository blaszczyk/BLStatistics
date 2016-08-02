package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import bn.blaszczyk.blstatistics.core.*;
import bn.blaszczyk.blstatistics.filters.*;

@SuppressWarnings("serial")
public class FunctionalResultTable extends JPanel implements ItemListener
{
	private static final Integer[] POINTS_FOR_WIN_OPTIONS = {2,3};
	
	
	private ResultTable resultTable = new ResultTable();	
	private JPanel optionsPanel;

	private JCheckBox home = new JCheckBox("Heimspiele",true);
	private JCheckBox away = new JCheckBox("Auswärtsspiele",true);
	private JLabel label = new JLabel("Punkte pro Sieg:",SwingConstants.RIGHT);
	private JComboBox<Integer> pointsForWinBox = new JComboBox<>(POINTS_FOR_WIN_OPTIONS);
	
	private JCheckBox relativeTable = new JCheckBox("Nach Spielen gewichtet",false);
	

	private Iterable<Game> games;
	private BiFilter<TeamResult, Game> filter = null;

	public FunctionalResultTable()
	{
		super(new BorderLayout(5, 5));		
		
		home.setBounds(10, 10, 110, 30);
		home.addItemListener(this);
		
		away.setBounds(130, 10, 120, 30);
		away.addItemListener(this);
		
		label.setBounds(340, 10, 100, 30);
		
		pointsForWinBox.setBounds(450, 10, 70, 30);
		pointsForWinBox.addItemListener(this);
		
		relativeTable.setBounds(600,10,170,30);
		relativeTable.addItemListener(this);
		
		optionsPanel = new JPanel();
		optionsPanel.setLayout(null);
		optionsPanel.setPreferredSize(new Dimension(600, 50));
		optionsPanel.add(home);
		optionsPanel.add(away);
		optionsPanel.add(label);
		optionsPanel.add(pointsForWinBox);
		optionsPanel.add(relativeTable);
		
		add(optionsPanel,BorderLayout.NORTH);
		add(new JScrollPane(resultTable),BorderLayout.CENTER);
	}

	public void setSource(Iterable<Game> games)
	{
		this.games = games;
		createTable();
	}
	
	
	private void setFilter()
	{
		if(home.isSelected())
			if(away.isSelected())
				filter = LogicalBiFilter.getTRUEBiFilter();
			else
				filter = TeamResultFilter.getHomeGameFilter();
		else
			if(away.isSelected())
				filter = TeamResultFilter.getAwayGameFilter();
			else
				filter = LogicalBiFilter.getFALSEBiFilter();
		createTable();
	}
	
	
	private void createTable()
	{
		Table table = new Table(games, filter,(Integer)pointsForWinBox.getSelectedItem());
		table.sort();	
		resultTable.setRelativeTable(relativeTable.isSelected());
		resultTable.setSource(table);
	}

	@Override
	public void itemStateChanged(ItemEvent e)
	{
		setFilter();
	}
	
}
