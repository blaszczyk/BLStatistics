package bn.blaszczyk.blstatistics.gui.tools;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public abstract class MyTableModel<T> implements TableModel 
{

	private List<T> tList;
	
	
	public MyTableModel(List<T> tList)
	{
		this.tList = tList;
	}
	
	public T getRowObject(int rowIndex)
	{
		return tList.get(rowIndex);
	}
	
	protected abstract Object getColumnValue(T t, int columnIndex);
	
	/*
	 * TableModel Methods
	 */
	@Override
	public int getRowCount()
	{
		return tList.size();
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return getColumnValue(getRowObject(rowIndex), columnIndex);
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{		
	}
	
	@Override
	public void addTableModelListener(TableModelListener l)
	{
	}
	
	@Override
	public void removeTableModelListener(TableModelListener l)
	{
	}
	
}
