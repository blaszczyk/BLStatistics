package bn.blaszczyk.blstatistics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Stack;

import javax.swing.*;


import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.tools.BLException;
import bn.blaszczyk.blstatistics.tools.FileIO;
import bn.blaszczyk.blstatistics.tools.FussballDatenRequest;

@SuppressWarnings("serial")
public class DownloadDialog extends JDialog implements ActionListener {

	private JDialog owner;
	
	private JLabel lblInfo = new JLabel("Starte Downloads");
	private JProgressBar prograssBar;
	private JLabel lblTimeLeft = new JLabel("geschätzte Restzeit: unbekannt");
	private JButton btnCancel = new JButton("Abbrechen");
	
	private List<Season> seasons;
	private int secsLeft = 10;
	private long initTimeStamp = System.currentTimeMillis();
	int counter = 0;
	
	private Thread dlThread = new Thread(() -> {
		for(Season season : seasons)
		{
			SwingUtilities.invokeLater( () -> {
				prograssBar.setValue(counter);
				lblInfo.setText(String.format("aktueller Download: %s - %4d",season.getLeague(),season.getYear()));
				lblTimeLeft.setText(String.format( "geschätzte Restzeit: %2d:%02d",secsLeft/60,secsLeft%60));
			});
			try
			{
				requestSeason(season);
			}
			catch (BLException e)
			{
				e.printStackTrace();
			}
			counter++;
			long timeStamp = System.currentTimeMillis();
			secsLeft = (int)( (timeStamp - initTimeStamp) * (seasons.size() - counter) / counter )/1000;
		}
		SwingUtilities.invokeLater( () -> {
			prograssBar.setValue(counter);
			lblInfo.setText(String.format("Download Beendet"));
			btnCancel.setText("Fertig");
		});
//		timeLeftThread.interrupt();
		secsLeft = 0;
	});

	public DownloadDialog(JDialog owner, List<Season> seasons)
	{
		super(owner, "Saison Download", true);
		this.owner = owner;
		this.seasons = seasons;
		
		secsLeft = seasons.size();
		
		setLayout(null);
		setSize(606, 180);
		setResizable(false);
		
		lblInfo.setBounds(10, 10, 300, 30);
		
		prograssBar = new JProgressBar(0, seasons.size());
		prograssBar.setStringPainted(true);
		prograssBar.setBounds(10, 50, 580, 40);
		
		lblTimeLeft.setBounds(10, 100, 300, 30);
		
		btnCancel.setBounds(440, 100, 150, 30);
		btnCancel.addActionListener(this);
		
		add(lblInfo);
		add(prograssBar);
		add(lblTimeLeft);
		add(btnCancel);
	}
	

	public void showDialog()
	{
		dlThread.start();
//		timeLeftThread.start();
		setLocationRelativeTo(owner);
		setVisible(true);	
	}
	

	private void requestSeason(Season season) throws BLException
	{		
		FussballDatenRequest.requestTable(season.getYear(),season.getLeague().getName());
		Stack<Game> gameStack = FussballDatenRequest.getGames();
		FussballDatenRequest.clearTable();
		season.addGames(gameStack);
		FileIO.saveSeason(season);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( dlThread.isAlive())
			if(JOptionPane.showConfirmDialog(this, "Downloads Abbrechen?", "Abbrechen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION)
				return;
		if(!dlThread.isInterrupted())
			dlThread.interrupt();
		dispose();			
	}
	
	
}
