package control;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Data_Manager;
import model.Table_Manager;

/**
 * Facilitates the deletion of the rows in the tables and their relations.
 */
public class Delete_Process {
    public Delete_Process(JTable table, JFrame main) {
        /*
         * If the table selected is the student table, else use the data from the course
         * table.
         */
        if (table.equals(Table_Manager.getStudentTable()))
            studentDelete(main);
        else
            courseDelete(main);
    }

    /**
     * If the row to be deleted is in the student table.
     * 
     * @param main
     */
    private void studentDelete(JFrame main) {
        // get the table of the student data and its model
        JTable students_table = Table_Manager.getStudentTable();
        DefaultTableModel students_table_model = (DefaultTableModel) students_table.getModel();

        // get the selected row
        int table_row_selected = students_table.getSelectedRow();
        String deleted_student_ID = students_table.getValueAt(table_row_selected, 3).toString();
        String deleted_student_course_code = students_table.getValueAt(table_row_selected, 6).toString();

        // ask for confirmation
        int chosen;
        if (!deleted_student_course_code.equals("N/A"))
            chosen = JOptionPane.showConfirmDialog(main,
                    "Currently enrolled in: " + deleted_student_course_code + ".\nDelete?", "Delete Confirmation",
                    JOptionPane.OK_CANCEL_OPTION);
        else
            chosen = JOptionPane.showConfirmDialog(main,
                    "Currently not enrolled .\nDelete?", "Delete Confirmation",
                    JOptionPane.OK_CANCEL_OPTION);

        // if the chosen option is not okay, cancel the action
        if (chosen != JOptionPane.OK_OPTION)
            return;

        try {
            // create the query for removing the student from the database
            PreparedStatement delete_statement = Data_Manager.getConnection()
                    .prepareStatement("DELETE FROM students WHERE id_number = \"" + deleted_student_ID + "\"");

            // execute and close the query
            delete_statement.execute();
            delete_statement.close();

            // remove the row from the table
            students_table_model.removeRow(table_row_selected);

            // for confirmation
            JOptionPane.showConfirmDialog(main, "Student " + deleted_student_ID + " removed.", "Student Deleted",
                    JOptionPane.CLOSED_OPTION);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(main, "MySQL Error: " + e.getMessage(), "Delete Data Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * If the row to be deleted is in the course table.
     * 
     * @param main
     */
    private void courseDelete(JFrame main) {
        try {
            // get the table of the course data and its model
            JTable courses_table = Table_Manager.getCourseTable();
            DefaultTableModel courses_table_model = (DefaultTableModel) courses_table.getModel();

            // get the selected row
            int table_row_selected = courses_table.getSelectedRow();
            String deleted_course_code = courses_table.getValueAt(table_row_selected, 0).toString();

            // create the query
            PreparedStatement count_query = Data_Manager.getConnection().prepareStatement(
                    "SELECT COUNT(course_code) FROM students WHERE course_code = \"" + deleted_course_code + "\";");

            // execute the query
            ResultSet rs = count_query.executeQuery();
            rs.next();

            // ask for confirmation
            int chosen = JOptionPane.showConfirmDialog(main,
                    "Currently enrolled: " + rs.getLong("COUNT(course_code)") + " \nDelete?", "Delete Confirmation",
                    JOptionPane.OK_CANCEL_OPTION);

            // if the chosen option is not okay, cancel the action
            if (chosen != JOptionPane.OK_OPTION)
                return;

            // close the count query
            count_query.close();

            // create a query for removing the course form the database
            PreparedStatement delete_statement = Data_Manager.getConnection()
                    .prepareStatement("DELETE FROM courses WHERE course_code = \"" + deleted_course_code + "\"");

            // execute the query
            delete_statement.execute();

            // remove the row
            courses_table_model.removeRow(table_row_selected);

            // update the course code of the students enrolled in the deleted course
            JTable students_table = Table_Manager.getStudentTable();
            for (int row_count = 0; row_count < students_table.getRowCount(); row_count++)
                if (students_table.getValueAt(row_count, students_table.getColumnCount() - 1)
                        .equals(deleted_course_code))
                    students_table.setValueAt("N/A", row_count, students_table.getColumnCount() - 1);

            delete_statement.close();

            // for confirmation
            JOptionPane.showConfirmDialog(main, "Course " + deleted_course_code + " removed.", "Course Deleted",
                    JOptionPane.CLOSED_OPTION);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(main, "MySQL Error: " + e.getMessage(), "Delete Data Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
