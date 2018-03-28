package bn.blaszczyk.fussballstats.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.fussballstats.model.*;
import bn.blaszczyk.fussballstats.gui.tools.ProgressDialog;
import bn.blaszczyk.fussballstats.tools.FussballException;
import bn.blaszczyk.fussballstats.tools.WeltFussballRequest;
import bn.blaszczyk.rose.RoseException;
import bn.blaszczyk.rosecommon.controller.ModelController;

@SuppressWarnings("serial")
public class LeagueManager extends JDialog implements ListSelectionListener, ActionListener 
{
	/*
	 * Constatns
	 */
	private static final String	ICON_FILE			= "data/manager.png";
	private static final String	DL_ICON_FILE		= "data/download.png";
	
	/*
	 * Components
	 */
	private final JList<League>	listLeagues;
	private final JTable tableSeasons;
	
	private final JButton btnClose = new JButton("Schließen");
	private final JButton btnUpdate = new JButton("Aktualisieren");
	private final JButton btnSeasonRequest = new JButton("Download");
	

	/*
	 * Variables
	 */
	private Window				owner;
	private List<League>		leagues;
	private final ModelController controller;
	
	/*
	 * Constructors
	 */
	public LeagueManager(Window owner, final ModelController controller)
	{
		super(owner, ModalityType.APPLICATION_MODAL);
		this.leagues = controller.getEntities(League.class);
		this.controller = controller;
		this.owner = owner;
		setTitle("Liga Manager");
		setSize(654, 405);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE)));
		setLayout(null);
		
		listLeagues = new JList<>(leagues.toArray(new League[leagues.size()]));
		listLeagues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listLeagues.addListSelectionListener(this);
		
		JScrollPane spLeagues = new JScrollPane(listLeagues);
		spLeagues.setBounds(10, 10, 230, 300);
		
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
		spSeasons.setBounds(250, 10, 390, 300);
		
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
	
	/*
	 * Show on Screen
	 */
	public void showDialog()
	{
		setLocationRelativeTo(owner);
		setVisible(true);
		repaint();
	}

	/*
	 * Internal Methods
	 */
	private void populateSeasonTable(League league)
	{
		if (league == null)
			return;
		Object[] columnNames = { "Saison", "Vereine", "Spieltage", "Spiele" };
		DefaultTableModel tm = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		List<Object[]> rows = new ArrayList<>();
		for (Season season : league.getSeasons())
		{
			final int teamCount = season.getTeams().size();
			final int matchdayCount = season.getMatchdays().size();
			final int gameCount = season.getMatchdays().stream().map(Matchday::getGames).mapToInt(Collection::size).sum();
			rows.add(new Object[]{ season.getYear(), teamCount, matchdayCount, gameCount });
		}
		rows.sort((o1, o2) -> Integer.compare((Integer) o2[0], (Integer) o1[0]));
		for (Object[] rowData : rows)
			tm.addRow(rowData);
		tableSeasons.setModel(tm);
	}
	
	private void updateSeasons()
	{
		final List<Season> currentSeasons = leagues.stream()
				.map(League::getSeasons)
				.flatMap(Collection::stream)
				.filter(s -> s.getYear().intValue() == FussballStats.THIS_SEASON)
				.collect(Collectors.toList());
		requestSeasons(currentSeasons);
	}
	
	private void requestSeasons()
	{
		final List<Season> seasons = new ArrayList<>();
		if (tableSeasons.getSelectedRows().length == 0)
		{
			if (listLeagues.getSelectedValue() != null)
			{
				final League league = listLeagues.getSelectedValue();
				seasons.addAll(league.getSeasons());
			}
		}
		else
		{
			for (int i : tableSeasons.getSelectedRows())
			{
				final int year = (int) tableSeasons.getModel().getValueAt(i, 0);
				final League league = listLeagues.getSelectedValue();
				league.getSeasons().stream()
					.filter(s -> s.getYear().intValue() == year)
					.forEach(seasons::add);
			}
		}
		requestSeasons(seasons);
	}
	
	private void requestSeasons(List<Season> seasons)
	{
		new Thread(() -> {
			Image icon = Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(DL_ICON_FILE));
			ProgressDialog progressDialog = new ProgressDialog(this, seasons.size(), "Download", icon, true);
			WeltFussballRequest request = new WeltFussballRequest(controller);
			SwingUtilities.invokeLater(() -> progressDialog.showDialog());//possible bug location
			progressDialog.appendInfo("Starte Download");
			try
			{
				for (Season season : seasons)
				{
					if (progressDialog.hasCancelRequest())
						if ( JOptionPane.showConfirmDialog(progressDialog, "Downloads Abbrechen?",
								"Abbruch Bestätigen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
							break;
						else
							progressDialog.undoCancelRequest();
					try
					{
						progressDialog.appendInfo("\nLade Saison: " + season);
						request.requestData(season);
						progressDialog.appendInfo(".Fertig");
					}
					catch (RoseException e)
					{
						e.printStackTrace();
						progressDialog.appendException(e);
					}
					progressDialog.incrementValue();
				}
			}
			catch (RoseException e)
			{
				e.printStackTrace();
				progressDialog.appendException(e);
			}
			
			progressDialog.appendInfo("\nDownloads Beendet");
			progressDialog.setFinished();
			populateSeasonTable(listLeagues.getSelectedValue());
		}).start();
	}
	
	/*
	 * ListSelectionListener Methods
	 */
	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		if (!e.getValueIsAdjusting())
		{
			if (e.getSource() == listLeagues)
				populateSeasonTable(listLeagues.getSelectedValue());
		}
	}

	/*
	 * ActionSelectionListener Methods
	 */
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
	
}
