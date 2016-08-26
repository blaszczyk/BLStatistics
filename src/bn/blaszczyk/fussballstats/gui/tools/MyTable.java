package bn.blaszczyk.fussballstats.gui.tools;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

@SuppressWarnings("serial")
public abstract class MyTable<T> extends JTable implements KeyListener
{
	/*
	 * Constants
	 */
	private static final DateFormat  DATE_FORMAT = new SimpleDateFormat("dd.MM.YY");
	private static final NumberFormat INT_FORMAT = NumberFormat.getIntegerInstance();
	private static final NumberFormat DOUBLE_FORMAT = new DecimalFormat("0.000",DecimalFormatSymbols.getInstance(Locale.GERMAN));
	
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
	private static final Color HEADER_COLOR = new Color(238,238,255);
	
	/*
	 * Custom Cell Renderer
	 */
	private final TableCellRenderer cellRenderer = new TableCellRenderer(){
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
				boolean hasFocus, int row, int column) {
			String text = "";
			if(value instanceof Date)
				text = DATE_FORMAT.format(value);
			else if(value instanceof Double)
				text = DOUBLE_FORMAT.format(value);
			else if(value instanceof Integer)
				text = INT_FORMAT.format(value);
			else if(value instanceof String)
				text = (String) value;
			
			JLabel c = new JLabel( text , columnAlignment(column) );
			c.setOpaque(true);
			if(row < 0 )
			{
				c.setText(" " + c.getText() + " ");
				c.setFont(HEADER_FONT);
				c.setBackground(HEADER_COLOR);
				c.setBorder(BorderFactory.createEtchedBorder());
			}
			else
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
	
	/*
	 * Variables
	 */
	private List<T> tList;
	protected final TableRowSorter<TableModel> sorter = new TableRowSorter<>();

	/*
	 * Constructor
	 */
	public MyTable()
	{
		setShowGrid(false);
		setIntercellSpacing(new Dimension(0, 0));
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTableHeader().setFont(HEADER_FONT);
		setRowSorter(sorter);
		
		setRowHeight(ODD_FONT.getSize() + 10);
		addKeyListener(this);
	}

	/*
	 * set Source for Table
	 */
	public void setSource(Iterable<T> source)
	{
		tList = new ArrayList<>();
		for(T t : source)
			tList.add(t);	
		setModel();
	}

	/*
	 * Abstract Methods
	 */
	protected abstract boolean isThisRowSelected(int rowIndex);
	protected abstract TableModel createTableModel(List<T> tList);
	protected abstract int columnWidth(int columnIndex);
	protected abstract int columnAlignment(int columnIndex);
	
	/*
	 * Internal Setters
	 */
	private void setModel()
	{
		TableModel model = createTableModel(tList);
		List<? extends SortKey> sortKeys = sorter.getSortKeys();
		if(sortKeys.isEmpty())
		{
			List<SortKey> newSortKeys = new ArrayList<SortKey>();
			newSortKeys.add(new SortKey(0, SortOrder.ASCENDING));
			sortKeys = newSortKeys;
		}
		setModel(model);
		sorter.setModel(model);
		sorter.setSortKeys(sortKeys);
		sorter.sort();
		setCellRenderer();
		setWidths();
		repaint();
	}
	
	private void setCellRenderer()
	{
		getTableHeader().setPreferredSize(new Dimension(getWidth(), 30));
		getTableHeader().setDefaultRenderer(cellRenderer);
		for(int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++)
			getColumnModel().getColumn(columnIndex).setCellRenderer( cellRenderer );
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
	
	/*
	 * Key Listener Methods
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
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
	}
	
}
