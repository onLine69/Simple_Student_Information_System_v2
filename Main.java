import model.Data_Manager;
import view.Login_Dialog;

/**
 * Runs the SSIS app.
 *
 * @author Caine Ivan R. Bautista
 * @date March 28,2024
 */
public class Main {
     public static void main(String[] args) {
          try {
               // create an instance to connect to the database
               new Data_Manager();

               // start with the login dialog
               new Login_Dialog("Connect to Database.").setVisible(true);
          } catch (Exception e) {
               e.printStackTrace();
          }
     }
}