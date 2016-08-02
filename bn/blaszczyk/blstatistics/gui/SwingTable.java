package bn.blaszczyk.blstatistics.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public abstract class SwingTable<T> extends JTable implements MouseListener, KeyListener
{
	private static final Color ODD_COLOR = new Color(247,247,247);
	private static final Color EVEN_COLOR = Color.WHITE;
	private static final Color SELECTED_COLOR = new Color(255,192,192);

	private static final Font ODD_FONT = new Font("Arial",Font.PLAIN,16);
	private static final Font EVEN_FONT = new Font("Arial",Font.PLAIN,16);
	private static final Font SELECTED_FONT = new Font("Arial",Font.BOLD,16);
	
	private static final Color ODD_FONT_COLOR = Color.BLACK;
	private static final Color EVEN_FONT_COLOR = Color.BLACK;
	private static final Color SELECTED_FONT_COLOR = Color.BLACK;

	private static final Font HEADER_FONT = new Font("Arial",Font.BOLD,16);
	
	private List<T> tList;
	private Comparator<T> comparator;
	private boolean isCompareBackwards = true;
	private int sortingColumn = -1;

	private int selectedRow = -1;

	
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
		addKeyListener(this);
		getTableHeader().setFont(HEADER_FONT);
		setRowHeight(ODD_FONT.getSize() + 10 );
		getSelectionModel().addListSelectionListener( e -> 	{
			selectedRow = e.getLastIndex();
			setModel();
		});
	}

	public void setSource(Iterable<T> source)
	{
		tList = new ArrayList<>();
		for(T t : source)
			tList.add(t);	
		selectedRow = -1;
		setModel();
	}
	
	
	public void setComparator(Comparator<T> comparator)
	{
		this.comparator = comparator;
		setModel();
	}
	
	protected void setCellRenderer()
	{
		for(int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++)
			getColumnModel().getColumn(columnIndex).setCellRenderer( new DefaultTableCellRenderer(){
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					
					if( isThisRowSelected(row) )
					{
						c.setBackground(SELECTED_COLOR);
						c.setFont( SELECTED_FONT );
						c.setForeground(SELECTED_FONT_COLOR);
					}
					else if( (row % 2) == 1)
					{
						c.setBackground(ODD_COLOR);
						c.setFont( ODD_FONT );
						c.setForeground(ODD_FONT_COLOR);
					}
					else
					{
						c.setBackground(EVEN_COLOR);
						c.setFont( EVEN_FONT );
						c.setForeground(EVEN_FONT_COLOR);
					}
					return c;
				}
				@Override
				public void setHorizontalAlignment( int alignment )
				{
					super.setHorizontalAlignment( SwingConstants.CENTER );
				}
			});
	}
	
	
	
	public void setModel()
	{
		if(comparator != null)
			tList.sort(comparator);
		setModel(createTableModel(tList));
		setCellRenderer();
		repaint();
	}

	public void addListSelectionListener(ListSelectionListener l)
	{
		getSelectionModel().addListSelectionListener(l);
	}
	
	public void removeListSelectionListener(ListSelectionListener l)
	{
		getSelectionModel().removeListSelectionListener(l);
	}

	
	protected Comparator<T> reverseComparator(Comparator<T> comparator)
	{
		return (t1,t2) -> comparator.compare(t2, t1);
	}	
	
	
	protected boolean isThisRowSelected(int rowIndex)
	{
		return rowIndex == selectedRow;
	}
	
	// TODO: Use this in GameTableModel and ResultTableModelS
	@SuppressWarnings("unchecked")
	protected Comparator<T> createDefaultComparator(int columnIndex)
	{
		if(Comparable.class.isAssignableFrom( getModel().getColumnClass(columnIndex) ) )
		{
			MyTableModel<T> tableModel = ((MyTableModel<T>) getModel());
			// TODO: Type argument T is wrong here. Fix before continuing
			return (t1,t2) -> ((Comparable<T>)tableModel.getColumnValue(t1, columnIndex)).compareTo((T) tableModel.getColumnValue(t2, columnIndex));
		}
		return null;
	}
	
	//TODO: To replace this:
	protected abstract Comparator<T> comparator(int columnIndex);
	
	protected abstract MyTableModel<T> createTableModel(List<T> tList);
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
			if(columnIndex == sortingColumn)
				isCompareBackwards = !isCompareBackwards;
			else
			{
				sortingColumn = columnIndex;
				isCompareBackwards = false;
			}
			comparator = comparator(columnIndex);
			if(isCompareBackwards)
				comparator = reverseComparator(comparator);
			setModel();
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

	/*
	 * Key Listener Methods
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
		e.consume();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		e.consume();
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			selectedRow-=2; 	//Fallthrough
		case KeyEvent.VK_DOWN:
			selectedRow++;
			getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
			setCellRenderer();
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		e.consume();
	}
	
}
