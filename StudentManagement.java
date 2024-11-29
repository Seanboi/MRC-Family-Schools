import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.*;

public class StudentManagement extends JPanel {
    private JTable grade10Table, grade11Table, grade12Table, grade13Table;
    private DefaultTableModel grade10Model, grade11Model, grade12Model, grade13Model;
    private ArrayList<studentinfo> grade10List, grade11List, grade12List, grade13List;
    private HashSet<String> usedIDs = new HashSet<>();

    public StudentManagement() {
        super(new BorderLayout());
    
        loadUsedIDs();

        grade10List = loadStudentData("grade10.txt");
        grade11List = loadStudentData("grade11.txt");
        grade12List = loadStudentData("grade12.txt");
        grade13List = loadStudentData("grade13.txt");
    
        String[] columnNames = {"Student ID", "First Name", "Last Name", "DOB", "Age", "Grade", "School",
                                "Contact Number", "Email", "Address",
                                "Guardian First Name", "Guardian Last Name", "Guardian Relation",
                                "Guardian Phone", "Guardian Email"};
        grade10Model = new DefaultTableModel(columnNames, 0);
        grade11Model = new DefaultTableModel(columnNames, 0);
        grade12Model = new DefaultTableModel(columnNames, 0);
        grade13Model = new DefaultTableModel(columnNames, 0);
    
        grade10Table = new JTable(grade10Model);
        grade11Table = new JTable(grade11Model);
        grade12Table = new JTable(grade12Model);
        grade13Table = new JTable(grade13Model);
    
        loadTableData(grade10List, grade10Model);
        loadTableData(grade11List, grade11Model);
        loadTableData(grade12List, grade12Model);
        loadTableData(grade13List, grade13Model);
    
        JLabel titleLabel = new JLabel("MRC STUDENT MANAGEMENT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 70, 140)); 
        titleLabel.setForeground(Color.WHITE); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
    
        JPanel gradesPanel = new JPanel();
        gradesPanel.setLayout(new BoxLayout(gradesPanel, BoxLayout.Y_AXIS));
        gradesPanel.setBackground(new Color(220, 240, 255));
        gradesPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        gradesPanel.add(createTablePanel("Grade 10 Students", grade10Table));
        gradesPanel.add(Box.createVerticalStrut(20)); 
        gradesPanel.add(createTablePanel("Grade 11 Students", grade11Table));
        gradesPanel.add(Box.createVerticalStrut(20));
        gradesPanel.add(createTablePanel("Grade 12 Students", grade12Table));
        gradesPanel.add(Box.createVerticalStrut(20));
        gradesPanel.add(createTablePanel("Grade 13 Students", grade13Table));
    
        JScrollPane scrollPane = new JScrollPane(gradesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); 
        add(scrollPane, BorderLayout.CENTER);
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(0, 70, 140)); 
    
        JButton addButton = createButton("Add Student");
        JButton updateButton = createButton("Update Student");
        JButton deleteButton = createButton("Delete Student");
        JButton searchButton = createButton("Search by Student ID");
        JButton searchByNameButton = createButton("Search by Name");
    
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(searchByNameButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setupAddFunctionality(addButton);
        setupUpdateFunctionality(updateButton);
        setupDeleteFunctionality(deleteButton);
        setupSearchFunctionality(searchButton);
        setupSearchByNameFunctionality(searchByNameButton);
    }
    

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 40));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 120, 215)); 
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createTablePanel(String gradeTitle, JTable table) {
        JPanel gradePanel = new JPanel(new BorderLayout());
        gradePanel.setBackground(Color.WHITE);
        gradePanel.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 2));

        JLabel gradeLabel = new JLabel(gradeTitle, SwingConstants.CENTER);
        gradeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gradeLabel.setOpaque(true);
        gradeLabel.setBackground(new Color(0, 120, 215)); 
        gradeLabel.setForeground(Color.WHITE);
        gradeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        gradePanel.add(gradeLabel, BorderLayout.NORTH);

        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(0, 120, 215));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setGridColor(Color.LIGHT_GRAY);
        table.setShowGrid(true);

        gradePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return gradePanel;
    }

    private void loadUsedIDs() {
        String[] gradeFiles = {"grade10.txt", "grade11.txt", "grade12.txt", "grade13.txt"};
        for (String file : gradeFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        usedIDs.add(parts[0].trim());
                    }
                }
            } catch (IOException e) {
                System.err.println("Error loading IDs from " + file + ": " + e.getMessage());
            }
        }
    }

    public String generateUniqueID() {
        String id;
        do {
            id = "L" + (1000 + (int) (Math.random() * 9000));
        } while (usedIDs.contains(id));
        usedIDs.add(id);
        return id;
    }


    private ArrayList<studentinfo> loadStudentData(String filename) {
        ArrayList<studentinfo> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                studentinfo student = studentinfo.fromCSV(line);
                if (student != null) {
                    students.add(student);
                }
            }
            students.sort((s1, s2) -> s1.getlastname().compareToIgnoreCase(s2.getlastname()));
    
            saveStudentData(students, filename);
    
        } catch (IOException e) {
            System.err.println("Error loading data from " + filename + ": " + e.getMessage());
        }
        return students;
    }
    
    

    private void loadTableData(ArrayList<studentinfo> students, DefaultTableModel model) {
        model.setRowCount(0); 
        for (studentinfo student : students) {
            contactinfo contact = student.getcontactinfo();
            guardianinfo guardian = student.getguardianinfo();

            model.addRow(new Object[]{
                    student.getstudentID(),
                    student.getfirstname(),
                    student.getlastname(),
                    student.getDob(),
                    student.getAge(),
                    student.getCurrentGrade(),
                    student.getCurrentSchool(),
                    contact != null ? contact.gettelnum() : "",
                    contact != null ? contact.getEmail() : "",
                    contact != null ? contact.getaddress() : "",
                    guardian != null ? guardian.getguardianfName() : "",
                    guardian != null ? guardian.getguardianlName() : "",
                    guardian != null ? guardian.getrelation() : "",
                    guardian != null ? guardian.gettelnum() : "",
                    guardian != null ? guardian.getEmail() : ""
            });
        }
    }

    public void addStudentToGrade(studentinfo student) {
        String grade = student.getCurrentGrade();
        ArrayList<studentinfo> targetList = null;
        DefaultTableModel targetModel = null;
        String targetFile = null;
    
        switch (grade) {
            case "10":
                targetList = grade10List;
                targetModel = grade10Model;
                targetFile = "grade10.txt";
                break;
            case "11":
                targetList = grade11List;
                targetModel = grade11Model;
                targetFile = "grade11.txt";
                break;
            case "12":
                targetList = grade12List;
                targetModel = grade12Model;
                targetFile = "grade12.txt";
                break;
            case "13":
                targetList = grade13List;
                targetModel = grade13Model;
                targetFile = "grade13.txt";
                break;
            default:
                JOptionPane.showMessageDialog(this, "Invalid grade specified.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }
    
        if (targetList.contains(student)) {
            JOptionPane.showMessageDialog(this, "Student is already in the grade list.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        targetList.add(student);
    
        targetList.sort((s1, s2) -> s1.getlastname().compareToIgnoreCase(s2.getlastname()));
    
        loadTableData(targetList, targetModel);
        saveStudentData(targetList, targetFile);
    }
    
    
    private void saveStudentData(ArrayList<studentinfo> studentList, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (studentinfo student : studentList) {
                writer.write(student.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving data to " + filename + ": " + e.getMessage());
        }
    }

    public void updateStudentGradeIfChanged(studentinfo student, String oldGrade) {
        if (!student.getCurrentGrade().equals(oldGrade)) {

            String newGrade = student.getCurrentGrade();
    
            student.setCurrentGrade(oldGrade);
            removeStudentFromGrade(student);
    
            student.setCurrentGrade(newGrade);
            addStudentToGrade(student);
        }
    }
    
    private void setupAddFunctionality(JButton addButton) {
        addButton.addActionListener(e -> {
            StudentEntry form = new StudentEntry(null, this);
            form.setVisible(true);
        });
    }

    private void setupUpdateFunctionality(JButton updateButton) {
        updateButton.addActionListener(e -> {
            JTable selectedTable = getSelectedTable();
            if (selectedTable != null) {
                int selectedRow = selectedTable.getSelectedRow(); 
                if (selectedRow >= 0) {
                    String studentID = selectedTable.getValueAt(selectedRow, 0).toString();
                    studentinfo selectedStudent = findStudentByID(studentID);
    
                    if (selectedStudent != null) {
                        StudentEntry updateForm = new StudentEntry(selectedStudent, this);
                        updateForm.setVisible(true);
    
                        updateForm.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                refreshTables(); 
                            }
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Student not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a student to update.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No table is selected.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
    
    
    public void refreshTables() {
        loadTableData(grade10List, grade10Model);
        loadTableData(grade11List, grade11Model);
        loadTableData(grade12List, grade12Model);
        loadTableData(grade13List, grade13Model);
    

        saveStudentData(grade10List, "grade10.txt");
        saveStudentData(grade11List, "grade11.txt");
        saveStudentData(grade12List, "grade12.txt");
        saveStudentData(grade13List, "grade13.txt");
    }
    
    

    private void setupDeleteFunctionality(JButton deleteButton) {
        deleteButton.addActionListener(e -> {
            String studentID = JOptionPane.showInputDialog(this, "Enter Student ID to Delete:");
            if (studentID != null && !studentID.trim().isEmpty()) {
                studentinfo studentToDelete = findStudentByID(studentID.trim());
                if (studentToDelete != null) {
                    removeStudentFromGrade(studentToDelete);
                    JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                    refreshTables();
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    

    private JTable getSelectedTable() {
        if (grade10Table.getSelectedRow() >= 0) return grade10Table;
        if (grade11Table.getSelectedRow() >= 0) return grade11Table;
        if (grade12Table.getSelectedRow() >= 0) return grade12Table;
        if (grade13Table.getSelectedRow() >= 0) return grade13Table;
        return null;
    }

    private studentinfo findStudentByID(String studentID) {
        for (studentinfo student : grade10List) {
            if (student.getstudentID().equals(studentID)) {
                return new studentinfo(student.getstudentID(), student.getfirstname(), student.getlastname(),
                    student.getDob(), student.getCurrentGrade(), student.getCurrentSchool(),
                    student.getcontactinfo(), student.getguardianinfo());
            }
        }
        for (studentinfo student : grade11List) {
            if (student.getstudentID().equals(studentID)) {
                return new studentinfo(student.getstudentID(), student.getfirstname(), student.getlastname(),
                    student.getDob(), student.getCurrentGrade(), student.getCurrentSchool(),
                    student.getcontactinfo(), student.getguardianinfo());
            }
        }
        for (studentinfo student : grade12List) {
            if (student.getstudentID().equals(studentID)) {
                return new studentinfo(student.getstudentID(), student.getfirstname(), student.getlastname(),
                    student.getDob(), student.getCurrentGrade(), student.getCurrentSchool(),
                    student.getcontactinfo(), student.getguardianinfo());
            }
        }
        for (studentinfo student : grade13List) {
            if (student.getstudentID().equals(studentID)) {
                return new studentinfo(student.getstudentID(), student.getfirstname(), student.getlastname(),
                    student.getDob(), student.getCurrentGrade(), student.getCurrentSchool(),
                    student.getcontactinfo(), student.getguardianinfo());
            }
        }
        return null;
    }

    private void setupSearchByNameFunctionality(JButton searchByNameButton) {
        searchByNameButton.addActionListener(e -> {
            String searchName = JOptionPane.showInputDialog(this, "Enter Name to Search (First or Last):");
            if (searchName != null && !searchName.trim().isEmpty()) {
                ArrayList<studentinfo> results = findStudentsByName(searchName.trim());
                if (!results.isEmpty()) {
                    displayStudentsByName(results);
                } else {
                    JOptionPane.showMessageDialog(this, "No students found with the given name.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private ArrayList<studentinfo> findStudentsByName(String name) {
    ArrayList<studentinfo> matchingStudents = new ArrayList<>();
    for (studentinfo student : grade10List) {
        if (student.getfirstname().equalsIgnoreCase(name) || student.getlastname().equalsIgnoreCase(name)) {
            matchingStudents.add(student);
        }
    }
    for (studentinfo student : grade11List) {
        if (student.getfirstname().equalsIgnoreCase(name) || student.getlastname().equalsIgnoreCase(name)) {
            matchingStudents.add(student);
        }
    }
    for (studentinfo student : grade12List) {
        if (student.getfirstname().equalsIgnoreCase(name) || student.getlastname().equalsIgnoreCase(name)) {
            matchingStudents.add(student);
        }
    }
    for (studentinfo student : grade13List) {
        if (student.getfirstname().equalsIgnoreCase(name) || student.getlastname().equalsIgnoreCase(name)) {
            matchingStudents.add(student);
        }
    }
    return matchingStudents;
    }

    private void displayStudentsByName(ArrayList<studentinfo> students) {
        StringBuilder details = new StringBuilder();
        for (studentinfo student : students) {
            details.append("Student ID: ").append(student.getstudentID()).append("\n");
            details.append("First Name: ").append(student.getfirstname()).append("\n");
            details.append("Last Name: ").append(student.getlastname()).append("\n");
            details.append("Date of Birth: ").append(student.getDob()).append("\n");
            details.append("Age: ").append(student.getAge()).append("\n");
            details.append("Grade: ").append(student.getCurrentGrade()).append("\n");
            details.append("School: ").append(student.getCurrentSchool()).append("\n");
    
            contactinfo contact = student.getcontactinfo();
            if (contact != null) {
                details.append("Contact Number: ").append(contact.gettelnum()).append("\n");
                details.append("Email: ").append(contact.getEmail()).append("\n");
                details.append("Address: ").append(contact.getaddress()).append("\n");
            }
    
            guardianinfo guardian = student.getguardianinfo();
            if (guardian != null) {
                details.append("Guardian First Name: ").append(guardian.getguardianfName()).append("\n");
                details.append("Guardian Last Name: ").append(guardian.getguardianlName()).append("\n");
                details.append("Relation: ").append(guardian.getrelation()).append("\n");
                details.append("Phone: ").append(guardian.gettelnum()).append("\n");
                details.append("Email: ").append(guardian.getEmail()).append("\n");
            }
    
            details.append("\n");
        }
    
        JOptionPane.showMessageDialog(this, details.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
    }
    

    private void removeStudentFromGrade(studentinfo student) {
        String grade = student.getCurrentGrade();
    
        ArrayList<studentinfo> targetList = null;
        DefaultTableModel targetModel = null;
        String targetFile = null;
    
        switch (grade) {
            case "10":
                targetList = grade10List;
                targetModel = grade10Model;
                targetFile = "grade10.txt";
                break;
            case "11":
                targetList = grade11List;
                targetModel = grade11Model;
                targetFile = "grade11.txt";
                break;
            case "12":
                targetList = grade12List;
                targetModel = grade12Model;
                targetFile = "grade12.txt";
                break;
            case "13":
                targetList = grade13List;
                targetModel = grade13Model;
                targetFile = "grade13.txt";
                break;
        }
    
        if (targetList != null && targetList.remove(student)) {
            loadTableData(targetList, targetModel);
            saveStudentData(targetList, targetFile);
        } else {
            JOptionPane.showMessageDialog(this, "Error: Student not found in the specified grade.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void setupSearchFunctionality(JButton searchButton) {
        searchButton.addActionListener(e -> {
            String searchID = JOptionPane.showInputDialog(this, "Enter Student ID to Search:");
            if (searchID != null && !searchID.trim().isEmpty()) {
                studentinfo student = findStudentByID(searchID.trim());
                if (student != null) {
                    displayStudentDetails(student);
                } else {
                    JOptionPane.showMessageDialog(this, "Student ID not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void displayStudentDetails(studentinfo student) {
        JPanel detailsPanel = new JPanel(new BorderLayout(10, 10));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        JLabel titleLabel = new JLabel("Student Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 70, 140)); 
        titleLabel.setForeground(Color.WHITE); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        detailsPanel.add(titleLabel, BorderLayout.NORTH);
    
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
    
        addDetail(contentPanel, "First Name:", student.getfirstname());
        addDetail(contentPanel, "Last Name:", student.getlastname());
        addDetail(contentPanel, "Date of Birth:", student.getDob());
        addDetail(contentPanel, "Grade:", student.getCurrentGrade());
        addDetail(contentPanel, "School:", student.getCurrentSchool());
    
        contactinfo contact = student.getcontactinfo();
        if (contact != null) {
            addDetail(contentPanel, "Contact Number:", contact.gettelnum());
            addDetail(contentPanel, "Email:", contact.getEmail());
        }
    
        guardianinfo guardian = student.getguardianinfo();
        if (guardian != null) {
            addDetail(contentPanel, "Guardian First Name:", guardian.getguardianfName());
            addDetail(contentPanel, "Guardian Last Name:", guardian.getguardianlName());
            addDetail(contentPanel, "Guardian Phone:", guardian.gettelnum());
            addDetail(contentPanel, "Guardian Email:", guardian.getEmail());
        }
    
        detailsPanel.add(contentPanel, BorderLayout.CENTER);
    
        JOptionPane.showMessageDialog(this, detailsPanel, "Student Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void addDetail(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
    
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(new Color(0, 70, 140)); 
        row.add(lbl, BorderLayout.WEST);
    
        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 14));
        row.add(val, BorderLayout.CENTER);
    
        panel.add(row);
        panel.add(Box.createVerticalStrut(10)); 
    }
    
}
