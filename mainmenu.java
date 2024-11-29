import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class mainmenu extends JFrame {
    private JPanel MainPanel;
    private JButton studentbtn, classbtn, gradebtn, staffbtn, reportbtn, attendbtn;

    public mainmenu() {
        setTitle("School Management System");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with white background
        MainPanel = new JPanel(new BorderLayout());
        MainPanel.setBackground(Color.WHITE);
        MainPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 2));

        // Title Panel
        JLabel titleLabel = new JLabel("WELCOME TO THE MAIN MENU");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(0, 70, 140));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        MainPanel.add(titleLabel, BorderLayout.NORTH);

        // Grid Panel for buttons
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        gridPanel.setBackground(Color.WHITE);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        // Create buttons with unique styles
        studentbtn = createStudentButton();
        classbtn = createClassButton();
        attendbtn = createAttendanceButton();
        gradebtn = createGradeButton();
        staffbtn = createStaffButton();
        reportbtn = createReportButton();

        // Add buttons to grid
        gridPanel.add(studentbtn);
        gridPanel.add(classbtn);
        gridPanel.add(attendbtn);
        gridPanel.add(gradebtn);
        gridPanel.add(staffbtn);
        gridPanel.add(reportbtn);

        MainPanel.add(gridPanel, BorderLayout.CENTER);

        // Add action listeners
        studentbtn.addActionListener(e -> openStudentManagement());
        classbtn.addActionListener(e -> openClassManagement());
        gradebtn.addActionListener(e -> openGradeManagement());
        attendbtn.addActionListener(e -> openattendManagement());
        staffbtn.addActionListener(e -> openStaffManagement());
        reportbtn.addActionListener(e -> openReportManagement());

        add(MainPanel);
    }

    // Individual button creation methods with unique styles
    private JButton createStudentButton() {
        JButton button = new JButton("<html><center>STUDENT<br>MANAGEMENT</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 70, 140)); // Royal blue
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 150));
        return button;
    }

    private JButton createClassButton() {
        JButton button = new JButton("<html><center>CLASS<br>MANAGEMENT</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 90, 170)); // Slightly lighter blue
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 150));
        return button;
    }

    private JButton createAttendanceButton() {
        JButton button = new JButton("<html><center>MARK<br>ATTENDANCE</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 110, 200)); // Light blue
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 150));
        return button;
    }

    private JButton createGradeButton() {
        JButton button = new JButton("<html><center>GRADE<br>MANAGEMENT</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 130, 230)); // Bright blue
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 150));
        return button;
    }

    private JButton createStaffButton() {
        JButton button = new JButton("<html><center>STAFF<br>MANAGEMENT</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 150, 255)); // Sky blue
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 150));
        return button;
    }

    private JButton createReportButton() {
        JButton button = new JButton("<html><center>ANALYTICS &<br>REPORTS</center></html>");
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(30, 170, 255)); // Light sky blue
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(250, 150));
        return button;
    }

    // Rest of the management window opening methods remain the same...
    private void openStudentManagement() {
        StudentManagement s = new StudentManagement();
        openManagementWindow("Student Management", s);
    }

    private void openClassManagement() {
        ClassManagement c = new ClassManagement();
        openManagementWindow("Class Management", c);
    }

    private void openGradeManagement() {
        GradeManagement g = new GradeManagement();
        openManagementWindow("Grade Management", g);
    }

    private void openattendManagement() {
        AttendanceManagement a = new AttendanceManagement();
        openManagementWindow("Attendance Management", a);
    }

    private void openStaffManagement() {
        StaffManager s = new StaffManager();
        openManagementWindow("Staff Management", s);
    }

    private void openReportManagement() {
        GradeReport r = new GradeReport();
        openManagementWindow("Report Management", r);
            }
        
            private void openManagementWindow(String title, GradeReport r) {
                // TODO Auto-generated method stub
                throw new UnsupportedOperationException("Unimplemented method 'openManagementWindow'");
            }
        
            // Helper method to open management windows
    private void openManagementWindow(String title, JComponent component) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.add(component);
        frame.setVisible(true);
    }
}