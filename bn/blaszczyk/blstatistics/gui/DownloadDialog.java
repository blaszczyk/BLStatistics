package bn.blaszczyk.blstatistics.gui;

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
	
	private Thread timeThread = new Thread(()->{
		while(true)
		{
			SwingUtilities.invokeLater(() -> {
				lblTimeLeft.setText(String.format( "geschätzte Restzeit: %2d Sekunde%s",secsLeft, secsLeft == 1 ? "" : "n"));
			});
			if(secsLeft > 0)
				secsLeft--;
			try
			{
				Thread.sleep(1000);
			}
			catch (InterruptedException e)
			{
				return;
			}
		}
	});
	
	private Thread dlThread = new Thread(() -> {
		for(Season season : seasons)
		{
			SwingUtilities.invokeLater( () -> {
				prograssBar.setValue(counter);
				lblInfo.setText(String.format("aktueller Download: %s - %4d",season.getLeague(),season.getYear()));
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
			secsLeft = (int)( (System.currentTimeMillis() - initTimeStamp) * (seasons.size() - counter) / counter )/1000;
		}
		timeThread.interrupt();
		secsLeft = (int) (System.currentTimeMillis() - initTimeStamp)/1000;
		SwingUtilities.invokeLater( () -> {
			prograssBar.setValue(counter);
			lblInfo.setText(String.format("Download Beendet"));
			lblTimeLeft.setText(String.format( "Dauer: %2d Sekunde%s",secsLeft, secsLeft == 1 ? "" : "n"));
			btnCancel.setText("Fertig");
		});
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
		
		lblInfo.setBounds(10, 10, 580, 30);
		
		prograssBar = new JProgressBar(0, seasons.size());
		prograssBar.setStringPainted(true);
		prograssBar.setBounds(10, 50, 580, 40);
		
		lblTimeLeft.setBounds(10, 100, 300, 30);
		
		btnCancel.setBounds(440, 100, 150, 30);
		btnCancel.addActionListener(this);
		btnCancel.setMnemonic('r');
		
		add(lblInfo);
		add(prograssBar);
		add(lblTimeLeft);
		add(btnCancel);
	}
	

	public void showDialog()
	{
		dlThread.start();
		setLocationRelativeTo(owner);
		setVisible(true);	
	}
	

	private void requestSeason(Season season) throws BLException
	{		
		FussballDatenRequest.requestData(season);
		Stack<Game> gameStack = FussballDatenRequest.getGames();
		FussballDatenRequest.clearTable();
		season.consumeGames(gameStack);
		FileIO.saveSeason(season);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if( dlThread.isAlive())
			if(JOptionPane.showConfirmDialog(this, "Sollen die Downloads abgebrochen werden?", "Downloads Abbrechen", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION)
				return;
		if(!dlThread.isInterrupted())
			dlThread.interrupt();
		if(!timeThread.isInterrupted())
			timeThread.interrupt();
		dispose();			
	}
	
	
}
