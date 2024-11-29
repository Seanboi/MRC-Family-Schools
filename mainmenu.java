import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Driver;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import java.util.Collections;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class mainmenu extends JFrame{

    private JPanel MainPanel;
    private JPanel Buttonpanel;
    private JButton studentbtn, classbtn, gradebtn, staffbtn, reportbtn, attendbtn;


    public mainmenu(){

    MainPanel = new JPanel();
    MainPanel.setLayout(new BoxLayout(MainPanel,BoxLayout.Y_AXIS));
    MainPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 2));

    Buttonpanel = new JPanel();
    Buttonpanel.setLayout(new BoxLayout(Buttonpanel,BoxLayout.Y_AXIS));
    Buttonpanel.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 2));

    setTitle("Main Menu");
    setLayout(new BorderLayout());
    setSize(400, 300);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Title label
    JLabel lblTitle = new JLabel("Welcome to the Main Menu",SwingConstants.CENTER);
    lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
    lblTitle.setOpaque(true);
    lblTitle.setBackground(new Color(0, 70, 140));
    lblTitle.setForeground(Color.WHITE);
    lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
    MainPanel.add(lblTitle);

    studentbtn = new JButton("Student Management");
    studentbtn.setFont(new Font("Arial", Font.BOLD, 14));
    studentbtn.setPreferredSize(new Dimension(200, 200));
    studentbtn.setForeground(Color.WHITE);
    studentbtn.setBackground(new Color(0, 120, 215)); 
    studentbtn.setFocusPainted(false);

    classbtn = new JButton("Class Management");
    classbtn.setFont(new Font("Arial", Font.BOLD, 14));
    classbtn.setPreferredSize(new Dimension(200, 200));
    classbtn.setForeground(Color.WHITE);
    classbtn.setBackground(new Color(0, 120, 215)); 
    classbtn.setFocusPainted(false);

    gradebtn = new JButton("Grade Management");
    gradebtn.setFont(new Font("Arial", Font.BOLD, 14));
    gradebtn.setPreferredSize(new Dimension(200, 200));
    gradebtn.setForeground(Color.WHITE);
    gradebtn.setBackground(new Color(0, 120, 215)); 
    gradebtn.setFocusPainted(false);

    attendbtn = new JButton("Attendance Management");
    attendbtn.setFont(new Font("Arial", Font.BOLD, 14));
    attendbtn.setPreferredSize(new Dimension(200, 200));
    attendbtn.setForeground(Color.WHITE);
    attendbtn.setBackground(new Color(0, 120, 215)); 
    attendbtn.setFocusPainted(false);

    staffbtn = new JButton("Staff Management");
    staffbtn.setFont(new Font("Arial", Font.BOLD, 14));
    staffbtn.setPreferredSize(new Dimension(200, 200));
    staffbtn.setForeground(Color.WHITE);
    staffbtn.setBackground(new Color(0, 120, 215)); 
    staffbtn.setFocusPainted(false);

    reportbtn = new JButton("Report Analytics");
    reportbtn.setFont(new Font("Arial", Font.BOLD, 14));
    reportbtn.setPreferredSize(new Dimension(200, 200));
    reportbtn.setForeground(Color.WHITE);
    reportbtn.setBackground(new Color(0, 120, 215)); 
    reportbtn.setFocusPainted(false);

    Buttonpanel.add(studentbtn);
    Buttonpanel.add(classbtn);
    Buttonpanel.add(gradebtn);
    Buttonpanel.add(attendbtn);
    Buttonpanel.add(staffbtn);
    Buttonpanel.add(reportbtn);

    studentbtn.addActionListener(new Stdbuttonlistener());
    classbtn.addActionListener(new classListener());
    gradebtn.addActionListener(new gradeListener());
    attendbtn.addActionListener(new attendListener());
    staffbtn.addActionListener(new StaffListener());
    reportbtn.addActionListener(new reportListener());

    
    add(Buttonpanel,BorderLayout.CENTER);
    add(MainPanel,BorderLayout.NORTH);

    }

    private class Stdbuttonlistener implements ActionListener{
        public void actionPerformed(ActionEvent e){
           openStudentManagement();
            }
        }

    private void openStudentManagement() {
        StudentManagement s = new StudentManagement();
        JFrame studFrame = new JFrame("Student Management");
        studFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        studFrame.setSize(900, 600);
        studFrame.add(s);
        studFrame.setVisible(true);
            }
        

    private class classListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            openClassManagement();
                }
            }

    private void openClassManagement() {
        ClassManagement c = new ClassManagement();
        JFrame classFrame = new JFrame("Class Management");
        classFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        classFrame.setSize(900, 600);
        classFrame.add(c);
        classFrame.setVisible(true);
            }
            
    
            
    private class gradeListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
                openGradeManagement();
                }
            }

    private void openGradeManagement() {
        GradeManagement gradeManagement = new GradeManagement();
        JFrame gradeFrame = new JFrame("Grade Management");
        gradeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gradeFrame.setSize(900, 600);
        gradeFrame.add(gradeManagement);
        gradeFrame.setVisible(true);
            }
        
            
    private class attendListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            openattendManagement();
                }
            }

    private void openattendManagement() {
        AttendanceManagement a = new AttendanceManagement();
        JFrame attendFrame = new JFrame("Attendance Management");
        attendFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        attendFrame.setSize(900, 600);
        attendFrame.add(a);
        attendFrame.setVisible(true);
            }

    private class StaffListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            openStaffManagement();
                }
            }

    private void openStaffManagement() {
        StaffManager s =  new StaffManager();
        JFrame staffFrame = new JFrame("Staff Management");
        staffFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        staffFrame.setSize(900, 600);
        staffFrame.add(s);
        staffFrame.setVisible(true);
            }
            
    private class reportListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            openReportManagement();
            }            
        }

    private void openReportManagement() {
        GradeReport r = new GradeReport();
        JFrame reportFrame = new JFrame("Report Management");
        reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reportFrame.setSize(900, 600);
        reportFrame.add(r);
        reportFrame.setVisible(true);
        }
    }




  
