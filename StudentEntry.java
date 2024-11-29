import javax.swing.*;
import java.awt.*;


public class StudentEntry extends JFrame {
    private JTextField txtStudentID, txtFirstName, txtLastName, txtDOB, txtSchool;
    private JComboBox<String> cmbGrade;
    private JTextField txtContactNumber, txtEmail, txtAddress;
    private JTextField txtGuardianFirstName, txtGuardianLastName, txtGuardianRelation, txtGuardianPhone, txtGuardianEmail;
    private JButton saveButton, cancelButton;

    private studentinfo student;
    private StudentManagement management;

    public StudentEntry(studentinfo student, StudentManagement management) {
        this.student = student;
        this.management = management;

        setTitle(student == null ? "Add New Student" : "Update Student");
        setLayout(new BorderLayout());
        setSize(500, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel studentInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        JLabel studentInfoHeading = new JLabel("Student Information");
        studentInfoHeading.setFont(new Font("Arial", Font.BOLD, 16)); 
        studentInfoHeading.setForeground(new Color(65, 105, 225)); 
        studentInfoPanel.add(studentInfoHeading);
        formPanel.add(studentInfoPanel); 

        JPanel studentIDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        studentIDPanel.add(new JLabel("Student ID:"));
        txtStudentID = new JTextField(20);
        txtStudentID.setEditable(false); 
        studentIDPanel.add(txtStudentID);
        formPanel.add(studentIDPanel);

        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("First Name:"));
        txtFirstName = new JTextField(10);
        namePanel.add(txtFirstName);
        namePanel.add(new JLabel("Last Name:"));
        txtLastName = new JTextField(10);
        namePanel.add(txtLastName);
        formPanel.add(namePanel);

        JPanel dobPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dobPanel.add(new JLabel("Date of Birth (YYYY/MM/DD):"));
        txtDOB = new JTextField(20);
        dobPanel.add(txtDOB);
        formPanel.add(dobPanel);

        JPanel gradeSchoolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gradeSchoolPanel.add(new JLabel("Grade:"));
        cmbGrade = new JComboBox<>(new String[]{"10", "11", "12", "13"});
        gradeSchoolPanel.add(cmbGrade);
        gradeSchoolPanel.add(new JLabel("Current School:"));
        txtSchool = new JTextField(15);
        gradeSchoolPanel.add(txtSchool);
        formPanel.add(gradeSchoolPanel);

        JPanel contactInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contactInfoPanel.add(new JLabel("Contact Number:"));
        txtContactNumber = new JTextField(10);
        contactInfoPanel.add(txtContactNumber);
        contactInfoPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField(15);
        contactInfoPanel.add(txtEmail);
        formPanel.add(contactInfoPanel);

        JPanel addressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addressPanel.add(new JLabel("Address:"));
        txtAddress = new JTextField(30);
        addressPanel.add(txtAddress);
        formPanel.add(addressPanel);

        JPanel guardianInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));     
        JLabel guardianInfoHeading = new JLabel("Guardian Information");
        guardianInfoHeading.setFont(new Font("Arial", Font.BOLD, 16));  
        guardianInfoHeading.setForeground(new Color(65, 105, 225));     
        guardianInfoPanel.add(guardianInfoHeading);     
        formPanel.add(guardianInfoPanel); 

        JPanel guardianNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        guardianNamePanel.add(new JLabel("Guardian First Name:"));
        txtGuardianFirstName = new JTextField(10);
        guardianNamePanel.add(txtGuardianFirstName);
        guardianNamePanel.add(new JLabel("Guardian Last Name:"));
        txtGuardianLastName = new JTextField(10);
        guardianNamePanel.add(txtGuardianLastName);
        formPanel.add(guardianNamePanel);

        JPanel guardianRelationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        guardianRelationPanel.add(new JLabel("Relation:"));
        txtGuardianRelation = new JTextField(10);
        guardianRelationPanel.add(txtGuardianRelation);
        formPanel.add(guardianRelationPanel);

        JPanel guardianContactPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        guardianContactPanel.add(new JLabel("Phone:"));
        txtGuardianPhone = new JTextField(10);
        guardianContactPanel.add(txtGuardianPhone);
        guardianContactPanel.add(new JLabel("Email:"));
        txtGuardianEmail = new JTextField(15);
        guardianContactPanel.add(txtGuardianEmail);
        formPanel.add(guardianContactPanel);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        if (student != null) {
            loadStudentData();
        } else {
            txtStudentID.setText(management.generateUniqueID()); 
        }
        saveButton.addActionListener(e -> saveStudent());
        cancelButton.addActionListener(e -> dispose());
    }

    private void loadStudentData() {
        txtStudentID.setText(student.getstudentID());
        txtFirstName.setText(student.getfirstname());
        txtLastName.setText(student.getlastname());
        txtDOB.setText(student.getDob());
        cmbGrade.setSelectedItem(student.getCurrentGrade());
        txtSchool.setText(student.getCurrentSchool());

        contactinfo contact = student.getcontactinfo();
        if (contact != null) {
            txtContactNumber.setText(contact.gettelnum());
            txtEmail.setText(contact.getEmail());
            txtAddress.setText(contact.getaddress());
        }

        guardianinfo guardian = student.getguardianinfo();
        if (guardian != null) {
            txtGuardianFirstName.setText(guardian.getguardianfName());
            txtGuardianLastName.setText(guardian.getguardianlName());
            txtGuardianRelation.setText(guardian.getrelation());
            txtGuardianPhone.setText(guardian.gettelnum());
            txtGuardianEmail.setText(guardian.getEmail());
        }
    }

    private void saveStudent() {
        try {
            String firstName = txtFirstName.getText().trim();
            String lastName = txtLastName.getText().trim();
            String dob = txtDOB.getText().trim();
            String grade = (String) cmbGrade.getSelectedItem();
            String school = txtSchool.getText().trim();
    
            String contactNumber = txtContactNumber.getText().trim();
            String email = txtEmail.getText().trim();
            String address = txtAddress.getText().trim();
    
            String guardianFirstName = txtGuardianFirstName.getText().trim();
            String guardianLastName = txtGuardianLastName.getText().trim();
            String guardianRelation = txtGuardianRelation.getText().trim();
            String guardianPhone = txtGuardianPhone.getText().trim();
            String guardianEmail = txtGuardianEmail.getText().trim();
    
            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty() || grade == null || school.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (student == null) {
                contactinfo contact = new contactinfo(contactNumber, email, address);
                guardianinfo guardian = new guardianinfo(guardianFirstName, guardianLastName, guardianRelation, guardianPhone, guardianEmail);
                studentinfo newStudent = new studentinfo(txtStudentID.getText().trim(), firstName, lastName, dob, grade, school, contact, guardian);
                management.addStudentToGrade(newStudent);
            } else { 
                String oldGrade = student.getCurrentGrade(); 
                student.setfirstname(firstName);
                student.setlastname(lastName);
                student.setDob(dob);
                student.setCurrentGrade(grade);
                student.setCurrentSchool(school);
    
                contactinfo contact = student.getcontactinfo();
                contact.updateContactinfo(contactNumber, email, address);
    
                guardianinfo guardian = student.getguardianinfo();
                guardian.updateguardianinfo(guardianFirstName, guardianLastName, guardianRelation, guardianPhone, guardianEmail);
    
                management.updateStudentGradeIfChanged(student, oldGrade);
            }
    
            JOptionPane.showMessageDialog(this, "Student saved successfully!");
            management.refreshTables(); 
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
