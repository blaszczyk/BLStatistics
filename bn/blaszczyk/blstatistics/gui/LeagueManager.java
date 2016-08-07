package bn.blaszczyk.blstatistics.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import bn.blaszczyk.blstatistics.core.League;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.tools.BLException;
import bn.blaszczyk.blstatistics.tools.FileIO;

@SuppressWarnings("serial")
public class LeagueManager extends JDialog implements ListSelectionListener, ActionListener
{
	private class LeagueItem implements Iterable<League>
	{
		private String path;
		private List<League> leagues;
		
		private LeagueItem(League league)
		{
			leagues = new ArrayList<>();
			leagues.add(league);
			path = league.getPathName();
		}

		private boolean addLeague(League league)
		{
			if(!league.getPathName().equals(path))
				return false;
			leagues.add(league);
			return true;
		}
		
		@Override
		public Iterator<League> iterator()
		{
			return leagues.iterator();
		}
		
		@Override
		public String toString()
		{
			return path;
		}
		
	}
	
	private JFrame owner;
	
	private JList<LeagueItem> leagueList;
	private JTable seasonTable;
	private JPanel actionPanel;

	private JButton btnClose = new JButton("Schlieﬂen");
	private JButton btnSelect = new JButton("Markieren");
	private JButton btnSeasonRequest = new JButton("Download");
	
	public LeagueManager(JFrame owner, List<League> leagues)
	{
		super(owner, "Liga Manager", true);
		this.owner = owner;
		setSize(654,405);
		setLayout(null);
				
		leagueList = new JList<>(createLeagueItems(leagues));
		leagueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		leagueList.addListSelectionListener( this );
		
		JScrollPane leaguePane = new JScrollPane(leagueList);
		leaguePane.setBounds(10,10,210, 300);
		
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
		
		JScrollPane seasonPane = new JScrollPane(seasonTable);
		seasonPane.setBounds(230, 10, 410, 300);
		
		btnSeasonRequest.addActionListener(this);
		btnSeasonRequest.setBounds(10, 10, 190, 30);
		btnSeasonRequest.setMnemonic('d');
		
		btnSelect.addActionListener(this);
		btnSelect.setBounds(220, 10, 190, 30);
		btnSelect.setMnemonic('m');
		
		btnClose.addActionListener(this);
		btnClose.setBounds(430, 10, 190, 30);
		btnClose.setMnemonic('s');
		
		actionPanel = new JPanel();
		actionPanel.setLayout(null);
		actionPanel.setBounds(10,320,630, 50);
		actionPanel.add(btnSeasonRequest);
		actionPanel.add(btnSelect);
		actionPanel.add(btnClose);
		
		
		
		add(leaguePane);
		add(seasonPane);
		add(actionPanel);
		setResizable(false);
			
	}

	public void showDialog()
	{
		setLocationRelativeTo(owner);
		setVisible(true);	
		repaint();
	}
	
	private void populateSeasonTable(LeagueItem leagueItem)
	{
		Object[] columnNames = {"Saison","Vorhanden", "Teams", "Spiele"};
		DefaultTableModel tm = new DefaultTableModel(columnNames,0){
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		for(League league : leagueItem)
			for(Season season : league)
			{
				String isSaved = FileIO.isSeasonSaved(season)? "Ja": "Nein";
				Object[] rowData = {season.getYear(), isSaved, season.getTeamCount(), season.getGameCount()};
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
		try
		{
			if(seasonTable.getSelectedRows().length == 0)
			{
				JOptionPane.showMessageDialog(this, "Keine Saisons markiert", "Fehler", JOptionPane.ERROR_MESSAGE);
				return;
			}
			List<Season> seasons = new ArrayList<>();
			for( int i : seasonTable.getSelectedRows() )
			{
				int year = (int) seasonTable.getModel().getValueAt(i, 0);
				for(League league : leagueList.getSelectedValue())
					if(league.hasSeason(year))
						seasons.add( league.getSeason(year) );
			}
			DownloadDialog dlDialog = new DownloadDialog(this, seasons);
			dlDialog.showDialog();
		}
		catch (BLException e)
		{
			e.printStackTrace();
		}
	}
	

	private LeagueItem[] createLeagueItems(List<League> leagues)
	{
		List<LeagueItem> leagueItems = new ArrayList<>();
		for( League league : leagues)
		{
			boolean exists = false;
			for(LeagueItem leagueItem : leagueItems)
				exists |= leagueItem.addLeague(league);
			if(!exists)
				leagueItems.add(new LeagueItem(league));
		}
		LeagueItem[] leagueArray = new LeagueItem[leagues.size()];
		leagueItems.toArray(leagueArray);
		return leagueArray; 
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if(!e.getValueIsAdjusting())
		{
			if(e.getSource() == leagueList)
				populateSeasonTable( leagueList.getSelectedValue() );
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
		{
			requestSeasons();
			populateSeasonTable( leagueList.getSelectedValue() );
		}
	}


}
