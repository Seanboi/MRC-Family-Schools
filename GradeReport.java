import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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



public class GradeReport extends JFrame {

    private JPanel pnlCommand;
    private JPanel searchpnlDisplay;
    private JPanel inputPanel;
    private JPanel reportPanel;
    private JPanel GradePanel;
    private GradeReport thisClass;
    private ArrayList<studentinfo> students;
    private ArrayList<studentinfo> students1;
    private ArrayList<studentinfo> students2;
    private ArrayList<studentinfo> students3;
    private ArrayList<studentinfo> students4;
    private ArrayList<ClassInfo> classList;
    private JTextField searchField; 
    private JButton searchID;
    private JButton searchname;
    private JFrame reportFrame;
    private String gradebookfile;
    private String attendancefile;
    private JButton backButton;

    


    public GradeReport(){
        setLayout(new BorderLayout());
        thisClass = this;
        students = new ArrayList<>();

        students1 = gatherStudentInfo("grade10.txt");
        students2 = gatherStudentInfo("grade11.txt");
        students3 = gatherStudentInfo("grade12.txt");
        students4 = gatherStudentInfo("grade13.txt");

        for(studentinfo s: students1){
            students.add(s);
        }

        for(studentinfo s: students2){
            students.add(s);
        }

        for(studentinfo s: students3){
            students.add(s);
        }

        for(studentinfo s: students4){
            students.add(s);
        }



        setSize(800,600);

        inputPanel = new JPanel();
        reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel,BoxLayout.Y_AXIS)); 
        reportPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 2));
        pnlCommand = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlCommand.setBackground(new Color(0, 70, 140));
        searchpnlDisplay = new JPanel(new BorderLayout());
        
        searchField = new JTextField(20);
        JLabel titleLabel = new JLabel("MRC REPORT SEARCH", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 70, 140)); 
        titleLabel.setForeground(Color.WHITE); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        inputPanel.add(new JLabel("Search for student by ID or Enter First and Last Name"));
        inputPanel.add(searchField);
        
        searchID = new JButton("Search by ID");
        searchID.setFont(new Font("Arial", Font.BOLD, 14));
        searchID.setPreferredSize(new Dimension(180, 40));
        searchID.setForeground(Color.WHITE);
        searchID.setBackground(new Color(0, 120, 215)); 
        searchID.setFocusPainted(false);

        searchname = new JButton("Search by Name");
        searchname.setFont(new Font("Arial", Font.BOLD, 14));
        searchname.setPreferredSize(new Dimension(180, 40));
        searchname.setForeground(Color.WHITE);
        searchname.setBackground(new Color(0, 120, 215)); 
        searchname.setFocusPainted(false);

        
        backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setPreferredSize(new Dimension(180, 40));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0, 120, 215));
        backButton.setFocusPainted(false);



        searchID.addActionListener(new SearchIDListener());
        searchname.addActionListener(new SearchnameListener());
        backButton.addActionListener(e -> {
            dispose();  
            new mainmenu().setVisible(true); 
        });
        
        

        pnlCommand.add(searchID);
        pnlCommand.add(searchname);
        pnlCommand.add(backButton);
        


        inputPanel.add(searchField);

         
        JScrollPane scrollPane = new JScrollPane(reportPanel);
        searchpnlDisplay.add(inputPanel, BorderLayout.NORTH);
        searchpnlDisplay.add(pnlCommand, BorderLayout.CENTER);
        searchpnlDisplay.add(reportPanel,BorderLayout.SOUTH);
        searchpnlDisplay.setBorder(BorderFactory.createLineBorder(new Color(0, 70, 140), 2));
        //searchpnlDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        add(searchpnlDisplay);
        pack();
        setVisible(true);

        

        
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


    public ArrayList<studentinfo> getstudents(){
        return students;
    }


    private studentinfo StudentIDSearch(String studentID){
        for (studentinfo s:students){
            if (s.getstudentID().equals(studentID)){
                return s;
            }
        }
        
        return null;
    }

    private studentinfo StudentnameSearch(String name){
        for (studentinfo s:students){
            if (s.getfirstname().equalsIgnoreCase(name) || s.getlastname().equalsIgnoreCase(name)){
                return s;
            }
        }
        
        return null;
    }

    private void createReport(studentinfo s){
        reportPanel.removeAll(); 
            // Clear the panel 
        if (s == null) { 
            JLabel noResultLabel = new JLabel("No student found with the given details.",SwingConstants.CENTER);
            noResultLabel.setFont(new Font("Arial",Font.BOLD,24));
            noResultLabel.setOpaque(true); 
            reportPanel.add(noResultLabel, BorderLayout.CENTER); 
        } else { 
            
            JLabel separator = new JLabel("----------------------------------------------------------------------",SwingConstants.CENTER); 
            separator.setFont(new Font("Arial", Font.PLAIN, 20));
            separator.setOpaque(true);
            reportPanel.add(separator);

            JLabel studentIDLabel = new JLabel("ID: " + s.getstudentID(),SwingConstants.CENTER); 
            studentIDLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            studentIDLabel.setOpaque(true);
            reportPanel.add(studentIDLabel);

            JLabel studentNameLabel = new JLabel("Name: " + s.getfirstname() + " " + s.getlastname(),SwingConstants.CENTER); 
            studentNameLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            studentNameLabel.setOpaque(true);
            reportPanel.add(studentNameLabel); 

            JLabel dobLabel = new JLabel("Date of Birth: " + s.getDob(),SwingConstants.CENTER);
            dobLabel.setFont(new Font("Arial", Font.PLAIN, 20));
            dobLabel.setOpaque(true);
            reportPanel.add(dobLabel);

            JLabel schooLabel = new JLabel("School: " + s.getCurrentSchool());
            schooLabel.setFont(new Font("Arial",Font.PLAIN,20));
            schooLabel.setOpaque(true);
            reportPanel.add(schooLabel);
            
            JLabel gradeLabel = new JLabel("Grade: " + s.getCurrentGrade());
            gradeLabel.setFont(new Font("Arial",Font.PLAIN,20));
            gradeLabel.setOpaque(true);
            reportPanel.add(schooLabel);

            /*JLabel gradesLabel = new JLabel("Grades: " + getStudentGrades(s.getstudentID()) ,SwingConstants.CENTER);
            gradesLabel.setFont(new Font("Arial", Font.BOLD, 20));
            gradesLabel.setOpaque(true); 
            reportPanel.add(gradesLabel); */


        }
    
        reportPanel.revalidate(); 
        reportPanel.repaint();
        
        repoFrame(); 
        

        
    }

    private void repoFrame(){
        JLabel titleLabel = new JLabel("RESULTS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 70, 140)); 
        titleLabel.setForeground(Color.WHITE); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Create a new JFrame to display the report 
        JFrame reportFrame = new JFrame("Student Report"); 
        reportFrame.setSize(400, 300); 
        reportFrame.setLayout(new BorderLayout());
        reportFrame.add(titleLabel, BorderLayout.NORTH);
        reportFrame.add(reportPanel, BorderLayout.CENTER);
        reportFrame.setVisible(true); 
        reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
    }

    public void compareStudentname(){}

    private ArrayList<studentinfo> gatherStudentInfo(String file)
    {       
        ArrayList<studentinfo>students = new ArrayList<studentinfo>();

        Scanner fscan = null;
        try{
            //for (int i=0; i<files.length; i++){
                fscan = new Scanner(new File(file));
                while(fscan.hasNext()){
                    String [] nextLine = fscan.nextLine().split(",");
                    String studID = nextLine[0];
                    String fName = nextLine[1];
                    String lName = nextLine[2];
                    String dob = nextLine[3];
                    String age = nextLine[4];
                    String grade = nextLine[5];
                    String school = nextLine[6];
                    String contactnum = nextLine[7];
                    String email = nextLine[8];
                    String addr = nextLine[9];
                    String guardFName = nextLine[10];
                    String guardLName = nextLine[11];
                    String relation = nextLine[12];
                    String guardnum = nextLine[13];
                    String guardEmail = nextLine[14];

                    guardianinfo guardian = new guardianinfo(guardFName,guardLName,relation,guardnum,guardEmail);
                    contactinfo contact = new contactinfo(contactnum, email, addr);
                    studentinfo student = new studentinfo(studID, fName, lName, dob, grade, school, contact, guardian);

                    students.add(student);
                }

                fscan.close();
            //}  
        }
        catch(IOException ioe)
        {}
        return students;
    }

    private String getStudentGrades(String studentID) {
        StringBuilder grades = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(gradebookfile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].equals(studentID)) {
                    // Assuming grade data is in the second column (index 1)
                    grades.append(data[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return grades.toString();
    }

    // Method to fetch attendance from a file based on student ID
    private String getStudentAttendance(String studentID) {
        StringBuilder attendance = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(attendancefile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].equals(studentID)) {
                    // Assuming attendance data is in the second column (index 1)
                    attendance.append(data[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attendance.toString();
    }

    
        
        
      
    private static void createAndShowGUI() {
        new GradeReport().setVisible(true);

    }



    private class SearchIDListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            String value = searchField.getText().trim();
            studentinfo s = StudentIDSearch(value);
            createReport(s);
                }
            }



    private class SearchnameListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
        String value = searchField.getText().trim();
        studentinfo s = StudentnameSearch(value);
        createReport(s);
                }
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

