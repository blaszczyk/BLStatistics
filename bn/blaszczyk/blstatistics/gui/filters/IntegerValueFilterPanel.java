package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class IntegerValueFilterPanel<T> extends AbstractFilterPanel<T> implements MouseWheelListener, KeyListener
{
	protected static final String EQ = "=";
	protected static final String NEQ = "!=";
	protected static final String GEQ = ">=";
	protected static final String LEQ = "<=";
	protected static final String GG = ">";
	protected static final String LL = "<";
	
	private JLabel label;
	private JComboBox<String> operatorBox;
	private JTextField valueField;
	private int defaultValue;

	private IntegerValueFilterPanel(String labelText, int defaultValue)
	{
		this.defaultValue = defaultValue;
		
		label = new JLabel(labelText);
		
		String[] operators = {EQ,NEQ,GG,GEQ,LL,LEQ};
		operatorBox = new JComboBox<>(operators);
		operatorBox.setMaximumSize(new Dimension(50,30));
		operatorBox.setInheritsPopupMenu(true);
		operatorBox.addActionListener(e -> setOperator());
		
		valueField = new JTextField(Integer.toString(defaultValue));
		valueField.setMaximumSize(new Dimension(70,30));
		valueField.setInheritsPopupMenu(true);
		valueField.addKeyListener(this);
		valueField.addMouseWheelListener(this);

		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setOperator();
	}
	
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
	
	
	public String getSelectedOperator()
	{
		return operatorBox.getSelectedItem().toString();
	}
	
	public int getReferenceInt()
	{
		int result = defaultValue;
		try
		{
			if(valueField.getText() == null)
				valueField.setText("0");
			result = Integer.parseInt(valueField.getText());
		}
		catch(NumberFormatException e)
		{
			valueField.setText(Integer.toString(defaultValue));
			JOptionPane.showMessageDialog(valueField, "Falschens Zahlenformat", "Error", JOptionPane.ERROR_MESSAGE);
		}	
		return result;
	}
	
	public String getLabel()
	{
		return label.getText();
	}
	
	protected abstract void setOperator();	

	@Override
	public String toString()
	{
		return getLabel() + " " + getSelectedOperator() + " " + getReferenceInt();
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
			setOperator();
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
			setOperator();
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
