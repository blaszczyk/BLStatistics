package bn.blaszczyk.fussballstats.gui;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
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
	private static final String info1 = "Entwickelt von: Michael Blaszczyk (michael.i.blaszczyk@gmail.com)";
	private static final String info2 = "Mit Unterstützung von: alfatraining (www.alfatraining.de)" ;
	private static final String info3 = "Fussballdaten bezogen von: www.weltfussball.de";
	private static final String info4 = "Icons von: www.iconfinder.com";
	
	private static final String[] infos = {info1,info2,info3,info4};
	
	/*
	 * Variables
	 */
	private Window owner;
	
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
			JLabel label = new JLabel(infos[i]);
			label.setFont(FONT);
			label.setBounds(10, 100 + 30 * i, 450, 30);
			add(label);
		}
		
		JButton btnClose = new JButton("Schliessen");
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

}
