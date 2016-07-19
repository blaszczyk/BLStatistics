package bn.blaszczyk.blstatistics.gui.filters;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public abstract class IntegerValueFilterPanel<T> extends AbstractFilterPanel<T>
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

	protected IntegerValueFilterPanel(String labelText, int defaultValue)
	{
		this.defaultValue = defaultValue;
		
		label = new JLabel(labelText);
		
		String[] operators = {EQ,NEQ,GG,GEQ,LL,LEQ};
		operatorBox = new JComboBox<>(operators);
		operatorBox.setMaximumSize(new Dimension(50,30));
		operatorBox.addActionListener(e -> setOperator());
		
		valueField = new JTextField(Integer.toString(defaultValue));
		valueField.setMaximumSize(new Dimension(70,30));
		valueField.addActionListener(e -> setOperator());

		setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
		setOperator();
	}
	
	@Override
	protected void addComponents()
	{
		add(label);
		add(operatorBox);
		add(valueField);
	}
	
	protected String getSelectedOperator()
	{
		return operatorBox.getSelectedItem().toString();
	}
	
	protected int getReferenceInt()
	{
		int result = defaultValue;
		try
		{
			result = Integer.parseInt(valueField.getText());
		}
		catch(NumberFormatException e)
		{
			valueField.setText(Integer.toString(defaultValue));
			JOptionPane.showMessageDialog(valueField, "Falschens Zahlenformat", "Error", JOptionPane.ERROR_MESSAGE);
		}	
		return result;
	}
	
	protected abstract void setOperator();	

	@Override
	public String toString()
	{
		return label.getText() + " " + getSelectedOperator() + " " + getReferenceInt();
	}
	
}
