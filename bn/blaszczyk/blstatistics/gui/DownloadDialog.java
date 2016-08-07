package bn.blaszczyk.blstatistics.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;

import bn.blaszczyk.blstatistics.BLStatistics;
import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.core.Season;
import bn.blaszczyk.blstatistics.tools.BLException;
import bn.blaszczyk.blstatistics.tools.FileIO;
import bn.blaszczyk.blstatistics.tools.FussballDatenRequest;
import bn.blaszczyk.blstatistics.tools.SeasonRequest;
import bn.blaszczyk.blstatistics.tools.WeltFussballRequest;

@SuppressWarnings("serial")
public class DownloadDialog extends JDialog implements ActionListener {

	private JDialog owner;
	
	private JTextArea infoArea = new JTextArea();
	private JProgressBar prograssBar;
	private JLabel lblTimeLeft = new JLabel("geschätzte Restzeit: unbekannt");
	private JButton btnCancel = new JButton("Abbrechen");
	
	private SeasonRequest request;
	private List<Season> seasons;
	private int secsLeft = 10;
	private long initTimeStamp = System.currentTimeMillis();
	int counter = 0;
	
	private Thread timeThread = new Thread(()->{
		int cnt = 5;
		while(true)
		{
			SwingUtilities.invokeLater(() -> { infoArea.append("."); } );
			cnt--;
			if(cnt == 0)
			{
				if(secsLeft > 0)
					secsLeft--;
				cnt = 5;
				SwingUtilities.invokeLater(() -> {
					lblTimeLeft.setText(String.format( "geschätzte Restzeit: %2d Sekunde%s",secsLeft, secsLeft == 1 ? "" : "n"));
				});
			}
			try
			{
				Thread.sleep(200);
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
				infoArea.append(String.format("\nLade Saison %s - %4d", season.getLeague().toString(), season.getYear() ) );
				infoArea.setCaretPosition(infoArea.getDocument().getLength());
			});
			try
			{
				requestSeason(season);
			}
			catch (BLException e)
			{
				infoArea.append("\n" + e.getErrorMessage());
				infoArea.setCaretPosition(infoArea.getDocument().getLength());
			}
			counter++;
			secsLeft = (int)( (System.currentTimeMillis() - initTimeStamp) * (seasons.size() - counter) / counter )/1000;
		}
		timeThread.interrupt();
		secsLeft = (int) (System.currentTimeMillis() - initTimeStamp)/1000;
		SwingUtilities.invokeLater( () -> {
			prograssBar.setValue(counter);
			infoArea.append("\nDownload beendet");
			infoArea.setCaretPosition(infoArea.getDocument().getLength());
			lblTimeLeft.setText(String.format( "Dauer: %2d Sekunde%s",secsLeft, secsLeft == 1 ? "" : "n"));
			btnCancel.setText("Fertig");
		});
	});

	public DownloadDialog(JDialog owner, List<Season> seasons)
	{
		super(owner, "Saison Download", true);
		this.owner = owner;
		this.seasons = seasons;
		
		switch (BLStatistics.getRequestSource())
		{
		case BLStatistics.FUSSBALL_DATEN:
			request = new FussballDatenRequest();
			break;
		case BLStatistics.WELT_FUSSBALL:
			request = new WeltFussballRequest();
			break;
		}
		
		secsLeft = seasons.size();
		
		setLayout(null);
		setSize(606, 340);
		setResizable(false);

		infoArea.setEditable(false);
		infoArea.setWrapStyleWord(true);
		infoArea.append("Starte Download");

		JScrollPane infoPane = new JScrollPane(infoArea);
		infoPane.setBounds(10, 10, 580, 200);
		
		prograssBar = new JProgressBar(0, seasons.size());
		prograssBar.setStringPainted(true);
		prograssBar.setBounds(10, 210, 580, 40);
		
		lblTimeLeft.setBounds(10, 260, 300, 30);
		
		btnCancel.setBounds(440, 260, 150, 30);
		btnCancel.addActionListener(this);
		btnCancel.setMnemonic('r');
		
		add(infoPane);
		add(prograssBar);
		add(lblTimeLeft);
		add(btnCancel);
	}
	

	public void showDialog()
	{
		dlThread.start();
		timeThread.start();
		setLocationRelativeTo(owner);
		setVisible(true);	
	}
	
	
	private void requestSeason(Season season) throws BLException
	{		
		request.requestData(season);
		Iterable<Game> games = request.getGames();
		season.consumeGames(games);
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
