package Client.Form;

import Client.GUIClientMain;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class NewProject extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = -1415350520861177149L;
	private static final String WORTH_IMG = "img/worth-logo.png";
	private static final String WORTH_ICON = "img/worth_icon.png";
	
	private Container container = getContentPane();
	
	private JLabel MainLabel = new JLabel("Insert the data of new project:");

	private JLabel NameLabel = new JLabel("Name");
	private JLabel DescriptionLabel = new JLabel("Description");
	private JLabel DataLabel = new JLabel("Deadline");
    
    private JTextField NameField = new JTextField();
    private JTextField DescriptionField = new JTextField();
    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    private JFormattedTextField Deadline = new JFormattedTextField(df);    
    
	private JButton ConfirmButton = new JButton("Confirm");
	private JButton BackButton = new JButton("Back");
    
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

   
    public NewProject() {
    	
    	setting();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();   
    }
    
    private void setting() {
        Deadline.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
              char c = e.getKeyChar();
              if (!((c >= '0') && (c <= '9') ||
                 (c == KeyEvent.VK_BACK_SPACE) ||
                 (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_SLASH)))        
              {
                JOptionPane.showMessageDialog(null, "Please Enter Valid (like dd/MM/yyyy)");
                e.consume();
              }
            }
          });
    }
    
    private void setLayoutManager() {
        container.setLayout(null);
    }
    
    private void setLocationAndSize() {
    	LogoLabel.setBounds(240, 20, 100, 100);

        MainLabel.setBounds(50, 150, 500, 30);
        MainLabel.setFont(new Font(MainLabel.getName(), Font.PLAIN, 18));
        
    	NameLabel.setBounds(50, 200, 100, 30);
    	NameField.setBounds(130, 200, 350, 30);
        
        DescriptionLabel.setBounds(50, 240, 100, 30);
        DescriptionField.setBounds(130, 240, 350, 30);
        
        DataLabel.setBounds(50, 280, 100, 30);
        Deadline.setBounds(130, 280, 350, 30);
        
        ConfirmButton.setBounds(350, 400, 200, 30);
        BackButton.setBounds(50, 400, 200, 30);
 
    }
 
    private void addComponentsToContainer() {
    	container.add(LogoLabel);
        container.add(MainLabel);
        
        container.add(NameLabel);
        container.add(NameField);
        container.add(DescriptionLabel);
        container.add(DescriptionField);
        container.add(DataLabel);
        container.add(Deadline);
        
        container.add(ConfirmButton);
        container.add(BackButton);
       
    }
 
    private void addActionEvent() {
    	ConfirmButton.addActionListener(this);
    	BackButton.addActionListener(this);
    }


	@Override
	public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ConfirmButton) {
        	
            String name = NameField.getText();
            String description = DescriptionField.getText();
            String owner = GUIClientMain.ReturnNickname();
            String deadline = Deadline.getText();
            
            if(name.isBlank() || deadline.isBlank()) {
            	JOptionPane.showMessageDialog(this, "The project must have a name and deadline");
            	return;
            }
            
            if (GUIClientMain.CreateProject(name, description, owner, deadline)) {
                JOptionPane.showMessageDialog(this, "Project Created");
                GUIClientMain.Home();
            } else {
                JOptionPane.showMessageDialog(this, "Project already exist");
            }
        }
        if (e.getSource() == BackButton) {
        	GUIClientMain.Home();
        }
	}
 
}
