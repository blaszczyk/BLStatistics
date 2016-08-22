package bn.blaszczyk.fussballstats.gui;

import java.awt.Toolkit;
import java.util.prefs.Preferences;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
	private static final String KEY_DB_NAME = "db_name";
	private static final String KEY_USER = "user";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_DB_MODE = "db_mode";
	private static final String KEY_USE_ALIASES = "use_aliases";
	private static final String KEY_SAVE_LAST_FILTER = "save_last_filter";
	
	private static final String ICON_FILE = "data/settings.png";
	
	private JFrame owner;
	
	private JTextField tfServer, tfDbName, tfUser, tfPassword;
	
	private JButton btnSave = new JButton("Speichern");
	private JButton btnClose = new JButton("Schließen");
	
	private JRadioButton rbHardDrive = new JRadioButton("Festplatte", !LeagueManager.isDbMode());
	private JRadioButton rbDataBase = new JRadioButton("Datenbank", LeagueManager.isDbMode());
	
	private JCheckBox chbAliases = new JCheckBox("Vereinsumbenennungen beachten", TeamAlias.isUseAliases());
	private JCheckBox chbSaveLastFilter = new JCheckBox("Filter beim Beenden speichern", FunctionalFilterPanel.isSaveLastFilter());

	public PrefsDialog(JFrame owner)
	{
		super(owner, "Einstellungen", true);
		this.owner = owner;
		
		setLayout(null);
		setSize(360, 410);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE)));

		addComponentRow(new JLabel("Spiele laden von"), 0, 170);
		addComponentRow(rbHardDrive, 1, 150);
		addComponentRow(rbDataBase, 2, 150);
		
		ButtonGroup bgDbMode = new ButtonGroup();
		bgDbMode.add(rbHardDrive);
		bgDbMode.add(rbDataBase);

		rbHardDrive.addItemListener( e -> setDbAccessDataEnabled(false));
		rbDataBase.addItemListener( e -> setDbAccessDataEnabled(true));
		
		tfServer = createTextFieldRow("Server", DBTools.getServer(), 3, false);
		tfDbName = createTextFieldRow("Datenbank", DBTools.getDbName(), 4, false);
		tfUser = createTextFieldRow("Benutzer", DBTools.getUser(), 5, false);
		tfPassword = createTextFieldRow("Passwort", DBTools.getPassword(), 6, true);
		setDbAccessDataEnabled(LeagueManager.isDbMode());
		
		addComponentRow(chbAliases, 7, 350);
		addComponentRow(chbSaveLastFilter, 8, 300);
		
		btnSave.addActionListener(e -> save());
		btnSave.setMnemonic('S');
		btnSave.setBounds(10, 330, 150, 30);
		
		btnClose.addActionListener(e -> cancel());
		btnClose.setMnemonic('C');
		btnClose.setBounds(180, 330, 150, 30);
		
		add(btnSave);
		add(btnClose);
	}


	public void showDialog()
	{
		setLocationRelativeTo(owner);
		setVisible(true);	
	}
	
	public static boolean initPrefs()
	{
		Preferences prefs = Preferences.userNodeForPackage(FussballStats.class);
		
		if(prefs.get(KEY_DB_MODE, null) == null)
			return false;
		
		String server = prefs.get(KEY_SERVER, "localhost");
		String dbName = prefs.get(KEY_DB_NAME, "fussballspiele");
		String user = prefs.get(KEY_USER, "root");
		String password = prefs.get(KEY_PASSWORD, null);
		DBTools.setAccessData(server, dbName, user, password);
		
		LeagueManager.setDbMode(prefs.getBoolean(KEY_DB_MODE, false));
		TeamAlias.setUseAliases(prefs.getBoolean(KEY_USE_ALIASES, false));
		FunctionalFilterPanel.setSaveLastFilter(prefs.getBoolean(KEY_SAVE_LAST_FILTER, false));
		return true;
	}

	private JTextField createTextFieldRow(String labelText, String defText, int column, boolean isPassword)
	{
		JLabel label = new JLabel(labelText,SwingConstants.RIGHT);
		JTextField textField;
		if(isPassword)
			textField = new JPasswordField(defText);
		else
			textField = new JTextField(defText);
		label.setBounds(10, 10 + column * 35 , 100, 25);
		textField.setBounds(120, 10 + column * 35 , 200, 25);
		
		add(label);
		add(textField);
		return textField;
	}

	private void addComponentRow(JComponent component, int column, int width)
	{
		component.setBounds(10, 10 + column * 35 , width, 25);
		add(component);
	}
	
	private void setDbAccessDataEnabled(boolean enabled)
	{
		tfServer.setEnabled(enabled);
		tfDbName.setEnabled(enabled);
		tfUser.setEnabled(enabled);
		tfPassword.setEnabled(enabled);
	}
	
	private void save()
	{
		boolean dbMode = rbDataBase.isSelected();
		LeagueManager.setDbMode(dbMode);
		
		String server = tfServer.getText();
		String dbName = tfDbName.getText();
		String user = tfUser.getText();
		String password = tfPassword.getText();
		DBTools.setAccessData(server, dbName, user, password);

		boolean useAliases = chbAliases.isSelected();
		TeamAlias.setUseAliases( useAliases );
		
		boolean saveLastFilter = chbSaveLastFilter.isSelected();
		FunctionalFilterPanel.setSaveLastFilter(saveLastFilter);	
	
		Preferences prefs = Preferences.userNodeForPackage(FussballStats.class);
		prefs.putBoolean(KEY_DB_MODE, dbMode);
		prefs.put(KEY_SERVER, server);
		prefs.put(KEY_DB_NAME, dbName);
		prefs.put(KEY_USER, user);
		prefs.put(KEY_PASSWORD, password);
		prefs.putBoolean(KEY_USE_ALIASES, useAliases);
		prefs.putBoolean(KEY_SAVE_LAST_FILTER, saveLastFilter);

		dispose();
	}
	
	private void cancel()
	{
		dispose();
	}

}
