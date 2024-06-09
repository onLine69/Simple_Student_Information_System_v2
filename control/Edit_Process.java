package control;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import model.Data_Manager;
import model.Table_Manager;

/**
 * Facilitates the editing of the rows in the tables and their relations.
 */
public class Edit_Process {
    private JTable table;
    private JDialog edit_dialog;

    public Edit_Process(JTable table, JDialog edit_dialog) {
        this.table = table;
        this.edit_dialog = edit_dialog;
    }

    /**
     * If the item to be edited is in the student table.
     * 
     * @param table_row_selected
     * @param new_last_name
     * @param new_first_name
     * @param new_middle_name
     * @param new_ID_number
     * @param new_year_level
     * @param new_gender
     * @param new_course_code
     */
    public void studentEdit(int table_row_selected, String new_last_name, String new_first_name, String new_middle_name,
            String new_ID_number, String new_year_level, String new_gender, String new_course_code) {
        try {
            // get the table
            JTable student_table = this.table;
            String old_ID_number = student_table.getValueAt(table_row_selected, 3).toString();
            // create the query for updating values
            String query = "UPDATE students SET last_name = ?, first_name = ?, middle_name = ?, id_number = ?, year_level = ?, gender = ?, course_code = ? WHERE id_number = ?;";

            // prepare the query, add the necessary parameters
            PreparedStatement edit_statement = Data_Manager.getConnection().prepareStatement(query);
            edit_statement.setString(1, new_last_name);
            edit_statement.setString(2, new_first_name);
            edit_statement.setString(3, new_middle_name);
            edit_statement.setString(4, new_ID_number);
            edit_statement.setString(5, new_year_level);
            edit_statement.setString(6, new_gender);
            if (new_course_code.equals("N/A"))
                edit_statement.setNull(7, Types.NULL);
            else
                edit_statement.setString(7, new_course_code);
            edit_statement.setString(8, old_ID_number);

            // execute the query
            edit_statement.executeUpdate();

            // change the values in the row of the edited course
            student_table.setValueAt(new_last_name, table_row_selected, 0);
            student_table.setValueAt(new_first_name, table_row_selected, 1);
            student_table.setValueAt(new_middle_name, table_row_selected, 2);
            student_table.setValueAt(new_ID_number, table_row_selected, 3);
            student_table.setValueAt(new_year_level, table_row_selected, 4);
            student_table.setValueAt(new_gender, table_row_selected, 5);
            student_table.setValueAt(new_course_code, table_row_selected, 6);

            // close the execution
            edit_statement.close();

            // for confirmation
            JOptionPane.showMessageDialog(this.edit_dialog, "Edit Success.");
            this.edit_dialog.dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this.edit_dialog, "MySQL Error: " + e.getMessage(), "Edit Data Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * If the item to be edited is in the course table.
     * 
     * @param table_row_selected
     * @param new_course_code
     * @param new_course_name
     */
    public void courseEdit(int table_row_selected, String new_course_code, String new_course_name) {
        try {
            // get the table
            JTable course_table = this.table;
            String old_course_code = course_table.getValueAt(table_row_selected, 0).toString();

            PreparedStatement edit_statement;
            // if the course code was changed
            if (!new_course_code.equals(course_table.getValueAt(table_row_selected, 0))) {
                String fkc_name = "students_courses_fk"; // the foreign key constraint name

                // remove the constraint first
                String remove_constraint = "ALTER TABLE students DROP CONSTRAINT " + fkc_name + ";";
                PreparedStatement remove_fkc = Data_Manager.getConnection().prepareStatement(remove_constraint);
                remove_fkc.execute();
                remove_fkc.close();

                // update the courses table in the database
                String update_courses = "UPDATE courses SET course_code = ?, course_name = ? WHERE course_code = ?;";
                edit_statement = Data_Manager.getConnection().prepareStatement(update_courses);
                edit_statement.setString(1, new_course_code);
                edit_statement.setString(2, new_course_name);
                edit_statement.setString(3, old_course_code);
                edit_statement.executeUpdate();
                edit_statement.close();

                // update the courses JTable
                course_table.setValueAt(new_course_code, table_row_selected, 0);
                course_table.setValueAt(new_course_name, table_row_selected, 1);

                // update the students table in the database
                String update_students = "UPDATE students SET course_code = ? WHERE course_code = ?;";
                edit_statement = Data_Manager.getConnection().prepareStatement(update_students);
                edit_statement.setString(1, new_course_code);
                edit_statement.setString(2, old_course_code);
                edit_statement.executeUpdate();
                edit_statement.close();

                // update the students JTable
                JTable student_table = Table_Manager.getStudentTable();
                for (int row_count = 0; row_count < student_table.getRowCount(); row_count++)
                    if (student_table.getValueAt(row_count, student_table.getColumnCount() - 1).equals(old_course_code))
                        student_table.setValueAt(new_course_code, row_count, student_table.getColumnCount() - 1);

                // add back the constraint
                String add_constraint = "ALTER TABLE students ADD CONSTRAINT " + fkc_name
                        + " FOREIGN KEY(course_code) REFERENCES courses(course_code) ON DELETE SET NULL;";
                PreparedStatement add_fkc = Data_Manager.getConnection().prepareStatement(add_constraint);
                add_fkc.execute();
                add_fkc.close();
            } else {
                // if the course name is the only changed value, update the database and JTable
                String query = "UPDATE courses SET course_name = ? WHERE course_code = ?;";
                edit_statement = Data_Manager.getConnection().prepareStatement(query);
                edit_statement.setString(1, new_course_name);
                edit_statement.setString(2, old_course_code);
                edit_statement.executeUpdate();
                edit_statement.close();

                course_table.setValueAt(new_course_name, table_row_selected, 1);
            }

            // for confirmation
            JOptionPane.showMessageDialog(this.edit_dialog, "Edit Success.");
            this.edit_dialog.dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this.edit_dialog, "MySQL Error: " + e.getMessage(), "Edit Data Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
