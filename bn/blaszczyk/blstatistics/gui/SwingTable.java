package bn.blaszczyk.blstatistics.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public abstract class SwingTable<T> extends JTable implements MouseListener
{
	private List<T> tList;
	private Comparator<T> comparator;
//	private boolean compareBackwards = false;

	
	public SwingTable(Iterable<T> source)
	{
		this();
		setSource(source);
	}
	
	public SwingTable()
	{
		for(int i = 0 ; i < this.getColumnCount(); i++)
		{
			int width = columnWidth(i);
			if( width >= 0 )
				getColumnModel().getColumn(i).setPreferredWidth(width);
		}
		getTableHeader().addMouseListener(this);
	}

	public void setSource(Iterable<T> source)
	{
		tList = new ArrayList<>();
		for(T t : source)
			tList.add(t);	
		resetModel();
	}
	
	
	public void setComparator(Comparator<T> comparator)
	{
		this.comparator = comparator;
		resetModel();
	}
	
	
	
	private void resetModel()
	{
		if(comparator != null)
			tList.sort(comparator);
		setModel(tableModel(tList));
	}
		
	
	protected abstract Comparator<T> comparator(int columnIndex);
	protected abstract TableModel tableModel(List<T> tList);
	protected abstract void doPopup(MouseEvent e);
	protected abstract int columnWidth(int columnIndex);
	
	
	/*
	 * Mouse Listener Methods
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if( e.getComponent() == getTableHeader() )
		{
			int columnIndex = columnAtPoint(e.getPoint());
			comparator = comparator(columnIndex);
			resetModel();
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		if(e.isPopupTrigger())
			doPopup(e);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		if(e.isPopupTrigger())
			doPopup(e);
	}
	
}
