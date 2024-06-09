package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import model.Data_Manager;

/**
 * Setup a login before connecting to the database.
 */
public class Login_Dialog extends JFrame {
     private final int DIALOG_WIDTH = 400;
     private final int DIALOG_HEIGHT = 100;
     private final Dimension dialog_dimension = new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT);

     private final GridBagLayout grid_bag_layout = new GridBagLayout();
     private final GridBagConstraints layout_Constraints = new GridBagConstraints();

     private JLabel username_label;
     private JTextField username_input;
     private JLabel password_label;
     private JPasswordField password_input;
     private JButton login_button;

     public Login_Dialog(String frame_title) {
          // setup the dialog
          this.setTitle(frame_title);
          this.getContentPane().setPreferredSize(dialog_dimension);
          this.setResizable(false);
          this.setLayout(grid_bag_layout);
          this.pack();
          this.setLocationRelativeTo(null);
          this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

          displayLoginForm();
     }

     /**
      * Main display.
      */
     private void displayLoginForm() {
          username_label = new JLabel("Username: ");
          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 0;
          layout_Constraints.gridy = 0;
          this.add(username_label, layout_Constraints);

          username_input = new JTextField();
          username_input.setPreferredSize(new Dimension(200, 30));
          layout_Constraints.gridx = 1;
          layout_Constraints.gridy = 0;
          this.add(username_input, layout_Constraints);

          password_label = new JLabel("Password: ");
          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 0;
          layout_Constraints.gridy = 1;
          this.add(password_label, layout_Constraints);

          password_input = new JPasswordField();
          password_input.setPreferredSize(new Dimension(200, 30));
          layout_Constraints.gridx = 1;
          layout_Constraints.gridy = 1;
          this.add(password_input, layout_Constraints);

          login_button = new JButton("LOGIN");
          login_button.setFocusable(false);
          login_button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    // connect to the database based on the login credentials
                    Data_Manager.createConnection(username_input.getText(), new String(password_input.getPassword()));
                    Login_Dialog.this.setTitle("Incorrect Username or Password.");

                    // if the connection is successful, display the main frame
                    if (Data_Manager.getAccess()) {
                         JOptionPane.showMessageDialog(Login_Dialog.this, "Login Successful", "Access Granted",
                                   JOptionPane.DEFAULT_OPTION);
                         Login_Dialog.this.dispose();
                         SwingUtilities.invokeLater(SSIS_Main_Display::new);
                    }
               }
          });
          layout_Constraints.gridx = 0;
          layout_Constraints.gridy = 3;
          layout_Constraints.gridwidth = 2;
          layout_Constraints.weighty = 0.25; // 25% free space
          this.add(login_button, layout_Constraints);
     }
}
