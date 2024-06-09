package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import control.Delete_Process;
import control.Filter_Process;
import model.Data_Manager;
import model.Table_Manager;

public class SSIS_Main_Display extends JFrame {
     // frame constants
     private static final int FRAME_HEIGHT = 750;
     private static final int FRAME_WIDTH = 1250;
     private final Dimension MAIN_DIMENSION = new Dimension(FRAME_WIDTH, FRAME_HEIGHT);
     private static final int PADDING_WIDTH = 20;
     private static final int PADDING_HEIGHT = 10;

     // panels for the display
     private JPanel banner_panel;
     private JPanel filter_panel;
     private JPanel option_panel;
     private JPanel table_panel;

     // filtering data
     private JComboBox<String> column_names;
     private JTextField search_input;
     private JButton search_button;

     // sorting data
     private JButton sort_button;

     // functional buttons
     private JButton add_button;
     private JButton edit_button;
     private JButton delete_button;
     private JButton students_button;
     private JButton courses_button;

     // data tables
     private JTable student_table;
     private JTable course_table;
     private static JTable display_table;

     public SSIS_Main_Display() {
          // basic layouts for the frame display
          this.setTitle("Simple Student Information System (SSIS)");
          this.getContentPane().setPreferredSize(MAIN_DIMENSION);
          this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
          this.setLayout(null);
          this.pack();
          this.setLocationRelativeTo(null);
          this.setResizable(false);
          this.setVisible(true);

          /*
           * Create a new login dialog incase the user wants to use this again.
           */
          WindowListener exitListener = new WindowAdapter() {
               @Override
               public void windowClosing(WindowEvent e) {
                    try {
                         // close the connection
                         Data_Manager.getConnection().close();
                         System.exit(0);
                    } catch (SQLException ex) {
                         // incase an error on closing the connection
                         JOptionPane.showMessageDialog(SSIS_Main_Display.this,
                                   "Failed to Close the Connection.",
                                   "Disconnection Error", JOptionPane.ERROR_MESSAGE);
                    }
               }
          };
          this.addWindowListener(exitListener);

          // setup the tables
          new Table_Manager(this);
          display_table = student_table = Table_Manager.getStudentTable();
          course_table = Table_Manager.getCourseTable();

          bannerAreaDisplay();
          optionAreaDisplay();
          filterAreaDisplay();
          tableAreaDisplay();
     }

