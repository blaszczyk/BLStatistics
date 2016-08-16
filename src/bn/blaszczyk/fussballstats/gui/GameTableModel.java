package bn.blaszczyk.fussballstats.gui;

import java.util.List;

import bn.blaszczyk.fussballstats.core.Game;
import bn.blaszczyk.fussballstats.gui.tools.MyTableModel;
import bn.blaszczyk.fussballstats.tools.TeamAlias;

public class GameTableModel extends MyTableModel<Game>
{	
	public GameTableModel(List<Game> games)
	{
		super(games);
	}


	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		if(columnIndex == 2 || columnIndex == 4)
			return Integer.class;
		return String.class;
	}

	@Override
	public int getColumnCount()
	{
		return 6;
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
		case 5:
			return "Gast";
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
			return TeamAlias.getAlias(game.getTeamH());
		case 2:
			return game.getGoalsH();
		case 3:
			return " : ";
		case 4:
			return game.getGoalsA();
		case 5:
			return TeamAlias.getAlias(game.getTeamA());
		}
		return "";
	}

}
