import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AttendanceManagement extends JPanel {
    private JTable classesTable;
    private DefaultTableModel tableModel;
    private JButton markAttendanceButton, viewAttendanceButton;

    public AttendanceManagement() {
        super(new BorderLayout());

        // Add Title Label
        JLabel titleLabel = new JLabel("MRC ATTENDANCE MANAGEMENT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 70, 140));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Table Column Names
        String[] columnNames = {"Class ID", "Grade", "Subject", "Exam Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
        classesTable = new JTable(tableModel);

        // Styling the Table
        classesTable.setRowHeight(25);
        classesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        classesTable.getTableHeader().setBackground(new Color(0, 120, 215));
        classesTable.getTableHeader().setForeground(Color.WHITE);
        classesTable.setFont(new Font("Arial", Font.PLAIN, 12));
        classesTable.setGridColor(Color.LIGHT_GRAY);
        classesTable.setShowGrid(true);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 2));
        tablePanel.add(new JScrollPane(classesTable), BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(0, 70, 140));

        markAttendanceButton = createButton("Mark Attendance");
        viewAttendanceButton = createButton("View Attendance");

        buttonPanel.add(markAttendanceButton);
        buttonPanel.add(viewAttendanceButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Load Data into the Table
        loadClasses();

        // Button Actions
        setupMarkAttendanceButton();
        setupViewAttendanceButton();
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(150, 40));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 120, 215));
        button.setFocusPainted(false);
        return button;
    }

    private void loadClasses() {
        tableModel.setRowCount(0); // Clear table before loading
        try (BufferedReader reader = new BufferedReader(new FileReader("classes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) { 
                    tableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading classes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setupMarkAttendanceButton() {
        markAttendanceButton.addActionListener(e -> {
            int selectedRow = classesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a class to mark attendance.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String classID = (String) tableModel.getValueAt(selectedRow, 0);
            new RegisterGUI(classID); 
        });
    }

    private void setupViewAttendanceButton() {
        viewAttendanceButton.addActionListener(e -> {
            int selectedRow = classesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a class to view attendance.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String classID = (String) tableModel.getValueAt(selectedRow, 0);
            try (BufferedReader reader = new BufferedReader(new FileReader(classID + "_attendance.txt"))) {
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
                JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Attendance for Class: " + classID,
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading attendance data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Attendance Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.add(new AttendanceManagement());
        frame.setVisible(true);
    }
}

