package bn.blaszczyk.blstatistics.gui.tools;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.JComboBox;
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
	private CycleListener listener = null;
	
	@SuppressWarnings("unchecked")
	public MyComboBox(List<T> allObjects, int boxWidth, boolean editable)
	{
		this((T[]) new Object[0], boxWidth,  editable);
		for(T t : allObjects)
			addItem(t);
	}
	
	public MyComboBox(T[] allObjects, int boxWidth, boolean editable)
	{
		super(allObjects);
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
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int steps = (int) (4 * e.getPreciseWheelRotation());
		moveSelection(steps);
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		e.consume();
		switch(e.getKeyCode())
		{
		case KeyEvent.VK_DOWN:
			moveSelection(1);
			break;
		case KeyEvent.VK_UP:
			moveSelection(-1);
			break;
		}
		requestFocusInWindow();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		e.consume();
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		e.consume();
		char keyChar = e.getKeyChar();
		if(Character.isAlphabetic(keyChar) || Character.isDigit(keyChar))
			selectByChar(Character.toLowerCase(keyChar));
		requestFocusInWindow();
	}
}
