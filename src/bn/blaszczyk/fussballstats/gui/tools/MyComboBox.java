package bn.blaszczyk.fussballstats.gui.tools;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.UIManager;



@SuppressWarnings("serial")
public class MyComboBox<T> extends JComboBox<T> implements MouseWheelListener, KeyListener
{

	public static interface CycleListener
	{
		public void cycle(CycleEvent e);
	}
	
	public static class CycleEvent
	{
		private final int direction;
		private CycleEvent(int direction)
		{
			this.direction = direction;
		}
		public int getDirection()
		{
			return direction;
		}
	}
	
	private int charCounter = 0;
	private char selectChar = '.';
	private boolean editable;
	private CycleListener listener = null;
	private T[] tArray;
	
	@SuppressWarnings("unchecked")
	public MyComboBox(List<T> tList, int boxWidth, boolean editable)
	{
		this( (T[]) new Object[0], boxWidth,  editable);
		for(T t : tList)
			addItem(t);
		this.tArray = toArray(tList);
		if(editable)
			getEditor().getEditorComponent().addKeyListener(this);
	}
	
	public MyComboBox(T[] tArray, int boxWidth, boolean editable)
	{
		super(tArray);
		this.editable = editable;
		this.tArray = tArray;
		setMaximumSize(new Dimension(boxWidth,30));
		setMinimumSize(new Dimension(boxWidth,30));
		addMouseWheelListener(this);
		addKeyListener(this);
		setInheritsPopupMenu(true);
		setEditable(editable);
		
		setFont( UIManager.getFont("ComboBox.font").deriveFont(Font.BOLD) );
	}
	
	public void setCycleListener(CycleListener listener)
	{
		this.listener = listener;
	}
	
	private void selectByChar(char c)
	{
		boolean hasChar = false;
		if( c == selectChar )
			charCounter++;
		else
		{
			charCounter = 0;
			selectChar = c;
		}
		int charCounterTmp = charCounter;
		for(int i = 0; i < getItemCount(); i++)
		{
			Object o = getItemAt(i);
			String name = o.toString();
			if(name.toLowerCase().startsWith( "" + Character.toLowerCase(c) ))
			{
				hasChar = true;
				if(charCounterTmp > 0)
				{
					charCounterTmp--;
					continue;
				}
				setSelectedIndex(i);
				return;
			}
		}
		charCounter = charCounterTmp - 1;
		if(hasChar)
			selectByChar(c);
	}

	public void moveSelection(int steps)
	{
		int newIndex = getSelectedIndex() + steps;
		if( newIndex < 0 )
		{
			if(listener != null)
				listener.cycle(new CycleEvent(1));
			newIndex = getItemCount()-1;
		}
		else if( newIndex >= getItemCount())
		{
			if(listener != null)
				listener.cycle(new CycleEvent(-1));
			newIndex = 0;
		}
		setSelectedIndex( newIndex );
	}
	
	@SuppressWarnings("unchecked")
	private T[] toArray(List<T> tList)
	{
		T[] tArray = (T[]) new Object[tList.size()];
		tList.toArray(tArray);
		return tArray;
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int steps = (int) (4 * e.getPreciseWheelRotation());
		moveSelection(steps);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_DOWN:
			moveSelection(1);
			e.consume();
			break;
		case KeyEvent.VK_UP:
			moveSelection(-1);
			e.consume();
			break;
		case KeyEvent.VK_RIGHT:
			setPopupVisible(true);
			break;
		case KeyEvent.VK_LEFT:
			setPopupVisible(false);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if(!editable)
			return;
		char c = e.getKeyChar();
		if( !Character.isISOControl(c) && !Character.isDigit(c) && !Character.isAlphabetic(c) )
			return;
		/*
		 * Save Status of ComboBox
		 */
		JTextField inputField = (JTextField)getEditor().getEditorComponent();
		String input = inputField.getText();
		int caret = inputField.getCaretPosition();
		
			ActionListener[] listeners = getActionListeners();
		for(ActionListener listener : listeners)
			removeActionListener(listener);
		
		/*
		 * Reset List
		 */
		removeAllItems();
		for(T t : tArray)
				if(t.toString().toLowerCase().contains(input.toLowerCase()))
				addItem(t);
		/*
		 * Reset Status
		 */
		inputField.setText(input);
		inputField.setCaretPosition(caret);
		for(ActionListener listener : listeners)
			addActionListener(listener);
		
		setPopupVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if(editable)
			return;
		char keyChar = e.getKeyChar();
		if(e.getModifiers() == InputEvent.ALT_DOWN_MASK)
			return;
		if(Character.isAlphabetic(keyChar) || Character.isDigit(keyChar))
			selectByChar(Character.toLowerCase(keyChar));
		requestFocusInWindow();
	}
}
