import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;


public class ClassManagement extends JPanel {
  private JTable classesTable;
  private DefaultTableModel tableModel;
  private JButton addClassButton, assignTeacherButton, assignStudentsButton, updateClassButton,
          deleteClassButton, viewClassButton, removeStudentButton, sortByGradeButton, sortBySubjectButton, 
          viewStudentClassesButton, viewTeacherClassesButton;

  public ClassManagement() {
      super(new BorderLayout());

      String[] columnNames = {"Class ID", "Grade", "Subject", "Exam Type"};
      tableModel = new DefaultTableModel(columnNames, 0);
      classesTable = new JTable(tableModel);

      // Load data into the table
      loadClasses();

      JLabel titleLabel = new JLabel("MRC CLASS MANAGEMENT", SwingConstants.CENTER);
      titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
      titleLabel.setOpaque(true);
      titleLabel.setBackground(new Color(0, 70, 140));
      titleLabel.setForeground(Color.WHITE);
      titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
      add(titleLabel, BorderLayout.NORTH);

      // Styling the table
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

      addClassButton = createButton("Add New Class");
      assignTeacherButton = createButton("Assign Teacher");
      assignStudentsButton = createButton("Assign Students");
      updateClassButton = createButton("Update Class");
      deleteClassButton = createButton("Delete Class");
      viewClassButton = createButton("View Class Data");
      removeStudentButton = createButton("Remove Student");
      sortByGradeButton = createButton("Sort by Grade");
      sortBySubjectButton = createButton("Sort by Subject");
      viewStudentClassesButton = createButton("View Student Classes");
      viewTeacherClassesButton = createButton("View Teacher Classes");

      buttonPanel.add(addClassButton);
      buttonPanel.add(assignTeacherButton);
      buttonPanel.add(assignStudentsButton);
      buttonPanel.add(updateClassButton);
      buttonPanel.add(deleteClassButton);
      buttonPanel.add(viewClassButton);
      buttonPanel.add(removeStudentButton);
      buttonPanel.add(sortByGradeButton);
      buttonPanel.add(sortBySubjectButton);
      buttonPanel.add(viewStudentClassesButton);
      buttonPanel.add(viewTeacherClassesButton);

      add(buttonPanel, BorderLayout.SOUTH);

      // Button functionality
      setupAddClassButton();
      setupAssignTeacherButton();
      setupAssignStudentsButton();
      setupUpdateClassButton();
      setupDeleteClassButton();
      setupViewClassButton();
      setupRemoveStudentButton();
      setupViewStudentClassesButton();
      setupViewTeacherClassesButton();
      setupSortButtons();
  }

