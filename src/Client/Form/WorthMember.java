/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.Form;

import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;


/**
 *
 * @author aless
 */
@SuppressWarnings("serial")
public class WorthMember extends javax.swing.JFrame {

	private static final String WORTH_ICON = "img/worth_icon.png";
	private DefaultListModel<String> users;
    /**
     * Creates new form Card
     */
    public WorthMember(DefaultListModel<String> model) {
    	this.users = model;
    	
        initComponents();
    	ImageConfig();
        Setting();
    }

	 public void ImageConfig() {
	     ImageIcon imgIcon = new ImageIcon(WORTH_ICON);
	     this.setIconImage(imgIcon.getImage());
	 }
 
	 public void RefreshList() {
         
         if(users.size() > 0) {
	         ListMember.setModel(users);
	         System.out.println("refresh");
         }
	 }
	 
	 public void Setting() {
         
         ListMember.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
         ListMember.setSelectionBackground(new java.awt.Color(153, 153, 153));
         
         jScrollPane2.setViewportView(ListMember);
         
     	 this.setTitle("Member on Worth");
     	 this.setVisible(true);
     	 
     	 //card.setBounds(10, 10, 600, 400);
     	 //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     	 this.setResizable(false);
     	 
     	 GraphicsConfiguration config = this.getGraphicsConfiguration();
     	 Rectangle bounds = config.getBounds();
     	 Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);

     	 int x = bounds.x + bounds.width - insets.right - this.getWidth();
     	 int y = bounds.y + insets.top;
     	 this.setLocation(x, y);

         if(users.size() > 0)
	         ListMember.setModel(users);
        
	 }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LabelGroupCard = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        ListMember = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        LabelGroupCard.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        LabelGroupCard.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        LabelGroupCard.setText("Member:");

        ListMember.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        ListMember.setSelectionBackground(new java.awt.Color(153, 153, 153));
        jScrollPane2.setViewportView(ListMember);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(LabelGroupCard, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(LabelGroupCard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 319, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void start() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WorthMember.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WorthMember.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WorthMember.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WorthMember.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LabelGroupCard;
    private javax.swing.JList<String> ListMember;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}