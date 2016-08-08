package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.UIManager;


public class ComboBoxFactory<T> implements MouseWheelListener, KeyListener
{

	private int charCounter = 0;
	private char selectChar = '1';
	private int boxWidth = 250;
	private T[] allObjects;

	@SuppressWarnings("unchecked")
	public ComboBoxFactory(List<T> allObjects)
	{
		this.allObjects = (T[]) new Object[allObjects.size()];
		allObjects.toArray(this.allObjects);
	}
	
	public ComboBoxFactory(T[] allObjects)
	{
		setArray(allObjects);
	}
	
	public void setArray(T[] allObjects)
	{
		this.allObjects = allObjects;
	}
	
	public void setBoxWidth(int width)
	{
		if( width > 0)
			boxWidth = width;
	}

	
	public JComboBox<T> createComboBox()
	{
		JComboBox<T> box = new JComboBox<>(allObjects);
		box.setMaximumSize(new Dimension(boxWidth,30));
		box.setMinimumSize(new Dimension(boxWidth,30));
		box.addMouseWheelListener(this);
		box.addKeyListener(this);
		box.setInheritsPopupMenu(true);
		box.setFont( UIManager.getFont("ComboBox.font").deriveFont(Font.PLAIN) );
		return box;
	}
	
	private void selectByChar(char c, JComboBox<?> box)
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
		for(int i = 0; i < box.getItemCount(); i++)
		{
			Object o = box.getItemAt(i);
			String name = o.toString();
			if(name.toLowerCase().startsWith( "" + Character.toLowerCase(c) ))
			{
				hasChar = true;
				if(charCounterTmp > 0)
				{
					charCounterTmp--;
					continue;
				}
				box.setSelectedIndex(i);
				return;
			}
		}
		charCounter = charCounterTmp - 1;
		if(hasChar)
			selectByChar(c, box);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int diff = (int) (4 * e.getPreciseWheelRotation());
		if( e.getSource() instanceof JComboBox)
		{
			JComboBox<?> box = (JComboBox<?>) e.getSource();
			int newIndex = box.getSelectedIndex() + diff;
			if( newIndex >= 0 && newIndex < box.getItemCount())
				box.setSelectedIndex( newIndex );
		}
	}



	@Override
	public void keyPressed(KeyEvent e)
	{
		e.consume();
		if(e.getSource() instanceof JComboBox)
		{
			JComboBox<?> box = (JComboBox<?>) e.getSource();
			int selectedIndex = box.getSelectedIndex();
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_DOWN:
				if(selectedIndex < box.getItemCount() - 1)
					box.setSelectedIndex(selectedIndex + 1);
				else
					box.setSelectedIndex(0);
				break;
			case KeyEvent.VK_UP:
				if(selectedIndex > 0)
					box.setSelectedIndex(selectedIndex  - 1); 
				else
					box.setSelectedIndex( box.getItemCount() - 1);
				break;
			}
			box.requestFocusInWindow();
		}			
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
		if(e.getSource() instanceof JComboBox)
		{
			JComboBox<?> box = (JComboBox<?>) e.getSource();
			char keyChar = e.getKeyChar();
			if(Character.isAlphabetic(keyChar) || Character.isDigit(keyChar))
				selectByChar(Character.toLowerCase(keyChar), box);
			box.requestFocusInWindow();
		}
	}

}