    private JButton createButton(String label) {
      String wrappedLabel = "<html><center>" + label.replace(" ", "<br>") + "</center></html>";
      JButton button = new JButton(wrappedLabel);
      button.setFont(new Font("Arial", Font.BOLD, 11));
      button.setPreferredSize(new Dimension(100, 50)); // Adjusted height to accommodate wrapped text
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
                if (parts.length >= 4) { // Ensure valid data
                    tableModel.addRow(new Object[]{parts[0], parts[1], parts[2], parts[3]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading classes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void setupAddClassButton() {
        addClassButton.addActionListener(e -> {
            ClassEntry classEntry = new ClassEntry(); // Opens the form for adding a new class
            classEntry.setVisible(true);
    
            // Listener to detect when the form is closed
            classEntry.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosed(java.awt.event.WindowEvent e) {
                    // Retrieve the created ClassInfo object
                    ClassInfo newClass = classEntry.getCreatedClass();
                    if (newClass != null) {
                        createAttendanceFile(newClass); // Create the attendance file
                    }
                    loadClasses(); // Reload the classes table to reflect updates
                }
            });
        });
    }
    
    // Method to create the attendance file for a new class
    private void createAttendanceFile(ClassInfo classInfo) {
        String classID = classInfo.getClassID();
        File attendanceFile = new File(classID + "_attendance.txt");
        File gradebookFile = new File(classID + "_gradebook.txt");
    
        try {
            // Create attendance file
            if (!attendanceFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFile))) {
                    writer.write("Student ID,Student Name");
                    writer.newLine();
    
                    ArrayList<String> students = classInfo.getStudents();
                    for (String student : students) {
                        String[] parts = student.split(" - ");
                        if (parts.length == 2) {
                            writer.write(parts[0] + "," + parts[1]); 
                            writer.newLine();
                        }
                    }
                }
            }
    
            // Create gradebook file
            if (!gradebookFile.exists()) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(gradebookFile))) {
                    writer.write("Student ID,Student Name,Average");
                    writer.newLine();
    
                    ArrayList<String> students = classInfo.getStudents();
                    for (String student : students) {
                        String[] parts = student.split(" - ");
                        if (parts.length == 2) {
                            // Initialize with default values
                            writer.write(parts[0] + "," + parts[1] + ",0,0,0,0"); 
                            writer.newLine();
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error creating attendance/gradebook files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    


    private void setupSortButtons() {
      // Sort by Grade button action
      sortByGradeButton.addActionListener(e -> sortClasses("grade"));
  
      // Sort by Subject button action
      sortBySubjectButton.addActionListener(e -> sortClasses("subject"));
    }

    private void sortClasses(String criteria) {
      ArrayList<String[]> classData = new ArrayList<>();
  
      // Read data from classes.txt
      try (BufferedReader reader = new BufferedReader(new FileReader("classes.txt"))) {
          String line;
          while ((line = reader.readLine()) != null) {
              String[] parts = line.split(",");
              if (parts.length >= 4) {
                  classData.add(parts);
              }
          }
      } catch (IOException e) {
          JOptionPane.showMessageDialog(this, "Error reading classes file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
          return;
      }
  
      // Sort the class data based on the selected criteria
      classData.sort((a, b) -> {
          if (criteria.equals("grade")) {
              return a[1].compareTo(b[1]); // Sort by Grade (index 1)
          } else if (criteria.equals("subject")) {
              return a[2].compareToIgnoreCase(b[2]); // Sort by Subject (index 2)
          }
          return 0;
      });
  
      // Update the table model with the sorted data
      tableModel.setRowCount(0); // Clear the table
      for (String[] classRow : classData) {
          tableModel.addRow(new Object[]{classRow[0], classRow[1], classRow[2], classRow[3]});
      }
  }



    private void setupAssignTeacherButton() {
        assignTeacherButton.addActionListener(e -> {
            int selectedRow = classesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a class to assign a teacher.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String classID = (String) tableModel.getValueAt(selectedRow, 0);

            // Load teachers
            ArrayList<String> teachers = loadTeachersFromFile();

            // Show dropdown for teachers
            String teacher = (String) JOptionPane.showInputDialog(this, "Select Teacher:", "Assign Teacher",
                    JOptionPane.QUESTION_MESSAGE, null, teachers.toArray(), teachers.isEmpty() ? null : teachers.get(0));

            if (teacher != null) {
                try {
                    ClassInfo classInfo = ClassInfo.loadFromFile(classID);
                    if (classInfo != null) {
                        classInfo.setTeacher(teacher);
                        classInfo.saveToFile();
                        JOptionPane.showMessageDialog(this, "Teacher assigned successfully!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error loading class details.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving teacher to class: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void setupAssignStudentsButton() {
        assignStudentsButton.addActionListener(e -> {
            int selectedRow = classesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a class to assign students.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            String classID = (String) tableModel.getValueAt(selectedRow, 0);
            String grade = classID.substring(4, 6); // Extract grade from class ID
            String gradeFile = "grade" + grade + ".txt";
    
            // Load students from the grade file
            ArrayList<String> students = loadBasicStudentData(gradeFile);
    
            // Load the class information and exclude students already assigned
            ClassInfo classInfo = ClassInfo.loadFromFile(classID);
            if (classInfo != null) {
                ArrayList<String> currentStudents = classInfo.getStudents();
                students.removeIf(currentStudents::contains); // Remove already assigned students
            } else {
                JOptionPane.showMessageDialog(this, "Error loading class details.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            // Create a list for selection
            JList<String> studentList = new JList<>(students.toArray(new String[0]));
            studentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    
            int result = JOptionPane.showConfirmDialog(this, new JScrollPane(studentList), "Select Students",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
            if (result == JOptionPane.OK_OPTION) {
                try {
                    ArrayList<String> addedStudents = new ArrayList<>();
    
                    for (String selectedStudent : studentList.getSelectedValuesList()) {
                        classInfo.addStudent(selectedStudent);
                        addedStudents.add(selectedStudent);
                    }
    
                    // Sort students by last name before saving
                    classInfo.setStudents(sortByLastName(classInfo.getStudents()));
    
                    // Save updated class file
                    classInfo.saveToFile();
    
                    // Update attendance file with new students
                    updateAttendanceAndGradebookFiles(classID, sortByLastName(addedStudents));
    
                    JOptionPane.showMessageDialog(this, "Students assigned successfully!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving students: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    // Helper method to sort students by last name
    private ArrayList<String> sortByLastName(ArrayList<String> students) {
        students.sort((a, b) -> {
            // Assumes format "StudentID - FirstName LastName"
            String lastNameA = a.substring(a.lastIndexOf(" ") + 1);
            String lastNameB = b.substring(b.lastIndexOf(" ") + 1);
            return lastNameA.compareToIgnoreCase(lastNameB);
        });
        return students;
    }
    
    // Method to update the attendance file with new students
    private void updateAttendanceAndGradebookFiles(String classID, ArrayList<String> addedStudents) {
        File attendanceFile = new File(classID + "_attendance.txt");
        File gradebookFile = new File(classID + "_gradebook.txt");
    
        // Read existing attendance and gradebook records to avoid duplicates
        ArrayList<String> existingStudentIDs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                existingStudentIDs.add(parts[0]);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading attendance file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Append new students to attendance file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFile, true))) {
            for (String student : addedStudents) {
                String[] parts = student.split(" - ");
                if (parts.length == 2 && !existingStudentIDs.contains(parts[0])) {
                    writer.write(parts[0] + "," + parts[1]); // Default attendance entry
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating attendance file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        // Append new students to gradebook file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(gradebookFile, true))) {
            for (String student : addedStudents) {
                String[] parts = student.split(" - ");
                if (parts.length == 2 && !existingStudentIDs.contains(parts[0])) {
                    // Initialize with default grade values
                    writer.write(parts[0] + "," + parts[1] + ",0,0,0,0");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating gradebook file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    
  

   private ArrayList<String> loadBasicStudentData(String gradeFile) {
    ArrayList<String> basicStudentData = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(gradeFile))) {
        String line;
        while ((line = reader.readLine()) != null) {
            studentinfo student = studentinfo.fromCSV(line); // Parse the student from the CSV line
            if (student != null) {
                // Create a string containing only the ID, First Name, and Last Name
                basicStudentData.add(student.getstudentID() + " - " + student.getfirstname() + " " + student.getlastname());
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error loading students: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    return basicStudentData;
}

private void setupUpdateClassButton() {
    updateClassButton.addActionListener(e -> {
        int selectedRow = classesTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a class to update.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String oldClassID = (String) tableModel.getValueAt(selectedRow, 0);
        ClassInfo classInfo = ClassInfo.loadFromFile(oldClassID);

        if (classInfo != null) {
            JFrame updateFrame = new JFrame("Update Class Details");
            updateFrame.setSize(400, 600);
            updateFrame.setLayout(new GridLayout(7, 2, 10, 10));

            JTextField classIDField = new JTextField(oldClassID);
            classIDField.setEditable(false);

            JComboBox<String> subjectDropdown = new JComboBox<>(new String[]{"Physics", "Math", "Biology", "Chemistry"});
            subjectDropdown.setSelectedItem(classInfo.getSubject());

            JComboBox<String> gradeLevelDropdown = new JComboBox<>(new String[]{"10", "11", "12", "13"});
            gradeLevelDropdown.setSelectedItem(classInfo.getGradeLevel());

            JComboBox<String> examTypeDropdown = new JComboBox<>(new String[]{"CSEC", "CAPE", "GCSE"});
            examTypeDropdown.setSelectedItem(classInfo.getExamType());

            JComboBox<String> teacherDropdown = new JComboBox<>(loadTeachersFromFile().toArray(new String[0]));
            teacherDropdown.setSelectedItem(classInfo.getTeacher());

            JTextArea studentsArea = new JTextArea(String.join("\n", classInfo.getStudents()));
            studentsArea.setEditable(false);
            JScrollPane studentScrollPane = new JScrollPane(studentsArea);

            updateFrame.add(new JLabel("Class ID:"));
            updateFrame.add(classIDField);
            updateFrame.add(new JLabel("Subject:"));
            updateFrame.add(subjectDropdown);
            updateFrame.add(new JLabel("Grade Level:"));
            updateFrame.add(gradeLevelDropdown);
            updateFrame.add(new JLabel("Exam Type:"));
            updateFrame.add(examTypeDropdown);
            updateFrame.add(new JLabel("Teacher:"));
            updateFrame.add(teacherDropdown);
            updateFrame.add(new JLabel("Students (read-only):"));
            updateFrame.add(studentScrollPane);

            JButton saveButton = new JButton("Save Changes");
            JButton cancelButton = new JButton("Cancel");

            updateFrame.add(saveButton);
            updateFrame.add(cancelButton);

            // ActionListener to dynamically update Class ID
            ActionListener updateIDListener = evt -> {
                String newSubject = (String) subjectDropdown.getSelectedItem();
                String newGradeLevel = (String) gradeLevelDropdown.getSelectedItem();
                String newExamType = (String) examTypeDropdown.getSelectedItem();
                String newClassID = generateNewClassID(newSubject, newGradeLevel, newExamType);
                classIDField.setText(newClassID);
            };

            subjectDropdown.addActionListener(updateIDListener);
            gradeLevelDropdown.addActionListener(updateIDListener);
            examTypeDropdown.addActionListener(updateIDListener);

            // Save button listener
            saveButton.addActionListener(saveEvent -> {
                String newClassID = classIDField.getText();
                String newSubject = (String) subjectDropdown.getSelectedItem();
                String newGradeLevel = (String) gradeLevelDropdown.getSelectedItem();
                String newExamType = (String) examTypeDropdown.getSelectedItem();
                String newTeacher = (String) teacherDropdown.getSelectedItem();
                

                boolean classIDChanged = !oldClassID.equals(newClassID);

                // Update class info
                classInfo.setSubject(newSubject);
                classInfo.setGradeLevel(newGradeLevel);
                classInfo.setExamType(newExamType);
                classInfo.setTeacher(newTeacher);
                
                try {
                    // Save updated class info to the new class file
                    classInfo.setClassID(newClassID);
                    classInfo.getStudents().clear();;
                    classInfo.saveToFile();

                    // Update classes.txt to reflect the class change
                    updateClassesFile(oldClassID, newClassID, newSubject, newGradeLevel, newExamType); // Pass all updated values


                    // If class ID has changed, delete the old class and attendance files
                    if (classIDChanged) {
                        deleteOldClassFile(oldClassID);
                        deleteOldFiles(oldClassID);
                    }

                    // Create new attendance file for the updated class
                    createNewAttendanceAndGradebookFiles(newClassID);

                    // Synchronize attendance file if needed
                    //synchronizeAttendanceFile(newClassID, newTeacher, classInfo);

                    // Show success message
                    JOptionPane.showMessageDialog(updateFrame, "Class updated successfully!");
                    loadClasses(); // Reload the classes table
                    updateFrame.dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(updateFrame, "Error saving updated class: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace(); // Print stack trace for debugging
                }
            });

            // Cancel button listener
            cancelButton.addActionListener(cancelEvent -> updateFrame.dispose());

            // Show the update frame
            updateFrame.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load class info.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    });
}


private void updateClassesFile(String oldClassID, String newClassID, String newSubject, String newGradeLevel, String newExamType) throws IOException {
    File classesFile = new File("classes.txt");
    List<String> lines = Files.readAllLines(classesFile.toPath());  // Reading the file into a list of strings
    List<String> updatedLines = new ArrayList<>();  // Creating a new list to store updated lines

    // Iterate over the lines in the classes.txt file
    for (String line : lines) {
        if (line.contains(oldClassID)) {
            // If the line contains the old class ID, replace the entire line with new class info
            updatedLines.add(newClassID + "," + newSubject + "," + newGradeLevel + "," + newExamType);
        } else {
            // Otherwise, keep the line unchanged
            updatedLines.add(line);
        }
    }

    // Write the updated lines back to the classes.txt file
    Files.write(classesFile.toPath(), updatedLines);
}





// Helper method to create a new attendance file with student IDs and names
private void createNewAttendanceAndGradebookFiles(String classID) throws IOException {
    File attendanceFile = new File(classID + "_attendance.txt");
    File gradebookFile = new File(classID + "_gradebook.txt");

    // Delete files if they already exist
    if (attendanceFile.exists()) attendanceFile.delete();
    if (gradebookFile.exists()) gradebookFile.delete();

    // Create new attendance file
    if (attendanceFile.createNewFile()) {
        List<String> attendanceContent = new ArrayList<>();
        attendanceContent.add("Student ID, Student Name");

        File classFile = new File(classID + ".txt");
        if (classFile.exists()) {
            List<String> studentLines = Files.readAllLines(classFile.toPath());

            for (String line : studentLines) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String studentID = parts[0].trim();
                    String studentName = parts[1].trim();
                    attendanceContent.add(studentID + ", " + studentName);
                }
            }
        }

        Files.write(attendanceFile.toPath(), attendanceContent, StandardCharsets.UTF_8);
    }

    // Create new gradebook file
    if (gradebookFile.createNewFile()) {
        List<String> gradebookContent = new ArrayList<>();
        gradebookContent.add("Student ID, Student Name, Assignments, Midterm, Final Exam, Overall Grade");

        File classFile = new File(classID + ".txt");
        if (classFile.exists()) {
            List<String> studentLines = Files.readAllLines(classFile.toPath());

            for (String line : studentLines) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String studentID = parts[0].trim();
                    String studentName = parts[1].trim();
                    // Initialize with default grade values
                    gradebookContent.add(studentID + ", " + studentName + ", 0, 0, 0, 0");
                }
            }
        }

        Files.write(gradebookFile.toPath(), gradebookContent, StandardCharsets.UTF_8);
    }
}



private void deleteOldClassFile(String oldClassID) {
    // Define the path where class files are stored (ensure this is the correct directory)
    String filePath = oldClassID + ".txt"; // Assumes the file name is <classID>.txt

    // Create a File object for the old class file
    File oldClassFile = new File(filePath);

    // Check if the file exists before trying to delete it
    if (oldClassFile.exists()) {
        if (oldClassFile.delete()) {
            System.out.println("Old class file " + oldClassID + " deleted successfully.");
        } else {
            System.out.println("Failed to delete old class file: " + oldClassID);
        }
    } else {
        System.out.println("Old class file does not exist: " + oldClassID);
    }
}


private void deleteOldFiles(String oldClassID) throws IOException {
    String attendanceFilePath = oldClassID + "_attendance.txt";
    String gradebookFilePath = oldClassID + "_gradebook.txt";
    
    File oldAttendanceFile = new File(attendanceFilePath);
    File oldGradebookFile = new File(gradebookFilePath);
    
    if (oldAttendanceFile.exists()) {
        if (!oldAttendanceFile.delete()) {
            throw new IOException("Failed to delete the old attendance file.");
        }
    }
    
    if (oldGradebookFile.exists()) {
        if (!oldGradebookFile.delete()) {
            throw new IOException("Failed to delete the old gradebook file.");
        }
    }
}



private void setupDeleteClassButton() { 
    deleteClassButton.addActionListener(e -> {
        int selectedRow = classesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a class to delete.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String classID = (String) tableModel.getValueAt(selectedRow, 0);

        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this class?", "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                // Delete the class file
                File classFile = new File(classID + ".txt");
                if (classFile.exists()) classFile.delete();

                // Delete the attendance file
                File attendanceFile = new File(classID + "_attendance.txt");
                if (attendanceFile.exists()) attendanceFile.delete();

                File gradebookFile = new File(classID + "_gradebook.txt");
                if (gradebookFile.exists()) gradebookFile.delete();


                // Update the classes.txt file to remove the deleted class
                ArrayList<String> updatedLines = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader("classes.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (!line.startsWith(classID)) updatedLines.add(line);
                    }
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter("classes.txt"))) {
                    for (String updatedLine : updatedLines) {
                        writer.write(updatedLine);
                        writer.newLine();
                    }
                }

                loadClasses();
                JOptionPane.showMessageDialog(this, "Class and associated attendance and gradebook files deleted successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting class: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });
}


    private void setupViewClassButton() {
        viewClassButton.addActionListener(e -> {
            int selectedRow = classesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a class to view.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String classID = (String) tableModel.getValueAt(selectedRow, 0);

            try (BufferedReader reader = new BufferedReader(new FileReader(classID + ".txt"))) {
                JTextArea textArea = new JTextArea();
                textArea.setEditable(false);
                String line;
                while ((line = reader.readLine()) != null) {
                    textArea.append(line + "\n");
                }
                JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Class Data", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading class data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void setupRemoveStudentButton() { 
        removeStudentButton.addActionListener(e -> {
            int selectedRow = classesTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a class.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
    
            String classID = (String) tableModel.getValueAt(selectedRow, 0);
            ClassInfo classInfo = ClassInfo.loadFromFile(classID);
    
            if (classInfo != null) {
                ArrayList<String> students = classInfo.getStudents();
                JList<String> studentList = new JList<>(students.toArray(new String[0]));
                studentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
                int result = JOptionPane.showConfirmDialog(this, new JScrollPane(studentList), "Select Student to Remove",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    
                if (result == JOptionPane.OK_OPTION) {
                    String selectedStudent = studentList.getSelectedValue();
                    if (selectedStudent != null) {
                        classInfo.removeStudent(selectedStudent);
                        try {
                            classInfo.saveToFile();
                            removeFromAttendanceFile(classID, selectedStudent); // Remove from attendance file
                            JOptionPane.showMessageDialog(this, "Student removed successfully!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Error removing student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }
    
    // Method to remove a student from the attendance file
    private void removeFromAttendanceFile(String classID, String studentInfo) {
        File attendanceFile = new File(classID + "_attendance.txt");
        if (attendanceFile.exists()) {
            try {
                ArrayList<String> updatedLines = new ArrayList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Skip the line if it belongs to the student being removed
                        if (!line.startsWith(studentInfo.split(" - ")[0])) {
                            updatedLines.add(line);
                        }
                    }
                }
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFile))) {
                    for (String updatedLine : updatedLines) {
                        writer.write(updatedLine);
                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error updating attendance file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
  

    private String generateNewClassID(String subject, String gradeLevel, String examType) {
        String subjectAbbreviation = subject.toUpperCase().substring(0, Math.min(subject.length(), 4));
        String randomPart = String.format("%02d", (int) (Math.random() * 100));
        String examAbbreviation = getExamTypeAbbreviation(examType);
        return subjectAbbreviation + gradeLevel + randomPart + examAbbreviation;
    }

    private ArrayList<String> loadTeachersFromFile() {
        ArrayList<String> teachers = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("teachers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) teachers.add(line.trim());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading teachers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return teachers;
    }
    private void updateClassesFileDirectly(String oldClassID, ClassInfo updatedClassInfo) throws IOException { 
        ArrayList<String> updatedLines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("classes.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(oldClassID)) {
                    // Replace the line that starts with oldClassID with the new class info
                    updatedLines.add(updatedClassInfo.getClassID() + "," +
                            updatedClassInfo.getGradeLevel() + "," +
                            updatedClassInfo.getSubject() + "," +
                            updatedClassInfo.getExamType());
                } else {
                    updatedLines.add(line);
                }
            }
        }
    
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("classes.txt"))) {
            // Write the updated class list back to the file
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        }
    }
    
    
    private String getExamTypeAbbreviation(String examType) {
        switch (examType) {
            case "CSEC":
                return "SE";
            case "CAPE":
                return "PE";
            case "GCSE":
                return "GE";
            default:
                return "";
        }
    }

    private void setupViewStudentClassesButton() {  
      viewStudentClassesButton.addActionListener(e -> {
          String studentID = JOptionPane.showInputDialog(this, "Enter Student ID:", "View Classes for Student", JOptionPane.QUESTION_MESSAGE);
          if (studentID == null || studentID.trim().isEmpty()) {
              return; // User canceled or entered invalid input
          }
  
          String studentName = ""; // Variable to store the student's name
          ArrayList<String> studentClasses = new ArrayList<>();
  
          // Retrieve student name and classes
          try (BufferedReader reader = new BufferedReader(new FileReader("classes.txt"))) {
              String line;
              while ((line = reader.readLine()) != null) {
                  String[] parts = line.split(",");
                  if (parts.length >= 1) {
                      String classID = parts[0];
                      ClassInfo classInfo = ClassInfo.loadFromFile(classID);
                      if (classInfo != null && classInfo.getStudents().stream().anyMatch(s -> s.startsWith(studentID))) {
                          // Extract student name from the class roster
                          for (String student : classInfo.getStudents()) {
                              if (student.startsWith(studentID)) {
                                  studentName = student.substring(student.indexOf("-") + 2); // Extract name from "ID - Name"
                                  break;
                              }
                          }
                          studentClasses.add(classID + " - " + classInfo.getSubject() + " (Grade: " + classInfo.getGradeLevel() + ")");
                      }
                  }
              }
          } catch (IOException ex) {
              JOptionPane.showMessageDialog(this, "Error reading classes file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              return;
          }
  
          // Display results
          if (studentClasses.isEmpty()) {
              JOptionPane.showMessageDialog(this, "No classes found for student ID: " + studentID, "No Classes Found", JOptionPane.INFORMATION_MESSAGE);
          } else {
              JTextArea textArea = new JTextArea("Student Name: " + studentName + "\n\nClasses:\n" + String.join("\n", studentClasses));
              textArea.setEditable(false);
              JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Classes for Student ID: " + studentID, JOptionPane.INFORMATION_MESSAGE);
          }
      });
    }
  
    private void setupViewTeacherClassesButton() {
      viewTeacherClassesButton.addActionListener(e -> {
          String teacherName = JOptionPane.showInputDialog(this, "Enter Teacher Name:", "View Classes for Teacher", JOptionPane.QUESTION_MESSAGE);
          if (teacherName == null || teacherName.trim().isEmpty()) {
              return; // User canceled or entered invalid input
          }
  
          ArrayList<String> teacherClasses = new ArrayList<>();
          try (BufferedReader reader = new BufferedReader(new FileReader("classes.txt"))) {
              String line;
              while ((line = reader.readLine()) != null) {
                  String[] parts = line.split(",");
                  if (parts.length >= 1) {
                      String classID = parts[0];
                      ClassInfo classInfo = ClassInfo.loadFromFile(classID);
                      if (classInfo != null && teacherName.equalsIgnoreCase(classInfo.getTeacher())) {
                          teacherClasses.add(classID + " - " + classInfo.getSubject() + " (Grade: " + classInfo.getGradeLevel() + ")");
                      }
                  }
              }
          } catch (IOException ex) {
              JOptionPane.showMessageDialog(this, "Error reading classes file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
              return;
          }
  
          // Display the teacher's classes
          if (teacherClasses.isEmpty()) {
              JOptionPane.showMessageDialog(this, "No classes found for teacher: " + teacherName, "No Classes Found", JOptionPane.INFORMATION_MESSAGE);
          } else {
              JTextArea textArea = new JTextArea("Teacher Name: " + teacherName + "\n\nClasses:\n" + String.join("\n", teacherClasses));
              textArea.setEditable(false);
              JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Classes for Teacher: " + teacherName, JOptionPane.INFORMATION_MESSAGE);
          }
      });
  }

  


  private void synchronizeAttendanceFile(String newClassID, String newTeacher, ClassInfo classInfo) {
    File attendanceFile = new File(newClassID + "_attendance.txt");
  

    // Ensure that we have a valid list of students
    List<String> currentStudents = classInfo.getStudents();
    if (currentStudents == null || currentStudents.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No students found for this class.", "Error", JOptionPane.ERROR_MESSAGE);
        return; // Exit early if no students
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFile))) {
        // Write the updated attendance file
        writer.write("Student ID,Student Name,Attendance"); // Header
        writer.newLine();

        for (String student : currentStudents) {
            String[] studentParts = student.split(" - ");  // Assumes student format is "ID - Name"
            if (studentParts.length == 2) {
                String studentRecord = studentParts[0] + "," + studentParts[1] + ",Absent"; // Default attendance is Absent
                writer.write(studentRecord);
                writer.newLine();
            }
        }
    } catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Error writing attendance file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

   
}



  public static void main(String[] args) {
    JFrame frame = new JFrame("Class Management System");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(900, 600);
    frame.add(new ClassManagement());
    frame.setVisible(true);
}
}
