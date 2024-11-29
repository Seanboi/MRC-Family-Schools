import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestEntry extends JDialog {
    private JTextField testIDField;
    private JTextField classIDField;
    private JTextField subjectField;
    private JTextField testNameField;
    private JSpinner testDateSpinner;
    private JButton saveButton, cancelButton;
    private boolean isSaved;
    private Test test;

    public TestEntry(JFrame parent, String classID, String subject, Test test) {
        super(parent, test == null ? "Add Test" : "Edit Test", true);
        this.test = test;
        this.isSaved = false;

        // Initialize components
        initComponents(classID, subject, test);
        
        // Layout setup
        setupLayout();
        
        // Add listeners
        setupListeners(test);

        // Final dialog setup
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void initComponents(String classID, String subject, Test existingTest) {
        // Initialize fields
        testIDField = new JTextField(existingTest == null ? Test.generateUniqueTestID() : existingTest.getTestID());
        testIDField.setEditable(false);

        classIDField = new JTextField(classID);
        classIDField.setEditable(false);

        subjectField = new JTextField(subject);
        subjectField.setEditable(false);

        testNameField = new JTextField(existingTest == null ? "" : existingTest.getTestName());

        // Date spinner setup
        SpinnerDateModel dateModel = new SpinnerDateModel();
        testDateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(testDateSpinner, "yyyy-MM-dd");
        testDateSpinner.setEditor(dateEditor);

        if (existingTest != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date testDate = sdf.parse(existingTest.getTestDate());
                testDateSpinner.setValue(testDate);
            } catch (Exception e) {
                testDateSpinner.setValue(new Date());
            }
        }

        // Buttons
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components to form
        formPanel.add(new JLabel("Test ID:"));
        formPanel.add(testIDField);
        formPanel.add(new JLabel("Class ID:"));
        formPanel.add(classIDField);
        formPanel.add(new JLabel("Subject:"));
        formPanel.add(subjectField);
        formPanel.add(new JLabel("Test Name:"));
        formPanel.add(testNameField);
        formPanel.add(new JLabel("Test Date:"));
        formPanel.add(testDateSpinner);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners(Test existingTest) {
        saveButton.addActionListener(e -> {
            if (validateInput()) {
                saveTest(existingTest);
            }
        });

        cancelButton.addActionListener(e -> dispose());
    }

    private boolean validateInput() {
        String testName = testNameField.getText().trim();
        
        // Validate test name
        if (testName.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Test name cannot be empty.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate test name length
        if (testName.length() > 50) {
            JOptionPane.showMessageDialog(this,
                "Test name cannot exceed 50 characters.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate date (not in future)
        Date selectedDate = (Date) testDateSpinner.getValue();
        if (selectedDate.after(new Date())) {
            JOptionPane.showMessageDialog(this,
                "Test date cannot be in the future.",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void saveTest(Test existingTest) {
        try {
            String testName = testNameField.getText().trim();
            String testDate = new SimpleDateFormat("yyyy-MM-dd")
                .format(testDateSpinner.getValue());

            if (existingTest == null) {
                // Create and save new test
                test = new Test(
                    testIDField.getText(),
                    testName,
                    testDate,
                    classIDField.getText(),
                    subjectField.getText()
                );
                test.saveTest();
                test.addToGradebook();
            } else {
                // Update existing test
                Test.updateTest(
                    existingTest.getTestID(),
                    testName,
                    testDate,
                    existingTest.getClassID()
                );
            }

            isSaved = true;
            dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error saving test: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Getters for dialog results
    public boolean isSaved() {
        return isSaved;
    }

    public Test getTest() {
        return test;
    }
}