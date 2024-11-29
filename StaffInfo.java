import java.util.ArrayList;

public class StaffInfo{
    private String staffID;
    private String fName;
    private String lName;
    private String position;
    private String dateOfBirth;
    private contactinfo contactInfo;
    private EmergencyContact emergencyContact;
    private ArrayList<StaffInfo> staffList = new ArrayList<>();

    //Constuctor
    public StaffInfo(String staffID, String fName, String lName, String position, String dateOfBith, contactinfo contactInfo, EmergencyContact emergencyContact){
        this.staffID = staffID;
        this.fName = fName;
        this.lName = fName;
        this.position = position;
        this.dateOfBirth = dateOfBith;
        this.contactInfo = contactInfo;
        this.emergencyContact = emergencyContact;
    }

    //Generating a random ID for the Staff memebers

    public String getStaffID(){
        return staffID;
    }

    public String getFname(){
        return fName;
    }
    
    public void setFname(String fName){
        this.fName = fName;
    }

    public String getLname(){
        return lName;
    }

    public void setLname(String lName){
        this.lName = lName;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position = position;
    }

    public String getDateOfBirth(){
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public contactinfo getContactInfo(){
        return contactInfo;
    }

    public void setContactInfo(contactinfo contactInfo){
        this.contactInfo = contactInfo;
    }

    public EmergencyContact getemergencyContact(){
        return emergencyContact;
    }

    public void setEmegencyContact(EmergencyContact emergencyContact){
        this.emergencyContact = emergencyContact;
    }

    public ArrayList<StaffInfo>getStaffList(){
        return staffList;
    }

    //Adding the new Staff to the Staff List
    public void addStaff(StaffInfo q){
        staffList.add(q);
    }

    

    //Update the Staff Information
    public void updateStaffInfo(String fName, String lName, String position, String dateOfBirth, contactinfo contactInfo, EmergencyContact emergencyContact) {
        this.fName = fName;
        this.lName = lName;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
        this.emergencyContact = emergencyContact;
    
    }

    //For logging/auditing
    public String toString() {
        return "Staff ID: " + staffID + ", First Name: " + fName + ",Last Name: " + lName + ", Position: " + position +
                ", DOB: " + dateOfBirth + ", Contact Info: " + contactInfo + ", Emergency Contact: " + emergencyContact;
    }

    public static StaffInfo fromCSV(String csv) {
        // Split the CSV string into parts using a comma as the delimiter
        String[] parts = csv.split(",");
        if (parts.length > 13) { // Adjust based on the required number of fields
            System.err.println("Invalid staff CSV: " + csv);
            return null;
        }
    
        try {
            // Parse contactinfo
            contactinfo contact = new contactinfo(
                parts[5].trim(), // email
                parts[6].trim(), // phone
                parts[7].trim()  // address
            );
    
            // Parse emergency contact
            EmergencyContact emergencyContact = new EmergencyContact(
                parts[8].trim(), // emergency contact name
                parts[9].trim(), // emergency contact phone
                parts[10].trim() // emergency contact relation
            );
    
            // Create and return the StaffInfo object
            return new StaffInfo(
                parts[0].trim(), // staffID (or generate in constructor)
                parts[1].trim(), // fName
                parts[2].trim(), // lName
                parts[3].trim(), // position
                parts[4].trim(), // dateOfBirth
                contact,         // contactInfo
                emergencyContact // emergencyContact
            );
        } catch (Exception e) {
            System.err.println("Error parsing CSV: " + e.getMessage());
            return null;
        }
    }
    
}