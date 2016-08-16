package bn.blaszczyk.fussballstats.gui.filters;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import bn.blaszczyk.fussballstats.filters.Filter;
import bn.blaszczyk.fussballstats.gui.tools.MyComboBox;

@SuppressWarnings("serial")
public abstract class IntegerValueFilterPanel<T> extends AbstractFilterPanel<T> implements CompareToFilterPanel<T>, MouseWheelListener, KeyListener
{
	private JLabel label;
	private JComboBox<String> operatorBox;
	private JTextField valueField;
	private int defaultValue;

	private IntegerValueFilterPanel(String labelText, int defaultValue)
	{
		super(false);
		this.defaultValue = defaultValue;
		
		label = new JLabel(labelText);
		label.setDisplayedMnemonic('f');
		
		operatorBox = new MyComboBox<>(OPERATORS,50,false);
		operatorBox.addActionListener(setFilterListener);
		
		valueField = new JTextField(Integer.toString(defaultValue));
		valueField.setMaximumSize(new Dimension(70,30));
		valueField.setInheritsPopupMenu(true);
		valueField.addKeyListener(this);
		valueField.addMouseWheelListener(this);

		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setFilter();
	}

	protected abstract Filter<T> getFilter();
	
	protected IntegerValueFilterPanel(String labelText, String operator, int defaultValue)
	{
		this(labelText,defaultValue);
		operatorBox.setSelectedItem(operator);
	}

	@Override
	protected void addComponents()
	{
		add(label);
		add(operatorBox);
		add(valueField);
	}
	
	@Override
	public String getOperator()
	{
		return operatorBox.getSelectedItem().toString();
	}
	
	public int getReferenceInt()
	{
		int result = defaultValue;
		try
		{
			result = Integer.parseInt(valueField.getText());
		}
		catch(NumberFormatException e)
		{
//			JOptionPane.showMessageDialog(valueField, "Falschens Zahlenformat", "Error", JOptionPane.ERROR_MESSAGE);
		}	
		return result;
	}
	
	@Override
	public String getReferenceValString()
	{
		return String.valueOf(getReferenceInt());
	}

	@Override
	public void invertOperator()
	{
		switch(getOperator())
		{
		case EQ:
			operatorBox.setSelectedItem(NEQ);
			break;
		case NEQ:
			operatorBox.setSelectedItem(EQ);
			break;
		case LEQ:
			operatorBox.setSelectedItem(GG);
			break;
		case GG:
			operatorBox.setSelectedItem(LEQ);
			break;
		case GEQ:
			operatorBox.setSelectedItem(LL);
			break;
		case LL:
			operatorBox.setSelectedItem(GEQ);
			break;
		}
	}

	@Override
	public String getLabel()
	{
		return label.getText();
	}
	
	
	protected void setFilter()
	{
		setFilter(getFilter());
	}

	@Override
	public String toString()
	{
		return getLabel() + " " + getOperator() + " " + getReferenceInt();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int diff = (int) (4 * e.getPreciseWheelRotation());
		if (e.getSource() instanceof JTextField)
		{
			JTextField tf = (JTextField) e.getSource();
			int newVal = diff + Integer.parseInt(tf.getText());
			tf.setText("" + newVal);
			setFilter();
		}
		
	}
	

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(e.getSource() == valueField)
		{
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_UP:
				valueField.setText( Integer.parseInt(valueField.getText()) + 1 + ""  );
				break;
			case KeyEvent.VK_DOWN:
				valueField.setText( Integer.parseInt(valueField.getText()) - 1 + ""  );
				break;
			}
		}			
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getSource() instanceof JTextField)
		{
			setFilter();
			((JTextField)e.getSource()).requestFocusInWindow();
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if (e.getSource() instanceof JTextField)
		{
			JTextField tf = (JTextField) e.getSource();
			tf.replaceSelection(null);
			char c = e.getKeyChar();
			if (!Character.isISOControl(c) && !Character.isDigit(c) )
			{
				Toolkit.getDefaultToolkit().beep();
				e.consume();
			}
		}
	}

}
