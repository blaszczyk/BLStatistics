package bn.blaszczyk.fussballstats.gui;

import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bn.blaszczyk.fussballstats.FussballStats;
import bn.blaszczyk.rosecommon.tools.CommonPreference;
import bn.blaszczyk.rosecommon.tools.Preference;
import bn.blaszczyk.rosecommon.tools.Preferences;

@SuppressWarnings("serial")
public class PrefsDialog extends JDialog
{
	/*
	 * Constants
	 */
	private static final String ICON_FILE = "data/settings.png";
	
	/*
	 * Components
	 */
	private JTextField tfServer, tfPort, tfDbName, tfUser, tfPassword;
	
	private final JRadioButton rbHardDrive = new JRadioButton();
	private final JRadioButton rbDataBase = new JRadioButton();
	
	private final JCheckBox chbLoadLastFilter = new JCheckBox();

	private final JButton btnSave = new JButton("Speichern");
	private final JButton btnDismiss = new JButton("Verwerfen");
	
	/*
	 * Variables
	 */
	private final Window owner;
	
	/*
	 * Constructor
	 */
	public PrefsDialog(Window owner)
	{
		super(owner, ModalityType.APPLICATION_MODAL);
		this.owner = owner;

		setTitle("Einstellungen");
		setLayout(null);
		setSize(345, 433);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE)));

		JLabel label = new JLabel("Spiele laden von");
		label.setBounds(10, 10, 170,30);
		add(label);
		
		final boolean dbMode = true;
		
		addButtonRow(rbHardDrive,"Festplatte", !dbMode,'F', 1, 150);
		addButtonRow(rbDataBase,"SQL Datenbank", dbMode,'D', 2, 150);
		
		ButtonGroup bgDbMode = new ButtonGroup();
		bgDbMode.add(rbHardDrive);
		bgDbMode.add(rbDataBase);

		rbHardDrive.addItemListener( e -> setDbAccessDataEnabled(false));
		rbDataBase.addItemListener( e -> setDbAccessDataEnabled(true));

		tfServer = createTextFieldRow("Server", CommonPreference.DB_HOST,'R',  3, false);
		tfPort = createTextFieldRow("Port", CommonPreference.DB_PORT,'P',  4, false);
		tfDbName = createTextFieldRow("Datenbank", CommonPreference.DB_NAME,'T', 5, false);
		tfUser = createTextFieldRow("Benutzer", CommonPreference.DB_USER,'B', 6, false);
		tfPassword = createTextFieldRow("Passwort", CommonPreference.DB_PASSWORD,'W', 7, true);
		setDbAccessDataEnabled(dbMode);
		
		addButtonRow(chbLoadLastFilter,"Letzten Filter beim Start laden", FunctionalFilterPanel.isLoadLastFilter(),'L', 8, 300);
		
		btnSave.addActionListener(e -> save());
		btnSave.setMnemonic('S');
		btnSave.setBounds(10, 365, 150, 30);
		
		btnDismiss.addActionListener(e -> dispose());
		btnDismiss.setMnemonic('V');
		btnDismiss.setBounds(180, 365, 150, 30);
		
		add(btnSave);
		add(btnDismiss);
	}

	/*
	 * Show on Screen
	 */
	public void showDialog()
	{
		setLocationRelativeTo(owner);
		setVisible(true);	
	}

	/*
	 * Internal Methods
	 */
	private JTextField createTextFieldRow(String labelText, Preference preference, char mnemonic, int column, boolean isPassword)
	{
		final String defText = preference.getDefaultValue().toString();
		JLabel label = new JLabel(labelText,SwingConstants.RIGHT);
		JTextField textField;
		if(isPassword)
			textField = new JPasswordField(defText);
		else
			textField = new JTextField(defText);
		label.setBounds(10, 10 + column * 35 , 100, 25);
		label.setLabelFor(textField);
		label.setDisplayedMnemonic(mnemonic);
		textField.setBounds(120, 10 + column * 35 , 200, 25);
		
		add(label);
		add(textField);
		return textField;
	}

	private void addButtonRow(AbstractButton button, String label, boolean selected, char mnemonic, int column, int width)
	{
		button.setBounds(10, 10 + column * 35 , 350, 25);
		button.setMnemonic(mnemonic);
		button.setText(label);
		button.setSelected(selected);
		add(button);
	}
	
	private void setDbAccessDataEnabled(boolean enabled)
	{
		tfServer.setEnabled(enabled);
		tfPort.setEnabled(enabled);
		tfDbName.setEnabled(enabled);
		tfUser.setEnabled(enabled);
		tfPassword.setEnabled(enabled);
	}
	
	private void save()
	{
		String server = tfServer.getText();
		String port = tfPort.getText();
		String dbName = tfDbName.getText();
		String user = tfUser.getText();
		String password = tfPassword.getText();
		
		boolean saveLastFilter = chbLoadLastFilter.isSelected();
		FunctionalFilterPanel.setLoadLastFilter(saveLastFilter);

		Preferences.putStringValue(CommonPreference.DB_HOST, server);
		Preferences.putStringValue(CommonPreference.DB_PORT, port);
		Preferences.putStringValue(CommonPreference.DB_NAME, dbName);
		Preferences.putStringValue(CommonPreference.DB_USER, user);
		Preferences.putStringValue(CommonPreference.DB_PASSWORD, password);

		dispose();
	}

}
