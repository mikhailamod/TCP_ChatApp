
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mikhail
 */
public class GUI_Main extends javax.swing.JFrame {
    
    //attributes
    String server;
    String username;
    int port;
	String messageType;
	User activeUser;
	ChatAppClient client;
	ArrayList<User> all_users;
    /**
     * Creates new form ChatAppGUI
     */
    public GUI_Main(String username, String _server, int _port) {
        this.username = username;
        this.server = _server;
        this.port = _port;
		messageType = "broadcast";
		activeUser = new User(username, server);
		all_users = new ArrayList<>();
		client = new ChatAppClient(server, port, username, this);
        initComponents();
		lbl_Heading.setText("Welcome  " + username);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grp_messageType = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        lbl_ChatArea = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btn_send = new javax.swing.JButton();
        lbl_Heading = new javax.swing.JLabel();
        rb_broadcast = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txa_ChatArea = new javax.swing.JTextArea();
        btn_attach = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txa_newMessage = new javax.swing.JTextArea();
        rb_private = new javax.swing.JRadioButton();
        txf_sendTo = new javax.swing.JTextField();
        rb_file = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAutoRequestFocus(false);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(55, 71, 79));
        jPanel2.setBorder(null);

        jScrollPane3.setBorder(null);

        jList1.setBackground(new java.awt.Color(55, 71, 79));
        jList1.setBorder(null);
        jList1.setForeground(new java.awt.Color(254, 254, 254));
        jList1.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList1);

        lbl_ChatArea.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_ChatArea.setForeground(new java.awt.Color(254, 254, 254));
        lbl_ChatArea.setText("Users");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_ChatArea)
                .addGap(66, 66, 66))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lbl_ChatArea)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 410, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jPanel3.setBackground(new java.awt.Color(98, 114, 123));
        jPanel3.setBorder(null);

        btn_send.setText("Send");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        lbl_Heading.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_Heading.setForeground(new java.awt.Color(244, 244, 244));
        lbl_Heading.setText("ChatApp");

        grp_messageType.add(rb_broadcast);
        rb_broadcast.setForeground(new java.awt.Color(224, 224, 224));
        rb_broadcast.setText("broadcast");
        rb_broadcast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_broadcastActionPerformed(evt);
            }
        });

        txa_ChatArea.setEditable(false);
        txa_ChatArea.setBackground(new java.awt.Color(224, 224, 224));
        txa_ChatArea.setColumns(20);
        txa_ChatArea.setRows(5);
        jScrollPane1.setViewportView(txa_ChatArea);

        btn_attach.setText("Attach File");

        txa_newMessage.setBackground(new java.awt.Color(224, 224, 224));
        txa_newMessage.setColumns(20);
        txa_newMessage.setRows(1);
        jScrollPane2.setViewportView(txa_newMessage);

        grp_messageType.add(rb_private);
        rb_private.setForeground(new java.awt.Color(224, 224, 224));
        rb_private.setText("private");
        rb_private.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_privateActionPerformed(evt);
            }
        });

        txf_sendTo.setBackground(new java.awt.Color(224, 224, 224));
        txf_sendTo.setEnabled(false);
        txf_sendTo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txf_sendToActionPerformed(evt);
            }
        });

        grp_messageType.add(rb_file);
        rb_file.setForeground(new java.awt.Color(224, 224, 224));
        rb_file.setText("file");
        rb_file.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rb_fileActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_Heading)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 652, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(rb_broadcast)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rb_private)
                        .addGap(18, 18, 18)
                        .addComponent(txf_sendTo, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(rb_file)
                        .addGap(18, 18, 18)
                        .addComponent(btn_attach, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 6, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_Heading)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rb_private)
                    .addComponent(txf_sendTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rb_broadcast)
                    .addComponent(btn_attach)
                    .addComponent(rb_file))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        client.shutdown();
    }//GEN-LAST:event_formWindowClosing

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed

        boolean empty = txa_newMessage.getText().equals("");
        if((!messageType.equals("file") && empty))//error prevention
        {
            JOptionPane.showMessageDialog(this, "No message was entered");
        }
        else if(messageType.equals("broadcast"))
        {
            String userInput = txa_newMessage.getText();
            send("broadcast", userInput, "all");
        }
        else if(messageType.equals("private"))
        {
            String sendTo = txf_sendTo.getText();
            String userInput = txa_newMessage.getText();
            send("private", userInput, sendTo);
        }
        else//send file
        {
            String filepath = JOptionPane.showInputDialog(this, "Enter filepath for file");
            //TODO:call file attach methods
        }
        txa_newMessage.setText("");
    }//GEN-LAST:event_btn_sendActionPerformed

    private void rb_fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_fileActionPerformed
        // TODO add your handling code here:
        messageType = "file";
        btn_attach.setEnabled(true);
        txf_sendTo.setEnabled(false);
    }//GEN-LAST:event_rb_fileActionPerformed

    private void rb_privateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_privateActionPerformed
        // TODO add your handling code here:
        messageType = "private";
        btn_attach.setEnabled(false);
        txf_sendTo.setEnabled(true);
    }//GEN-LAST:event_rb_privateActionPerformed

    private void rb_broadcastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rb_broadcastActionPerformed
        // TODO add your handling code here:
        messageType = "broadcast";
        btn_attach.setEnabled(false);
        txf_sendTo.setEnabled(false);
    }//GEN-LAST:event_rb_broadcastActionPerformed

    private void txf_sendToActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txf_sendToActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txf_sendToActionPerformed

		boolean empty = txa_newMessage.getText().equals("");
		if((!messageType.equals("file") && empty))//error prevention
		{
			JOptionPane.showMessageDialog(this, "No message was entered");
		}
		else if(messageType.equals("broadcast"))
		{
			String userInput = txa_newMessage.getText();
			txa_ChatArea.append("\nYou[broadcast]: " + userInput);
			send("broadcast", userInput, "all");
		}
		else if(messageType.equals("private"))
		{
			String sendTo = txf_sendTo.getText();
			String userInput = txa_newMessage.getText();
			txa_ChatArea.append("\nYou[private to " + sendTo + "]: " + userInput);
			send("private", userInput, sendTo);
		}
		else//send file
		{
			String filepath = JOptionPane.showInputDialog(this, "Enter filepath for file");
			//TODO:call file attach methods
		}
		txa_newMessage.setText("");
    }//GEN-LAST:event_btn_sendActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        client.shutdown();
    }//GEN-LAST:event_formWindowClosing

	//given a message, print to textArea
	public void recieve(String message)
	{
		txa_ChatArea.append(message);
	}
	
	public void recieve(Message m)
	{
		if(m.getTag().equals("userList"))
		{
			all_users = new ArrayList<>(m.getUserList());
		}
	}
	
	//given an input message, send to client class
	public void send(String type, String message, String sendTo)
	{
		client.send(type, message, sendTo);
	}
/*
    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI_Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI_Main().setVisible(true);
            }
        });
    }*/

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_attach;
    private javax.swing.JButton btn_send;
    private javax.swing.ButtonGroup grp_messageType;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lbl_ChatArea;
    private javax.swing.JLabel lbl_Heading;
    private javax.swing.JRadioButton rb_broadcast;
    private javax.swing.JRadioButton rb_file;
    private javax.swing.JRadioButton rb_private;
    private javax.swing.JTextArea txa_ChatArea;
    private javax.swing.JTextArea txa_newMessage;
    private javax.swing.JTextField txf_sendTo;
    // End of variables declaration//GEN-END:variables
}
