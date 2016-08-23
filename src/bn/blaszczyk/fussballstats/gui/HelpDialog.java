package bn.blaszczyk.fussballstats.gui;

import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import bn.blaszczyk.fussballstats.FussballStats;

@SuppressWarnings("serial")
public class HelpDialog extends JDialog
{
	
	private static final String ICON_FILE = "data/help.png";

//	private static final String INFO_TEXT = "<html>Title<ul><li><font color=red>rot</font><li><font size=+6>gross</font></ul></html>";
	private static final String INFO_TEXT = "info";
	
	private Window owner;
	private JTabbedPane tabbedPane;
	private JLabel info = new JLabel(INFO_TEXT);
	private JLabel info2 = new JLabel(INFO_TEXT);
	private JLabel info3 = new JLabel(INFO_TEXT);
	
	public HelpDialog(Window owner)
	{
		super(owner, ModalityType.APPLICATION_MODAL);
		this.owner = owner;
		setTitle("Hilfe");
		setLayout(null);
		setSize(500,500);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE)));
		
		tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setBounds(10, 10, 400, 400);
		tabbedPane.addTab("Über", info);
		tabbedPane.addTab("Über2", info2);
		tabbedPane.addTab("Über3", info3);
		
		add(tabbedPane);
	}


	public void showDialog()
	{
		setLocationRelativeTo(owner);
		setVisible(true);
	}

	public static void main(String[] args)
	{
		new HelpDialog(null).showDialog();
	}

}
