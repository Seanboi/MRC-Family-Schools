public class StaffInfo {
    private String staffID;
    private String fname;
    private String lname;
    private String position;
    private String dateOfBirth;
    private contactinfo contactInfo;
    private EmergencyContact emergencyContact;

    public StaffInfo(String staffID, String fname, String lname, String position, String dateOfBirth,
                     contactinfo contactInfo, EmergencyContact emergencyContact) {
        this.staffID = staffID;
        this.fname = fname;
        this.lname = lname;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.contactInfo = contactInfo;
        this.emergencyContact = emergencyContact;
    }

    // Getters and setters
    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public contactinfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(contactinfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public EmergencyContact getemergencyContact() {
        return emergencyContact;
    }

    public void setEmegencyContact(EmergencyContact emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public static StaffInfo fromCSV(String csv) {
        String[] values = csv.split(",");
        if (values.length < 13) {
            return null;
        }
        // Return new instance from CSV line data
        return new StaffInfo(values[0], values[1], values[2], values[4], values[3],
                             new contactinfo(values[5], values[6], values[7]),
                             new EmergencyContact(values[8], values[9], values[10], values[11]));
    }

    @Override
    public String toString() {
        return staffID + "," + fname + "," + lname + "," + dateOfBirth + "," + position + "," +
                contactInfo.gettelnum() + "," + contactInfo.getEmail() + "," + contactInfo.getaddress() + "," +
                emergencyContact.getcontactName() + "," + emergencyContact.getrelationship() + "," +
                emergencyContact.getcontactinfo() + "," + emergencyContact.getemail();
    }
}
