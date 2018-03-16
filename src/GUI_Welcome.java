
import java.awt.TextField;
import java.awt.event.KeyListener;
import java.io.*;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mikhail
 */
public class GUI_Welcome extends javax.swing.JFrame{

    //Attributes
    private boolean isSimple;
    private static int PORT_NUMBER = 6000;
    /**
     * Creates new form GUI_Login
     */
    public GUI_Welcome() {
        initComponents();
        ImageIcon i = new ImageIcon("back.jpg");
        jLabel2.setIcon(i);
        jPanel3.setVisible(false);
        isSimple = true;
		SwingUtilities.getRootPane(this).setDefaultButton(btn_continue);
		txf_username.requestFocus();
                
                
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grp_login = new javax.swing.ButtonGroup();
        jCheckBox1 = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        rb_simple = new javax.swing.JRadioButton();
        lbl_username = new javax.swing.JLabel();
        rb_advanced = new javax.swing.JRadioButton();
        txf_username = new javax.swing.JTextField();
        btn_continue = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        lbl_server = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        txf_server = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        txf_port = new javax.swing.JTextField();
        lbl_port = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jCheckBox1.setText("jCheckBox1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(98, 114, 123));

        jPanel2.setBackground(new java.awt.Color(16, 32, 39));
        jPanel2.setBorder(null);

        grp_login.add(rb_simple);
        rb_simple.setForeground(new java.awt.Color(240, 240, 240));
        rb_simple.setSelected(true);
        rb_simple.setText("Simple");
        rb_simple.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_simpleActionPerformed(evt);
            }
        });

        lbl_username.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        lbl_username.setForeground(new java.awt.Color(240, 240, 240));
        lbl_username.setText("Username:");

        grp_login.add(rb_advanced);
        rb_advanced.setForeground(new java.awt.Color(240, 240, 240));
        rb_advanced.setText("Advanced");
        rb_advanced.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_advancedActionPerformed(evt);
            }
        });

        txf_username.setBackground(new java.awt.Color(16, 32, 39));
        txf_username.setForeground(new java.awt.Color(254, 254, 254));
        txf_username.setBorder(null);
        txf_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txf_usernameActionPerformed(evt);
            }
        });

        btn_continue.setText("Continue");
        btn_continue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_continueActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(254, 254, 254));
        jLabel1.setText("Welcome to ChatApp");

        jPanel3.setBackground(new java.awt.Color(16, 32, 39));

        lbl_server.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        lbl_server.setForeground(new java.awt.Color(240, 240, 240));
        lbl_server.setText("Server IP:");
        lbl_server.setBorder(null);

        txf_server.setEditable(false);
        txf_server.setBackground(new java.awt.Color(16, 32, 39));
        txf_server.setForeground(new java.awt.Color(254, 254, 254));
        txf_server.setBorder(null);
        txf_server.setEnabled(false);

        txf_port.setEditable(false);
        txf_port.setBackground(new java.awt.Color(16, 32, 39));
        txf_port.setForeground(new java.awt.Color(254, 254, 254));
        txf_port.setBorder(null);
        txf_port.setEnabled(false);

        lbl_port.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        lbl_port.setForeground(new java.awt.Color(240, 240, 240));
        lbl_port.setText("Port:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txf_port, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                    .addComponent(txf_server)
                    .addComponent(lbl_port)
                    .addComponent(lbl_server)
                    .addComponent(jSeparator2)
                    .addComponent(jSeparator3))
                .addContainerGap(72, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_server)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txf_server, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_port)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txf_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(rb_simple)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rb_advanced))
                            .addComponent(lbl_username, javax.swing.GroupLayout.Alignment.LEADING))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_continue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .addComponent(jSeparator1)
                            .addComponent(txf_username, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(74, 74, 74))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addComponent(lbl_username)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txf_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb_simple)
                    .addComponent(rb_advanced))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(btn_continue)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rb_simpleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_simpleActionPerformed
        // TODO add your handling code here:
        jPanel3.setVisible(false);
        txf_server.setVisible(false);
        txf_port.setVisible(false);
        txf_server.setEditable(false);
        txf_server.setEnabled(false);
        txf_port.setEditable(false);
        txf_port.setEnabled(false);
        isSimple = true;
    }//GEN-LAST:event_rb_simpleActionPerformed

    private void rb_advancedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_advancedActionPerformed
        // TODO add your handling code here:
        jPanel3.setVisible(true);
        txf_server.setVisible(true);
        txf_port.setVisible(true);
        txf_server.setEditable(true);
        txf_port.setEditable(true);
        txf_server.setEnabled(true);
        txf_port.setEnabled(true);
        isSimple = false;
    }//GEN-LAST:event_rb_advancedActionPerformed

    private void btn_continueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_continueActionPerformed
        // TODO add your handling code here:
        String username = txf_username.getText();
        String server = "";
        int port = PORT_NUMBER;
        if(!isSimple)
        {
            server = txf_server.getText();
            port = Integer.parseInt(txf_port.getText());
	}
        else
        {
            server = getHostName(port);
            
        }
        if(AuthManager.exists(username))//if username exists, make them login
        {
            this.setVisible(false);
            GUI_Login login;
	    login =new GUI_Login(username,server, port, false);
            login.setVisible(true);
            login.setBounds(this.getBounds());
        }
        else
        {
	    this.setVisible(false);
	    GUI_Login login;
	    login =new GUI_Login(username,server, port, true);
            login.setVisible(true);
            login.setBounds(this.getBounds());
        }
        
    }//GEN-LAST:event_btn_continueActionPerformed

    private void txf_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txf_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txf_usernameActionPerformed

    
    //Returns hostname
    public static String getHostName(int portNumber)
    {
	    String hostname = null;
	    System.out.println("Fetching hostname and using Port Number " + portNumber);
	    try{

		    Process p = Runtime.getRuntime().exec("hostname");//run the hostname command
		    BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		    String line = null;

		    while((line = input.readLine()) != null)
		    {
			    hostname = line;
		    }//end while
		    return hostname;
	    }//end try
	    catch (IOException e)
	    {
		    System.out.println(e);
		    return e.toString();
	    }
    }//end getHostName
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI_Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Welcome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //get user info

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_Welcome().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_continue;
    private javax.swing.ButtonGroup grp_login;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lbl_port;
    private javax.swing.JLabel lbl_server;
    private javax.swing.JLabel lbl_username;
    private javax.swing.JRadioButton rb_advanced;
    private javax.swing.JRadioButton rb_simple;
    private javax.swing.JTextField txf_port;
    private javax.swing.JTextField txf_server;
    private javax.swing.JTextField txf_username;
    // End of variables declaration//GEN-END:variables
}
