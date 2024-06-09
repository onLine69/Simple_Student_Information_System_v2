package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import control.Edit_Process;
import control.Filter_Process;
import model.Table_Manager;

/**
 * Display and facilitates the editing row dialog and editing the data of the
 * selected rows.
 */
public class Edit_Dialog extends JDialog {
    private final int DIALOG_WIDTH = 400;
    private final int DIALOG_HEIGHT = 300;
    private final Dimension dialog_dimension = new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT);

    private final GridBagLayout grid_bag_layout = new GridBagLayout();
    private final GridBagConstraints layout_Constraints = new GridBagConstraints();

    // for student data
    private JLabel surname_label;
    private JTextField surname_data;
    private JLabel first_name_label;
    private JTextField first_name_data;
    private JLabel middle_name_label;
    private JTextField middle_name_data;
    private JLabel ID_number_label;
    private JTextField ID_number_data;
    private JLabel year_level_label;
    private JComboBox<String> year_level_data;
    private JLabel gender_label;
    private JTextField gender_data;
    private JLabel course_label;
    private JComboBox<String> course_data;

    // for course data
    private JLabel course_code_label;
    private JTextField course_code_data;
    private JLabel course_name_label;
    private JTextField course_name_data;

    private JButton edit_button;

    private Edit_Process edit_data;

    public Edit_Dialog(JTable table, JFrame main) {
        // setup the dialog
        this.setTitle("Edit Item:");
        this.getContentPane().setPreferredSize(dialog_dimension);
        this.setResizable(false);
        this.setLayout(grid_bag_layout);
        this.pack();
        this.setLocationRelativeTo(main);
        this.setModalityType(DEFAULT_MODALITY_TYPE);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        edit_data = new Edit_Process(table, this);

        /*
         * If the table selected is the student table, else use the data from the course
         * table.
         */
        if (table.equals(Table_Manager.getStudentTable()))
            displayEditStudent(Table_Manager.getStudentTable());
        else
            displayEditCourse(Table_Manager.getCourseTable());
    }

    /**
     * JDialog for editing courses.
     * 
     * @param student_table
     */
    private void displayEditStudent(JTable student_table) {
        // retrieving the data from each cell of the row
        String[] selected_row_data = new String[student_table.getColumnCount()];

        // get the selected row and the data in its cells
        int table_row_selected = student_table.getSelectedRow();
        for (int column = 0; column < student_table.getColumnCount(); column++)
            selected_row_data[column] = student_table.getValueAt(table_row_selected, column).toString();

        // Arranging the displays
        surname_label = new JLabel("Surname: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 0;
        this.add(surname_label, layout_Constraints);

        surname_data = new JTextField();
        surname_data.setPreferredSize(new Dimension(200, 30));
        surname_data.setText(selected_row_data[0]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 0;
        this.add(surname_data, layout_Constraints);

        first_name_label = new JLabel("First Name: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 1;
        this.add(first_name_label, layout_Constraints);

        first_name_data = new JTextField();
        first_name_data.setPreferredSize(new Dimension(200, 30));
        first_name_data.setText(selected_row_data[1]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 1;
        this.add(first_name_data, layout_Constraints);

        middle_name_label = new JLabel("Middle Name: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 2;
        this.add(middle_name_label, layout_Constraints);

        middle_name_data = new JTextField();
        middle_name_data.setPreferredSize(new Dimension(200, 30));
        middle_name_data.setText(selected_row_data[2]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 2;
        this.add(middle_name_data, layout_Constraints);

        ID_number_label = new JLabel("ID Number: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 3;
        this.add(ID_number_label, layout_Constraints);

        ID_number_data = new JTextField();
        ID_number_data.setPreferredSize(new Dimension(200, 30));
        ID_number_data.setText(selected_row_data[3]);
        ID_number_data.addFocusListener(new FocusListener() {
            // This is to add a placeholder inside the textfield.
            @Override
            public void focusGained(FocusEvent e) {
                if (ID_number_data.getText().equals("YYYY-MMMM"))
                    ID_number_data.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (ID_number_data.getText().isEmpty())
                    ID_number_data.setText("YYYY-MMMM");
            }
        });
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 3;
        this.add(ID_number_data, layout_Constraints);

        year_level_label = new JLabel("Year Level: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 4;
        this.add(year_level_label, layout_Constraints);

        year_level_data = new JComboBox<>(
                new String[] { "1st year", "2nd year", "3rd year", "4th year", "5th year", "6th year", "More..." });
        year_level_data.setSelectedItem(selected_row_data[4]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 4;
        this.add(year_level_data, layout_Constraints);

        gender_label = new JLabel("Gender: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 5;
        this.add(gender_label, layout_Constraints);

        gender_data = new JTextField();
        gender_data.setPreferredSize(new Dimension(200, 30));
        gender_data.setText(selected_row_data[5]);
        layout_Constraints.gridx = 1;
        layout_Constraints.gridy = 5;
        this.add(gender_data, layout_Constraints);

        course_label = new JLabel("Course Code: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 6;
        this.add(course_label, layout_Constraints);

        // list the courses available in a readable way
        JTable course_table = Table_Manager.getCourseTable();
        int course_count = course_table.getRowCount();
        String[] courses_listed = new String[course_count + 1];
        courses_listed[0] = "N/A-Not Enrolled";
        for (int row_count = 0; row_count < course_count; row_count++)
            courses_listed[row_count + 1] = course_table.getValueAt(row_count, 0) + "-"
                    + course_table.getValueAt(row_count, 1);

        Arrays.sort(courses_listed);

        course_data = new JComboBox<>();
        for (String course : courses_listed) {
            course_data.addItem(course);
            if (course.split("-")[0].equals(selected_row_data[6]))
                course_data.setSelectedItem(course);
        }
        course_data.setPreferredSize(new Dimension(300, 30));
        course_data.addActionListener(e -> {
            course_data.setToolTipText(course_data.getSelectedItem().toString());
        });
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 7;
        layout_Constraints.gridwidth = 2;
        this.add(course_data, layout_Constraints);

        // arranging the button and setting its functionality
        edit_button = new JButton("Edit Item");
        edit_button.setFocusable(false);
        edit_button.setToolTipText("Add the edited item to the table.");
        edit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if there is at least one field empty
                if (surname_data.getText().isEmpty() || first_name_data.getText().isEmpty() ||
                        middle_name_data.getText().isEmpty() || ID_number_data.getText().isEmpty() ||
                        ID_number_data.getText().equals("YYYY-MMMM") || gender_data.getText().isEmpty())
                    JOptionPane.showMessageDialog(Edit_Dialog.this, "Fill all fields.", "Missing Information",
                            JOptionPane.WARNING_MESSAGE);
                // check if there are changes
                else if (surname_data.getText().toString()
                        .equals(student_table.getValueAt(table_row_selected, 0).toString())
                        && first_name_data.getText().toString()
                                .equals(student_table.getValueAt(table_row_selected, 1).toString())
                        && middle_name_data.getText().toString()
                                .equals(student_table.getValueAt(table_row_selected, 2).toString())
                        && ID_number_data.getText().toString()
                                .equals(student_table.getValueAt(table_row_selected, 3).toString())
                        && year_level_data.getSelectedItem().toString()
                                .equals(student_table.getValueAt(table_row_selected, 4).toString())
                        && gender_data.getText().toString()
                                .equals(student_table.getValueAt(table_row_selected, 5).toString())
                        && course_data.getSelectedItem().toString().split("-")[0]
                                .equals(student_table.getValueAt(table_row_selected, 6).toString())) {
                    JOptionPane.showMessageDialog(Edit_Dialog.this, "No changes were made.", "Input the same",
                            JOptionPane.INFORMATION_MESSAGE);
                    Edit_Dialog.this.dispose();
                } else {
                    // update the new data to the table then close the dialog
                    if (isIDValid(ID_number_data.getText().toString()))
                        edit_data.studentEdit(table_row_selected, surname_data.getText().toString(),
                                first_name_data.getText().toString(), middle_name_data.getText().toString(),
                                ID_number_data.getText().toString(), year_level_data.getSelectedItem().toString(),
                                gender_data.getText().toString(),
                                course_data.getSelectedItem().toString().split("-")[0]);
                }
            }
        });
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 9;
        layout_Constraints.gridwidth = 2;
        this.add(edit_button, layout_Constraints);
    }

    /**
     * Check if the ID is in the correct format.
     * 
     * @param ID_number
     * @return
     */
    private boolean isIDValid(String ID_number) {
        if (ID_number.length() != 9) {
            JOptionPane.showMessageDialog(Edit_Dialog.this, "Should be YYYY-MMMM format. Length should be 9",
                    "Invalid ID Format", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            // Define the pattern using regular expression
            String pattern = "\\d{4}-\\d{4}";

            // Compile the pattern
            Pattern p = Pattern.compile(pattern);

            // Match the input string with the pattern
            Matcher m = p.matcher(ID_number);

            if (!m.matches()) {
                JOptionPane.showMessageDialog(Edit_Dialog.this, "Should be YYYY-MMMM format. With only numbers.",
                        "Invalid ID Format", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }
    }

    /**
     * JDialog for editing students.
     * 
     * @param course_table
     */
    private void displayEditCourse(JTable course_table) {
        // setup the table model to edit the rows
        DefaultTableModel course_table_model = (DefaultTableModel) course_table.getModel();

        // retrieving the data from each cell of the row
        String[] selected_row_data = new String[course_table_model.getColumnCount()];

        // get the selected row and the data in its cells
        int table_row_selected = course_table.getSelectedRow();
        for (int column = 0; column < course_table.getColumnCount(); column++)
            selected_row_data[column] = course_table.getValueAt(table_row_selected, column).toString();

        // Arranging the displays
        course_code_label = new JLabel("Course Code: ");
        layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 0;
        layout_Constraints.gridwidth = 2;
        this.add(course_code_label, layout_Constraints);

        course_code_data = new JTextField();
        course_code_data.setPreferredSize(new Dimension(200, 30));
        course_code_data.setText(selected_row_data[0]);
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 1;
        layout_Constraints.gridwidth = 2;
        this.add(course_code_data, layout_Constraints);

        course_name_label = new JLabel("Course Name: ");
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 2;
        layout_Constraints.gridwidth = 2;
        this.add(course_name_label, layout_Constraints);

        course_name_data = new JTextField();
        course_name_data.setPreferredSize(new Dimension(200, 30));
        course_name_data.setText(selected_row_data[1]);
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 3;
        layout_Constraints.gridwidth = 2;
        this.add(course_name_data, layout_Constraints);

        // arranging the button and setting its functionality
        edit_button = new JButton("Edit Item");
        layout_Constraints.gridx = 0;
        layout_Constraints.gridy = 9;
        layout_Constraints.gridwidth = 2;
        edit_button.setFocusable(false);
        edit_button.setToolTipText("Add the edited item to the table.");
        edit_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // check if there is at least one field empty
                if (course_code_data.getText().isEmpty() || course_name_data.getText().isEmpty())
                    JOptionPane.showMessageDialog(Edit_Dialog.this, "Fill all fields.", "Missing Information",
                            JOptionPane.WARNING_MESSAGE);
                // check if there are changes
                else if (course_code_data.getText().toString()
                        .equals(course_table.getValueAt(table_row_selected, 0).toString())
                        && course_name_data.getText().toString()
                                .equals(course_table.getValueAt(table_row_selected, 1).toString())) {
                    JOptionPane.showMessageDialog(Edit_Dialog.this, "No changes were made.", "Input the same",
                            JOptionPane.INFORMATION_MESSAGE);
                    Edit_Dialog.this.dispose();
                }
                // add the new data to the table then close the dialog
                else {
                    selected_row_data[0] = course_code_data.getText().toString();
                    selected_row_data[1] = course_name_data.getText().toString();
                    if (isCodeUnique(course_table, selected_row_data[0], table_row_selected))
                        edit_data.courseEdit(table_row_selected, selected_row_data[0], selected_row_data[1]);
                }
            }
        });
        this.add(edit_button, layout_Constraints);
    }

    /**
     * Check if the course code is unique.
     * 
     * @param course_table
     * @param course_code_pattern
     * @param table_row_selected
     * @return
     */
    private boolean isCodeUnique(JTable course_table, String course_code_pattern, int table_row_selected) {
        String selected_course_code = course_table.getValueAt(table_row_selected, 0).toString();

        // filter the course_code
        Filter_Process.rowFilter(course_table, course_code_pattern, 0);

        if (course_table.getRowCount() > 0) {
            // just get the first row
            String remaining_course_code = course_table.getValueAt(0, 0).toString();

            // compare if the selected data is the same with the remaining data
            if (!remaining_course_code.equals(selected_course_code)) {
                JOptionPane.showMessageDialog(Edit_Dialog.this,
                        "Course Name: " + course_code_pattern + "\nalready exist.",
                        "Duplication of Entry", JOptionPane.ERROR_MESSAGE);
                Filter_Process.cancelFilter(course_table);
                course_table.setRowSelectionInterval(table_row_selected, table_row_selected);
                return false;
            }
        }

        Filter_Process.cancelFilter(course_table);
        course_table.setRowSelectionInterval(table_row_selected, table_row_selected);
        return true;
    }
}
