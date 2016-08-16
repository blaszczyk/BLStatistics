package bn.blaszczyk.fussballstats.gui.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
public abstract class MyTable<T> extends JTable implements KeyListener
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
	private static final Color HEADER_COLOR = new Color(238,238,238);
	
	private final TableCellRenderer cellRenderer = new TableCellRenderer(){
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {

			if(row < 0)
			{
				String headerText = "";
				if(value != null)
					headerText= value.toString();
				JLabel c = new JLabel(" " + headerText + " ", columnAlignment(column));
				c.setOpaque(true);
				c.setFont(HEADER_FONT);
				c.setBackground(HEADER_COLOR);
				c.setBorder(BorderFactory.createEtchedBorder());
				return c;
			}
			JLabel c = new JLabel(value.toString(), columnAlignment(column) );
			c.setOpaque(true);
			c.setMaximumSize(new Dimension(columnWidth(column),ODD_FONT.getSize() + 10 ));
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
	};
	
	private List<T> tList;
	private Comparator<T> comparator = comparator(0);
	private boolean isCompareBackwards = true;
	private int sortingColumn = -1;
	
	public MyTable()
	{
		setShowGrid(false);
		setIntercellSpacing(new Dimension(0, 0));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setFont(HEADER_FONT);
		setRowHeight(ODD_FONT.getSize() + 10);
		addKeyListener(this);
		getTableHeader().addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				sortByColumn( columnAtPoint(e.getPoint()));
			}
		});
	}

	public void setSource(Iterable<T> source)
	{
		tList = new ArrayList<>();
		for(T t : source)
			tList.add(t);	
		setModel();
	}
	
	
	public void setComparator(Comparator<T> comparator)
	{
		this.comparator = comparator;
		setModel();
	}
	
	protected void setCellRenderer()
	{
		getTableHeader().setPreferredSize(new Dimension(getWidth(), 30));
		getTableHeader().setDefaultRenderer(cellRenderer);
		for(int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++)
			getColumnModel().getColumn(columnIndex).setCellRenderer( cellRenderer );
	}
	
	
	
	public void setModel()
	{
		if(comparator != null)
			tList.sort(comparator);
		setModel(createTableModel(tList));
		setCellRenderer();
		setWidths();
		repaint();
	}
	
	private void sortByColumn(int columnIndex)
	{
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
	
	private void setWidths()
	{
		for(int i = 0 ; i < this.getColumnCount(); i++)
		{
			int width = columnWidth(i);
			if( width >= 0 )
			{
				getColumnModel().getColumn(i).setPreferredWidth(width);
				getColumnModel().getColumn(i).setMinWidth(width);
				getColumnModel().getColumn(i).setMaxWidth(width);
			}
		}
	}
	
	private void moveSelection(int nrRows)
	{
		int selectedRow = getSelectionModel().getAnchorSelectionIndex();
		selectedRow += nrRows;
		if(selectedRow < 0)
			selectedRow = 0;
		else if(selectedRow >= getRowCount())
			selectedRow = getRowCount() - 1;
		getSelectionModel().setAnchorSelectionIndex(selectedRow);
		scrollRectToVisible( getCellRect(selectedRow, 0, false));
	}
	
	protected Comparator<T> reverseComparator(Comparator<T> comparator)
	{
		return (t1,t2) -> comparator.compare(t2, t1);
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

	
	
	protected abstract boolean isThisRowSelected(int rowIndex);
	protected abstract MyTableModel<T> createTableModel(List<T> tList);
	protected abstract int columnWidth(int columnIndex);
	protected abstract int columnAlignment(int columnIndex);
	

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
			moveSelection(-1);
			break;
		case KeyEvent.VK_DOWN:
			moveSelection(1);
			break;
		case KeyEvent.VK_PAGE_UP:
			moveSelection(-5);
			break;
		case KeyEvent.VK_PAGE_DOWN:
			moveSelection(5);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		e.consume();
	}
	
}
