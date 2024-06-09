package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import model.Data_Manager;

/**
 * Facilitates the sorting process of the tables.
 */
public class Sort_Process {
     private static Connection connect = Data_Manager.getConnection(); // get the connection to the database

     public Sort_Process(String table_name, int sort_by_index, String order_by) {
          if (table_name.equals("Students Table"))
               sortStudentTable(sort_by_index, order_by);
          else
               sortCourseTable(sort_by_index, order_by);
     }

     /**
      * Sort the students table.
      * 
      * @param sort_by_index
      * @param order_by
      */
     private void sortStudentTable(int sort_by_index, String order_by) {
          // the columns in the database
          String[] students_column = new String[] { "last_name", "first_name", "middle_name", "id_number", "year_level",
                    "gender", "course_code" };

          // get the table model and erase its contents
          DefaultTableModel students_table_model = Data_Manager.getStudentsModel();
          students_table_model.setRowCount(0);

          try {
               // SELECT data in order based on the column
               String sort_query;
               if (sort_by_index == 0)
                    sort_query = "SELECT * FROM students ORDER BY " + students_column[0] + " " + order_by + ", "
                              + students_column[1] + " " + order_by + ", " + students_column[2] + " " + order_by + ";";
               else
                    sort_query = "SELECT * FROM students ORDER BY " + students_column[sort_by_index + 2] + " "
                              + order_by + ", " + students_column[0] + " ASC, " + students_column[1] + " ASC, "
                              + students_column[2] + " ASC;";

               // Create and Execute the query
               PreparedStatement get_sorted_students = connect.prepareStatement(sort_query);
               ResultSet rs = get_sorted_students.executeQuery();

               // fetch the table from the database and copy to the JTable
               while (rs.next()) {
                    String last_name = rs.getString(students_column[0]);
                    String first_name = rs.getString(students_column[1]);
                    String middle_name = rs.getString(students_column[2]);
                    String id_number = rs.getString(students_column[3]);
                    String year_level = rs.getString(students_column[4]);
                    String gender = rs.getString(students_column[5]);
                    String course_code;

                    if (rs.getString("course_code") == null)
                         course_code = "N/A";
                    else
                         course_code = rs.getString(students_column[6]);

                    students_table_model.addRow(new Object[] { last_name, first_name, middle_name, id_number,
                              year_level, gender, course_code });
               }

               // close the query
               get_sorted_students.close();
          } catch (SQLException e) {
               e.printStackTrace();
          }
     }

     /**
      * Sort the courses table.
      * 
      * @param sort_by_index
      * @param order_by
      */
     private void sortCourseTable(int sort_by_index, String order_by) {
          // the columns in the database
          String[] courses_column = new String[] { "course_code", "course_name" };

          // get the table and erase its contents
          DefaultTableModel courses_table_model = Data_Manager.getCoursesModel();
          courses_table_model.setRowCount(0);

          try {
               // SELECT data in order based on the column
               String sort_query = "SELECT * FROM courses ORDER BY "
                         + courses_column[sort_by_index] + " " + order_by + ";";

               // Create and Execute the query
               PreparedStatement get_sorted_courses = connect.prepareStatement(sort_query);
               ResultSet rs = get_sorted_courses.executeQuery();

               // fetch the table from the database and copy to the JTable
               while (rs.next()) {
                    String course_code = rs.getString(courses_column[0]);
                    String course_name = rs.getString(courses_column[1]);

                    courses_table_model.addRow(new Object[] { course_code, course_name });
               }

               // close the query
               get_sorted_courses.close();

          } catch (SQLException e) {
               e.printStackTrace();
          }
     }
}
