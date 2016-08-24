package bn.blaszczyk.fussballstats.gui;

import java.awt.Toolkit;
import java.awt.Window;
import java.util.prefs.Preferences;

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
import bn.blaszczyk.fussballstats.tools.DBTools;
import bn.blaszczyk.fussballstats.tools.TeamAlias;

@SuppressWarnings("serial")
public class PrefsDialog extends JDialog
{
	private static final String KEY_SERVER = "server";
	private static final String KEY_PORT = "port";
	private static final String KEY_DB_NAME = "db_name";
	private static final String KEY_USER = "user";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_DB_MODE = "db_mode";
	private static final String KEY_USE_ALIASES = "use_aliases";
	private static final String KEY_SAVE_LAST_FILTER = "save_last_filter";
	
	private static final String ICON_FILE = "data/settings.png";
	
	private static final Preferences PREFERENCES = Preferences.userNodeForPackage(FussballStats.class);
	
	private Window owner;
	
	private JTextField tfServer, tfPort, tfDbName, tfUser, tfPassword;
	
	private JRadioButton rbHardDrive = new JRadioButton();
	private JRadioButton rbDataBase = new JRadioButton();
	
	private JCheckBox chbAliases = new JCheckBox();
	private JCheckBox chbLoadLastFilter = new JCheckBox();

	private JButton btnSave = new JButton("Speichern");
	private JButton btnDismiss = new JButton("Verwerfen");
	
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
		
		addButtonRow(rbHardDrive,"Festplatte", !LeagueManager.isDbMode(),'F', 1, 150);
		addButtonRow(rbDataBase,"SQL Datenbank", LeagueManager.isDbMode(),'D', 2, 150);
		
		ButtonGroup bgDbMode = new ButtonGroup();
		bgDbMode.add(rbHardDrive);
		bgDbMode.add(rbDataBase);

		rbHardDrive.addItemListener( e -> setDbAccessDataEnabled(false));
		rbDataBase.addItemListener( e -> setDbAccessDataEnabled(true));

		tfServer = createTextFieldRow("Server", DBTools.getServer(),'R',  3, false);
		tfPort = createTextFieldRow("Port", DBTools.getPort(),'P',  4, false);
		tfDbName = createTextFieldRow("Datenbank", DBTools.getDbName(),'T', 5, false);
		tfUser = createTextFieldRow("Benutzer", DBTools.getUser(),'B', 6, false);
		tfPassword = createTextFieldRow("Passwort", DBTools.getPassword(),'W', 7, true);
		setDbAccessDataEnabled(LeagueManager.isDbMode());
		
		addButtonRow(chbAliases,"Vereinsumbenennungen beachten", TeamAlias.isUseAliases(), 'U',8, 350);
		addButtonRow(chbLoadLastFilter,"Letzten Filter beim Start laden", FunctionalFilterPanel.isLoadLastFilter(),'L', 9, 300);
		
		btnSave.addActionListener(e -> save());
		btnSave.setMnemonic('S');
		btnSave.setBounds(10, 365, 150, 30);
		
		btnDismiss.addActionListener(e -> dispose());
		btnDismiss.setMnemonic('V');
		btnDismiss.setBounds(180, 365, 150, 30);
		
		add(btnSave);
		add(btnDismiss);
	}


	public void showDialog()
	{
		setLocationRelativeTo(owner);
		setVisible(true);	
	}
	
	public static boolean initPrefs()
	{
		if(PREFERENCES.get(KEY_DB_MODE, null) == null)
			return false;

		String server = PREFERENCES.get(KEY_SERVER, "localhost");
		String port = PREFERENCES.get(KEY_PORT, "3306");
		String dbName = PREFERENCES.get(KEY_DB_NAME, "fussballspiele");
		String user = PREFERENCES.get(KEY_USER, "root");
		String password = PREFERENCES.get(KEY_PASSWORD, null);
		DBTools.setAccessData(server, port, dbName, user, password);
		
		LeagueManager.setDbMode(PREFERENCES.getBoolean(KEY_DB_MODE, false));
		TeamAlias.setUseAliases(PREFERENCES.getBoolean(KEY_USE_ALIASES, false));
		FunctionalFilterPanel.setLoadLastFilter(PREFERENCES.getBoolean(KEY_SAVE_LAST_FILTER, false));
		return true;
	}

	private JTextField createTextFieldRow(String labelText, String defText, char mnemonic, int column, boolean isPassword)
	{
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
		boolean dbMode = rbDataBase.isSelected();
		LeagueManager.setDbMode(dbMode);

		String server = tfServer.getText();
		String port = tfPort.getText();
		String dbName = tfDbName.getText();
		String user = tfUser.getText();
		String password = tfPassword.getText();
		DBTools.setAccessData(server, port, dbName, user, password);

		boolean useAliases = chbAliases.isSelected();
		TeamAlias.setUseAliases( useAliases );
		
		boolean saveLastFilter = chbLoadLastFilter.isSelected();
		FunctionalFilterPanel.setLoadLastFilter(saveLastFilter);	
	
		PREFERENCES.putBoolean(KEY_DB_MODE, dbMode);
		PREFERENCES.put(KEY_SERVER, server);
		PREFERENCES.put(KEY_PORT, port);
		PREFERENCES.put(KEY_DB_NAME, dbName);
		PREFERENCES.put(KEY_USER, user);
		PREFERENCES.put(KEY_PASSWORD, password);
		PREFERENCES.putBoolean(KEY_USE_ALIASES, useAliases);
		PREFERENCES.putBoolean(KEY_SAVE_LAST_FILTER, saveLastFilter);

		dispose();
	}

}
