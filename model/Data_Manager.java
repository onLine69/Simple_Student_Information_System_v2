package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import control.Sort_Process;

/**
 * Facilitates the connection and data fetching/storing from the database.
 */
public class Data_Manager {
     private static Connection connect;
     private static DefaultTableModel courses_table_model;
     private static DefaultTableModel students_table_model;
     private static String[] course_column_names;
     private static String[] student_column_names;
     private static boolean allow_access = false;

     public Data_Manager() {
     }

     /**
      * Create a connection to the database.
      * 
      * @param username
      * @param password
      */
     public static void createConnection(String username, String password) {
          try {
               // change for new connection
               String sql_name = "mysql";
               String port = "127.0.0.1:3306";
               String connection_name = "ssis_database";
               connect = DriverManager.getConnection("jdbc:" + sql_name + "://" + port + "/" + connection_name,
                         username, password);
               getCoursesData(connect);
               getStudentsData(connect);

               Data_Manager.allow_access = true;
          } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "MySQL Error: " + e.getMessage(), "Connection Error",
                         JOptionPane.ERROR_MESSAGE);
          }
     }

     /**
      * Share the access. For the Login.
      * 
      * @return allow_access
      */
     public static boolean getAccess() {
          return Data_Manager.allow_access;
     }

     /**
      * Share the connection to the database.
      * 
      * @return
      */
     public static Connection getConnection() {
          return Data_Manager.connect;
     }

     /**
      * Process the initial fetch of the student data from the database.
      * 
      * @param connect
      */
     private static void getStudentsData(Connection connect) {
          // get the column names
          student_column_names = new String[] { "Last Name", "First Name", "Middle Name", "ID Number",
                    "Year Level", "Gender", "Course Code" };

          // setup the table model
          students_table_model = new DefaultTableModel(0, 0) {
               // prevent editing directly in the cell table
               @Override
               public boolean isCellEditable(int row, int column) {
                    return false;
               }
          };
          students_table_model.setColumnIdentifiers(student_column_names);

          // use sort to avoid code duplication for fetching data from the database
          new Sort_Process("Students Table", 0, "ASC");
     }

     /**
      * Share the fetched data for the students.
      * 
      * @return students_table_model
      */
     public static DefaultTableModel getStudentsModel() {
          return Data_Manager.students_table_model;
     }

     /**
      * Process the initial fetch of the courses data from the database.
      * 
      * @param connect
      */
     private static void getCoursesData(Connection connect) {
          // get the column names
          course_column_names = new String[] { "Course Code", "Course Name" };

          // setup the table and its model
          courses_table_model = new DefaultTableModel(0, 0) {
               // prevent editing directly in the cell table
               @Override
               public boolean isCellEditable(int row, int column) {
                    return false;
               }
          };
          courses_table_model.setColumnIdentifiers(course_column_names);

          // use sort to avoid code duplication for fetching data from the database
          new Sort_Process("Courses Table", 0, "ASC");
     }

     /**
      * Share the fetched data for the students.
      * 
      * @return courses_table_model
      */
     public static DefaultTableModel getCoursesModel() {
          return Data_Manager.courses_table_model;
     }
}
