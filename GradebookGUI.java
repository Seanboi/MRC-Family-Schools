import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GradebookGUI extends JFrame {
    private JTable gradebookTable;
    private DefaultTableModel tableModel;
    private String classID;
    private String gradebookFile;

    private final Color DARK_BLUE = new Color(0, 70, 140);
    private final Color MEDIUM_BLUE = new Color(0, 120, 215);
    private final Color WHITE = Color.WHITE;
    
    private JButton addTestButton;
    private JButton editTestButton;
    private JButton deleteTestButton;
    private JButton saveButton;
    private JButton editGradesButton;

    public GradebookGUI(String classID) {
        this.classID = classID;
        this.gradebookFile = classID + "_gradebook.txt";

        setTitle("Gradebook - " + classID);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);

        initComponents();
        setupLayout();
        loadGradebook();
        setupListeners();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        gradebookTable = new JTable(tableModel);
        

        gradebookTable.setRowHeight(25);
        gradebookTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        gradebookTable.getTableHeader().setBackground(MEDIUM_BLUE);
        gradebookTable.getTableHeader().setForeground(WHITE);
        gradebookTable.setFont(new Font("Arial", Font.PLAIN, 12));
        gradebookTable.setGridColor(Color.LIGHT_GRAY);
        gradebookTable.setShowGrid(true);
        gradebookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gradebookTable.getTableHeader().setReorderingAllowed(false);
        gradebookTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        
        addTestButton = createStyledButton("Add Test");
        editTestButton = createStyledButton("Edit Test");
        deleteTestButton = createStyledButton("Delete Test");
        saveButton = createStyledButton("Save Changes");
        editGradesButton = createStyledButton("Edit Grades");
    }

    private JButton createStyledButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(150, 40));
        button.setForeground(WHITE);
        button.setBackground(MEDIUM_BLUE);
        button.setFocusPainted(false);
        return button;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Gradebook - Class " + classID, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(DARK_BLUE);
        titleLabel.setForeground(WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(gradebookTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BLUE, 2));
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        tablePanel.add(scrollPane);
        add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(DARK_BLUE);
        buttonPanel.add(addTestButton);
        buttonPanel.add(editTestButton);
        buttonPanel.add(deleteTestButton);
        buttonPanel.add(editGradesButton);
        buttonPanel.add(saveButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadGradebook() {
        tableModel.setRowCount(0); 
        File file = new File(gradebookFile);
        if (!file.exists()) {
            createNewGradebook();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String headerLine = reader.readLine();
            if (headerLine == null) {
                showError("Empty gradebook file");
                return;
            }
            String[] columns = headerLine.split(",");
            tableModel.setColumnCount(0);
            for (String column : columns) {
                tableModel.addColumn(column.trim());
            }

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String[] rowData = new String[tableModel.getColumnCount()];
                System.arraycopy(data, 0, rowData, 0, Math.min(data.length, rowData.length));
                tableModel.addRow(rowData);
            
            }
        } catch (IOException e) {
            showError("Error loading gradebook: " + e.getMessage());
        }
    }

    private void createNewGradebook() {
        List<String[]> students = getStudentsFromClassFile();
        if (students.isEmpty()) {
            showError("No students found for this class");
            return;
        }

        tableModel.setColumnCount(0);
        tableModel.addColumn("Student ID");
        tableModel.addColumn("Student Name");
        tableModel.addColumn("Average");

        for (String[] student : students) {
            tableModel.addRow(new Object[]{student[0], student[1], ""});
        }

        saveGradebook();
    }

    private List<String[]> getStudentsFromClassFile() {
        List<String[]> students = new ArrayList<>();
        File classFile = new File(classID + ".txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(classFile))) {
            String line;
            boolean studentsSection = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("Students:")) {
                    studentsSection = true;
                    continue;
                }
                if (studentsSection && line.startsWith("-")) {
                    String[] parts = line.substring(2).split(" - ", 2);
                    if (parts.length == 2) {
                        students.add(new String[]{parts[0].trim(), parts[1].trim()});
                    }
                }
            }
        } catch (IOException e) {
            showError("Error reading class file: " + e.getMessage());
        }

        return students;
    }

    private void setupListeners() {
        addTestButton.addActionListener(e -> addTest());
        editTestButton.addActionListener(e -> editTest());
        deleteTestButton.addActionListener(e -> deleteTest());
        editGradesButton.addActionListener(e -> editGrades());
        saveButton.addActionListener(e -> saveGradebook());
    }

    private void addTest() {
        TestEntry testEntry = new TestEntry(this, classID, getSubject(), null);
        testEntry.setVisible(true);
        if (testEntry.isSaved()) {
            loadGradebook();
        }
    }

    private void editTest() {
        String selectedTest = selectTest("Select Test to Edit");
        if (selectedTest == null) return;

        Test test = findTest(selectedTest);
        if (test != null) {
            TestEntry testEntry = new TestEntry(this, classID, getSubject(), test);
            testEntry.setVisible(true);
            if (testEntry.isSaved()) {
                loadGradebook();
            }
        }
    }

    private void editGrades() {
        String selectedTest = selectTest("Select Test to Edit Grades");
        if (selectedTest == null) return;

        int testColumn = findTestColumn(selectedTest);
        if (testColumn == -1) return;

        JDialog gradeEditor = new JDialog(this, "Edit Grades - " + selectedTest, true);
        gradeEditor.getContentPane().setBackground(WHITE);
        ((JPanel)gradeEditor.getContentPane()).setBorder(
            BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gradeEditor.setLayout(new BorderLayout());
        gradeEditor.setSize(400, 500);
        gradeEditor.setLocationRelativeTo(this);

        JPanel gradesPanel = new JPanel();
        gradesPanel.setBackground(WHITE);
        gradesPanel.setLayout(new BoxLayout(gradesPanel, BoxLayout.Y_AXIS));
        
        JScrollPane scrollPane = new JScrollPane(gradesPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_BLUE));

        List<JTextField> gradeFields = new ArrayList<>();
        
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String studentId = (String) tableModel.getValueAt(row, 0);
            String studentName = (String) tableModel.getValueAt(row, 1);
            String currentGrade = (String) tableModel.getValueAt(row, testColumn);

            JPanel studentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            studentPanel.setBackground(WHITE);
            
            JLabel studentLabel = new JLabel(studentId + " - " + studentName + ": ");
            studentLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            
            JTextField gradeField = new JTextField(currentGrade, 5);
            gradeField.setFont(new Font("Arial", Font.PLAIN, 12));
            gradeFields.add(gradeField);

            studentPanel.add(studentLabel);
            studentPanel.add(gradeField);
            gradesPanel.add(studentPanel);
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(DARK_BLUE);
        JButton saveButton = createStyledButton("Save");
        JButton cancelButton = createStyledButton("Cancel");

        saveButton.addActionListener(e -> {
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                String grade = gradeFields.get(row).getText().trim();
                if (!grade.isEmpty()) {
                    try {
                        double gradeValue = Double.parseDouble(grade);
                        if (gradeValue >= 0 && gradeValue <= 100) {
                            tableModel.setValueAt(grade, row, testColumn);
                            calculateAverage(row);
                        } else {
                            showError("Grade must be between 0 and 100 for student: " + 
                                    tableModel.getValueAt(row, 1));
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        showError("Invalid grade format for student: " + 
                                tableModel.getValueAt(row, 1));
                        return;
                    }
                } else {
                    tableModel.setValueAt("", row, testColumn);
                    calculateAverage(row);
                }
            }
            saveGradebook();
            gradeEditor.dispose();
        });

        cancelButton.addActionListener(e -> gradeEditor.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gradeEditor.add(scrollPane, BorderLayout.CENTER);
        gradeEditor.add(buttonPanel, BorderLayout.SOUTH);
        gradeEditor.setVisible(true);
    }

    private void deleteTest() {
        String selectedTest = selectTest("Select Test to Delete");
        if (selectedTest == null) return;
    
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this test?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION);
    
        if (confirm == JOptionPane.YES_OPTION) {
            String testID = selectedTest.split(" ")[0];
            deleteTestColumn(selectedTest);
            saveGradebook();
            deleteFromTestsFile(testID);
        }
    }
    
    private void deleteFromTestsFile(String testIDToDelete) {
        File testsFile = new File("Tests.txt");
        File tempFile = new File("Tests_temp.txt");
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(testsFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] testData = line.split(",");
                if (!testData[0].equals(testIDToDelete)) {
                    writer.write(line);
                    writer.newLine();
                }
            }
            
            reader.close();
            writer.close();
            
            testsFile.delete();
            tempFile.renameTo(testsFile);
            
        } catch (IOException e) {
            showError("Error deleting test from Tests.txt: " + e.getMessage());
        }
    }

    private String selectTest(String title) {
        List<String> testColumns = new ArrayList<>();
        for (int i = 3; i < tableModel.getColumnCount(); i++) {
            testColumns.add(tableModel.getColumnName(i));
        }

        if (testColumns.isEmpty()) {
            showWarning("No tests available");
            return null;
        }

        return (String) JOptionPane.showInputDialog(this,
            "Select a test:",
            title,
            JOptionPane.QUESTION_MESSAGE,
            null,
            testColumns.toArray(),
            testColumns.get(0));
    }

    private void calculateAverage(int row) {
        double sum = 0;
        int count = 0;
        for (int col = 3; col < tableModel.getColumnCount(); col++) {
            String value = (String) tableModel.getValueAt(row, col);
            if (value != null && !value.trim().isEmpty()) {
                try {
                    sum += Double.parseDouble(value);
                    count++;
                } catch (NumberFormatException ignored) {}
            }
        }
        String average = count > 0 ? String.format("%.2f", sum / count) : "";
        tableModel.setValueAt(average, row, 2);
    }

    private void saveGradebook() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(gradebookFile));
            
            StringBuilder header = new StringBuilder();
            for (int col = 0; col < tableModel.getColumnCount(); col++) {
                if (col > 0) header.append(",");
                header.append(tableModel.getColumnName(col));
            }
            writer.write(header.toString());
            writer.newLine();
            
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                StringBuilder rowStr = new StringBuilder();
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    if (col > 0) rowStr.append(",");
                    Object value = tableModel.getValueAt(row, col);
                    rowStr.append(value != null ? value.toString() : "");
                }
                writer.write(rowStr.toString());
                writer.newLine();
            }
            
            writer.close();
            showInfo("Gradebook saved successfully");
        } catch (IOException e) {
            showError("Error saving gradebook: " + e.getMessage());
        }
    }

    private Test findTest(String testHeader) {
        String testID = testHeader.split(" ")[0];
        return Test.loadTestsForClass(classID).stream()
            .filter(t -> t.getTestID().equals(testID))
            .findFirst()
            .orElse(null);
    }

    private void deleteTestColumn(String columnName) {
        int column = findTestColumn(columnName);
        if (column != -1) {

            List<String> newColumns = new ArrayList<>();
            List<List<String>> newData = new ArrayList<>();
            
            for (int i = 0; i < tableModel.getColumnCount(); i++) {
                if (i != column) {
                    newColumns.add(tableModel.getColumnName(i));
                }
            }
            
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                List<String> newRow = new ArrayList<>();
                for (int col = 0; col < tableModel.getColumnCount(); col++) {
                    if (col != column) {
                        newRow.add((String)tableModel.getValueAt(row, col));
                    }
                }
                newData.add(newRow);
            }
            

            String[] columnArray = newColumns.toArray(new String[0]);
            
            Object[][] dataArray = new Object[newData.size()][newColumns.size()];
            for (int i = 0; i < newData.size(); i++) {
                dataArray[i] = newData.get(i).toArray(new String[0]);
            }
            
            tableModel.setDataVector(dataArray, columnArray);
            
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                calculateAverage(row);
            }
        }
    }

    private int findTestColumn(String columnName) {
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            if (tableModel.getColumnName(i).equals(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private String getSubject() {
        try (BufferedReader reader = new BufferedReader(new FileReader(classID + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Subject:")) {
                    return line.substring(8).trim();
                }
            }
        } catch (IOException ignored) {}
        return "Unknown Subject";
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}