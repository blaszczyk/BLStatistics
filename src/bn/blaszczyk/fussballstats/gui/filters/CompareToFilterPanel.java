package bn.blaszczyk.fussballstats.gui.filters;

public interface CompareToFilterPanel<T> extends FilterPanel<T>
{
	public static final String EQ = "=";
	public static final String NEQ = "!=";
	public static final String GEQ = ">=";
	public static final String LL = "<";
	public static final String LEQ = "<=";
	public static final String GG = ">";
	
	public static final String[] OPERATORS = {EQ,NEQ,GG,GEQ,LL,LEQ};
	
	public String getLabel();
	public String getReferenceValString();
	public String getOperator();
	public void invertOperator();

}
