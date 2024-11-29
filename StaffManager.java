import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.io.*;

public class StaffManager extends JPanel{
    private JTable staffTable;
    private DefaultTableModel staffModel;
    private ArrayList<StaffInfo> staffList = new ArrayList<>();
    private HashSet<String> usedIDS = new HashSet<>();

    public StaffManager(){
        super(new BorderLayout());
    
        loadUsedIDs();

        staffList = loadStaffData("Staff.txt");
    
        String[] columnNames = {"Staff ID", "First Name", "Last Name", "DOB", "Position",
                                "Contact Number", "Email", "Address",
                                "Emergency Contact First Name", "Emergency Contact Last Name", "Emergency Contact Relation",
                                "Emergency Contact Phone", "Emergency Contact Email"};
        staffModel = new DefaultTableModel(columnNames, 0);
        staffTable = new JTable(staffModel);
    
        loadTableData(staffList, staffModel);
    
        JLabel titleLabel = new JLabel("MRC STAFF MANAGEMENT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 70, 140)); 
        titleLabel.setForeground(Color.WHITE); 
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
    
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(0, 70, 140)); 
    
        JButton addButton = createButton("Add Staff");
        JButton updateButton = createButton("Update Staff");
        JButton deleteButton = createButton("Delete Staff");
        JButton searchButton = createButton("Search by Staff ID");
    
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setupAddFunctionality(addButton);
        setupUpdateFunctionality(updateButton);
        setupDeleteFunctionality(deleteButton);
        setupSearchFunctionality(searchButton);
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

    private void loadUsedIDs() {
        String[] gradeFiles = {"Staff.txt"};
        for (String file : gradeFiles) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 0) {
                        usedIDS.add(parts[0].trim());
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
        } while (usedIDS.contains(id));
        usedIDS.add(id);
        return id;
    }

    private ArrayList<StaffInfo> loadStaffData(String filename) {
        ArrayList<StaffInfo> staffs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                StaffInfo student = StaffInfo.fromCSV(line);
                if (student != null) {
                    staffs.add(student);
                }
            }
            staffs.sort((s1, s2) -> s1.getLname().compareToIgnoreCase(s2.getLname()));
    
