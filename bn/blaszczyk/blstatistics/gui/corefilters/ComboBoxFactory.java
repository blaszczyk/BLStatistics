package bn.blaszczyk.blstatistics.gui.corefilters;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;
import javax.swing.UIManager;

import bn.blaszczyk.blstatistics.core.League;

public class ComboBoxFactory implements MouseWheelListener, KeyListener
{
	public static final int TEAM = 0;
	public static final int LEAGUE = 1;

	private int charCounter = 0;
	private char selectChar = '1';
	private Iterable<? extends Object> allObjects;
	private int mode;

	public ComboBoxFactory(Iterable<? extends Object> allObjects, int mode)
	{
		this.allObjects = allObjects;
		this.mode = mode;
	}
	


	public JComboBox<String> createTeamBox()
	{
		if(mode != TEAM)
			throw new UnsupportedOperationException("This Factory cannot create TeamBox.");
		JComboBox<String> teamBox = new JComboBox<>();
		for(Object team : allObjects)
		{
			teamBox.addItem((String) team);
		}
		teamBox.setMaximumSize(new Dimension(250,30));
		teamBox.setMinimumSize(new Dimension(250,30));
		teamBox.addMouseWheelListener(this);
		teamBox.addKeyListener(this);
		teamBox.setInheritsPopupMenu(true);
		teamBox.setFont( UIManager.getFont("ComboBox.font").deriveFont(Font.PLAIN) );
		return teamBox;
	}


	public JComboBox<League> createLeagueBox()
	{
		if(mode != LEAGUE)
			throw new UnsupportedOperationException("This Factory cannot create LeagueBox.");
		JComboBox<League> leagueBox = new JComboBox<>();
		for(Object team : allObjects)
		{
			leagueBox.addItem((League) team);
		}
		leagueBox.setMaximumSize(new Dimension(250,30));
		leagueBox.setMinimumSize(new Dimension(250,30));
		leagueBox.addMouseWheelListener(this);
		leagueBox.addKeyListener(this);
		leagueBox.setInheritsPopupMenu(true);
		leagueBox.setFont( UIManager.getFont("ComboBox.font").deriveFont(Font.PLAIN) );
		return leagueBox;
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
