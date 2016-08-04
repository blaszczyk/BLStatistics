package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;
import javax.swing.UIManager;

import bn.blaszczyk.blstatistics.core.Game;
import bn.blaszczyk.blstatistics.gui.filters.AbstractFilterPanel;

@SuppressWarnings("serial")
public abstract class AbstractTeamFilterPanel extends AbstractFilterPanel<Game> implements MouseWheelListener, KeyListener
{

	private int charCounter = 0;
	private char selectChar = '1';
	protected Iterable<String> allTeams;
	
	protected AbstractTeamFilterPanel(Iterable<String> allTeams)
	{
		this.allTeams = allTeams;
	}
	

	protected JComboBox<String> createTeamBox(Iterable<String> allTeams)
	{
		JComboBox<String> teamBox = new JComboBox<String>();
		for(String team : allTeams)
		{
			teamBox.addItem(team);
		}
		teamBox.setMaximumSize(new Dimension(250,30));
		teamBox.setMinimumSize(new Dimension(250,30));
		teamBox.addMouseWheelListener(this);
		teamBox.addKeyListener(this);
		teamBox.setInheritsPopupMenu(true);
		teamBox.setFont( UIManager.getFont("ComboBox.font").deriveFont(Font.PLAIN) );
		return teamBox;
	}

	private void selectByChar(char c, JComboBox<String> box)
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
			if(box.getItemAt(i).toLowerCase().startsWith( "" + Character.toLowerCase(c) ))
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
		charCounter = charCounterTmp - 1;
		if(hasChar)
			selectByChar(c, box);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int diff = (int) (4 * e.getPreciseWheelRotation());
		if( e.getSource() instanceof JComboBox)
		{
			JComboBox<String> box = (JComboBox<String>) e.getSource();
			int newIndex = box.getSelectedIndex() + diff;
			if( newIndex >= 0 && newIndex < box.getItemCount())
				box.setSelectedIndex( newIndex );
		}
	}



	@SuppressWarnings("unchecked")
	@Override
	public void keyPressed(KeyEvent e)
	{
		e.consume();
		if(e.getSource() instanceof JComboBox)
		{
			JComboBox<String> box = (JComboBox<String>) e.getSource();
			int selectedIndex = box.getSelectedIndex();
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_DOWN:
				if(selectedIndex < box.getItemCount() - 1)
					box.setSelectedIndex(selectedIndex + 1); 
				break;
			case KeyEvent.VK_UP:
				if(selectedIndex > 0)
					box.setSelectedIndex(selectedIndex  - 1); 
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

	@SuppressWarnings("unchecked")
	@Override
	public void keyTyped(KeyEvent e)
	{
		e.consume();
		if(e.getSource() instanceof JComboBox)
		{
			JComboBox<String> box = (JComboBox<String>) e.getSource();
			char keyChar = e.getKeyChar();
			if(Character.isAlphabetic(keyChar) || Character.isDigit(keyChar))
				selectByChar(Character.toLowerCase(keyChar), box);
			box.requestFocusInWindow();
		}
		
	}
	
	

}
