package bn.blaszczyk.blstatistics.filters;

public interface FilterCreator<T,U>
{
	public Filter<T> createFilter(U u);
}
