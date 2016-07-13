package bn.blaszczyk.blstatistics.core;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class League
{
	private List<Season> seasons = new ArrayList<>();
	private String name;

	public League(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}
	
	public List<Season> getSeasons()
	{
		return seasons;
	}

	public boolean addSeason(Season e)
	{
		return seasons.add(e);
	}
	
	public Season getSeason(int year)
	{
		for(Season s : seasons)
			if(s.getYear() == year)
				return s;
		return null;
	}
	
	public void saveToFile(int year)
	{
		Season season = getSeason(year);
		if(season == null)
			return;
		try
		{
			FileWriter file = new FileWriter(name + "/" + year + ".bls");
			season.write( file );
			file.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void loadFromFile(int year)
	{
		Season season = getSeason(year);
		if(season == null)
		{
			season = new Season(year);
			seasons.add(season);
		}
		try
		{
			FileReader file = new FileReader(name + "/" + year + ".bls");
			season.read( file );
			file.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
