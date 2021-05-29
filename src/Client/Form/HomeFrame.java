package Client.Form;

import Client.GUIClientMain;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class HomeFrame extends JFrame implements ActionListener, MouseListener {
	
	private static final long serialVersionUID = -1415350520861177149L;
	private static final String WORTH_IMG = "img/worth-logo.png";
	private static final String WORTH_ICON = "img/worth_icon.png";
	
	private Container container = getContentPane();
	private JLabel userLabel;
    
	private JButton NewProject = new JButton("Create a new project");
	private JButton ShowProject = new JButton("Show menu of projects");
    
	private JLabel Logout = new JLabel("Vuoi fare il logout?");
	private JLabel LabelLogout = new JLabel("Esci");
    
	private JLabel LogoLabel = ImageConfig();

    public JLabel ImageConfig() {
        try {
        	
        	ImageIcon imgIcon = new ImageIcon(WORTH_ICON);
        	this.setIconImage(imgIcon.getImage());
        	
            BufferedImage img = ImageIO.read(new File(WORTH_IMG));
            ImageIcon icon = new ImageIcon(img);
            JLabel label = new JLabel(icon);
            return label;
         } catch (IOException e) {
            e.printStackTrace();
         }
		return null;
    }
   
    public HomeFrame(String name) {
    	this.userLabel = new JLabel("Ciao " + name + ", bentornato su worth!");
    	
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();
        addMouseEvent();
        ColorLabel();    
    }
 
    private void ColorLabel() {
    	LabelLogout.setForeground(Color.BLUE);
    }
    
    private void setLayoutManager() {
        container.setLayout(null);
    }
 
    private void setLocationAndSize() {
    	LogoLabel.setBounds(240, 20, 100, 100);
    	
        userLabel.setBounds(50, 150, 500, 30);
        userLabel.setFont(new Font(userLabel.getName(), Font.PLAIN, 18));
        
        ShowProject.setBounds(50, 205, 200, 30);
        NewProject.setBounds(350, 205, 200, 30);
        
        Logout.setBounds(220, 250, 200, 30);
        LabelLogout.setBounds(330, 250, 200, 30);
 
    }
 
    private void addComponentsToContainer() {
    	container.add(LogoLabel);
        container.add(userLabel);
        
        container.add(ShowProject);
        container.add(NewProject);
        
        container.add(Logout);
        container.add(LabelLogout);
    }
 
    private void addActionEvent() {
    	ShowProject.addActionListener(this);
    	NewProject.addActionListener(this);
    }
 
    private void addMouseEvent() {
    	LabelLogout.addMouseListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ShowProject) {
        	GUIClientMain.ShowProjects();
        }
        if (e.getSource() == NewProject) {
        	GUIClientMain.NewProject();
        }
    }


	@Override
	public void mouseClicked(MouseEvent e) {
    	if(e.getSource() == LabelLogout) {
    		GUIClientMain.Logout();
    	}
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
 
}