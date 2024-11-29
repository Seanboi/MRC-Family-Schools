import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class GradeGUI extends JFrame {
    private Gradebook gradebook;
    private JTextField testIdField, subjectField, classIdField, studentIDField, scoreField;
    private JTable gradeTable;
    private DefaultTableModel tableModel;

    public GradeGUI() {
        gradebook = new Gradebook();

        // main window
        setTitle("Grade Management");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        add(topPanel, BorderLayout.NORTH);

        // Test ID input field
        topPanel.add(new JLabel("Test ID:"));
        testIdField = new JTextField();
        topPanel.add(testIdField);

        // Subject input field
        topPanel.add(new JLabel("Subject:"));
        subjectField = new JTextField();
        topPanel.add(subjectField);

        // Class ID input field
        topPanel.add(new JLabel("Class ID:"));
        classIdField = new JTextField();
        topPanel.add(classIdField);

        // Student ID input field
        topPanel.add(new JLabel("Student ID:"));
        studentIDField = new JTextField();
        topPanel.add(studentIDField);

        // Score input field
        topPanel.add(new JLabel("Score:"));
        scoreField = new JTextField();
        topPanel.add(scoreField);

        // Grade data table
        tableModel = new DefaultTableModel(new Object[]{"Test ID", "Subject", "Class", "Student ID", "Score"}, 0);
        gradeTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(gradeTable);
        add(tableScrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Add test button
        JButton addTestButton = new JButton("Add Test");
        addTestButton.addActionListener(e -> addTest());
        bottomPanel.add(addTestButton);

        // Add score button
        JButton addScoreButton = new JButton("Add Score");
        addScoreButton.addActionListener(e -> addScore());
        bottomPanel.add(addScoreButton);

        // Save data button
        JButton saveButton = new JButton("Save Data");
        saveButton.addActionListener(e -> saveDataToFile("gradebook_data.txt"));
        bottomPanel.add(saveButton);

        // Load data button
        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(e -> loadDataFromFile("gradebook_data.txt"));
        bottomPanel.add(loadButton);

        // Ensure grade data is loaded after components are initialized
        loadDataFromFile("gradebook_data.txt");
    }

    private void displayGradebook() {
        tableModel.setRowCount(0);

        for (Test test : gradebook.getTests()) {
            for (Map.Entry<String, Double> entry : test.getScores().entrySet()) {
                Object[] row = new Object[]{test.getTestId(), test.getSubject(), test.getClassId(), entry.getKey(), entry.getValue()};
                tableModel.addRow(row);
            }
        }
    }

    private void addTest() {
        String testId = testIdField.getText();
        String subject = subjectField.getText();
        String classId = classIdField.getText();

        if (testId.isEmpty() || subject.isEmpty() || classId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all test details.");
            return;
        }

        // Create and add new test
        gradebook.addTest(new Test(testId, subject, classId));

        // Update display
        displayGradebook();
    }

    private void addScore() {
        String testId = testIdField.getText();
        String studentId = studentIDField.getText();
        String scoreText = scoreField.getText();

        if (testId.isEmpty() || studentId.isEmpty() || scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all score details.");
            return;
        }

        try {
            double score = Double.parseDouble(scoreText);
            gradebook.addStudentScore(testId, studentId, score);
            displayGradebook();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid score.");
        }
    }

    private void loadDataFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Test currentTest = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Test ID:")) {
                    // Parse test information (e.g., Test ID, Subject, Class)
                    String[] testDetails = line.split(", ");
                    String testId = testDetails[0].split(": ")[1];
                    String subject = testDetails[1].split(": ")[1];
                    String classId = testDetails[2].split(": ")[1];

                    // New Test object to add to Gradebook
                    currentTest = new Test(testId, subject, classId);
                    gradebook.addTest(currentTest);
                } else if (line.startsWith("\tStudent ID:")) {
                    // Parse student score information
                    String[] scoreDetails = line.split(", ");
                    String studentId = scoreDetails[0].split(": ")[1];
                    double score = Double.parseDouble(scoreDetails[1].split(": ")[1]);

                    if (currentTest != null) {
                        currentTest.addScore(studentId, score);
                    }
                }
            }
            displayGradebook();
        } catch (IOException e) {
            System.out.println("Error loading grades from file: " + e.getMessage());
        }
    }

    private void saveDataToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Test test : gradebook.getTests()) {
                // Write test information (Test ID, Subject, Class ID)
                writer.write("Test ID: " + test.getTestId() + ", Subject: " + test.getSubject() + ", Class: " + test.getClassId());
                writer.newLine();

                // Write student scores for the test
                for (Map.Entry<String, Double> entry : test.getScores().entrySet()) {
                    writer.write("\tStudent ID: " + entry.getKey() + ", Score: " + entry.getValue());
                    writer.newLine();
                }
                writer.newLine();
            }
            System.out.println("Grades saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving grades to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GradeGUI().setVisible(true));
    }
}
