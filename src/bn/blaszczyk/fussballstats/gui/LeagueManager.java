package bn.blaszczyk.fussballstats.gui;

import java.awt.Image;
import java.awt.Toolkit;
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

import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.core.League;
import bn.blaszczyk.fussballstats.core.Season;
import bn.blaszczyk.fussballstats.gui.tools.ProgressDialog;
import bn.blaszczyk.fussballstats.tools.FussballException;
import bn.blaszczyk.fussballstats.tools.DBConnection;
import bn.blaszczyk.fussballstats.tools.DBTools;
import bn.blaszczyk.fussballstats.tools.FileIO;
import bn.blaszczyk.fussballstats.tools.WeltFussballRequest;

@SuppressWarnings("serial")
public class LeagueManager extends JDialog implements ListSelectionListener, ActionListener 
{
	private static final String	ICON_FILE			= "data/manager.png";
	private static final String	DL_ICON_FILE		= "data/download.png";
	
	private JFrame				owner;
	
	private JList<LeagueItem>	listLeagues;
	private JTable				tableSeasons;
	
	private JButton				btnClose			= new JButton("Schlieﬂen");
	private JButton				btnUpdate			= new JButton("Aktualisieren");
	private JButton				btnSeasonRequest	= new JButton("Download");
	
	private List<League>		leagues;
	
	private static boolean dbMode = false;
	
