package bn.blaszczyk.blstatistics.gui;

import java.util.List;

import bn.blaszczyk.blstatistics.core.Game;

public class GameTableModel extends MyTableModel<Game>
{	
	public GameTableModel(List<Game> games)
	{
		super(games);
	}


	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if(columnIndex > 2)
			return Integer.class;
		return String.class;
	}

	@Override
	public int getColumnCount()
	{
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
			return "Datum";
		case 1:
			return "Heim";
		case 2:
			return "Gast";
		case 3:
			return "Tore Heim";
		case 4:
			return "Tore Gast";
		}
		return null;
	}

	@Override
	protected Object getColumnValue(Game game, int columnIndex)
	{
		switch(columnIndex)
		{
		case 0:
			return Game.DATE_FORMAT.format(game.getDate());
		case 1:
			return game.getTeam1();
		case 2:
			return game.getTeam2();
		case 3:
			return game.getGoals1();
		case 4:
			return game.getGoals2();
		}
		return "";
	}

}
