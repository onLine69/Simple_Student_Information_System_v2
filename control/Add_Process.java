package control;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.Data_Manager;

/**
 * Facilitates the addition of the rows in the tables and their relations.
 */
public class Add_Process {
    private JTable table;
    private JDialog add_dialog;

    public Add_Process(JTable table, JDialog add_dialog) {
        this.table = table;
        this.add_dialog = add_dialog;
    }

    /**
     * If the item to be added is in the student table.
     * 
     * @param surname_data
     * @param first_name_data
     * @param middle_name_data
     * @param ID_number_data
     * @param year_level_data
     * @param gender_data
     * @param course_code_data
     */
    public void studentAdd(String surname_data, String first_name_data, String middle_name_data, String ID_number_data,
            String year_level_data, String gender_data, String course_code_data) {
        try {
            // get the table model
            DefaultTableModel table_model = (DefaultTableModel) this.table.getModel();

            // create the query
            PreparedStatement insert_statement;
            if (course_code_data.equals("N/A"))
                insert_statement = Data_Manager.getConnection().prepareStatement(
                        "INSERT INTO students (last_name, first_name, middle_name, id_number, year_level, gender) VALUE (\""
                                + surname_data + "\",\"" + first_name_data + "\",\"" + middle_name_data + "\",\""
                                + ID_number_data + "\",\"" + year_level_data + "\",\"" + gender_data + "\")");
            else
                insert_statement = Data_Manager.getConnection()
                        .prepareStatement("INSERT INTO students VALUE (\"" + surname_data + "\",\"" + first_name_data
                                + "\",\"" + middle_name_data + "\",\"" + ID_number_data + "\",\"" + year_level_data
                                + "\",\"" + gender_data + "\",\"" + course_code_data + "\")");

            // execute the query
            insert_statement.execute();

            // add the new student to the JTable
            table_model.addRow(new Object[] { surname_data, first_name_data, middle_name_data, ID_number_data,
                    year_level_data, gender_data, course_code_data });

            // close the query
            insert_statement.close();

            // for confirmation
            JOptionPane.showMessageDialog(this.add_dialog, "Add Success.");
            this.add_dialog.dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this.add_dialog, "MySQL Error: " + e.getMessage(), "Add Data Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * If the item to be added is in the course table.
     * 
     * @param course_code_data
     * @param course_name_data
     */
    public void courseAdd(String course_code_data, String course_name_data) {
        try {
            // get the table model
            DefaultTableModel table_model = (DefaultTableModel) this.table.getModel();

            // create the query
            PreparedStatement insert_statement = Data_Manager.getConnection().prepareStatement(
                    "INSERT INTO courses VALUE (\"" + course_code_data + "\",\"" + course_name_data + "\")");

            // execute the query
            insert_statement.execute();

            // add the new course to the JTable
            table_model.addRow(new Object[] { course_code_data, course_name_data });

            // close the query
            insert_statement.close();

            // for confirmation
            JOptionPane.showMessageDialog(this.add_dialog, "Add Success.");
            this.add_dialog.dispose();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this.add_dialog, "MySQL Error: " + e.getMessage(), "Add Data Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
