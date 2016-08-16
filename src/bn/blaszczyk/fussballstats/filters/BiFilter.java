package bn.blaszczyk.fussballstats.filters;

public interface BiFilter <T,U>
{
	public boolean check(T t,U u);
}
