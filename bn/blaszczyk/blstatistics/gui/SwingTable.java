package bn.blaszczyk.blstatistics.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public abstract class SwingTable<T> extends JTable implements MouseListener
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

	private int selectedRow = -1;
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
	
	private void setCellRenderer()
	{
		for(int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++)
			getColumnModel().getColumn(columnIndex).setCellRenderer( new DefaultTableCellRenderer(){
				@Override
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
						boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					
					if( row == selectedRow )
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
		
	
	protected abstract Comparator<T> comparator(int columnIndex);
	protected abstract TableModel createTableModel(List<T> tList);
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
	
}
