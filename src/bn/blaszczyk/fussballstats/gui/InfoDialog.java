package bn.blaszczyk.fussballstats.gui;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import bn.blaszczyk.fussballstats.FussballStats;

@SuppressWarnings("serial")
public class InfoDialog extends JDialog
{

	/*
	 * Constants
	 */
	private static final String ICON_FILE = "data/help.png";
	private static final String LOGO_FILE = "data/icon.png";
	private static final Font TITLEFONT = new Font("Arial",Font.BOLD,26);
	private static final Font FONT = new Font("Arial",Font.PLAIN,14);
	
	private static final JLabel title = new JLabel("FussballStats", new ImageIcon(FussballStats.class.getResource(LOGO_FILE)),SwingConstants.CENTER);
	private static final String info1 = "<html>von Michael Blaszczyk (<font color=\"blue\">michael.i.blaszczyk@gmail.com</font>)";
	private static final String info2 = "<html>repo: <font color=\"blue\">github.com/blaszczyk/Fussballstats</font>";
	private static final String info3 = "<html>Fussballdaten von: <font color=\"blue\">www.weltfussball.de</font>";
	private static final String info4 = "<html>Icons von: <font color=\"blue\">www.iconfinder.com</font>";
	
	private static final String[] infos = {info1,info2,info3,info4};
	
	/*
	 * Variables
	 */
	private final  Window owner;
	
	/*
	 * Constructors
	 */
	public InfoDialog(Window owner)
	{
		super(owner, ModalityType.APPLICATION_MODAL);
		this.owner = owner;
		
		setTitle("Über Fussballstats");
		setLayout(null);
		setSize(470,340);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(FussballStats.class.getResource(ICON_FILE)));

		title.setBounds(10, 10, 450, 60);
		title.setFont(TITLEFONT);
		add(title);
		
		
		for(int i = 0; i < infos.length; i++)
		{
			JTextPane  label = new CopyableLabel(infos[i]);
			label.setBounds(10, 100 + 30 * i, 450, 30);
			add(label);
		}
		
		JButton btnClose = new JButton("Schliessen");
		btnClose.setMnemonic('S');
		btnClose.addActionListener(e -> dispose());
		btnClose.setBounds(100, 260, 270, 30);
		add(btnClose);
		
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
	 * Inner Classes
	 */
	private static class CopyableLabel extends JTextPane {

	    public CopyableLabel(String text) {
	        setContentType("text/html");
	        setFont(FONT);
	        setEditable(false);
	        setOpaque(false);
	        putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
	        setText(text);
	    }
	}
}
