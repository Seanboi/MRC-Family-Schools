import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ClassEntry extends JFrame {
    private JTextField classIDField;
    private JComboBox<String> examTypeDropdown;
    private JComboBox<String> subjectDropdown;
    private JComboBox<String> gradeLevelDropdown;
    private JButton saveButton, cancelButton;
    private ClassInfo createdClass; // Field to store the created class

    public ClassEntry() {
        setTitle("Add New Class");
        setSize(500, 350);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Class ID
        JPanel classIDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        classIDPanel.add(new JLabel("Class ID:"));
        classIDField = new JTextField(20);
        classIDField.setEditable(false);
        classIDPanel.add(classIDField);
        formPanel.add(classIDPanel);

        // Exam Type
        JPanel examTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        examTypePanel.add(new JLabel("Exam Type:"));
        examTypeDropdown = new JComboBox<>(new String[]{"CSEC", "CAPE", "GCSE"});
        examTypePanel.add(examTypeDropdown);
        formPanel.add(examTypePanel);

        // Subject
        JPanel subjectPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subjectPanel.add(new JLabel("Subject:"));
        subjectDropdown = new JComboBox<>(new String[]{"Physics", "Math", "Biology", "Chemistry"});
        subjectPanel.add(subjectDropdown);
        formPanel.add(subjectPanel);

        // Grade Level
        JPanel gradeLevelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gradeLevelPanel.add(new JLabel("Grade Level:"));
        gradeLevelDropdown = new JComboBox<>(new String[]{"10", "11", "12", "13"});
        gradeLevelPanel.add(gradeLevelDropdown);
        formPanel.add(gradeLevelPanel);

        add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Listeners
        setupGenerateIDListener();
        setupSaveButton();
        setupCancelButton();

        setVisible(true);
    }

    private void setupGenerateIDListener() {
        ActionListener listener = e -> generateClassID();
        examTypeDropdown.addActionListener(listener);
        subjectDropdown.addActionListener(listener);
        gradeLevelDropdown.addActionListener(listener);
    }

    private void generateClassID() {
        String examType = (String) examTypeDropdown.getSelectedItem();
        String subject = (String) subjectDropdown.getSelectedItem();
        String gradeLevel = (String) gradeLevelDropdown.getSelectedItem();

        if (examType != null && subject != null && gradeLevel != null) {
            String classID = ClassInfo.generateClassID(subject, gradeLevel, examType);
            classIDField.setText(classID);
        }
    }

    private void setupSaveButton() {
        saveButton.addActionListener(e -> {
            String classID = classIDField.getText();
            String examType = (String) examTypeDropdown.getSelectedItem();
            String subject = (String) subjectDropdown.getSelectedItem();
            String gradeLevel = (String) gradeLevelDropdown.getSelectedItem();

            if (classID.isEmpty() || examType == null || subject == null || gradeLevel == null) {
                JOptionPane.showMessageDialog(this, "All fields must be filled before saving.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Create ClassInfo object
            createdClass = new ClassInfo(classID, gradeLevel, subject, examType);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("classes.txt", true))) {
                writer.write(createdClass.getClassID() + "," + createdClass.getGradeLevel() + "," +
                        createdClass.getSubject() + "," + createdClass.getExamType());
                writer.newLine();
                createdClass.saveToFile(); // Save details to the class-specific file
                JOptionPane.showMessageDialog(this, "Class saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the form
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving class: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void setupCancelButton() {
        cancelButton.addActionListener(e -> {
            createdClass = null; // No class created if cancelled
            dispose();
        });
    }

    /**
     * Returns the created ClassInfo object after the form is closed.
     * @return the created ClassInfo, or null if the form was cancelled.
     */
    public ClassInfo getCreatedClass() {
        return createdClass;
    }
}