	public LeagueManager(JFrame owner, List<League> leagues)
	{
		super(owner, "Liga Manager", true);
		this.leagues = leagues;
		this.owner = owner;
		setSize(654, 405);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE)));
		setLayout(null);
		
		listLeagues = new JList<>(createLeagueItems(leagues));
		listLeagues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listLeagues.addListSelectionListener(this);
		
		JScrollPane spLeagues = new JScrollPane(listLeagues);
		spLeagues.setBounds(10, 10, 210, 300);
		
		tableSeasons = new JTable() {
			DefaultTableCellRenderer renderCenter = new DefaultTableCellRenderer();
			{
				renderCenter.setHorizontalAlignment(SwingConstants.CENTER);
			}
			
			@Override
			public TableCellRenderer getCellRenderer(int arg0, int arg1)
			{
				return renderCenter;
			}
		};
		tableSeasons.getSelectionModel().addListSelectionListener(this);
		tableSeasons.setColumnSelectionAllowed(false);
		tableSeasons.setRowSelectionAllowed(true);
		tableSeasons.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		
		JScrollPane spSeasons = new JScrollPane(tableSeasons);
		spSeasons.setBounds(230, 10, 410, 300);
		
		btnSeasonRequest.addActionListener(this);
		btnSeasonRequest.setBounds(10, 10, 190, 30);
		btnSeasonRequest.setMnemonic('d');
		
		btnUpdate.addActionListener(this);
		btnUpdate.setBounds(220, 10, 190, 30);
		btnUpdate.setMnemonic('a');
		
		btnClose.addActionListener(this);
		btnClose.setBounds(430, 10, 190, 30);
		btnClose.setMnemonic('s');
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(10, 320, 630, 50);
		panel.add(btnSeasonRequest);
		panel.add(btnUpdate);
		panel.add(btnClose);
		
		add(spLeagues);
		add(spSeasons);
		add(panel);
		setResizable(false);
		
	}
	
	public void showDialog()
	{
		setLocationRelativeTo(owner);
		setVisible(true);
		repaint();
	}
	
	public static boolean isDbMode()
	{
		return dbMode;
	}

	public static void setDbMode(boolean dbMode)
	{
		LeagueManager.dbMode = dbMode;
	}

	private void populateSeasonTable(LeagueItem leagueItem)
	{
		if (leagueItem == null)
			return;
		Object[] columnNames = { "Saison", "Spieltage", "Teams", "Spiele" };
		DefaultTableModel tm = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		List<Object[]> rows = new ArrayList<>();
		for (League league : leagueItem)
			for (Season season : league)
				rows.add(new Object[]{ season.getYear(), season.getMatchDayCount(), season.getTeamCount(), season.getGameCount() });
		rows.sort((o1, o2) -> Integer.compare((Integer) o1[0], (Integer) o2[0]));
		for (Object[] rowData : rows)
			tm.addRow(rowData);
		tableSeasons.setModel(tm);
	}
	
	private void updateSeasons()
	{
		List<Season> currentSeasons = new ArrayList<>();
		for (League league : leagues)
			if (league.hasSeason(League.THIS_SEASON))
				currentSeasons.add(league.getSeason(League.THIS_SEASON));
		requestSeasons(currentSeasons);
	}
	
	private void requestSeasons()
	{
		List<Season> seasons = new ArrayList<>();
		if (tableSeasons.getSelectedRows().length == 0)
		{
			if (listLeagues.getSelectedValue() != null)
				for (League league : listLeagues.getSelectedValue())
					for (Season season : league)
						seasons.add(season);
		}
		else
			for (int i : tableSeasons.getSelectedRows())
			{
				int year = (int) tableSeasons.getModel().getValueAt(i, 0);
				for (League league : listLeagues.getSelectedValue())
					if (league.hasSeason(year))
						seasons.add(league.getSeason(year));
			}
		requestSeasons(seasons);
	}
	
	private LeagueItem[] createLeagueItems(List<League> leagues)
	{
		List<LeagueItem> leagueItems = new ArrayList<>();
		for (League league : leagues)
		{
			boolean exists = false;
			for (LeagueItem leagueItem : leagueItems)
				exists |= leagueItem.addLeague(league);
			if (!exists)
				leagueItems.add(new LeagueItem(league));
		}
		LeagueItem[] leagueArray = new LeagueItem[leagues.size()];
		leagueItems.toArray(leagueArray);
		return leagueArray;
	}
	
	private void requestSeasons(List<Season> seasons)
	{
		new Thread(() -> {
			Image icon = Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(DL_ICON_FILE));
			ProgressDialog progressDialog = new ProgressDialog(this, seasons.size(), "Download", icon, true);
			WeltFussballRequest request = new WeltFussballRequest();
			SwingUtilities.invokeLater(() -> progressDialog.showDialog());
			try
			{
				if(dbMode)
				{
					progressDialog.appendInfo("Verbinde mit Datenbank");
					DBTools.openMySQLDatabase();
				}
				for (Season season : seasons)
				{
					if (progressDialog.hasCancelRequest())
						if ( JOptionPane.showConfirmDialog(progressDialog, "Downloads Abbrechen?",
								"Abbruch Best‰tigen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
							break;
						else
							progressDialog.undoCancelRequest();
					try
					{
						progressDialog.appendInfo(String.format("\nLade Saison: %s - %4d", season.getLeague().toString(), season.getYear()));
						request.requestData(season);
						List<Game> games = request.getGames();
						season.setGames(games);
						
						if(dbMode)
						{
							if(!DBTools.tableExists(season.getLeague()));
								DBTools.createTable(season.getLeague());
							DBTools.insertSeason(season);
						}
						else
							FileIO.saveSeason(season);
						
						progressDialog.appendInfo(".Fertig");
					}
					catch (FussballException e)
					{
						progressDialog.appendException(e);
					}
					progressDialog.incrementValue();
				}
			}
			catch (FussballException e)
			{
				progressDialog.appendException(e);
			}
			
			progressDialog.appendInfo("\nDownloads Beendet");
			if(dbMode)
				DBConnection.closeConnection();
			progressDialog.setFinished();
		}).start();
	}
	
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
		{
			if (e.getSource() == listLeagues)
				populateSeasonTable(listLeagues.getSelectedValue());
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == btnClose)
			dispose();
		else if (e.getSource() == btnUpdate)
			updateSeasons();
		else if (e.getSource() == btnSeasonRequest)
			requestSeasons();
		populateSeasonTable(listLeagues.getSelectedValue());
	}
	
	private class LeagueItem implements Iterable<League> 
	{
		private String			path;
		private List<League>	leagues;
		
		private LeagueItem(League league)
		{
			leagues = new ArrayList<>();
			leagues.add(league);
			path = league.getPathName();
		}
		
		private boolean addLeague(League league)
		{
			if (!league.getPathName().equals(path))
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
	
	
}
