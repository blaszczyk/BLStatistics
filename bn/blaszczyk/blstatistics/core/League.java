package bn.blaszczyk.blstatistics.core;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class League implements Iterable<Season>
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
			FileWriter file = new FileWriter("leagues/"+name + "/" + year + ".bls");
			season.write( file );
			file.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean loadFromFile(File file)
	{
		try
		{
			int year = Integer.parseInt(file.getName().substring(0,4));
			Season season = getSeason(year);
			if(season == null)
			{
				season = new Season(year);
				seasons.add(season);
			}
			FileReader reader = new FileReader(file);
			season.read( reader );
			reader.close();
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean loadFromFile(int year)
	{
		File file = new File("leagues/" + name + "/" + year + ".bls");
		return loadFromFile(file);
	}

	@Override
	public Iterator<Season> iterator()
	{
		return seasons.iterator();
	}
}
