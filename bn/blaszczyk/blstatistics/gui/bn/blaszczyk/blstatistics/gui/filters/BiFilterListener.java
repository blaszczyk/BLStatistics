package bn.blaszczyk.blstatistics.gui.filters;

public interface BiFilterListener<T,U>
{
	public void filter(BiFilterEvent<T,U> e);
}
