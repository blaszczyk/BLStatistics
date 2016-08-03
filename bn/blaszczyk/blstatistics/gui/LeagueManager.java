package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.League;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.tools.BLException;
import bn.blaszczyk.blstatistics.tools.FileIO;
import bn.blaszczyk.blstatistics.tools.FussballDatenRequest;

@SuppressWarnings("serial")
public class LeagueManager extends JDialog implements ListSelectionListener, ActionListener
{
	private JFrame owner;
	
	private JList<League> leagueList;
	private JTable seasonTable;
	private JPanel actionPanel;

	private JButton btnClose = new JButton("Schlie�en");
	private JButton btnSelect = new JButton("Markiere Ungeladene");
	private JButton btnSeasonRequest = new JButton("Saisons Downloaden");
	
	
	/*
	 * TODO:
	 * - Implement nice Download Dialog
	 * - Find nice way to open on Request in MainFrame
	 */
	
	public LeagueManager(JFrame owner, League[] leagues)
	{
		super(owner, "Liga Manager", true);
		this.owner = owner;
		setLayout(new BorderLayout(5,5));
		setResizable(false);
		
		leagueList = new JList<>(leagues);
		leagueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		leagueList.addListSelectionListener( this );
		leagueList.setPreferredSize(new Dimension(150, 300));
		
		seasonTable = new JTable() {
		    DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
		    {
		        renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
		    }
		    @Override
		    public TableCellRenderer getCellRenderer (int arg0, int arg1) {
		        return renderCenter;
		    }
		};
		seasonTable.getSelectionModel().addListSelectionListener(this);
		seasonTable.setColumnSelectionAllowed(false);
		seasonTable.setRowSelectionAllowed(true);
		seasonTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		btnSeasonRequest.addActionListener(this);
		btnSeasonRequest.setBounds(10, 10, 190, 30);
		
		btnSelect.addActionListener(this);
		btnSelect.setBounds(220, 10, 190, 30);
		
		btnClose.addActionListener(this);
		btnClose.setBounds(430, 10, 190, 30);
		
		actionPanel = new JPanel();
		actionPanel.setLayout(null);
		actionPanel.setPreferredSize(new Dimension(630, 50));
		actionPanel.add(btnSeasonRequest);
		actionPanel.add(btnSelect);
		actionPanel.add(btnClose);
		
		add(new JScrollPane(leagueList),BorderLayout.WEST);
		add(new JScrollPane(seasonTable),BorderLayout.CENTER);
		add(actionPanel,BorderLayout.SOUTH);
		pack();
			
	}

	public void showDialog()
	{
		setLocationRelativeTo(owner);
		setVisible(true);	
		repaint();
	}
	
	private void setSeasonTable(League league)
	{
		Object[] columnNames = {"Saison","Daten Vorhanden"};
		DefaultTableModel tm = new DefaultTableModel(columnNames,0){
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		for(Season season : league)
		{
			String isSaved = FileIO.isSeasonSaved(season)? "Ja": "Nein";
			Object[] rowData = {season.getYear(), isSaved};
			tm.addRow(rowData);
		}
		seasonTable.setModel(tm);
	}
	
	private void selectUnloaded()
	{
		seasonTable.getSelectionModel().clearSelection();
		for(int i = 0; i < seasonTable.getRowCount(); i++)
			if(seasonTable.getModel().getValueAt(i, 1).equals("Nein"))
				seasonTable.getSelectionModel().addSelectionInterval(i, i);
				
	}
	
	private void requestSeasons()
	{
		for( int i : seasonTable.getSelectedRows() )
		{
			int year = (int) seasonTable.getModel().getValueAt(i, 0);
			try
			{
				requestSeason(leagueList.getSelectedValue().getSeason(year));
			}
			catch (BLException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	private void requestSeason(Season season) throws BLException
	{		
		FussballDatenRequest.requestTableMuted(season.getYear(),season.getLeague().getName());
		Stack<Game> gameStack = FussballDatenRequest.getGames();
		FussballDatenRequest.clearTable();
		season.addGames(gameStack);
		FileIO.saveSeason(season);
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if(!e.getValueIsAdjusting())
		{
			if(e.getSource() == leagueList)
				setSeasonTable( leagueList.getSelectedValue() );
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btnClose)
			dispose();
		else if(e.getSource() == btnSelect)
			selectUnloaded();
		else if(e.getSource() == btnSeasonRequest)
			requestSeasons();
	}


}