            saveStaffData(staffs, filename);
    
        } catch (IOException e) {
            System.err.println("Error loading data from " + filename + ": " + e.getMessage());
        }
        return staffs;
    }

    private void loadTableData(ArrayList<StaffInfo> staffs, DefaultTableModel model) {
        model.setRowCount(0); 
        for (StaffInfo student : staffs) {
            contactinfo contact = student.getContactInfo();
            EmergencyContact emergency = student.getemergencyContact();

            model.addRow(new Object[]{
                    student.getStaffID(),
                    student.getFname(),
                    student.getLname(),
                    student.getDateOfBirth(),
                    student.getPosition(),
                    contact != null ? contact.gettelnum() : "",
                    contact != null ? contact.getEmail() : "",
                    contact != null ? contact.getaddress() : "",
                    emergency != null ? emergency.getcontactName() : "",
                    emergency != null ? emergency.getrelationship() : "",
                    emergency != null ? emergency.getcontactinfo() : "",
            });
        }
    }

    private void saveStaffData(ArrayList<StaffInfo> staffsList, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (StaffInfo staff : staffsList) {
                writer.write(staff.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving data to " + filename + ": " + e.getMessage());
        }
    }

    //Method to add staff information
    public void addStaff(String staffID, String fName, String lName, String position, String dateOfBirth, contactinfo contactInfo, EmergencyContact emergencyContact) {
        StaffInfo newStaff = new StaffInfo(staffID, fName, lName, position, dateOfBirth, contactInfo, emergencyContact);
        staffList.add(newStaff);
        System.out.println("Staff added successfully: " + newStaff);
    }

    // Method to update existing staff information
    public void updateStaff(String staffID, String fName, String lName, String position, String dateOfBirth, contactinfo contactInfo, EmergencyContact emergencyContact) {
        boolean staffFound = false;
        for (StaffInfo staff : staffList) {
            if (staff.getStaffID().equals(staffID)) {
                staff.setFname(fName);
                staff.setLname(lName);
                staff.setPosition(position);
                staff.setDateOfBirth(dateOfBirth);
                staff.setContactInfo(contactInfo);
                staff.setEmegencyContact(emergencyContact);
                System.out.println("Staff updated successfully: " + staff);
                staffFound = true;
                break;
            }
        }
        if (!staffFound) {
            System.out.println("Staff with ID " + staffID + " not found.");
        }
    }

    private void setupAddFunctionality(JButton addButton) {
        addButton.addActionListener(e -> {
            StaffEntry form = new StaffEntry(null, this);
            form.setVisible(true);
        });
    }

    private void setupUpdateFunctionality(JButton updateButton) {
        updateButton.addActionListener(e -> {
            JTable selectedTable = getSelectedTable();
            if (selectedTable != null) {
                int selectedRow = selectedTable.getSelectedRow(); 
                if (selectedRow >= 0) {
                    String staffID = selectedTable.getValueAt(selectedRow, 0).toString();
                    StaffInfo selectedStaff = findStaffByID(staffID);
    
                    if (selectedStaff != null) {
                        StaffEntry updateForm = new StaffEntry(selectedStaff, this);
                        updateForm.setVisible(true);
    
                        updateForm.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                                refreshTables(); 
                            }
                        });
                    } else {
                        JOptionPane.showMessageDialog(this, "Staff not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a staff to update.", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No table is selected.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void setupDeleteFunctionality(JButton deleteButton) {
        deleteButton.addActionListener(e -> {
            String staffID = JOptionPane.showInputDialog(this, "Enter Staff ID to Delete:");
            if (staffID != null && !staffID.trim().isEmpty()) {
                StaffInfo staffToDelete = findStaffByID(staffID.trim());
                if (staffToDelete != null) {
                    deleteStaff(staffToDelete);
                    JOptionPane.showMessageDialog(this, "Staff deleted successfully.");
                    refreshTables();
                } else {
                    JOptionPane.showMessageDialog(this, "Staff ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void setupSearchFunctionality(JButton searchButton) {
        searchButton.addActionListener(e -> {
            String searchID = JOptionPane.showInputDialog(this, "Enter Staff ID to Search:");
            if (searchID != null && !searchID.trim().isEmpty()) {
                StaffInfo staff = findStaffByID(searchID.trim());
                if (staff != null) {
                    displayStaff(staff);
                } else {
                    JOptionPane.showMessageDialog(this, "Staff ID not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    public void refreshTables() {
        loadTableData(staffList, staffModel);
        saveStaffData(staffList, "Staff.txt");
        
    }

    private StaffInfo findStaffByID(String staffID) {
        for (StaffInfo staff : staffList) {
            if (staff.getStaffID().equals(staffID)) {
                return new StaffInfo(staff.getStaffID(), staff.getFname(), staff.getLname(),
                    staff.getDateOfBirth(), staff.getPosition(),staff.getContactInfo(), 
                    staff.getemergencyContact());
            }
        }
        return null;
    }

    private JTable getSelectedTable() {
        if (staffTable.getSelectedRow() >= 0) return staffTable;
        return null;
    }

    // Method to delete staff by ID
    public void deleteStaff(StaffInfo staffID) {
        boolean staffFound = false;
        for (int i = 0; i < staffList.size(); i++) {
            if (staffList.get(i).getStaffID().equals(staffID)) {
                staffList.remove(i);
                System.out.println("Staff with ID " + staffID + " deleted successfully.");
                staffFound = true;
                break;
            }
        }
        if (!staffFound) {
            System.out.println("Staff with ID " + staffID + " not found.");
        }
    }

    // Method to display all staff
    public void displayStaff(StaffInfo staffs) {
        if (staffList.isEmpty()) {
            System.out.println("No staff members to display.");
        } else {
            System.out.println("Staff List:");
            for (StaffInfo staff : staffList) {
                System.out.println(staff);
            }
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Class Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.add(new StaffManager());
        frame.setVisible(true);
    }

}