     /**
      * Display the top-most part of the frame. Shows the "SSIS" and the buttons to
      * control what table to display.
      */
     private void bannerAreaDisplay() {
          // setup the panel
          banner_panel = new JPanel();
          banner_panel.setBounds(0, 0, this.getWidth(), 75);
          banner_panel.setBackground(this.getBackground());
          banner_panel.setLayout(null);

          // setup the logo/name
          JLabel SSIS_name = new JLabel("SSIS (/*益*)/彡┻━┻");
          SSIS_name.setBounds(PADDING_WIDTH * 5, PADDING_HEIGHT, 400, 50);
          SSIS_name.setFont(new Font("Courier", Font.BOLD, 32));
          SSIS_name.setHorizontalAlignment(SwingConstants.CENTER);
          SSIS_name.setToolTipText("Created by: Caine Ivan R. Bautista (2022-0378)");
          banner_panel.add(SSIS_name);

          // setup the button to display the student data table
          students_button = new JButton("Students");
          students_button.setBounds(850, PADDING_HEIGHT * 2, 120, 30);
          students_button.setFont(new Font("Times New Roman", Font.PLAIN, 22));
          students_button.setToolTipText("Display the table listing the students registered.");
          students_button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    // to prevent confusion to what table currently displayed
                    students_button.setEnabled(false);
                    courses_button.setEnabled(true);

                    display_table.getSelectionModel().clearSelection(); // clear the previous table selection
                    Filter_Process.cancelFilter(display_table); // cancel the filter
                    display_table = student_table; // change the table to be displayed
                    refreshTable(); // refresh the display table
                    refreshFilterArea();
               }
          });
          banner_panel.add(students_button);

          // setup the button to display the course data table
          courses_button = new JButton("Courses");
          courses_button.setBounds(1000, PADDING_HEIGHT * 2, 120, 30);
          courses_button.setFont(new Font("Times New Roman", Font.PLAIN, 22));
          courses_button.setToolTipText("Display the table listing the courses registered.");
          courses_button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    // to prevent confusion to what table currently displayed
                    courses_button.setEnabled(false);
                    students_button.setEnabled(true);

                    display_table.getSelectionModel().clearSelection(); // clear the previous table selection
                    Filter_Process.cancelFilter(display_table); // cancel the filter
                    display_table = course_table; // change the table to be displayed
                    refreshTable(); // refresh the display table
                    refreshFilterArea();
               }
          });
          banner_panel.add(courses_button);

          this.add(banner_panel);
     }

     /**
      * Display the second part of the frame. Shows the buttons for the functionality
      * of the app. Add, Edit, Delete, Save, and Display the information on the
      * table.
      */
     private void optionAreaDisplay() {
          // setup the panel
          option_panel = new JPanel();
          option_panel.setBounds(650, banner_panel.getHeight(), this.getWidth() - 650, 100);
          option_panel.setBackground(this.getBackground());
          option_panel.setLayout(null);

          // setup and add functionality of the add button
          add_button = new JButton("Add Item");
          add_button.setBounds(PADDING_WIDTH * 2 + 120 * 1, PADDING_HEIGHT * 3, 120, 30);
          add_button.setFont(new Font("Times New Roman", Font.PLAIN, 18));
          add_button.setToolTipText("Add item to the table.");
          add_button.addActionListener(e -> {
               new Add_Dialog(display_table, SSIS_Main_Display.this).setVisible(true);
          });
          option_panel.add(add_button);

          // setup and add functionality of the edit button
          edit_button = new JButton("Edit Item");
          edit_button.setBounds(PADDING_WIDTH * 3 + 120 * 2, PADDING_HEIGHT * 3, 120, 30);
          edit_button.setFont(new Font("Times New Roman", Font.PLAIN, 18));
          edit_button.setToolTipText("Edit the selected row in the table.");
          edit_button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    if (display_table.getSelectedRow() < 0)
                         JOptionPane.showMessageDialog(SSIS_Main_Display.this, "Please select a row.",
                                   "Edit Data Error", JOptionPane.ERROR_MESSAGE);
                    // display the edit_dialog
                    else
                         new Edit_Dialog(display_table, SSIS_Main_Display.this).setVisible(true);
               }
          });
          option_panel.add(edit_button);

          // setup and add functionality of the delete button
          delete_button = new JButton("Delete Item");
          delete_button.setBounds(PADDING_WIDTH * 4 + 120 * 3, PADDING_HEIGHT * 3, 120, 30);
          delete_button.setFont(new Font("Times New Roman", Font.PLAIN, 18));
          delete_button.setToolTipText("Delete the selected row in the table.");
          delete_button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    if (display_table.getSelectedRow() < 0)
                         JOptionPane.showMessageDialog(SSIS_Main_Display.this, "Please select a row.",
                                   "Delete Data Error", JOptionPane.ERROR_MESSAGE);
                    else
                         new Delete_Process(display_table, SSIS_Main_Display.this); // facilitate the deletion process
               }
          });
          option_panel.add(delete_button);

          this.add(option_panel);
     }

     /**
      * Display the third part of the display. Shows the filtering options to search
      * the data in the table.
      */
     private void filterAreaDisplay() {
          filter_panel = new JPanel();
          filter_panel.setBounds(0, banner_panel.getHeight(), 650, 100);
          filter_panel.setBackground(this.getBackground());
          filter_panel.setLayout(null);

          refreshFilterArea();

          this.add(filter_panel);
     }

     /**
      * Refresh the filtering area.
      */
     private void refreshFilterArea() {
          // get the columns of the displayed table
          String[] columns = new String[display_table.getColumnCount() + 1];
          columns[0] = "Select Column...";
          for (int column_count = 0; column_count < display_table.getColumnCount(); column_count++)
               columns[column_count + 1] = display_table.getColumnName(column_count);

          column_names = new JComboBox<>(columns);
          column_names.setBounds(PADDING_WIDTH * 1, PADDING_HEIGHT * 3, 120, 30);
          column_names.addActionListener(e -> {
               if (column_names.getSelectedIndex() == 0)
                    Filter_Process.cancelFilter(display_table);
          });

          // setup the search field
          search_input = new JTextField("Search Here");
          search_input.setBounds(PADDING_WIDTH * 2 + 110, PADDING_HEIGHT * 3, 350, 30);
          search_input.addFocusListener(new FocusListener() {
               // This is to add a placeholder inside the textfield.
               @Override
               public void focusGained(FocusEvent e) {
                    if (search_input.getText().equals("Search Here"))
                         search_input.setText("");
               }

               @Override
               public void focusLost(FocusEvent e) {
                    if (search_input.getText().isEmpty())
                         search_input.setText("Search Here");
               }
          });

          // setup the search button
          search_button = new JButton("Search");
          search_button.setBounds(PADDING_WIDTH * 3 + 460, PADDING_HEIGHT * 3, 100, 30);
          search_button.setFont(new Font("Times New Roman", Font.PLAIN, 18));
          search_button.setToolTipText("Search the data inputted with respect to the selected column.");
          search_button.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                    // secure that something is inputted to be search and a column is selected
                    if (column_names.getSelectedItem().equals(column_names.getItemAt(0)))
                         JOptionPane.showMessageDialog(SSIS_Main_Display.this, "Select a column.", "Invalid Column",
                                   JOptionPane.WARNING_MESSAGE);
                    else if (search_input.getText().equals("") || search_input.getText().equals("Search Here"))
                         JOptionPane.showMessageDialog(SSIS_Main_Display.this, "Enter something to search.",
                                   "Empty Search", JOptionPane.WARNING_MESSAGE);
                    else
                         Filter_Process.regexFilter(display_table, search_input.getText().toString(),
                                   column_names.getSelectedIndex() - 1);
               }
          });

          // button for sorting the tables
          sort_button = new JButton("Sort Table");
          sort_button.setBounds(PADDING_WIDTH * 1, PADDING_HEIGHT * 4 + 30, 120, 30);
          sort_button.addActionListener(e -> {
               new Sort_Dialog(sort_button, display_table.getName().toString()).setVisible(true);
          });

          filter_panel.removeAll();
          filter_panel.revalidate();
          filter_panel.repaint();

          filter_panel.add(column_names);
          filter_panel.add(search_input);
          filter_panel.add(search_button);
          filter_panel.add(sort_button);
     }

     /**
      * Display the last part of the display. Shows the tables gathered from the csv
      * files.
      */
     private void tableAreaDisplay() {
          // setup the table panel
          table_panel = new JPanel();
          table_panel.setBounds(0, banner_panel.getHeight() + option_panel.getHeight(),
                    this.getWidth() - PADDING_WIDTH + 5,
                    this.getHeight() - banner_panel.getHeight() - option_panel.getHeight() - 4 * PADDING_HEIGHT);
          table_panel.setLayout(new BoxLayout(table_panel, BoxLayout.Y_AXIS));

          // default table to display
          students_button.setEnabled(false);

          refreshTable(); // refresh the display in table_panel

          this.add(table_panel);
     }

     /**
      * Refreshes the table displayed.
      */
     private void refreshTable() {
          // setup and manage the display_table
          JScrollPane table_ScrollPane = new JScrollPane(display_table);
          table_ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
          table_ScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

          // remove the current objects displayed in the table_panel, then refresh
          table_panel.removeAll();
          table_panel.revalidate();
          table_panel.repaint();
          table_panel.add(table_ScrollPane);
     }
}
