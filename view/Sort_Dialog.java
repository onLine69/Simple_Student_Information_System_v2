package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.WindowConstants;

import control.Sort_Process;
import model.Table_Manager;

/**
 * Display a dialog for sorting the data displayed in the tables.
 */
public class Sort_Dialog extends JDialog {
     private final int DIALOG_WIDTH = 250;
     private final int DIALOG_HEIGHT = 100;
     private final Dimension dialog_dimension = new Dimension(DIALOG_WIDTH, DIALOG_HEIGHT);

     private final GridBagLayout grid_bag_layout = new GridBagLayout();
     private final GridBagConstraints layout_Constraints = new GridBagConstraints();

     private JComboBox<String> sorting_options;
     private JComboBox<String> order;
     private JLabel sort_label = new JLabel("Sort By: ");
     private JLabel order_label = new JLabel("Order By: ");
     private JButton sort_button;

     public Sort_Dialog(JButton main, String table_name) {
          this.setTitle("Sort " + table_name);
          this.getContentPane().setPreferredSize(dialog_dimension);
          this.setResizable(false);
          this.setLayout(grid_bag_layout);
          this.pack();
          this.setLocationRelativeTo(main);
          this.setModalityType(DEFAULT_MODALITY_TYPE);
          this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

          if (table_name.equals("Students Table"))
               displayStudentSort(table_name);
          else
               displayCourseSort(table_name);
     }

     /**
      * Display the necessary components for sorting the students table.
      * 
      * @param table_name
      */
     private void displayStudentSort(String table_name) {
          JTable student_table = Table_Manager.getStudentTable();

          // prepare the sorting options
          String[] sort_options = new String[student_table.getColumnCount() - 2];
          sort_options[0] = "Full Name";
          for (int column_count = 3; column_count < student_table.getColumnCount(); column_count++)
               sort_options[column_count - 2] = student_table.getColumnName(column_count);

          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 0;
          layout_Constraints.gridy = 0;
          layout_Constraints.gridwidth = 1;
          this.add(sort_label, layout_Constraints);

          sorting_options = new JComboBox<>(sort_options);
          sorting_options.setToolTipText("Select which column be based on sorting.");
          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 1;
          layout_Constraints.gridy = 0;
          layout_Constraints.gridwidth = 1;
          this.add(sorting_options, layout_Constraints);

          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 0;
          layout_Constraints.gridy = 1;
          layout_Constraints.gridwidth = 1;
          this.add(order_label, layout_Constraints);

          order = new JComboBox<>();
          order.addItem("ASC");
          order.addItem("DESC");
          order.setToolTipText("\"ASC\" for Ascending Order and \"DESC\" for Descending Order.");
          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 1;
          layout_Constraints.gridy = 1;
          layout_Constraints.gridwidth = 1;
          this.add(order, layout_Constraints);

          sort_button = new JButton("Sort");
          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 0;
          layout_Constraints.gridy = 2;
          layout_Constraints.gridwidth = 2;
          sort_button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    new Sort_Process(table_name, sorting_options.getSelectedIndex(),
                              order.getSelectedItem().toString());
                    Sort_Dialog.this.dispose();
               }
          });
          this.add(sort_button, layout_Constraints);
     }

     /**
      * Display the necessary components for sorting the courses table.
      * 
      * @param table_name
      */
     private void displayCourseSort(String table_name) {
          JTable course_table = Table_Manager.getCourseTable();

          // prepare the sorting options
          String[] sort_options = new String[course_table.getColumnCount()];
          for (int column_count = 0; column_count < course_table.getColumnCount(); column_count++)
               sort_options[column_count] = course_table.getColumnName(column_count);

          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 0;
          layout_Constraints.gridy = 0;
          layout_Constraints.gridwidth = 1;
          this.add(sort_label, layout_Constraints);

          sorting_options = new JComboBox<>(sort_options);
          sorting_options.setToolTipText("Select which column be based on sorting.");
          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 1;
          layout_Constraints.gridy = 0;
          layout_Constraints.gridwidth = 1;
          this.add(sorting_options, layout_Constraints);

          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 0;
          layout_Constraints.gridy = 1;
          layout_Constraints.gridwidth = 1;
          this.add(order_label, layout_Constraints);

          order = new JComboBox<>();
          order.addItem("ASC");
          order.addItem("DESC");
          order.setToolTipText("\"ASC\" for Ascending Order and \"DESC\" for Descending Order.");
          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 1;
          layout_Constraints.gridy = 1;
          layout_Constraints.gridwidth = 1;
          this.add(order, layout_Constraints);

          sort_button = new JButton("Sort");
          layout_Constraints.fill = GridBagConstraints.HORIZONTAL;
          layout_Constraints.gridx = 0;
          layout_Constraints.gridy = 2;
          layout_Constraints.gridwidth = 2;
          sort_button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    new Sort_Process(table_name, sorting_options.getSelectedIndex(),
                              order.getSelectedItem().toString());
                    Sort_Dialog.this.dispose();
               }
          });
          this.add(sort_button, layout_Constraints);
     }
}
