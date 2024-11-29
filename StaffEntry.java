import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StaffEntry extends JDialog {
    private JTextField staffIDField, fnameField, lnameField, positionField, dobField;
    private JTextField telnumField, emailField, addressField;
    private JTextField emergencyNameField, emergencyRelField, emergencyPhoneField, emergencyEmailField;
    private JButton submitButton, cancelButton;
    private StaffManager staffManager;

    // Constructor for adding new staff (staff = null)
    public StaffEntry(StaffInfo staff, StaffManager staffManager) {
        setTitle("Add Staff");
        this.staffManager = staffManager;

        // Set up the dialog size and layout
        setSize(400, 500);
        setLayout(new GridLayout(12, 2, 10, 10));
        setLocationRelativeTo(null); // Center the dialog on the screen

        // If staff is not null, we are editing the staff, otherwise we're adding new staff
        if (staff != null) {
            staffIDField = new JTextField(staff.getStaffID());
            fnameField = new JTextField(staff.getFname());
            lnameField = new JTextField(staff.getLname());
            positionField = new JTextField(staff.getPosition());
            dobField = new JTextField(staff.getDateOfBirth());
            telnumField = new JTextField(staff.getContactInfo().gettelnum());
            emailField = new JTextField(staff.getContactInfo().getEmail());
            addressField = new JTextField(staff.getContactInfo().getaddress());
            emergencyNameField = new JTextField(staff.getemergencyContact().getcontactName());
            emergencyRelField = new JTextField(staff.getemergencyContact().getrelationship());
            emergencyPhoneField = new JTextField(staff.getemergencyContact().getcontactinfo());
            emergencyEmailField = new JTextField(staff.getemergencyContact().getemail());
        } else {
            // If adding new staff, generate an ID for them
            staffIDField = new JTextField(staffManager.generateUniqueID());
            fnameField = new JTextField();
            lnameField = new JTextField();
            positionField = new JTextField();
            dobField = new JTextField();
            telnumField = new JTextField();
            emailField = new JTextField();
            addressField = new JTextField();
            emergencyNameField = new JTextField();
            emergencyRelField = new JTextField();
            emergencyPhoneField = new JTextField();
            emergencyEmailField = new JTextField();
        }

        // Set up the fields
        add(new JLabel("Staff ID:"));
        add(staffIDField);
        add(new JLabel("First Name:"));
        add(fnameField);
        add(new JLabel("Last Name:"));
        add(lnameField);
        add(new JLabel("Position:"));
        add(positionField);
        add(new JLabel("Date of Birth:"));
        add(dobField);
        add(new JLabel("Contact Number:"));
        add(telnumField);
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Address:"));
        add(addressField);
        add(new JLabel("Emergency Contact Name:"));
        add(emergencyNameField);
        add(new JLabel("Emergency Contact Relation:"));
        add(emergencyRelField);
        add(new JLabel("Emergency Contact Phone:"));
        add(emergencyPhoneField);
        add(new JLabel("Emergency Contact Email:"));
        add(emergencyEmailField);

        // Add Submit and Cancel buttons
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create a new staff info object and add it to the staff list
                contactinfo contact = new contactinfo(telnumField.getText(), emailField.getText(), addressField.getText());
                EmergencyContact emergency = new EmergencyContact(emergencyNameField.getText(), emergencyRelField.getText(),
                                                                emergencyPhoneField.getText(), emergencyEmailField.getText());
                staffManager.addStaff(staffIDField.getText(), fnameField.getText(), lnameField.getText(), positionField.getText(),
                                      dobField.getText(), contact, emergency);

                // Close the dialog after adding staff
                staffManager.refreshTables();
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);

        setModal(true); // Make it modal (block interaction with other windows until this one is closed)
        setVisible(true);
    }
}
