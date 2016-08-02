package bn.blaszczyk.blstatistics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.text.NumberFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import bn.blaszczyk.blstatistics.core.Game;

@SuppressWarnings("serial")
public class FunctionalGameTable extends JPanel
{
	private static final int ROW_HEIGHT = 25;
	private static final int ROW_BORDER = 10;
	
	private static final Font FONT = new Font("Arial",Font.BOLD,16);
	private JPanel summaryPanel = new JPanel();
	private GameTable gameTable = new GameTable();
	
	private int summaryRowCount = 0;
	private int nrGames;
	private int nrGoals;
	private int nrHomeGoals;
	private int nrAwayGoals;
	private int nrHomeWins;
	private int nrDraws;
	private int nrAwayWins;
	
	
	public FunctionalGameTable()
	{
		super(new BorderLayout(5,5));
		
		summaryPanel.setLayout(null);
		summaryPanel.setPreferredSize(new Dimension(350 , 7 * ROW_HEIGHT + 2 * ROW_BORDER));
				
		add(summaryPanel, BorderLayout.NORTH);
		add( new JScrollPane(gameTable), BorderLayout.CENTER);
	}

	public GameTable getGameTable()
	{
		return gameTable;
	}

	private void clearStatistics()
	{
		nrGames = 0;
		nrGoals = 0;
		nrHomeGoals = 0;
		nrAwayGoals = 0;
		nrHomeWins = 0;
		nrDraws = 0;
		nrAwayWins = 0;
	}
	
	private void addToStatistics(Game game)
	{
		nrGames++;
		nrGoals += game.getGoals();
		nrHomeGoals += game.getGoals1();
		nrAwayGoals += game.getGoals2();
		switch(game.getWinner())
		{
		case Game.HOME:
			nrHomeWins++;
			break;
		case Game.AWAY:
			nrAwayWins++;
			break;
		case Game.DRAW:
			nrDraws++;
			break;
		}
	}
	
	public void setGameList( Iterable<Game> gameList)
	{
		gameTable.setSource(gameList);
		clearStatistics();
		for(Game game : gameList)
			addToStatistics(game);
		drawSummaryPanel();
	}
	
	private void drawSummaryPanel()
	{
		summaryRowCount = 0;
		summaryPanel.removeAll();
		addSummaryRow("Spiele", nrGames);
		addSummaryRow("Tore", nrGoals);
		addSummaryRow("Heimtore", nrHomeGoals);
		addSummaryRow("Auswärtstore", nrAwayGoals);
		addSummaryRow("Heimsiege", nrHomeWins);
		addSummaryRow("Auswärtssiege", nrAwayWins);
		addSummaryRow("Unentschieden", nrDraws);
		repaint();
	}
	
	private void addSummaryRow(String text, int value)
	{
		JLabel textLabel = new JLabel(text + ":", SwingConstants.RIGHT);
		textLabel.setBounds(110, ROW_BORDER + summaryRowCount * ROW_HEIGHT, 150, ROW_HEIGHT);
		textLabel.setFont(FONT);
		summaryPanel.add(textLabel);
		
		JLabel valueLabel = new JLabel( NumberFormat.getIntegerInstance().format(value), SwingConstants.RIGHT);
		valueLabel.setBounds(270, ROW_BORDER + summaryRowCount * ROW_HEIGHT, 50, ROW_HEIGHT);
		valueLabel.setFont(FONT);
		summaryPanel.add(valueLabel);
		
		summaryRowCount++;
		
	}
	
	

	
	
}
