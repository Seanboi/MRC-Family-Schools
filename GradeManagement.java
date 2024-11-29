import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class GradeManagement extends JPanel {
    private JTable classesTable;
    private DefaultTableModel tableModel;
    private JButton viewGradesButton;
    private JButton openGradebookButton, backButton;

    public GradeManagement() {
        super(new BorderLayout());

        backButton = createButton("Back to Main Menu");
        backButton.setBackground(new Color(0, 70, 140));
        
        // Add Title Label
        JLabel titleLabel = new JLabel("MRC GRADE MANAGEMENT", SwingConstants.CENTER);
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
        classesTable.setDefaultEditor(Object.class, null);
        classesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        classesTable.getTableHeader().setReorderingAllowed(false);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 2));
        tablePanel.add(new JScrollPane(classesTable), BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(0, 70, 140));
        buttonPanel.add(backButton);

        
        setupBackButton();


        viewGradesButton = createButton("View Grades");
        openGradebookButton = createButton("Open Gradebook");

        buttonPanel.add(viewGradesButton);
        buttonPanel.add(openGradebookButton);

        add(buttonPanel, BorderLayout.SOUTH);

        loadClasses();
        setupListeners();
    }

    private void setupBackButton() {

        backButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof JFrame) {
            window.dispose();
            new mainmenu().setVisible(true);
                }
            });
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
        tableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader("classes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    tableModel.addRow(new Object[]{
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim()
                    });
                }
            }
        } catch (IOException e) {
            showError("Error loading classes: " + e.getMessage());
        }
    }

    private void setupListeners() {
        viewGradesButton.addActionListener(e -> viewGrades());
        openGradebookButton.addActionListener(e -> openGradebook());
    }

    private void openGradebook() {
        int selectedRow = classesTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Please select a class to open the gradebook.");
            return;
        }

        String classID = (String) tableModel.getValueAt(selectedRow, 0);
        GradebookGUI gradebook = new GradebookGUI(classID);
        gradebook.setVisible(true);
    }

    private void viewGrades() {
        int selectedRow = classesTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Please select a class to view grades.");
            return;
        }

        String classID = (String) tableModel.getValueAt(selectedRow, 0);
        String gradebookFile = classID + "_gradebook.txt";

        File file = new File(gradebookFile);
        if (!file.exists()) {
            showInfo("No gradebook found for this class.");
            return;
        }

        try {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }

            JTextArea textArea = new JTextArea(content.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Arial", Font.PLAIN, 12));
            textArea.setBackground(Color.WHITE);
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140)));

            JOptionPane.showMessageDialog(this, scrollPane,
                "Grades for Class: " + classID,
                JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            showError("Error reading grades: " + e.getMessage());
        }
    }

    // Error handling methods
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", 
            JOptionPane.WARNING_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private static void createAndShowGUI(){
        JFrame frame = new JFrame("Grade Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.add(new GradeManagement());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
    }
    
}